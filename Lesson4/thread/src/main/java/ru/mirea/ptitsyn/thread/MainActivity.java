package ru.mirea.ptitsyn.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ru.mirea.ptitsyn.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int counter = 0;

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

            int totalLessons = Integer.parseInt(totalStr);
            int studyDays = Integer.parseInt(daysStr);

            new Thread(() -> {
                double average = (double) totalLessons / studyDays;
                runOnUiThread(() -> binding.resultTextView.setText("Среднее количество пар в день: " + average));
            }).start();
        });

        binding.slowButton.setOnClickListener(v -> {
            new Thread(new Runnable() {
                public void run() {
                    int numberThread = counter++;
                    Log.d("ThreadProject", String.format("Запущен поток № %d студентом группы № %s номер по списку № %d ", numberThread, "БСБО-08-23", 20));
                    long endTime = System.currentTimeMillis() + 20 * 1000;
                    while (System.currentTimeMillis() < endTime) {
                        synchronized (this) {
                            try {
                                wait(endTime - System.currentTimeMillis());
                                Log.d(MainActivity.class.getSimpleName(), "Endtime: " + endTime);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        Log.d("ThreadProject", "Выполнен поток № " + numberThread);
                    }
                }
            }).start();
        });
    }
}
