package com.ap.model;

import com.ap.model.UserDao;
import com.ap.model.User;
import java.security.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class AuthService {
    private final UserDao userDAO;

    public AuthService(UserDao userDAO) {
        this.userDAO = userDAO;
    }

    public String authenticate(String username, String pin) throws Exception {
        User user = userDAO.getUserByUsername(username);
        if (user == null) return null;

        String hash = hashPin(pin, user.getSalt());
        if (user.getPasswordHash().equals(hash)) {
            return user.getUsername() + ":" + user.getRole();
        }
        return null;
    }

    public String hashPin(String pin, byte[] salt) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        byte[] hashed = md.digest(pin.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashed) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
