package com.example.roman.testmaykor;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.Map;



public class SendFileFragment extends Fragment {

    private EditText mETxtFirstName, mETxtLastName, mETxtPhoto;
    private Button mBtn;
    private final String URL = "http://10.0.2.2:8080/multipartGlasfish/webapi/secured/upload";
    private File file;
    private String mLogin, mPassword;
    private boolean iSPhotoPickUp = false;
    public static final int PICK_IMAGE = 10;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_file, null);
        mBtn = view.findViewById(R.id.button);
        mETxtPhoto = view.findViewById(R.id.eTxtPhoto);
        mETxtFirstName = view.findViewById(R.id.eTxtFirstName);
        mETxtLastName = view.findViewById(R.id.eTxtLastName);
        mETxtPhoto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    requestPermission();
                    photoIntent();
                }
                return false;
            }
        });

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(iSPhotoPickUp){
                    MyTask myTask = new MyTask();
                    myTask.execute();
                }
            }
        });

        mPassword = getActivity().getIntent().getStringExtra(MainFragment.PASSWORD);
        mLogin = getActivity().getIntent().getStringExtra(MainFragment.LOGIN);
        return view;
    }
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},23
                );
            }
        }
    }

    void photoIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyApplication");
        file = new File(mediaStorageDir.getPath() + File.separator +
                "passport.jpeg");
        Uri picUri = Uri.fromFile(file); // create
        intent.putExtra(MediaStore.EXTRA_OUTPUT,picUri);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == getActivity().RESULT_OK){
            mETxtPhoto.setText("Готово");
            iSPhotoPickUp = true;
        }
    }

    class MyTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return makeRequest();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result){
                makeToast();
            }
        }
    }

    Boolean makeRequest(){
        Map<String, String> param = new HashMap<String, String>(2);
        param.put("firstName", mETxtFirstName.getText().toString());
        param.put("lastName", mETxtLastName.getText().toString());
        String basicAuth = "Basic " + Base64.encodeToString((mLogin + ":" + mPassword).getBytes(), Base64.NO_WRAP);
        RequestSendFile requestSendFile = new RequestSendFile();
        return requestSendFile.multipartRequest(URL, param, file.toString(), basicAuth,"file", "image/jpeg");
    }

    void makeToast(){
        Toast.makeText(getActivity(), "Запрос отправлен", Toast.LENGTH_LONG).show();
    }
}
