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
import com.justyna.englishsubtitled.model.Achievement;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class MenuAchievementsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PleaseWaitAdapter pleaseWaitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_single_list);

        TextView title = findViewById(R.id.title);
        title.setText(R.string.achievements);

        recyclerView = findViewById(R.id.films);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        pleaseWaitAdapter = new PleaseWaitAdapter(this);
        recyclerView.setAdapter(pleaseWaitAdapter);

        new AchievementsRetriever().execute();
    }

    private class AchievementsRetriever extends AsyncTask<Void, Void, List<Achievement>> {
        @Override
        protected List<Achievement> doInBackground(Void... voids) {
            String baseUrl = Configuration.getInstance().getBackendUrl();
            HttpEntity<String> entity = ConnectionUtils.getInstance().prepareHttpEntity();

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<List<Achievement>> achievements;

            try {
                achievements = restTemplate.exchange(baseUrl + "/achievements",
                        HttpMethod.GET, entity, new ParameterizedTypeReference<List<Achievement>>() {
                        });
            } catch (Exception e) {
                this.cancel(true);
                return null;
            }

            return achievements.getBody();
        }

        @Override
        protected void onPostExecute(List<Achievement> achievements) {
            super.onPostExecute(achievements);
            RecyclerView.Adapter adapter = new AchievementsAdapter(achievements);
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            pleaseWaitAdapter.reportNoInternetConnection();
        }
    }
}
