package com.codemunger.bangaloretrafficticket;

import android.support.v4.app.Fragment;
import  android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.frame_container);
        if (f == null) {
            f =new TicketListFragment();
            fm.beginTransaction().add(R.id.frame_container,f).commit();

        }
    }

}
