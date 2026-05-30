package ru.mirea.ptitsyn.lesson6;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import ru.mirea.ptitsyn.lesson6.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private SharedPreferences sharedPref;
    private static final String PREFS_NAME = "mirea_settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        String group = sharedPref.getString("GROUP", "");
        int number = sharedPref.getInt("NUMBER", 0);
        String movie = sharedPref.getString("MOVIE", "");
        binding.etGroup.setText(group);
        binding.etNumber.setText(String.valueOf(number));
        binding.etMovie.setText(movie);

        binding.btnSave.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("GROUP", binding.etGroup.getText().toString());
            int num = Integer.parseInt(binding.etNumber.getText().toString());
            editor.putInt("NUMBER", num);
            editor.putString("MOVIE", binding.etMovie.getText().toString());
            editor.apply();
        });
    }
}