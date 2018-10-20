package com.justyna.englishsubtitled.menu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.justyna.englishsubtitled.Configuration;
import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.model.Film;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static com.justyna.englishsubtitled.DisableSSLCertificateCheckUtil.disableChecks;

public class MenuFindLessonActivity extends AppCompatActivity {
    private TextView title;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_lessons_in_films);

        title = findViewById(R.id.title);
        title.setText(R.string.start_new_lesson);

        recyclerView = findViewById(R.id.films);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<Film> films = Collections.emptyList();
        try {
            films = new FilmsRetriever().execute().get();
        } catch (Exception e) {
            System.out.println("CRITICAL: Failed to download films list from a server.");
        }

        Collections.sort(films, (film, t1) -> film.filmTitle.compareTo(t1.filmTitle));

        // specify an adapter
        adapter = new FilmsAdapter(films, this);
        recyclerView.setAdapter(adapter);
    }

    private static class FilmsRetriever extends AsyncTask<Void, Void, List<Film>> {
        @Override
        protected List<Film> doInBackground(Void... voids) {
            try {
                disableChecks();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String baseUrl = Configuration.getInstance().getBackendUrl();
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", accessToken.getToken());
            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<List<Film>> progress =
                    restTemplate.exchange(baseUrl + "/films/",
                            HttpMethod.GET, entity, new ParameterizedTypeReference<List<Film>>() {
                            });

            return progress.getBody();
        }
    }
}
