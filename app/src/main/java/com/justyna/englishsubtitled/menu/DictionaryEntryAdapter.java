package com.justyna.englishsubtitled.menu;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.justyna.englishsubtitled.Configuration;
import com.justyna.englishsubtitled.model.Translation;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.List;

import static com.justyna.englishsubtitled.DisableSSLCertificateCheckUtil.disableChecks;

public class DictionaryEntryAdapter extends RecyclerView.Adapter<DictionaryEntryAdapter.MyViewHolder> {
    private List<Translation> dataset;
    private MenuDictionaryActivity caller;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linearLayout;

        public MyViewHolder(LinearLayout v) {
            super(v);
            linearLayout = v;
        }
    }

    public DictionaryEntryAdapter(List<Translation> dataset, MenuDictionaryActivity caller) {
        this.dataset = dataset;
        this.caller = caller;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DictionaryEntryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        // create a new view
        LinearLayout linearLayout = new LinearLayout(parent.getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setWeightSum(100f);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView englishWord = new TextView(linearLayout.getContext());
        TextView polishWord = new TextView(linearLayout.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 45f);
        englishWord.setLayoutParams(layoutParams);
        polishWord.setLayoutParams(layoutParams);
        englishWord.setTextSize(TypedValue.COMPLEX_UNIT_PT, 10);
        englishWord.setPadding(20, 0, 0, 0);
        polishWord.setTextSize(TypedValue.COMPLEX_UNIT_PT, 10);
        ImageButton remove = new ImageButton(linearLayout.getContext());
        remove.setBackgroundResource(android.R.drawable.ic_delete);
        remove.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 10f));
        linearLayout.addView(englishWord, 0);
        linearLayout.addView(polishWord, 1);
        linearLayout.addView(remove, 2);
        return new MyViewHolder(linearLayout);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Translation translation = dataset.get(position);
        ((TextView) holder.linearLayout.getChildAt(0)).setText(translation.getEngWord());
        ((TextView) holder.linearLayout.getChildAt(1)).setText(translation.getPlWord());
        ((ImageButton) holder.linearLayout.getChildAt(2)).setOnClickListener(view -> {
            boolean result = false;
            try {
                result = new DictionaryEntryRemover().execute(translation).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result) {
                caller.refreshDictionaryData();
                caller.refreshDictionaryView();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataset.size();
    }

    private static class DictionaryEntryRemover extends AsyncTask<Translation, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Translation... translations) {
            if (translations.length < 1) return false;
            Translation translation = translations[0];
            try {
                disableChecks();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String baseUrl = Configuration.getInstance().getBackendUrl();
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", accessToken.getToken());
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");

            Gson gson = new Gson();
            String json = gson.toJson(translation);

            HttpEntity<String> entity = new HttpEntity<>(json, headers);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

            ResponseEntity<String> result =
                    restTemplate.exchange(baseUrl + "/bookmarks/remove",
                            HttpMethod.PUT, entity, String.class);

            if (result.getStatusCode() != HttpStatus.OK) {
                System.out.println("Back-end responded with: " + result.getBody());
                return false;
            }
            return true;
        }
    }
}
