package ru.mirea.ptitsyn.lesson4;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ru.mirea.ptitsyn.lesson4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.playButton.setOnClickListener(v -> {
            if (!isPlaying) {
                binding.statusTextView.setText("Playing...");
                isPlaying = true;
                Toast.makeText(this, "Playback started", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Already playing", Toast.LENGTH_SHORT).show();
            }
        });

        binding.stopButton.setOnClickListener(v -> {
            binding.statusTextView.setText("Stopped");
            isPlaying = false;
            Toast.makeText(this, "Playback stopped", Toast.LENGTH_SHORT).show();
        });
    }
}