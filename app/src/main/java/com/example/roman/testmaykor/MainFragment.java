package com.example.roman.testmaykor;

import android.app.Fragment;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;



public class MainFragment extends Fragment {

    final String BaseURL = "http://10.0.2.2:8080/multipartGlasfish/webapi/secured/message";
    public static final String PASSWORD = "password";
    public static final String LOGIN = "login";
    private CardView mCardViweLogin;
    private EditText mETxtUsername, mETxtPassword;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        mCardViweLogin = view.findViewById(R.id.cardViewLogin);
        mETxtUsername = view.findViewById(R.id.eTxtUsername);
        mETxtPassword = view.findViewById(R.id.eTxtPassword);

        mCardViweLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request();
            }
        });
        return view;
    }

    private void request() {
        if(checkNetwork()) {
            MyTask my = new MyTask();
            my.execute();
        } else {
            Toast.makeText(getActivity(), "Нет подключения к интернету", Toast.LENGTH_LONG).show();
        }
    }

    boolean checkNetwork(){
        final ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnected());
    }

    class MyTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            RequestMain requestMain = new RequestMain();
            return requestMain.fetch(BaseURL, mETxtUsername.getText().toString(), mETxtPassword.getText().toString());
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result){
                startSecondActivity();
            } else {
                makeToast();
            }
        }
    }

    void startSecondActivity(){
        Intent intent = new Intent(getActivity(), SendFileActivity.class);
        intent.putExtra(PASSWORD, mETxtPassword.getText().toString());
        intent.putExtra(LOGIN, mETxtUsername.getText().toString());
        startActivity(intent);
    }

    void makeToast(){
        Toast.makeText(getActivity(), "неверный логин или пароль", Toast.LENGTH_SHORT).show();
    }




}
