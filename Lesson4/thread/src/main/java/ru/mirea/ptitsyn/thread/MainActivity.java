package ru.mirea.ptitsyn.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ru.mirea.ptitsyn.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Thread mainThread = Thread.currentThread();
        binding.threadNameTextView.setText("Название потока: " + mainThread.getName());
        mainThread.setName("MyMainThread");
        binding.threadNameTextView.append(" -> изменено на: " + mainThread.getName());

        binding.calculateButton.setOnClickListener(v -> {
            String totalStr = binding.totalLessonsEditText.getText().toString();
            String daysStr = binding.studyDaysEditText.getText().toString();
            if (totalStr.isEmpty() || daysStr.isEmpty()) {
                Toast.makeText(this, "Введите оба значения", Toast.LENGTH_SHORT).show();
                return;
            }
            int totalLessons = Integer.parseInt(totalStr);
            int studyDays = Integer.parseInt(daysStr);
            if (studyDays == 0) {
                Toast.makeText(this, "Количество дней не может быть 0", Toast.LENGTH_SHORT).show();
                return;
            }
            new Thread(() -> {
                double average = (double) totalLessons / studyDays;
                runOnUiThread(() -> binding.resultTextView.setText("Среднее количество пар в день: " + average));
            }).start();
        });

        binding.slowButton.setOnClickListener(v -> {
            new Thread(() -> {
                long endTime = System.currentTimeMillis() + 20 * 1000;
                while (System.currentTimeMillis() < endTime) {
                    // имитация работы
                }
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Heavy work done in background", Toast.LENGTH_SHORT).show());
            }).start();
            Toast.makeText(this, "Heavy work started in background", Toast.LENGTH_SHORT).show();
        });
    }
}
