package com.justyna.englishsubtitled.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.justyna.englishsubtitled.LessonsActivity;
import com.justyna.englishsubtitled.model.Film;
import com.justyna.englishsubtitled.model.LessonSummary;

import java.util.List;

public class FilmsAdapter extends RecyclerView.Adapter<FilmsAdapter.MyViewHolder> {
    private List<Film> dataset;
    private AppCompatActivity caller;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;

        MyViewHolder(LinearLayout v) {
            super(v);
            linearLayout = v;
        }
    }

    FilmsAdapter(List<Film> dataset, AppCompatActivity caller) {
        this.dataset = dataset;
        this.caller = caller;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FilmsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        LinearLayout linearLayout = new LinearLayout(parent.getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView v = new TextView(linearLayout.getContext());
        linearLayout.addView(v, 0);
        return new MyViewHolder(linearLayout);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ((TextView) holder.linearLayout.getChildAt(0)).setText(dataset.get(position).getFilmTitle());

        holder.linearLayout.removeViews(1, holder.linearLayout.getChildCount() - 1);

        for (LessonSummary lessonSummary : dataset.get(position).getLessons()) {
            Button button = new Button(holder.linearLayout.getContext());
            button.setText(lessonSummary.lessonTitle);
            button.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            button.setOnClickListener(v -> {
                Intent intent = new Intent(caller, LessonsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                caller.startActivity(intent);
            });
            holder.linearLayout.addView(button);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
