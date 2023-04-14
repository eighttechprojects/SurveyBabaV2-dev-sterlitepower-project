package com.surveybaba.AESCrypt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCrypt
{
    private static final String TAG = "AESCrypt";
    private static String key                = "0123456789abcdef";
    private static final String IV           = "abcdef9876543210";
    private static final String CIPHER_NAME  = "AES/CBC/PKCS5PADDING";
    private static final int  CIPHER_KEY_LEN = 16; //128 bits

//------------------------------------------------- Encrypt Data------------------------------------------------------------------------------------------------------------------------
//
//    public static String encrypt(String value) throws Exception
//    {
//        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
//        SecretKeySpec secretKey = new SecretKeySpec(fixKey().getBytes(StandardCharsets.UTF_8), "AES");
//        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(CIPHER_NAME);
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey,ivParameterSpec);
//        byte [] encryptedByteValue = cipher.doFinal(value.getBytes());
//        String encryptedDataInBase64 = Base64.encodeToString(encryptedByteValue,Base64.DEFAULT);
//        String ivInBase64 = Base64.encodeToString(IV.getBytes(StandardCharsets.UTF_8),Base64.DEFAULT);
//        return encryptedDataInBase64 +":"+ivInBase64;
//    }

    public static String encrypt(String value) throws Exception
    {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec secretKey = new SecretKeySpec(fixKey().getBytes(StandardCharsets.UTF_8), "AES");
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(CIPHER_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey,ivParameterSpec);
        byte [] encryptedByteValue = cipher.doFinal(value.getBytes());
        String encryptedDataInBase64 = Base64.encodeToString(encryptedByteValue,Base64.DEFAULT);
        String ivInBase64 = Base64.encodeToString(IV.getBytes(StandardCharsets.UTF_8),Base64.DEFAULT);
        return encryptedDataInBase64 +":"+ivInBase64;
    }

    public static byte[] encrypt(byte[] value) throws Exception
    {
        byte[] e = null;
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec secretKey = new SecretKeySpec(fixKey().getBytes(StandardCharsets.UTF_8), "AES");
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(CIPHER_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey,ivParameterSpec);
        e = cipher.doFinal(value);
        return e;
    }


//------------------------------------------------- Decrypt Data ------------------------------------------------------------------------------------------------------------------------

    public static String decrypt(String value) throws Exception
    {
        String[] data = value.split(":");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(Base64.decode(data[1],Base64.DEFAULT));
        SecretKeySpec secretKey = new SecretKeySpec(fixKey().getBytes(StandardCharsets.UTF_8), "AES");
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(CIPHER_NAME);
        cipher.init(Cipher.DECRYPT_MODE, secretKey,ivParameterSpec);
        byte[] decryptedValue64 = Base64.decode(data[0], Base64.DEFAULT);
        byte[] decryptedByteValue = cipher.doFinal(decryptedValue64);
        return new String(decryptedByteValue);
    }

    public static String decrypt(Context context,String value) throws Exception
    {
        String[] data = value.split(":");
        Log.e(TAG, data[0]);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(Base64.decode(data[1],Base64.DEFAULT));
        SecretKeySpec secretKey = new SecretKeySpec(fixKey().getBytes(StandardCharsets.UTF_8), "AES");
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(CIPHER_NAME);
        cipher.init(Cipher.DECRYPT_MODE, secretKey,ivParameterSpec);
        byte[] decryptedValue64 = Base64.decode(data[0], Base64.DEFAULT);
        byte[] decryptedByteValue = cipher.doFinal(decryptedValue64);
        return new String(decryptedByteValue);
    }

//------------------------------------------------- Key ------------------------------------------------------------------------------------------------------------------------

    private static String fixKey() {
        if (key.length() < CIPHER_KEY_LEN) {
            int numPad = CIPHER_KEY_LEN - key.length();
            for (int i = 0; i < numPad; i++) {
                key += "0"; //0 pad to len 16 bytes
            }
            return key;
        }
        if (key.length() > CIPHER_KEY_LEN) {
            return key.substring(0, CIPHER_KEY_LEN); //truncate to 16 bytes
        }
        return key;
    }

