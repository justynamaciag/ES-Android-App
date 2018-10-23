package com.justyna.englishsubtitled.menu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;

import com.justyna.englishsubtitled.Configuration;
import com.justyna.englishsubtitled.ConnectionUtils;
import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.model.Translation;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

public class MenuDictionaryActivity extends AppCompatActivity {
    private ImageButton englishAscending;
    private ImageButton englishDescending;
    private ImageButton polishAscending;
    private ImageButton polishDescending;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Translation> translations = Collections.emptyList();
    private SortingStrategy sortingStrategy = SortingStrategy.ENGLISH_ASCENDING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_dictionary);

        englishAscending = findViewById(R.id.englishAscending);
        englishDescending = findViewById(R.id.englishDescending);
        polishAscending = findViewById(R.id.polishAscending);
        polishDescending = findViewById(R.id.polishDescending);

        recyclerView = findViewById(R.id.films);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        refreshDictionaryData();
        refreshDictionaryView();

        englishAscending.setOnClickListener(view -> {
            sortingStrategy = SortingStrategy.ENGLISH_ASCENDING;
            refreshDictionaryView();
        });
        englishDescending.setOnClickListener(view -> {
            sortingStrategy = SortingStrategy.ENGLISH_DESCENDING;
            refreshDictionaryView();
        });
        polishAscending.setOnClickListener(view -> {
            sortingStrategy = SortingStrategy.POLISH_ASCENDING;
            refreshDictionaryView();
        });
        polishDescending.setOnClickListener(view -> {
            sortingStrategy = SortingStrategy.POLISH_DESCENDING;
            refreshDictionaryView();
        });
    }

    protected void refreshDictionaryData() {
        try {
            translations = new DictionaryRetriever().execute().get();
        } catch (Exception e) {
            System.out.println("CRITICAL: Failed to download translation list from a server.");
        }
    }

    protected void refreshDictionaryView() {
        sort();

        // specify an adapter
        adapter = new DictionaryEntryAdapter(translations, this);
        recyclerView.setAdapter(adapter);
    }

    private void sort() {
        switch (sortingStrategy) {
            case ENGLISH_ASCENDING:
                Collections.sort(translations, (translation, t1) -> translation.getEngWord().compareTo(t1.getEngWord()));
                break;
            case ENGLISH_DESCENDING:
                Collections.sort(translations, (translation, t1) -> t1.getEngWord().compareTo(translation.getEngWord()));
                break;
            case POLISH_ASCENDING:
                Collections.sort(translations, (translation, t1) -> translation.getPlWord().compareTo(t1.getPlWord()));
                break;
            case POLISH_DESCENDING:
                Collections.sort(translations, (translation, t1) -> t1.getPlWord().compareTo(translation.getPlWord()));
        }
    }

    private enum SortingStrategy {
        ENGLISH_ASCENDING, ENGLISH_DESCENDING, POLISH_ASCENDING, POLISH_DESCENDING
    }

    private static class DictionaryRetriever extends AsyncTask<Void, Void, List<Translation>> {
        @Override
        protected List<Translation> doInBackground(Void... voids) {
            String baseUrl = Configuration.getInstance().getBackendUrl();
            HttpEntity<String> entity = ConnectionUtils.getInstance().prepareHttpEntity();

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<List<Translation>> progress =
                    restTemplate.exchange(baseUrl + "/bookmarks/",
                            HttpMethod.GET, entity, new ParameterizedTypeReference<List<Translation>>() {
                            });

            return progress.getBody();
        }
    }
}
