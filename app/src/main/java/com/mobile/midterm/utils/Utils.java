package com.mobile.midterm.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.mobile.midterm.R;

public class Utils {
    public static void showSnackBar(View view, String message){
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.close, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });

        snackbar.show();
    }

    public static void showAlertDialog(int gravity, String message, Context mContext){
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.message_alert);

        Window window = dialog.getWindow();

        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttribute  = window.getAttributes();
        windowAttribute.gravity = gravity;
        window.setAttributes(windowAttribute);
        dialog.setCancelable(true);

        Button mainButton = (Button) dialog.findViewById(R.id.alertMainButton);
        TextView alertMessage = (TextView) dialog.findViewById(R.id.alertBody);
        alertMessage.setText(message);

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
