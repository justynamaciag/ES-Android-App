package com.justyna.englishsubtitled.menu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.justyna.englishsubtitled.Configuration;
import com.justyna.englishsubtitled.ConnectionUtils;
import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.model.Film;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

public class MenuFindLessonActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_single_list);

        TextView title = findViewById(R.id.title);
        title.setText(R.string.start_new_lesson);

        recyclerView = findViewById(R.id.films);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new PleaseWaitAdapter(this));

        new FilmsRetriever().execute();
    }

    private class FilmsRetriever extends AsyncTask<Void, Void, List<Film>> {
        @Override
        protected List<Film> doInBackground(Void... voids) {
            String baseUrl = Configuration.getInstance().getBackendUrl();
            HttpEntity<String> entity = ConnectionUtils.getInstance().prepareHttpEntity();

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<List<Film>> progress =
                    restTemplate.exchange(baseUrl + "/films/",
                            HttpMethod.GET, entity, new ParameterizedTypeReference<List<Film>>() {
                            });

            return progress.getBody();
        }

        @Override
        protected void onPostExecute(List<Film> films) {
            super.onPostExecute(films);

            Collections.sort(films, (film, t1) -> film.filmTitle.compareTo(t1.filmTitle));

            // specify an adapter
            RecyclerView.Adapter adapter = new FilmsAdapter(films, MenuFindLessonActivity.this);
            recyclerView.setAdapter(adapter);
        }
    }
}
