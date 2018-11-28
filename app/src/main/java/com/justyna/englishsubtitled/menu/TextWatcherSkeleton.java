package com.justyna.englishsubtitled.menu;

import android.text.TextWatcher;

/***
 * The only purpose of this interface is to override TextWatcher interface, so that it only needed
 * implementation of one non-default method, so that it could be done with a method reference.
 */
interface TextWatcherSkeleton extends TextWatcher {
    @Override
    default void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    default void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }
}
