/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Services;

import Entities.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dragos
 */
@Service
public class SecurityService {

    public byte[] hashPassword(String password) {
        byte[] hashedPassword = new byte[32];
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            hashedPassword = messageDigest.digest();
            hashedPassword = Base64.encodeBase64(hashedPassword);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SecurityService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hashedPassword;
    }

    public String generateToken(User user) {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[64];
        random.nextBytes(bytes);
        String token = bytes.toString();
        return token;
    }

}
