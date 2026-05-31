package ru.mirea.ptitsyn.lesson7;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ru.mirea.ptitsyn.lesson7.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.timeButton.setOnClickListener(v -> getTimeFromServer());
    }

    private void getTimeFromServer() {
        executor.execute(() -> {
            try {
                Socket socket = new Socket("time-a.nist.gov", 13);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                reader.readLine();
                String secondLine = reader.readLine();
                socket.close();

                String parsed = parseTimeString(secondLine);
                mainHandler.post(() -> binding.timeTV.setText(parsed));
            } catch (Exception e) {
                mainHandler.post(() -> binding.timeTV.setText("Ошибка: " + e.getMessage()));
            }
        });
    }

    private String parseTimeString(String line) {
        // Формат: "59735 25-05-30 08:15:42 50 0 0 123.4 UTC(NIST) *"
        String[] parts = line.split(" ");
        if (parts.length >= 3) {
            String datePart = parts[1]; // "25-05-30" (YY-MM-DD)
            String timePart = parts[2]; // "08:15:42"
            return "Дата: " + datePart + "\nВремя: " + timePart + " UTC";
        }
        return "Не удалось разобрать: " + line;
    }
}