//------------------------------------------------- DecryptResponse ------------------------------------------------------------------------------------------------------------------------

    public static String decryptResponse(String response){
        try {
            return AESCrypt.decrypt(response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String decryptResponse(Context context,String response){
        try {
            return AESCrypt.decrypt(context,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

//------------------------------------------------- Save Response Data ------------------------------------------------------------------------------------------------------------------------

    public static void saveResponseData(Context context,String date) {
        try {
            FileOutputStream fos = context.openFileOutput("AESCrypt", Context.MODE_PRIVATE);
            fos.write(date.getBytes());
            fos.flush();
            fos.close();
            Log.e(TAG,"File Save");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

//------------------------------------------------- Read Response Data ------------------------------------------------------------------------------------------------------------------------

    public static String readResponseData(Context context) {
        String userdata = null;
        try {
            FileInputStream fis = context.openFileInput("AESCrypt");
            byte[] dataArray = new byte[fis.available()];
            while (fis.read(dataArray) != -1) {
                userdata = new String(dataArray);
            }
            fis.close();
            return userdata;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return userdata;
    }

    public static byte[] readResponse(Context context) {
//        byte[] dataArray = null;
//        try {
//            FileInputStream fis = context.openFileInput("AESCrypt");
//            dataArray = new byte[fis.available()];
//            BufferedInputStream buf = new BufferedInputStream(fis);
//            try {
//                buf.read(dataArray);
//                buf.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        } catch (IOException e) {
//            Log.e(TAG, e.getMessage());
//        }
//        return dataArray;
        byte[] contents = null;

        try {
            FileInputStream fis = context.openFileInput("AESCrypt");
            int size =  fis.available();
            contents = new byte[size];
            BufferedInputStream buf = new BufferedInputStream(fis);
            try {
                buf.read(contents);
                buf.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return contents;
    }

    public static byte[] read(Context context) {

        byte[] contents = null;

        try {
            FileInputStream fis = context.openFileInput("AESCrypt");
            int size =  fis.available();
            contents = new byte[size];
            fis.read(contents,0,contents.length);
            fis.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return contents;
    }
}

/*
public class AESCrypt
{
    private static String key                = "0123456789abcdef";
    private static final String IV           = "abcdef9876543210";
    private static final String CIPHER_NAME  = "AES/CBC/PKCS5PADDING";
    private static final int  CIPHER_KEY_LEN = 16; //128 bits

//------------------------------------------------- Encrypt Data------------------------------------------------------------------------------------------------------------------------

    public static String encrypt(String value) throws Exception
    {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec secretKey = new SecretKeySpec(fixKey().getBytes(StandardCharsets.UTF_8), "AES");
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(CIPHER_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey,ivParameterSpec);
        byte [] encryptedByteValue = cipher.doFinal(value.getBytes());
        String encryptedDataInBase64 = Base64.encodeToString(encryptedByteValue,Base64.DEFAULT);
        String ivInBase64 = Base64.encodeToString(IV.getBytes(StandardCharsets.UTF_8),Base64.DEFAULT);
        return encryptedDataInBase64 +":"+ivInBase64;
    }



//------------------------------------------------- Decrypt Data ------------------------------------------------------------------------------------------------------------------------

    public static String decrypt(String value) throws Exception
    {
        String[] data = value.split(":");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(Base64.decode(data[1],Base64.DEFAULT));
        SecretKeySpec secretKey = new SecretKeySpec(fixKey().getBytes(StandardCharsets.UTF_8), "AES");
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(CIPHER_NAME);
        cipher.init(Cipher.DECRYPT_MODE, secretKey,ivParameterSpec);





        byte[] decryptedValue64 = Base64.decode(data[0], Base64.DEFAULT);
        byte[] decryptedByteValue = cipher.doFinal(decryptedValue64);
        return new String(decryptedByteValue);
    }



//------------------------------------------------- Key ------------------------------------------------------------------------------------------------------------------------

    private static String fixKey() {
        if (key.length() < CIPHER_KEY_LEN) {
            int numPad = CIPHER_KEY_LEN - key.length();
            for (int i = 0; i < numPad; i++) {
                key += "0"; //0 pad to len 16 bytes
            }
            return key;
        }
        if (key.length() > CIPHER_KEY_LEN) {
            return key.substring(0, CIPHER_KEY_LEN); //truncate to 16 bytes
        }
        return key;
    }

//------------------------------------------------- DecryptResponse ------------------------------------------------------------------------------------------------------------------------

    public static String decryptResponse(String response){
        try {
            return AESCrypt.decrypt(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


}
*/