package fr.xephi.authme.security.crypts;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import fr.xephi.authme.AuthMe;

/**
 */
public class MYBB implements EncryptionMethod {

    /**
     * Method getHash.
     * @param password String
     * @param salt String
     * @param name String
    
    
    
     * @return String * @throws NoSuchAlgorithmException * @see fr.xephi.authme.security.crypts.EncryptionMethod#getHash(String, String, String) */
    @Override
    public String getHash(String password, String salt, String name)
            throws NoSuchAlgorithmException {
        return getMD5(getMD5(salt) + getMD5(password));
    }

    /**
     * Method comparePassword.
     * @param hash String
     * @param password String
     * @param playerName String
    
    
    
     * @return boolean * @throws NoSuchAlgorithmException * @see fr.xephi.authme.security.crypts.EncryptionMethod#comparePassword(String, String, String) */
    @Override
    public boolean comparePassword(String hash, String password,
            String playerName) throws NoSuchAlgorithmException {
        String salt = AuthMe.getInstance().database.getAuth(playerName).getSalt();
        return hash.equals(getHash(password, salt, playerName));
    }

    /**
     * Method getMD5.
     * @param message String
    
    
     * @return String * @throws NoSuchAlgorithmException */
    private static String getMD5(String message)
            throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.reset();
        md5.update(message.getBytes());
        byte[] digest = md5.digest();
        return String.format("%0" + (digest.length << 1) + "x", new BigInteger(1, digest));
    }
}
