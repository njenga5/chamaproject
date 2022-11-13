package com.mwanzobaraka.hasher;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class dedicated to hashing of passwords
 */
public class HashingProvider {

    private static byte[] getSHA(String in) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        return md.digest(in.getBytes());
    }

    private static String hexToString(byte[] hash){
        BigInteger bi = new BigInteger(1, hash);

        StringBuilder sb = new StringBuilder(bi.toString(16));
        while(sb.length() < 64){
            sb.insert(0, '0');
        }

        return sb.toString();
    }


    public static boolean compareHex(String pass, String hex) throws NoSuchAlgorithmException {
        return generateHex(pass).equals(hex);
    }

    public static String generateHex(String pass) throws NoSuchAlgorithmException {
        return hexToString(getSHA(pass));
    }
}
