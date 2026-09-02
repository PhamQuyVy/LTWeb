package com.hcmute.demo.util;

import java.security.SecureRandom;
import java.sql.Timestamp;

public class OtpUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateOtp() {
        int number = RANDOM.nextInt(1_000_000);
        return String.format("%06d", number);
    }

    public static Timestamp generateExpiry() {
        long expireMillis = System.currentTimeMillis()
                + Constant.OTP_EXPIRE_MINUTES * 60_000L;
        return new Timestamp(expireMillis);
    }

    public static boolean isExpired(Timestamp otpExpiry) {
        if (otpExpiry == null) {
            return true;
        }
        return otpExpiry.before(new Timestamp(System.currentTimeMillis()));
    }
}