package ru.mirea.ptitsyn.internalfilestorage;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import ru.mirea.ptitsyn.internalfilestorage.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final String FILE_NAME = "history_event.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSave.setOnClickListener(v -> {
            String event = binding.etEvent.getText().toString();
            String desc = binding.etDescription.getText().toString();
            String content = "Событие: " + event + "\nОписание: " + desc;
            try (FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE)) {
                fos.write(content.getBytes());
                binding.tvStatus.setText("Сохранено во внутреннем хранилище");
            } catch (IOException e) {
                binding.tvStatus.setText("Ошибка: " + e.getMessage());
            }
        });

        try (FileInputStream fis = openFileInput(FILE_NAME)) {
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            String content = new String(buffer);
        } catch (IOException ignored) {}
    }
}