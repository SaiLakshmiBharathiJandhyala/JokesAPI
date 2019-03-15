package com.example.jokesapi;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class JokesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private final int loader_id = 20;
    private final String jokes_url = "http://api.icndb.com/jokes/random/";
    private RecyclerView rv;
    private ArrayList<JokeModel> jokeModels;
    private String strings;
    private ProgressBar progressBar;
    int positionIndex;
    private LinearLayoutManager llmanager;
    int topView;
    private long currentVisiblePosition;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jokes);
        rv = findViewById(R.id.jokes_recycler);
        strings = getIntent().getStringExtra("num");
        progressBar = findViewById(R.id.progress_bar);
        jokeModels = new ArrayList<>();
        llmanager = new LinearLayoutManager(this);
        rv.setLayoutManager(llmanager);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean connected = (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) || (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
        if (connected)
            getSupportLoaderManager().initLoader(loader_id, null, JokesActivity.this);
        else
            Toast.makeText(this, "NO INTERNET", Toast.LENGTH_SHORT).show();

    }

    @NonNull
    @Override
    public Loader onCreateLoader(int i, @Nullable Bundle bundle) {
        return new AsyncTaskLoader<String>(this) {
            @Nullable
            @Override
            public String loadInBackground() {
                try {
                    URL url = new URL(jokes_url.concat(strings));
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.connect();
                    InputStream is = httpURLConnection.getInputStream();
                    Scanner sn = new Scanner(is);
                    sn.useDelimiter("\\A");
                    if (sn.hasNext())
                        return sn.next();
                    return null;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                jokeModels.clear();
                progressBar.setVisibility(View.VISIBLE);
                forceLoad();

            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        if (s != null) {
            progressBar.setVisibility(View.GONE);
            try {
                JSONObject root = new JSONObject(s);
                JSONArray values = root.getJSONArray("value");
                for (int i = 0; i < values.length(); i++) {
                    JSONObject jokeIndex = values.getJSONObject(i);
                    String joke = jokeIndex.getString("joke");
                    jokeModels.add(new JokeModel(joke));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            rv.setAdapter(new JokeAdapter(JokesActivity.this, jokeModels));
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        currentVisiblePosition = 0;
        currentVisiblePosition = ((LinearLayoutManager) Objects.requireNonNull(rv.getLayoutManager())).findFirstCompletelyVisibleItemPosition();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Objects.requireNonNull(rv.getLayoutManager()).scrollToPosition((int) currentVisiblePosition);
        currentVisiblePosition = 0;
    }
}
