package com.example.jokesapi;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.JokeHolder> {
    private final JokesActivity jokesActivity;
    private final ArrayList<JokeModel> strings;

    public JokeAdapter(JokesActivity jokesActivity, ArrayList<JokeModel> strings) {
        this.jokesActivity = jokesActivity;
        this.strings = strings;
    }

    @NonNull
    @Override
    public JokeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new JokeHolder(LayoutInflater.from(jokesActivity).inflate(R.layout.joke_design, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull JokeHolder jokeHolder, int i) {
        jokeHolder.tv.setText(strings.get(i).getModeljoke());


    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public class JokeHolder extends RecyclerView.ViewHolder {
        final TextView tv;

        JokeHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.jokes_text_view);
        }
    }
}
