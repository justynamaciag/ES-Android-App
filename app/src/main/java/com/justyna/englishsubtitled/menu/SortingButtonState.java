package com.justyna.englishsubtitled.menu;

import android.widget.ImageButton;

import com.justyna.englishsubtitled.R;

final class SortingButtonState {
    private State state;
    private ImageButton button;

    SortingButtonState(ImageButton button, State state) {
        this.button = button;
        this.state = state;
        this.button.setImageResource(state.resource);
    }

    void setState(State state) {
        this.state = state;
        button.setImageResource(state.resource);
    }

    State getState() {
        return state;
    }

    enum State {
        ASCENDING(R.drawable.icons8_sort_up_50),
        DESCENDING(R.drawable.icons8_sort_down_50),
        NONE(R.drawable.icons8_sort_50);

        private int resource;

        State(int resource) {
            this.resource = resource;
        }
    }
}
