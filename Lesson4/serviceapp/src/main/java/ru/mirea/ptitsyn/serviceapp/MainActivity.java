package ru.mirea.ptitsyn.serviceapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import ru.mirea.ptitsyn.serviceapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS}, 200);
        }

        binding.playButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlayerService.class);
            startForegroundService(intent);
            binding.currentSongTextView.setText("zvezdochka mall main theme");
            binding.statusTextView.setText("Статус: Играет");
        });

        binding.stopButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlayerService.class);
            stopService(intent);
            binding.statusTextView.setText("Статус: Остановлено");
        });
    }
}