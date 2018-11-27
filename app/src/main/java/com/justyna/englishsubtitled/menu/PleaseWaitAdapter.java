package com.justyna.englishsubtitled.menu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.justyna.englishsubtitled.R;

public class PleaseWaitAdapter extends RecyclerView.Adapter<PleaseWaitAdapter.AchievementHolder> {
    private final TextView message;

    public static class AchievementHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        AchievementHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

    PleaseWaitAdapter(Context context) {
        message = new TextView(context);
        message.setText(R.string.please_wait);
    }

    public void reportNoInternetConnection() {
        message.setText(R.string.data_download_failure);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AchievementHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {

        return new AchievementHolder(message);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AchievementHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView = message;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 1;
    }
}
