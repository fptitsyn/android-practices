package ru.mirea.ptitsyn.cryptoloader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import ru.mirea.ptitsyn.cryptoloader.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private ActivityMainBinding binding;
    private static final int LOADER_ID = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.encryptButton.setOnClickListener(v -> {
            String plainText = binding.inputEditText.getText().toString();
            if (plainText.isEmpty()) {
                Toast.makeText(this, "Enter text", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                SecretKey key = generateKey();
                byte[] encrypted = encryptMsg(plainText, key);
                Bundle bundle = new Bundle();
                bundle.putByteArray(MyLoader.ARG_CRYPT_TEXT, encrypted);
                bundle.putByteArray(MyLoader.ARG_KEY, key.getEncoded());
                LoaderManager.getInstance(this).initLoader(LOADER_ID, bundle, this);
            } catch (Exception e) {
                Toast.makeText(this, "Encryption error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private SecretKey generateKey() throws Exception {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed("any seed".getBytes());
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(256, sr);
        return kg.generateKey();
    }

    private byte[] encryptMsg(String message, SecretKey secret) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        return cipher.doFinal(message.getBytes());
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new MyLoader(this, args);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        Snackbar.make(binding.getRoot(), "Decrypted: " + data, Snackbar.LENGTH_LONG).show();
        LoaderManager.getInstance(this).destroyLoader(loader.getId());
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) { }
}