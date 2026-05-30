package ru.mirea.ptitsyn.notebook;

import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import ru.mirea.ptitsyn.notebook.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSave.setOnClickListener(v -> saveToFile());
        binding.btnLoad.setOnClickListener(v -> loadFromFile());
    }

    private String ensureTxtExtension(String fileName) {
        if (!fileName.toLowerCase().endsWith(".txt")) {
            return fileName + ".txt";
        }
        return fileName;
    }

    private void saveToFile() {
        String fileName = binding.etFileName.getText().toString().trim();
        if (fileName.isEmpty()) {
            binding.tvStatus.setText("Введите имя файла");
            return;
        }
        fileName = ensureTxtExtension(fileName);

        String quote = binding.etQuote.getText().toString();
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, fileName);
        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
            osw.write(quote);
            binding.tvStatus.setText("Сохранено в " + file.getAbsolutePath());
            binding.etFileName.setText(fileName);
        } catch (Exception e) {
            binding.tvStatus.setText("Ошибка: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        String fileName = binding.etFileName.getText().toString().trim();
        if (fileName.isEmpty()) {
            binding.tvStatus.setText("Введите имя файла");
            return;
        }
        fileName = ensureTxtExtension(fileName);

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(dir, fileName);
        if (!file.exists()) {
            binding.tvStatus.setText("Файл не найден: " + fileName);
            return;
        }
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(isr)) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            binding.etQuote.setText(sb.toString().trim());
            binding.tvStatus.setText("Загружено из " + file.getAbsolutePath());
            binding.etFileName.setText(fileName);
        } catch (Exception e) {
            binding.tvStatus.setText("Ошибка: " + e.getMessage());
        }
    }
}