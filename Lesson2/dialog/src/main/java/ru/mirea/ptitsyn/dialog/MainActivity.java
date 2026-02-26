package ru.mirea.ptitsyn.dialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private CoordinatorLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        layout = findViewById(R.id.coordinatorLayout);
    }

    public void onClickShowDialog(View view) {
        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "mirea");
    }

    public void onOkClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"Иду дальше\"!",
                Toast.LENGTH_LONG).show();
    }
    public void onCancelClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"Нет\"!",
                Toast.LENGTH_LONG).show();
    }
    public void onNeutralClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"На паузе\"!",
                Toast.LENGTH_LONG).show();
    }

    public void onClickShowTimeDialog(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Snackbar snackbar = Snackbar
                        .make(layout, String.format(Locale.ROOT,
                                "%d часов %d минут", hourOfDay, minute),
                                Snackbar.LENGTH_LONG)
                        .setAction("UNDO", v ->
                                Toast.makeText(
                                                MainActivity.this,
                                                "Undo Clicked",
                                                Toast.LENGTH_SHORT
                                        )
                                        .show());

                snackbar.show();
            }
        };

        MyTimeDialogFragment timeDialogFragment = new MyTimeDialogFragment(this,
                onTimeSetListener, 12, 0, true);
        timeDialogFragment.show();
    }

    public void onClickShowDateDialog(View view) {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Snackbar snackbar = Snackbar
                        .make(layout, String.format(Locale.ROOT,
                                        "%d число %d месяца %d года", dayOfMonth, month, year),
                                Snackbar.LENGTH_LONG)
                        .setAction("UNDO", v ->
                                Toast.makeText(
                                                MainActivity.this,
                                                "Undo Clicked",
                                                Toast.LENGTH_SHORT
                                        )
                                        .show());

                snackbar.show();
            }
        };

        MyDateDialogFragment dateDialogFragment = new MyDateDialogFragment(MainActivity.this,
                onDateSetListener, 2026, 1, 1);
        dateDialogFragment.show();
    }

    public void onClickShowProgressDialog(View view) {

    }
}