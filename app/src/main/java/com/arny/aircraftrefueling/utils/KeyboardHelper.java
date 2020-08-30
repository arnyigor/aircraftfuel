package com.arny.aircraftrefueling.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardHelper {

    public static void hideKeyboard(Activity activity) {
        hideKeyboard(activity, 0);
    }

    public static void hideKeyboard(Activity activity, int flags) {
        try {
            if (activity != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                View focus = activity.getWindow().getDecorView().getRootView();
                if (focus != null && imm != null) {
                    imm.hideSoftInputFromWindow(focus.getWindowToken(), flags);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboard(Context context, View view) {
        hideKeyboard(context, view, 0);
    }

    public static void hideKeyboard(Context context, View view, int flags) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), flags);
        }
    }
}