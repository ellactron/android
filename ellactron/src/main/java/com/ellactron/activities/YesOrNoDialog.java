package com.ellactron.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;

/**
 * Created by ji.wang on 2017-07-19.
 */

public class YesOrNoDialog {
    public static void show(Activity parent,
                            Integer message,
                            Integer title,
                            DialogInterface.OnClickListener onOk,
                            DialogInterface.OnClickListener onCancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setIcon(R.mipmap.ic_alert);

        if (null != title)
            builder.setTitle(title);

        builder.setMessage(message)
                .setPositiveButton(R.string.button_ok, onOk)
                .setNegativeButton(R.string.button_cancel, onCancel);
        Dialog dialog = builder.create();

        try {
            dialog.show();
        } catch (Exception e) {
            Log.e(YesOrNoDialog.class.getName(), e.getMessage());
        }
    }
}
