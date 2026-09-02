package com.hcmute.demo.service.impl;

import com.hcmute.demo.dao.AccountDao;
import com.hcmute.demo.dao.impl.AccountDaoImpl;
import com.hcmute.demo.entity.Account;
import com.hcmute.demo.service.AccountService;
import com.hcmute.demo.util.MailUtil;
import com.hcmute.demo.util.OtpUtil;

public class AccountServiceImpl implements AccountService {

    private final AccountDao accountDao = new AccountDaoImpl();

    @Override
    public Result register(Account account) {

        if (accountDao.getByEmail(account.getEmail()) != null) {
            return Result.EMAIL_EXISTED;
        }

        if (accountDao.getByUsername(account.getUsername()) != null) {
            return Result.USERNAME_EXISTED;
        }

        String otpCode = OtpUtil.generateOtp();

        account.setOtpCode(otpCode);
        account.setOtpExpiry(OtpUtil.generateExpiry());
        account.setStatus("PENDING");


        accountDao.insert(account);

        try {
            MailUtil.sendOtpEmail(account.getEmail(), account.getFullName(), otpCode);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.MAIL_ERROR;
        }

        return Result.OK;
    }

    @Override
    public Result verifyOtp(String email, String otpCode) {

        Account account = accountDao.getByEmail(email);

        if (account == null) {
            return Result.NOT_FOUND;
        }
        if ("ACTIVE".equals(account.getStatus())) {
            return Result.ALREADY_ACTIVE;
        }
        if (OtpUtil.isExpired(account.getOtpExpiry())) {
            return Result.OTP_EXPIRED;
        }
        if (account.getOtpCode() == null || !account.getOtpCode().equals(otpCode)) {
            return Result.OTP_WRONG;
        }

        accountDao.activate(account.getId());

        return Result.OK;
    }

    @Override
    public Result resendOtp(String email) {

        Account account = accountDao.getByEmail(email);

        if (account == null) {
            return Result.NOT_FOUND;
        }
        if ("ACTIVE".equals(account.getStatus())) {
            return Result.ALREADY_ACTIVE;
        }

        String otpCode = OtpUtil.generateOtp();
        java.sql.Timestamp expiry = OtpUtil.generateExpiry();

        accountDao.updateOtp(account.getId(), otpCode, expiry);

        try {
            MailUtil.sendOtpEmail(account.getEmail(), account.getFullName(), otpCode);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.MAIL_ERROR;
        }

        return Result.OK;
    }

    @Override
    public Account getByEmail(String email) {
        return accountDao.getByEmail(email);
    }
        @Override
    public Result login(String username, String password) {
        Account account = accountDao.getByUsername(username);
        if (account == null) {
            return Result.NOT_FOUND;
        }
        if (!account.getPassword().equals(password)) {
            return Result.WRONG_PASSWORD;
        }
        if (!"ACTIVE".equals(account.getStatus())) {
            return Result.NOT_ACTIVE;
        }
        return Result.OK;
    }

    @Override
    public Account getByUsername(String username) {
        return accountDao.getByUsername(username);
    }

    @Override
    public Result forgotPassword(String email) {
        Account account = accountDao.getByEmail(email);
        if (account == null) {
            return Result.NOT_FOUND;
        }
        String otpCode = OtpUtil.generateOtp();
        java.sql.Timestamp expiry = OtpUtil.generateExpiry();
        accountDao.updateOtp(account.getId(), otpCode, expiry);
        try {
            MailUtil.sendResetPasswordEmail(account.getEmail(), account.getFullName(), otpCode);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.MAIL_ERROR;
        }
        return Result.OK;
    }

    @Override
    public Result resetPassword(String email, String otpCode, String newPassword) {
        Account account = accountDao.getByEmail(email);
        if (account == null) {
            return Result.NOT_FOUND;
        }
        if (OtpUtil.isExpired(account.getOtpExpiry())) {
            return Result.OTP_EXPIRED;
        }
        if (account.getOtpCode() == null || !account.getOtpCode().equals(otpCode)) {
            return Result.OTP_WRONG;
        }
        accountDao.updatePassword(account.getId(), newPassword);
        accountDao.updateOtp(account.getId(), null, null);
        return Result.OK;
    }
}