package ru.mirea.ptitsyn.mireaproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import ru.mirea.ptitsyn.mireaproject.databinding.FragmentPermissionBinding;

public class PermissionFragment extends Fragment {
    private FragmentPermissionBinding binding;

    private final ActivityResultLauncher<String> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    binding.tvPermissionStatus.setText("Камера: РАЗРЕШЕНА");
                } else {
                    binding.tvPermissionStatus.setText("Камера: ЗАПРЕЩЕНА");
                }
            });

    private final ActivityResultLauncher<String> micLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    binding.tvPermissionStatus.setText("Микрофон: РАЗРЕШЕН");
                } else {
                    binding.tvPermissionStatus.setText("Микрофон: ЗАПРЕЩЕН");
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPermissionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnCheckCamera.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                binding.tvPermissionStatus.setText("Камера: РАЗРЕШЕНА");
            } else {
                cameraLauncher.launch(Manifest.permission.CAMERA);
            }
        });

        binding.btnCheckMicrophone.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                binding.tvPermissionStatus.setText("Микрофон: РАЗРЕШЕН");
            } else {
                micLauncher.launch(Manifest.permission.RECORD_AUDIO);
            }
        });
    }
}