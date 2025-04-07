package com.my.utils;

import java.util.HashSet;
import java.util.Set;

public class RandomUtils {
    public static String randomLicensePlate() {
        String plateNO = "";
        //省号
        String[] sheng = {"京", "津", "沪", "渝", "冀", "吉", "辽", "黑", "湘", "鄂", "甘", "晋", "陕", "豫", "川", "云", "桂",
                "蒙", "贵", "青", "藏", "新", "宁", "粤", "琼", "闽", "苏", "浙", "赣", "鲁", "皖"};
        int i = (int) (Math.random() * (sheng.length));
        plateNO += sheng[i];
        //字母
        plateNO += getA();
        //字母总数
        int Anum = (int) (Math.random() * 3);
        Set<Integer> set = new HashSet<>();
        while (set.size() != Anum) {
            set.add((int) (Math.random() * 5));
        }
        //插入编号
        for (int k = 0; k < 5; k++) {
            if (set.contains(k)) {
                plateNO += getA();
            } else {
                plateNO += (int) (Math.random() * 10);
            }
        }
        return plateNO;
    }

    //获取一个随机字母
    public static String getA() {
        int j = 0;
        do {
            j = Integer.valueOf('A') + (int) (Math.random() * 26);
        } while ((j == 73) || (j == 79));
        return "" + (char) j;
    }
}
