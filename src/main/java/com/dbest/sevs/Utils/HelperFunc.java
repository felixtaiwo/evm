package com.dbest.sevs.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

public class HelperFunc {
    private final String chars ="1234567890ABCDEF";
    Random random = new Random();
    @Autowired
    private SecretKey secretKey = getSecretKey();
    @Autowired
    private IvParameterSpec iv=generateIv();
    private final String algorithm = "AES/CBC/PKCS5Padding";

    @Bean
    public  IvParameterSpec generateIv() {
        byte[] ivv = {-115, -28, -30, -52, 44, -86, -62, 123, -37, 116, -62, 56, 62, 40, 87, 95};
        iv=new IvParameterSpec(ivv);
        return iv;
    }
    @Bean
    public SecretKey getSecretKey(){
        String key = "OTA5OTA4MDk4OWpPIUHGTPOTA5OTA4MDk4OWpPIUHGT";
        byte[] decodedKey = Base64.getDecoder().decode(key);
       secretKey= new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
       return secretKey;
   }
    public  SealedObject encryptObject(Serializable object) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, IOException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        return new SealedObject(object, cipher);
    }

    public  Serializable decryptObject(SealedObject sealedObject) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            ClassNotFoundException, BadPaddingException, IllegalBlockSizeException,
            IOException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        return (Serializable) sealedObject.getObject(cipher);
    }

    public String generateToken(String type){
        switch(type){
            case "confirmAccount":
                return genTok(4);
            default:
                return genTok(6);
        }
    }

    private String genTok(int j) {
        String tok="";
        for(int i =0;i<j;i++){
            tok=tok+(chars.charAt(random.nextInt(chars.length())));
        }
        return tok;
    }

    public String generatePassword() {
        int size =random.nextInt(3)+8;
        String password="";
        for(int i =0;i<size;i++){
            password=password+chars.charAt(random.nextInt(chars.length()));
        }
        return password;
    }
    public String generateRef(){
        String ref=String.valueOf(new Date().getTime());
        for(int i =0;i<3;i++){
            ref=ref+chars.charAt(random.nextInt(chars.length()));
        }
        return ref;
    }
}
