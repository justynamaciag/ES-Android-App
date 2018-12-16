package com.justyna.englishsubtitled.games.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.justyna.englishsubtitled.R;

import java.util.List;

public class CrosswordAdapter extends BaseAdapter {

    private List<TextView> letters;
    private Context mContext;


    public CrosswordAdapter(Context mContext, List<TextView> letters) {
        this.mContext = mContext;
        this.letters = letters;
    }

    @Override
    public int getCount() {
        return letters.size();
    }

    @Override
    public TextView getItem(int position) {
        return letters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return letters.get(position).getId();

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.crossword, null);
        }

        TextView textView = view.findViewById(R.id.crossword);
        textView.setText(letters.get(position).getText());
        return textView;
    }
}
