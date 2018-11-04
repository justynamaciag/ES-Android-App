package com.justyna.englishsubtitled.utils;

import android.os.AsyncTask;

import com.facebook.AccessToken;
import com.justyna.englishsubtitled.Configuration;
import com.justyna.englishsubtitled.LessonRetriever;
import com.justyna.englishsubtitled.model.Translation;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.justyna.englishsubtitled.DisableSSLCertificateCheckUtil.disableChecks;

public class DictionarySender {

    public static Boolean addToDict(Translation translation){

        Boolean result;
        try {
            result = new AddDictionary().execute(translation).get();
        }catch (InterruptedException | ExecutionException e) {
            System.out.println(e);
            result = false;
        }
        return result;
    }

    private static class AddDictionary extends AsyncTask<Translation, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Translation... translations) {
            if(translations.length < 1) return false;
            Translation translation = (Translation) translations[0];


            try {
                disableChecks();
            } catch (Exception e) {
                e.printStackTrace();
            }

            String baseUrl = Configuration.getInstance().getBackendUrl();
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", accessToken.getToken());


            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("engWord", translation.getEngWord());
            body.add("plWord", translation.getPlWord());

            HttpEntity<?> entity = new HttpEntity<Object>(headers, body);

            RestTemplate restTemplate = new RestTemplate();

            System.out.println(entity.getBody());


            ResponseEntity<String> result =
                    restTemplate.exchange(baseUrl + "/bookmarks", HttpMethod.PUT, entity, String.class);

            if (result.getStatusCode() != HttpStatus.OK) {
                System.out.println("Back-end responded with: " + result.getBody());
                return true;
            }
            return false;
        }
    }

}
