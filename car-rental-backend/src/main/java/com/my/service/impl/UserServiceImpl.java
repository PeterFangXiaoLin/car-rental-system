package com.my.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.common.DeleteRequest;
import com.my.common.ErrorCode;
import com.my.constant.CommonConstant;
import com.my.domain.dto.user.*;
import com.my.domain.entity.User;
import com.my.domain.enums.VerifyResultEnum;
import com.my.domain.enums.UserGenderEnum;
import com.my.domain.enums.UserRoleEnum;
import com.my.domain.enums.VerifyStatusEnum;
import com.my.domain.vo.LoginUserVO;
import com.my.domain.vo.UserVO;
import com.my.exception.BusinessException;
import com.my.manager.CosManager;
import com.my.mapper.UserMapper;
import com.my.service.UserService;
import com.qcloud.cos.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.my.constant.FileConstant.ALLOWED_FILE_SUFFIXES;
import static com.my.constant.FileConstant.ONE_MB;
import static com.my.constant.RedisConstant.CAPTCHA_PREFIX;
import static com.my.constant.UserConstant.*;

/**
* @author Administrator
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-02-27 11:50:27
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CosManager cosManager;

    @Override
    public void validate(User user, boolean isAdd) {
        String userAccount = user.getUserAccount();
        String userPassword = user.getUserPassword();
        String userName = user.getUserName();
        String userProfile = user.getUserProfile();
        String phoneNumber = user.getPhoneNumber();
        String email = user.getEmail();

        if (isAdd) {
            if (StrUtil.isBlank(userAccount)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能为空");
            }
            if (StrUtil.isBlank(userPassword)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不能为空");
            }
        }

        if (StrUtil.isNotBlank(userPassword)) {
            if (userPassword.length() < MIN_PASSWORD_LEN || userPassword.length() > MAX_PASSWORD_LEN) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码在 6-20 位之间");
            }
        }

        if (StrUtil.isNotBlank(userName)) {
            if (userName.length() > MAX_USERNAME_LEN) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "昵称不能超过 20 个字符");
            }
        }

        if (StrUtil.isNotBlank(userProfile)) {
            if (userProfile.length() > MAX_PROFILE_LEN) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "简介不能超过 200 个字符");
            }
        }

//        if (StrUtil.isNotBlank(phoneNumber)) {
//            if (!PhoneUtil.isMobile(phoneNumber)) {
//                throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号格式错误");
//            }
//        }
//
//        if (StrUtil.isNotBlank(email)) {
//            if (!Validator.isEmail(email)) {
//                throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式错误");
//            }
//        }
    }

    @Override
    public Long userRegister(UserRegisterRequest userRegisterRequest) {
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String captchaCode = userRegisterRequest.getCaptchaCode();
        String captchaKey = userRegisterRequest.getCaptchaKey(); // 需要在 UserRegisterRequest 中添加此字段

        // 1. 校验参数
        if (StrUtil.hasBlank(userAccount, userPassword, checkPassword, captchaCode, captchaKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < MIN_ACCOUNT_LEN || userAccount.length() > MAX_ACCOUNT_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号在 4-20 位之间");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        if (userPassword.length() < MIN_PASSWORD_LEN || userPassword.length() > MAX_PASSWORD_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码在 6-20 位之间");
        }

        // 2. 验证码校验
        String redisKey = CAPTCHA_PREFIX + captchaKey;
        String correctCaptcha = stringRedisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isBlank(correctCaptcha)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码已过期");
        }
        if (!captchaCode.equalsIgnoreCase(correctCaptcha)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误");
        }
        // 验证码使用后立即删除
        stringRedisTemplate.delete(redisKey);

        // 3. 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已存在");
        }

        // 4. 加密密码
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 5. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        // 生成默认昵称
        user.setUserName(DEFAULT_USER_NAME);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }

        return user.getId();
    }

    @Override
    public LoginUserVO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        // 1. 校验参数
        if (StrUtil.hasBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < MIN_ACCOUNT_LEN || userAccount.length() > MAX_ACCOUNT_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号在 4-20 位之间");
        }
        if (userPassword.length() < MIN_PASSWORD_LEN || userPassword.length() > MAX_PASSWORD_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码在 6-20 位之间");
        }

        // 2. 加密密码
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 3. 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);

        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }

        // 4. 记录用户的登录态
        HttpSession session = request.getSession();
        session.setAttribute(USER_LOGIN_STATE, user);

        // 5. 将用户信息脱敏后返回
        return this.getLoginUserVO(user);
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        return BeanUtil.toBean(user, LoginUserVO.class);
    }

    @Override
    public LoginUserVO getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        return this.getLoginUserVO(currentUser);
    }

    @Override
    public Boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return isAdmin(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue() == user.getUserRole();
    }

    @Override
    public boolean updateUserPassword(UserUpdatePasswordRequest userUpdatePasswordRequest, HttpServletRequest request) {
        if (userUpdatePasswordRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 1. 获取当前登录用户
        LoginUserVO loginUser = this.getLoginUser(request);

        // 2. 校验参数
        String oldPassword = userUpdatePasswordRequest.getOldPassword();
        String newPassword = userUpdatePasswordRequest.getNewPassword();
        String checkPassword = userUpdatePasswordRequest.getCheckPassword();

        if (StrUtil.hasBlank(oldPassword, newPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }

        // 新密码和确认密码相同
        if (!newPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的新密码不一致");
        }

        // 密码长度不能小于 8 位
        if (newPassword.length() < MIN_PASSWORD_LEN || newPassword.length() > MAX_PASSWORD_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度在 6-20 位之间");
        }

        // 3. 验证原密码是否正确
        User user = userMapper.selectById(loginUser.getId());
        String encryptOldPassword = DigestUtils.md5DigestAsHex((SALT + oldPassword).getBytes());
        if (!encryptOldPassword.equals(user.getUserPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "原密码错误");
        }

        // 4. 新密码不能与原密码相同
        if (oldPassword.equals(newPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "新密码不能与原密码相同");
        }

        // 5. 加密新密码
        String encryptNewPassword = DigestUtils.md5DigestAsHex((SALT + newPassword).getBytes());

        // 6. 更新数据库中的用户密码
        User updateUser = new User();
        updateUser.setId(loginUser.getId());
        updateUser.setUserPassword(encryptNewPassword);
        boolean result = this.updateById(updateUser);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "密码更新失败");
        }

        // 7. 退出登录，让用户重新登录
        this.userLogout(request);

        return true;
    }

    @Override
    public UserVO updateUser(UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        // 1. 校验参数
        if (userUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 2. 获取当前登录用户
        LoginUserVO loginUser = this.getLoginUser(request);
        Long userId = loginUser.getId();

        // 3. 参数校验
        String phoneNumber = userUpdateRequest.getPhoneNumber();
        String email = userUpdateRequest.getEmail();
        Integer gender = userUpdateRequest.getGender();

        // 手机号格式校验
        if (StrUtil.isNotBlank(phoneNumber)) {
            if (!PhoneUtil.isMobile(phoneNumber)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号格式错误");
            }
            // 判断手机号是否已被其他用户使用
            long count = this.lambdaQuery().eq(User::getPhoneNumber, phoneNumber).ne(User::getId, userId).count();
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号已被使用");
            }
        }

        // 性别校验
        if (ObjUtil.isNotNull(gender)) {
            UserGenderEnum enumByValue = UserGenderEnum.getEnumByValue(gender);
            if (enumByValue == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "性别错误");
            }
        }

        // 邮箱格式校验
        if (StrUtil.isNotBlank(email)) {
            if (!Validator.isEmail(email)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式错误");
            }
        }

        // 4. 创建更新对象
        User user = BeanUtil.toBean(userUpdateRequest, User.class);
        user.setId(userId);
        user.setEditTime(new Date());

        // 5. 更新数据库
        boolean result = this.updateById(user);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }

        // 6. 返回更新后的用户信息
        User updatedUser = this.getById(userId);
        return BeanUtil.toBean(updatedUser, UserVO.class);
    }

    @Override
    public String updateAvatar(MultipartFile file, HttpServletRequest request) {
        // 1. 校验参数
        if (file == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 校验文件大小
        long fileSize = file.getSize();
        if (fileSize > 5 * ONE_MB) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过5MB");
        }

        // 校验文件类型
        String originalFilename = file.getOriginalFilename();
        String fileSuffix = FileUtil.getSuffix(originalFilename);
        if (!ALLOWED_FILE_SUFFIXES.contains(fileSuffix)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件格式不支持");
        }

        // 2. 获取当前登录用户
        LoginUserVO loginUser = this.getLoginUser(request);

        try {
            // 上传文件到 OSS，得到访问地址（这里假设你使用的是阿里云 OSS）
            String filePath = String.format("avatar/%s/%s.%s", loginUser.getId(),
                    UUID.randomUUID(), fileSuffix);

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(fileSize);

            String avatarUrl = cosManager.putObjectByStream(filePath, file.getInputStream(), objectMetadata);

            // 更新用户头像地址
            User user = new User();
            user.setId(loginUser.getId());
            user.setUserAvatar(avatarUrl);
            boolean result = this.updateById(user);
            if (!result) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新头像失败");
            }

            return avatarUrl;
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传失败");
        }
    }

    @Override
    public Long addUser(UserAddRequest userAddRequest) {
        // 1. 校验参数
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 2. 校验必要参数
        User user = BeanUtil.toBean(userAddRequest, User.class);
        validate(user, true);

        // 3. 校验账号是否重复
        String userAccount = user.getUserAccount();

        long count = this.lambdaQuery()
                .eq(User::getUserAccount, userAccount)
                .count();
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已存在");
        }

        // 4. 加密密码
        String userPassword = user.getUserPassword();
        if (StrUtil.isBlank(userPassword)) {
            userPassword = DEFAULT_PASSWORD;
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 5. 构建用户对象
        user.setUserPassword(encryptPassword);
        // 设置默认值
        String userName = user.getUserName();
        if (StrUtil.isBlank(userName)) {
            user.setUserName(userAccount);
        }
        // 其他默认值由数据库默认值完成

        // 6. 保存用户
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "添加用户失败");
        }

        return user.getId();
    }

    @Override
    public Boolean adminUpdateUser(UserAdminUpdateRequest userAdminUpdateRequest) {
        // 1. 校验参数
        if (userAdminUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User user = BeanUtil.toBean(userAdminUpdateRequest, User.class);
        validate(user, false);

        // 2. 更新用户
        boolean result = this.updateById(user);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新用户失败");
        }

        return true;
    }

    @Override
    public Boolean adminDeleteUser(DeleteRequest deleteRequest) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long id = deleteRequest.getId();
        User user = this.getById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        int delete = userMapper.deleteById(id);
        if (delete < 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return true;
    }

    @Override
    public Page<UserVO> pageUserVO(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long current = userQueryRequest.getCurrent();
        long pageSize = userQueryRequest.getPageSize();
        if (current <= 0 || pageSize <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分页参数不合法");
        }

        QueryWrapper<User> queryWrapper = getQueryWrapper(userQueryRequest);
        Page<User> userPage = userMapper.selectPage(Page.of(current, pageSize), queryWrapper);
        Page<UserVO> userVOPage = new Page<>(current, pageSize, userPage.getTotal());
        userVOPage.setRecords(getUserVOList(userPage.getRecords()));
        return userVOPage;
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (userQueryRequest == null) {
            return queryWrapper;
        }

        Long id = userQueryRequest.getId();
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String phoneNumber = userQueryRequest.getPhoneNumber();
        String email = userQueryRequest.getEmail();
        Integer memberLevel = userQueryRequest.getMemberLevel();
        Integer userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();

        queryWrapper.eq(id != null, "id", id);
        queryWrapper.like(StrUtil.isNotBlank(userAccount), "userAccount", userAccount);
        queryWrapper.like(StrUtil.isNotBlank(userName), "userName", userName);
        queryWrapper.like(StrUtil.isNotBlank(phoneNumber), "phoneNumber", phoneNumber);
        queryWrapper.like(StrUtil.isNotBlank(email), "email", email);
        queryWrapper.eq(memberLevel != null, "memberLevel", memberLevel);
        queryWrapper.eq(userRole != null, "userRole", userRole);
        queryWrapper.orderBy(StrUtil.isNotBlank(sortField), sortOrder.equalsIgnoreCase(CommonConstant.SORT_ORDER_ASC), sortField);

        return queryWrapper;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        return BeanUtil.toBean(user, UserVO.class);
    }

    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream()
                .map(this::getUserVO)
                .collect(Collectors.toList());
    }

    @Override
    public UserVO getUserById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = this.getById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        return BeanUtil.toBean(user, UserVO.class);
    }

    @Override
    public User getUser(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = this.getById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        return user;
    }

    @Override
    public boolean authUser(UserAuthRequest userAuthRequest, HttpServletRequest request) {
        if (userAuthRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserVO loginUser = this.getLoginUser(request);

        String realName = userAuthRequest.getRealName();
        String idCardNumber = userAuthRequest.getIdCardNumber();
        String driverLicenseNo = userAuthRequest.getDriverLicenseNo();
        String driverLicenseType = userAuthRequest.getDriverLicenseType();
        Date driverLicenseIssueDate = userAuthRequest.getDriverLicenseIssueDate();
        Date driverLicenseExpireDate = userAuthRequest.getDriverLicenseExpireDate();

        if (StrUtil.hasBlank(realName, idCardNumber, driverLicenseNo, driverLicenseType)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (driverLicenseIssueDate == null || driverLicenseExpireDate == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (driverLicenseIssueDate.after(driverLicenseExpireDate)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "驾驶证有效期不能小于驾驶证发证日期");
        }

        User user = BeanUtil.toBean(userAuthRequest, User.class);
        user.setId(loginUser.getId());
        user.setVerifyStatus(VerifyStatusEnum.VERIFYING.getValue());
        user.setVerifyTime(new Date());
        user.setVerifyResult(VerifyResultEnum.REVIEWING.getValue());

        boolean result = this.updateById(user);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统异常");
        }

        return true;
    }

    @Override
    public boolean reviewUser(UserReviewRequest userReviewRequest, HttpServletRequest request) {
        if (userReviewRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = userReviewRequest.getId();
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        Integer reviewStatus = userReviewRequest.getReviewStatus();
        VerifyResultEnum verifyResultEnum = VerifyResultEnum.getEnumByValue(reviewStatus);
        if (verifyResultEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 校验状态
        Integer verifyResult = user.getVerifyResult();
        if (verifyResult == verifyResultEnum.getValue()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请勿重复审核");
        }
        // 修改状态
        User updateUser = new User();
        updateUser.setId(userId);
        // 通过
        if (verifyResultEnum == VerifyResultEnum.PASS) {
            updateUser.setVerifyStatus(VerifyStatusEnum.VERIFIED.getValue());
            updateUser.setVerifyResult(VerifyResultEnum.PASS.getValue());
        } else if (verifyResultEnum == VerifyResultEnum.REJECT) {
            // 拒绝
            updateUser.setVerifyStatus(VerifyStatusEnum.VERIFY_FAILED.getValue());
            updateUser.setVerifyResult(VerifyResultEnum.REJECT.getValue());
            updateUser.setRejectReason(userReviewRequest.getRejectReason());
        }
        updateUser.setReviewId(this.getLoginUser(request).getId());
        updateUser.setReviewTime(new Date());

        boolean result = this.updateById(updateUser);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "审核失败");
        }
        return true;
    }
}




