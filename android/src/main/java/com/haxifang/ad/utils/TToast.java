package com.haxifang.ad.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;


public final class TToast {
    private static Toast sToast;

    public static void show(Context context, String msg) {
        show(context, msg, Toast.LENGTH_LONG);
    }

    public static void show(Context context, String msg, int duration) {
        Toast toast = getToast(context);
        if (toast != null) {
            toast.setDuration(duration);
            toast.setText(String.valueOf(msg));
            toast.show();
        } else {
            Log.i("TToast", "toast msg: " + String.valueOf(msg));
        }
    }

    @SuppressLint("ShowToast")
    private static Toast getToast(Context context) {
        if (context == null) {
            return sToast;
        }
        if (sToast == null) {
            synchronized (TToast.class) {
                if (sToast == null) {
                    sToast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_LONG);
                }
            }
        }
        return sToast;
    }

}
