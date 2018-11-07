package com.justyna.englishsubtitled.menu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.justyna.englishsubtitled.R;

public class PleaseWaitAdapter extends RecyclerView.Adapter<PleaseWaitAdapter.AchievementHolder> {
    private final TextView pleaseWait;

    public static class AchievementHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        AchievementHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

    PleaseWaitAdapter(Context context) {
        pleaseWait = new TextView(context);
        pleaseWait.setText(R.string.please_wait);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AchievementHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {

        return new AchievementHolder(pleaseWait);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AchievementHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView = pleaseWait;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 1;
    }
}
