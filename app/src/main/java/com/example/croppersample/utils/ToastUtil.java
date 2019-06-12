package com.example.croppersample.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {
    public static void shortMsg(Context context, String msg) {
        showMsg(context, msg, Toast.LENGTH_SHORT);
    }

    public static void longMsg(Context context, String msg) {
        showMsg(context, msg, Toast.LENGTH_LONG);
    }

    private static void showMsg(Context context, String msg, int length) {
        if (context == null || TextUtils.isEmpty(msg)) return;
        Toast toast = Toast.makeText(context, null, length);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setText(msg);
        toast.show();
    }
}
