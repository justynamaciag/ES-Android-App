package com.justyna.englishsubtitled.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.justyna.englishsubtitled.R;

import java.util.List;

public class WordButtonsAdapter extends BaseAdapter {

    customButtonListener customListener;
    private Context mContext;
    private List<Button> buttons;


    public interface customButtonListener {
        void onButtonClickListner(Button b);

    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListener = listener;
    }


    public WordButtonsAdapter(Context c, List<Button> buttons) {
        mContext = c;
        this.buttons = buttons;
    }

    @Override
    public int getCount() {
        return buttons.size();
    }

    @Override
    public Button getItem(int position) {
        return buttons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return buttons.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.buttons,  null);
        }

        Button button = (Button) view.findViewById(R.id.buttons);
        button.setText(buttons.get(position).getText());

        button.setOnClickListener(v -> {
            if (customListener != null) {
                customListener.onButtonClickListner(button);
            }
        });


        return button;

    }
}