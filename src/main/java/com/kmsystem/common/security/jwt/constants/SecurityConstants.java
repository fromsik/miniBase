package com.kmsystem.common.security.jwt.constants;

public final class SecurityConstants {

    public static final String AUTH_LOGIN_URL = "/login";
    ///login?Id=test01&Pw=test01
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";

    // Signing key for HS512 algorithm
    // You can use the page http://www.allkeysgenerator.com/ to generate all kinds
}
