package ir.microsign.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Mohammad on 9/29/14.
 */
public class Security {
    public static final String md5(final String s) {
        final String MD5 = "MD5";

        // Create MD5 Hash
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(MD5);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (digest == null) return s;
        digest.update(s.getBytes());
        byte messageDigest[] = digest.digest();

        // Create Hex String
        StringBuilder hexString = new StringBuilder();
        for (byte aMessageDigest : messageDigest) {
            String h = Integer.toHexString(0xFF & aMessageDigest);
            while (h.length() < 2)
                h = "0" + h;
            hexString.append(h);
        }
        return hexString.toString();
    }
}

