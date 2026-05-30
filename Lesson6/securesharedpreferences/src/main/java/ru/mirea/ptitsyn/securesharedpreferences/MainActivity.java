package ru.mirea.ptitsyn.securesharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import java.io.IOException;
import java.security.GeneralSecurityException;
import ru.mirea.ptitsyn.securesharedpreferences.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private SharedPreferences securePrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            KeyGenParameterSpec keySpec = MasterKeys.AES256_GCM_SPEC;
            String masterKeyAlias = MasterKeys.getOrCreate(keySpec);

            securePrefs = EncryptedSharedPreferences.create(
                    "poet_prefs",
                    masterKeyAlias,
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        String savedPoet = securePrefs.getString("favorite_poet", "");
        binding.tvSavedName.setText("Сохранённое имя: " + (savedPoet.isEmpty() ? "не указано" : savedPoet));
        binding.etPoetName.setText(savedPoet);

        binding.btnSave.setOnClickListener(v -> {
            String poetName = binding.etPoetName.getText().toString();
            securePrefs.edit().putString("favorite_poet", poetName).apply();
            binding.tvSavedName.setText("Сохранённое имя: " + poetName);
        });
    }
}