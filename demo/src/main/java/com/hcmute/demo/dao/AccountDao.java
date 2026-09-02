package com.hcmute.demo.dao;

import com.hcmute.demo.entity.Account;

public interface AccountDao {

    void insert(Account account);
    Account getByEmail(String email);
    Account getByUsername(String username);
    void updateOtp(int id, String otpCode, java.sql.Timestamp otpExpiry);
    void updatePassword(int id, String newPassword);
    void activate(int id);
    Account get(int id);

}