package ru.mirea.ptitsyn.looper;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;
import ru.mirea.ptitsyn.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MyLooperThread myLooperThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myLooperThread = new MyLooperThread();
        myLooperThread.start();

        binding.sendButton.setOnClickListener(v -> {
            String ageStr = binding.ageEditText.getText().toString();
            String job = binding.jobEditText.getText().toString();

            int age = Integer.parseInt(ageStr);

            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putInt("age", age);
            bundle.putString("job", job);
            msg.setData(bundle);
            myLooperThread.getHandler().sendMessage(msg);

            binding.statusTextView.setText("Статус: обработка (задержка " + age + " сек)");
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myLooperThread.quit();
    }
}