package com.example.roman.testmaykor;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class SendFileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_file);

        Fragment fragment = new SendFileFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.contener_send_file, fragment);
        ft.commit();
    }
}


