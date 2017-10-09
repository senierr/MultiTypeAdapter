package com.senierr.demo;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

    protected Toast toast = null;

    protected void showToast(String toastStr) {
        if (toast == null) {
            toast = Toast.makeText(this, toastStr, Toast.LENGTH_SHORT);
        }
        toast.setText(toastStr);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}
