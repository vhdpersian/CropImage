package com.example.ui.uitemplate;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.pm.PackageManager;
import android.os.DeadObjectException;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    AccountManager accountManager;
    ListView list_deal;
    final int[] imgs = new int[]{
            R.drawable.temp_deal_img,
            R.drawable.temp_deal_img,
            R.drawable.temp_deal_img,
            R.drawable.temp_deal_img,
            R.drawable.temp_deal_img,
            R.drawable.temp_deal_img,
            R.drawable.temp_deal_img,
            R.drawable.temp_deal_img,
            R.drawable.temp_deal_img,
            R.drawable.temp_deal_img,
            R.drawable.temp_deal_img,
            R.drawable.temp_deal_img,
            R.drawable.temp_deal_img
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {

            list_deal = (ListView) findViewById(R.id.list_deals);
            DealAdapter dealAdapter=new DealAdapter(MainActivity.this,imgs);
            list_deal.setAdapter(dealAdapter);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    1);

        }
        else
        {
          

            list_deal = (ListView) findViewById(R.id.list_deals);
            DealAdapter dealAdapter=new DealAdapter(MainActivity.this,imgs);
            list_deal.setAdapter(dealAdapter);

        }


    }
}
