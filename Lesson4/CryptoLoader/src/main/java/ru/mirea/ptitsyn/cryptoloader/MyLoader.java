package ru.mirea.ptitsyn.cryptoloader;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.loader.content.AsyncTaskLoader;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MyLoader extends AsyncTaskLoader<String> {
    public static final String ARG_CRYPT_TEXT = "cryptText";
    public static final String ARG_KEY = "key";
    private byte[] cryptText;
    private byte[] keyBytes;

    public MyLoader(@NonNull Context context, Bundle args) {
        super(context);
        if (args != null) {
            cryptText = args.getByteArray(ARG_CRYPT_TEXT);
            keyBytes = args.getByteArray(ARG_KEY);
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        if (cryptText == null || keyBytes == null) return null;
        try {
            SecretKey originalKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
            return decryptMsg(cryptText, originalKey);
        } catch (Exception e) {
            Log.e("MyLoader", "Decryption error", e);
            return "Decryption failed";
        }
    }

    private String decryptMsg(byte[] cipherText, SecretKey secret) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        return new String(cipher.doFinal(cipherText));
    }
}