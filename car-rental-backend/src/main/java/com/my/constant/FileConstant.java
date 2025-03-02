package com.my.constant;

import java.util.List;

public interface FileConstant {
    List<String> ALLOWED_FILE_SUFFIXES = List.of("jpg", "jpeg", "png", "webp");

    int ONE_MB = 1024 * 1024;
}
