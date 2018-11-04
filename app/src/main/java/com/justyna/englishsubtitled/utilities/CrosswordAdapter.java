package com.justyna.englishsubtitled.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.justyna.englishsubtitled.R;

import java.util.List;

public class CrosswordAdapter extends BaseAdapter {

    customTVListener customTVListener;
    private List<TextView> letters;
    private Context mContext;

    public interface customTVListener {
        boolean onTVClickListner(int position, TextView tv, MotionEvent event);

    }

    public void setCustomTVListner(customTVListener listener) {
        this.customTVListener = listener;
    }

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

        TextView textView = (TextView) view.findViewById(R.id.crossword);
        textView.setText(letters.get(position).getText());

//        textView.setOnTouchListener((v,e) -> {
//            System.out.println(letters.indexOf(textView));
//            if (customTVListener != null) {
//                return customTVListener.onTVClickListner(letters.indexOf(textView), textView, e);
//            }
//            return false;
//        });
        return textView;
    }
}
