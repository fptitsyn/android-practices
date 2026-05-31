package ru.mirea.ptitsyn.mireaproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mirea.ptitsyn.mireaproject.databinding.FragmentNetworkBinding;
import ru.mirea.ptitsyn.mireaproject.retrofit.ApiService;
import ru.mirea.ptitsyn.mireaproject.retrofit.Fact;

public class NetworkFragment extends Fragment {
    private FragmentNetworkBinding binding;
    private ApiService apiService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNetworkBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://uselessfacts.jsph.pl/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        binding.btnLoadFact.setOnClickListener(v -> loadFact());
        loadFact(); // загрузить при старте
    }

    private void loadFact() {
        binding.tvFact.setText("Загрузка...");
        apiService.getRandomFact().enqueue(new Callback<Fact>() {
            @Override
            public void onResponse(Call<Fact> call, Response<Fact> response) {
                if (response.isSuccessful() && response.body() != null) {
                    binding.tvFact.setText(response.body().getText());
                } else {
                    binding.tvFact.setText("Ошибка загрузки");
                }
            }

            @Override
            public void onFailure(Call<Fact> call, Throwable t) {
                binding.tvFact.setText("Ошибка: " + t.getMessage());
            }
        });
    }
}