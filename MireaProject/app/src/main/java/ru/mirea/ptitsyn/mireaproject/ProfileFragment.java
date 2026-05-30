package ru.mirea.ptitsyn.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import ru.mirea.ptitsyn.mireaproject.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private SharedPreferences prefs;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        prefs = requireActivity().getSharedPreferences("user_profile", Context.MODE_PRIVATE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String name = prefs.getString("name", "");
        String email = prefs.getString("email", "");
        binding.etName.setText(name);
        binding.etEmail.setText(email);

        binding.btnSaveProfile.setOnClickListener(v -> {
            String newName = binding.etName.getText().toString();
            String newEmail = binding.etEmail.getText().toString();
            prefs.edit()
                    .putString("name", newName)
                    .putString("email", newEmail)
                    .apply();
            binding.tvProfileStatus.setText("Профиль сохранён: " + newName + ", " + newEmail);
        });
    }
}