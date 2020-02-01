package app.security.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {

    public static String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public static void main(String[] args) {
        String password = "password123&";
        String encryptedPassword = encryptPassword(password);

        System.out.println("Encrypted Password: " + encryptedPassword);
    }

}