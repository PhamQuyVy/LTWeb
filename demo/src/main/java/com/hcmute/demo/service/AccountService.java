package com.hcmute.demo.service;

import com.hcmute.demo.entity.Account;

public interface AccountService {

    enum Result {
        OK,
        EMAIL_EXISTED,
        USERNAME_EXISTED,
        NOT_FOUND,
        ALREADY_ACTIVE,
        OTP_WRONG,
        OTP_EXPIRED,
        MAIL_ERROR,
         WRONG_PASSWORD,
        NOT_ACTIVE
    }

    Result register(Account account);
    Result verifyOtp(String email, String otpCode);
    Result resendOtp(String email);
    Account getByEmail(String email);
    Result login (String username, String password);
    Account getByUsername(String username);
    Result forgotPassword(String email);
    Result resetPassword(String email, String otpCode, String newPassword);
    


}