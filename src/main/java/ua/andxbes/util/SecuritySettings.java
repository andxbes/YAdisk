/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.andxbes.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import sun.misc.BASE64Encoder;

/**
 *
 * @author Andr
 */
public final class SecuritySettings {
    private static  final String ALGORITHM = "DES",
	    WORD_ENCRIPT = "UTF8";

    private final static class MySecretKey implements SecretKey {

	private final byte[] key = new byte[]{2, 1, 4, 6, 2, 1, 4, 6}; // ключ
	// не должен иметь длину более 8 байт, для безопасного шифрования его
	// необходимо изменить

	@Override
	public String getAlgorithm() {
	    return ALGORITHM;
	}

	@Override
	public String getFormat() {
	    return "RAW";
	}

	@Override
	public byte[] getEncoded() {
	    return key;
	}
    }

    private static SecretKey key;

    private static Cipher ecipher;
    private static Cipher dcipher;

    static {
	try {
	    key = new MySecretKey();
	    ecipher = Cipher.getInstance(ALGORITHM);
	    dcipher = Cipher.getInstance(ALGORITHM);
	    ecipher.init(Cipher.ENCRYPT_MODE, key);
	    dcipher.init(Cipher.DECRYPT_MODE, key);
	} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException ex) {
	    Logger.getLogger(SecuritySettings.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    /**
     * Функция шифрования
     *
     * @param str строка открытого текста
     * @return зашифрованная строка в формате Base64
     */
    public static String encrypt(String str) {
	try {
	    byte[] utf8 = str.getBytes(WORD_ENCRIPT);
	    byte[] enc = ecipher.doFinal(utf8);
	    return new BASE64Encoder().encode(enc);
	} catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
	    Logger.getLogger(SecuritySettings.class.getName()).log(Level.SEVERE, null, ex);
	}
	return null;
    }

    /**
     * Функция расшифрования
     *
     * @param str зашифрованная строка в формате Base64
     * @return расшифрованная строка
     * @throws java.io.IOException
     */
    public static String decrypt(String str) throws IOException {
	try {
	    byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
	    byte[] utf8 = dcipher.doFinal(dec);
	    return new String(utf8, WORD_ENCRIPT);
	} catch (IllegalBlockSizeException | BadPaddingException | IOException ex) {
	    Logger.getLogger(SecuritySettings.class.getName()).log(Level.SEVERE, null, ex);
	}
	return null;
    }
}
