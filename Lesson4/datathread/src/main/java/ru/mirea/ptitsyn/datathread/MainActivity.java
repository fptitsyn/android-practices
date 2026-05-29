package ru.mirea.ptitsyn.datathread;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ru.mirea.ptitsyn.datathread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        appendText("=== Starting sequence test ===");

        // 1. runOnUiThread – немедленно
        runOnUiThread(() -> appendText("1. runOnUiThread: выполняется немедленно в UI-потоке"));

        // 2. post – отправляет в очередь MessageQueue, выполнится после текущего кода
        handler.post(() -> appendText("2. Handler.post: выполняется после завершения текущего кода"));

        // 3. postDelayed – с задержкой 2000 мс
        handler.postDelayed(() -> appendText("3. postDelayed (2000 мс): выполняется после задержки"), 2000);

        // 4. View.post – аналогично handler.post, но привязан к View
        binding.infoTextView.post(() -> appendText("4. View.post: выполняется после текущего этапа разметки"));

        // 5. Ещё один runOnUiThread после задержки
        handler.postDelayed(() -> runOnUiThread(() -> appendText("5. runOnUiThread после задержки: также выполняется немедленно при вызове")), 1000);

        handler.postDelayed(() -> {
            appendText("\n--- Различия ---");
            appendText("• runOnUiThread: запускает Runnable немедленно в UI-потоке, если вызван не из UI-потока, иначе синхронно.");
            appendText("• Handler.post: помещает в MessageQueue, выполняется по порядку после текущего сообщения.");
            appendText("• View.post: аналогично Handler.post, но гарантирует выполнение после того, как View будет присоединена.");
            appendText("• postDelayed: аналогично post, но с задержкой.");
            appendText("Последовательность: runOnUiThread (синхронно), затем View.post, Handler.post, затем отложенные задачи.");
        }, 3000);
    }

    private void appendText(String text) {
        runOnUiThread(() -> {
            String current = binding.infoTextView.getText().toString();
            binding.infoTextView.setText(current + "\n" + text);
            Log.d("DataThread", text);
        });
    }
}