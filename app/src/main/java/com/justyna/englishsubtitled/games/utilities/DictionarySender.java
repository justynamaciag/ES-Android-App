package com.justyna.englishsubtitled.games.utilities;

import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.justyna.englishsubtitled.Configuration;
import com.justyna.englishsubtitled.ConnectionUtils;
import com.justyna.englishsubtitled.LessonsActivity;
import com.justyna.englishsubtitled.R;
import com.justyna.englishsubtitled.model.Translation;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.lang.ref.WeakReference;
import java.nio.charset.Charset;

public class DictionarySender {


    public void addToDict(Translation translation, LessonsActivity lessonsActivity) {

        new AddDictionary(lessonsActivity).execute(translation);

    }

    private class AddDictionary extends AsyncTask<Translation, Void, Boolean> {

        private WeakReference<LessonsActivity> activityReference;
        private Translation translation;

        AddDictionary(LessonsActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected Boolean doInBackground(Translation... translations) {
            if (translations.length < 1) return false;
            translation = translations[0];

            String baseUrl = Configuration.getInstance().getBackendUrl();

            Gson gson = new Gson();
            String json = gson.toJson(translation);

            HttpEntity<String> entity = ConnectionUtils.getInstance().prepareHttpEntity(json);


            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

            ResponseEntity<String> result;
            try {
                result = restTemplate.exchange(baseUrl + "/bookmarks/", HttpMethod.PUT, entity, String.class);
            } catch (Exception e) {
                this.cancel(true);
                return true;
            }

            if (result.getStatusCode() != HttpStatus.OK) {
                System.out.println("Back-end responded with: " + result.getBody());
                handleInsertionFailure();
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            activityReference.get().incrementDictionaryAdditions();
            Toast.makeText(activityReference.get(), R.string.dictionary_success, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            handleInsertionFailure();
        }

        private void handleInsertionFailure() {
            translation.setDictionaryAdded(false);
            Toast.makeText(activityReference.get(), R.string.dictionary_failure, Toast.LENGTH_LONG).show();
        }
    }

}
