package com.justyna.englishsubtitled.games.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.justyna.englishsubtitled.R;

public class LoadingFragment extends Fragment {
    TextView textView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        textView = new TextView(this.getContext());
        textView.setText(R.string.please_wait);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PT, 10);
        return textView;
    }

    public void reportLoadingFailure() {
        textView.setText(R.string.data_download_failure);
    }
}
