package com.zhuooo.demo;

import java.util.UUID;

public class UuidUtils {
    public static String generateUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("\\-", "");
    }
}
