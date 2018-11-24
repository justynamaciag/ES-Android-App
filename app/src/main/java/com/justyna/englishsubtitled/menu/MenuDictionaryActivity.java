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

import java.text.Collator;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class MenuDictionaryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private final List<Translation> translations = new LinkedList<>();

    private SortingStrategy sortingStrategy = SortingStrategy.ENGLISH_ASCENDING;

    private SortingButtonState englishSortState;
    private SortingButtonState polishSortState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_dictionary);

        ImageButton englishSort = findViewById(R.id.englishSort);
        ImageButton polishSort = findViewById(R.id.polishSort);

        englishSortState = new SortingButtonState(englishSort, SortingButtonState.State.ASCENDING);
        polishSortState = new SortingButtonState(polishSort, SortingButtonState.State.NONE);

        recyclerView = findViewById(R.id.films);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new PleaseWaitAdapter(this));

        refreshDictionaryData();

        englishSort.setOnClickListener(view -> {
            polishSortState.setState(SortingButtonState.State.NONE);

            switch (englishSortState.getState()) {
                case NONE:
                case DESCENDING:
                    englishSortState.setState(SortingButtonState.State.ASCENDING);
                    sortingStrategy = SortingStrategy.ENGLISH_ASCENDING;
                    break;
                case ASCENDING:
                    englishSortState.setState(SortingButtonState.State.DESCENDING);
                    sortingStrategy = SortingStrategy.ENGLISH_DESCENDING;
                    break;
            }
            refreshDictionaryView();
        });

        polishSort.setOnClickListener(view -> {
            englishSortState.setState(SortingButtonState.State.NONE);

            switch (polishSortState.getState()) {
                case NONE:
                case DESCENDING:
                    polishSortState.setState(SortingButtonState.State.ASCENDING);
                    sortingStrategy = SortingStrategy.POLISH_ASCENDING;
                    break;
                case ASCENDING:
                    polishSortState.setState(SortingButtonState.State.DESCENDING);
                    sortingStrategy = SortingStrategy.POLISH_DESCENDING;
                    break;
            }
            refreshDictionaryView();
        });
    }

    protected void refreshDictionaryData() {
        new DictionaryRetriever().execute();
    }

    protected void refreshDictionaryView() {
        List<Translation> sortedTranslations = new LinkedList<>();
        synchronized (translations) {
            sort();
            sortedTranslations.addAll(translations);
        }

        // specify an adapter
        RecyclerView.Adapter adapter = new DictionaryEntryAdapter(sortedTranslations, this);
        recyclerView.setAdapter(adapter);
    }

    private void sort() {
        Collator collator = Collator.getInstance(new Locale("pl", "PL"));

        switch (sortingStrategy) {
            case ENGLISH_ASCENDING:
                Collections.sort(translations, (translation, t1) -> translation.getEngWord().compareTo(t1.getEngWord()));
                break;
            case ENGLISH_DESCENDING:
                Collections.sort(translations, (translation, t1) -> t1.getEngWord().compareTo(translation.getEngWord()));
                break;
            case POLISH_ASCENDING:
                Collections.sort(translations, (translation, t1) -> collator.compare(translation.getPlWord(), t1.getPlWord()));
                break;
            case POLISH_DESCENDING:
                Collections.sort(translations, (translation, t1) -> collator.compare(t1.getPlWord(), translation.getPlWord()));
        }
    }

    private enum SortingStrategy {
        ENGLISH_ASCENDING, ENGLISH_DESCENDING, POLISH_ASCENDING, POLISH_DESCENDING
    }

    private class DictionaryRetriever extends AsyncTask<Void, Void, List<Translation>> {
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

        @Override
        protected void onPostExecute(List<Translation> receivedTranslations) {
            super.onPostExecute(receivedTranslations);
            synchronized (translations) {
                translations.clear();
                translations.addAll(receivedTranslations);
            }
            refreshDictionaryView();
        }
    }
}
