package com.example.hudson.tadszap.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
;

import com.example.hudson.tadszap.Modelo.Mensagem;
import com.example.hudson.tadszap.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.Date;

import android.Manifest;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class Fragment1 extends Fragment {

    private static final Integer REQUEST_IMAGE_CAPTURE = 70;
    private String mCurrentPhotoPath;

    ImageView mPhotoView;
    ImageView photoCapture;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;

    private FirebaseDatabase mFirebase;
    private DatabaseReference mReference;

    private String mUsername;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View v = inflater.inflate(R.layout.fragment1, container, false);

        photoCapture = v.findViewById(R.id.imageCapture);
        mPhotoView = v.findViewById(R.id.mPhotoView);

        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference();

        mFirebase = FirebaseDatabase.getInstance();
        mReference = mFirebase.getReference();

        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  takePictureIntent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if  (takePictureIntent.resolveActivity(getActivity().getPackageManager())  !=  null)  {
                    startActivityForResult(takePictureIntent,  REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        SharedPreferences prefs = getActivity().getSharedPreferences("PREFS_NAME", MODE_PRIVATE);
        mUsername = prefs.getString("username", "Não Conhecido");

        return v;
    }

    private void getPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        else
            return;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data == null)
            return;

        else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Uri selectedImageUri = data.getData();
            StorageReference photoref = mStorageReference.child(mUsername + " " + selectedImageUri.getLastPathSegment());
            //para upload da imagem basta photoref.putFile(selectedImageUri);
            //addOnSuccessListener para saber quando a imagem foi enviada
            photoref.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get a URL to the uploaded content
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //Log.i("TESTE", uri.toString());
                            Mensagem m = new Mensagem(null, new Date(), mUsername, uri.toString());
                            mReference.push().setValue(m);
                        }
                    });
                }
            });
        }
    }

}
