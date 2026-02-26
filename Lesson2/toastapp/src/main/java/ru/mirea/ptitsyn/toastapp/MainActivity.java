package ru.mirea.ptitsyn.toastapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            editText = findViewById(R.id.editTextText);
            button = findViewById(R.id.button);

            View.OnClickListener onClickListener = v1 -> {
                int count = editText.getText().length();
                Toast.makeText(getApplicationContext(),
                        String.format(Locale.ROOT, "СТУДЕНТ № %d ГРУППА %d Количество символов - %d", count, count, count),
                        Toast.LENGTH_LONG).show();
            };
            
            button.setOnClickListener(onClickListener);

            return insets;
        });
    }
}