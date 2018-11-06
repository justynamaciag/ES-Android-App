package com.justyna.englishsubtitled.menu;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.justyna.englishsubtitled.model.Achievement;
import com.justyna.englishsubtitled.model.AchievementsDetails;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AchievementsAdapter extends RecyclerView.Adapter<AchievementsAdapter.AchievementHolder> {
    private List<Achievement> dataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class AchievementHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;

        AchievementHolder(LinearLayout v) {
            super(v);
            linearLayout = v;
        }
    }

    AchievementsAdapter(List<Achievement> dataset) {
        this.dataset = dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AchievementHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        // create a new view
        LinearLayout linearLayout = new LinearLayout(parent.getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setWeightSum(100f);

        ImageView imageView = new ImageView(linearLayout.getContext());
        imageView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 10f));
        imageView.setImageResource(android.R.drawable.star_big_on);

        LinearLayout details = new LinearLayout(linearLayout.getContext());
        details.setOrientation(LinearLayout.VERTICAL);
        details.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 90f));
        details.setPadding(40, 0, 0, 0);

        TextView name = new TextView(details.getContext());
        name.setTextSize(TypedValue.COMPLEX_UNIT_PT, 10);
        TextView description = new TextView(details.getContext());
        description.setTextSize(TypedValue.COMPLEX_UNIT_PT, 7);
        TextView date = new TextView(details.getContext());
        date.setTextSize(TypedValue.COMPLEX_UNIT_PT, 7);

        details.addView(name, 0);
        details.addView(description, 1);
        details.addView(date, 2);

        linearLayout.addView(imageView, 0);
        linearLayout.addView(details, 1);

        return new AchievementHolder(linearLayout);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AchievementHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        AchievementsDetails achievement = AchievementsDetails.valueOf(dataset.get(position).getAchievement());

        LinearLayout description = (LinearLayout) holder.linearLayout.getChildAt(1);

        ((TextView) description.getChildAt(0)).setText(achievement.getPrettyName());

        ((TextView) description.getChildAt(1)).setText(achievement.getDescription());

        Date date = new Date(dataset.get(position).getTimestamp());
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        StringBuffer stringBuffer = new StringBuffer();
        formatter.format(date, stringBuffer, new FieldPosition(0));
        ((TextView) description.getChildAt(2)).setText(stringBuffer.toString());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
