package com.senierr.rvadapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_normal:
                startActivity(new Intent(this, NormalActivity.class));
                break;
            case R.id.btn_empty_error:
                startActivity(new Intent(this, EmptyAndErrorActivity.class));
                break;
            case R.id.btn_header_footer:
                startActivity(new Intent(this, HeaderFooterActivity.class));
                break;
            case R.id.btn_click:
                startActivity(new Intent(this, ItemClickActivity.class));
                break;
            case R.id.btn_selection:
                startActivity(new Intent(this, SelectionActivity.class));
                break;
            case R.id.btn_load_more:
                startActivity(new Intent(this, LoadMoreActivity.class));
                break;
        }
    }
}
