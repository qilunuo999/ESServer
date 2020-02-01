package com.mychaincode.server.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class PBKDF2
{
    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

    //盐的长度
    private static final int SALT_SIZE = 16;

    //生成密文的长度
    private static final int HASH_SIZE = 32;

    // 迭代次数
    private static final int PBKDF2_ITERATIONS = 1000;

    /**
     * 对输入的密码进行验证
     *
     */
    public static boolean verify(String password, String salt, String key)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 用相同的盐值对用户输入的密码进行加密
        String result = getPBKDF2(password, salt);
        // 把加密后的密文和原密文进行比较，相同则验证成功，否则失败
        return result.equals(key);
    }

    /**
     * 根据password和salt生成密文
     * 获取后写入数据库
     */
    public static String getPBKDF2(String password, String salt) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        //将16进制字符串形式的salt转换成byte数组
        byte[] bytes = DatatypeConverter.parseHexBinary(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), bytes, PBKDF2_ITERATIONS, HASH_SIZE * 4);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        byte[] hash = secretKeyFactory.generateSecret(spec).getEncoded();
        //将byte数组转换为16进制的字符串
        return DatatypeConverter.printHexBinary(hash);
    }


    /**
     * 生成随机盐值
     *
     */
    public static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] bytes = new byte[SALT_SIZE / 2];
        random.nextBytes(bytes);
        //将byte数组转换为16进制的字符串
        return DatatypeConverter.printHexBinary(bytes);
    }
}
