package ru.mirea.ptitsyn.mireaproject.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("random.json?language=en")
    Call<Fact> getRandomFact();
}