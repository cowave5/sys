package com.cowave.sys.admin.domain.auth;

import org.apache.commons.codec.binary.Base64;

import java.util.UUID;

/**
 * @author shanhuiming
 */
public class IdFormater {

    private static final String format = "%s-%s-%s-%s";

    /**
     * 格式化id
     */
    public static String formatId(String idType, String tenantId, String subType){
        String shortUuid = Base64.encodeBase64URLSafeString(UUID.randomUUID().toString().getBytes()).substring(0, 10);
        return format.formatted(idType, tenantId, subType, shortUuid);
    }

    public static void main(String[] args) {
        System.out.println(formatId("user", "cowave", "sys"));
    }
}
