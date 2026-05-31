package ru.mirea.ptitsyn.mireaproject.retrofit;

import com.google.gson.annotations.SerializedName;

public class Fact {
    @SerializedName("text")
    private String text;

    public String getText() { return text; }
}
