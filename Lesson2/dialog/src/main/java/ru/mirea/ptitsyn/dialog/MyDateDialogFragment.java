package ru.mirea.ptitsyn.dialog;

import android.app.DatePickerDialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyDateDialogFragment extends DatePickerDialog {
    public MyDateDialogFragment(@NonNull Context context, @Nullable OnDateSetListener listener, int year, int month, int dayOfMonth) {
        super(context, listener, year, month, dayOfMonth);
    }
}
