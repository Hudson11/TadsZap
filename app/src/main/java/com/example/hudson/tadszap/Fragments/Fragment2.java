package com.example.hudson.tadszap.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hudson.tadszap.Adapters.RecyclerMensagemAdapter;
import com.example.hudson.tadszap.GestureDetector.MyRecyclerTouchEvent;
import com.example.hudson.tadszap.Modelo.Mensagem;
import com.example.hudson.tadszap.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class Fragment2 extends Fragment {

    private List<Mensagem> listaMensagem = new ArrayList<>();
    //Objetos do Firebase e do banco da dados
    private FirebaseDatabase mFirebase;
    private DatabaseReference mReference;

    //Objeto Child para escultar os eventos do Firebase
    private ChildEventListener mChildEventListener;

    //Objeto de AuthStateListener
    private FirebaseAuth.AuthStateListener authStateListener;
    //Objeto de FirebaseAuth
    private FirebaseAuth mFirebaseAuth;

    /*
     *       Objeto de AuthStateListener será adicionado ao FirebaseAuth
     *   em dois momentos, no onResumo() e onPause()
     * */

    //Objeto de FirebaseStorage e de Referencia ao Storage
    private FirebaseStorage mStorage;
    private StorageReference mStorageReference;

    private static final String ANONYMOUS = "anonymous";
    private static final int CODIGO_LOGAR = 55;
    private static final int CODIGO_PHOTO = 56;
    private String mUsername;

    private ImageView enviar;
    private ImageView camera;
    private EditText conteudo;

    /*
        Obejtos necessários para a implementação do RecyclerView do chat
    * */
    private RecyclerView recyclerView;
    private RecyclerMensagemAdapter adapter;

    //constructor
    public Fragment2(){
        //Required constructor empty
    }

    @Override
    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle saveInstanceState){
        View v = inflate.inflate(R.layout.fragment2, container, false);

        enviar = v.findViewById(R.id.enviar);
        camera = v.findViewById(R.id.camera);
        conteudo = v.findViewById(R.id.conteudo);

        mFirebase = FirebaseDatabase.getInstance();
        mReference = mFirebase.getReference().child("mensagens");
        mFirebaseAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();
        mUsername = ANONYMOUS;

        /*
        *   RECYCLERVIEW IMPLEMENTATION
        *   O resto da implementação fica dentro no ChildListener no Firebase
        * */
        recyclerView = v.findViewById(R.id.recylerViewMessage);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layout);

        //Ação para abrir o Maps cado seja um link correspondente
        recyclerView.addOnItemTouchListener(new MyRecyclerTouchEvent(getContext(), recyclerView, new MyRecyclerTouchEvent.OnItemClickListener() {
            @Override
            public void mapLinkTouchEvent(View view, int position) {
                Mensagem escolhida = listaMensagem.get(position);
                try {
                    String[] link = escolhida.getTexto().split("/");
                    if (link[0].equals("https:")){
                        Toast.makeText(getContext(), "String Esplitada", Toast.LENGTH_SHORT).show();
                        Uri uri = Uri.parse(escolhida.getTexto());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }else
                        return;
                }catch(NullPointerException e){

                }
            }
        }));


        //Ação para enviar mensagem
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(conteudo.getText().toString().equals(""))
                    return;;

                Mensagem m = new Mensagem(conteudo.getText().toString(),new Date(), mUsername, null);
                mReference.push().setValue(m);
                conteudo.setText("");
            }
        });

        //Ação para anexar foto ao chat
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "complete action using"), CODIGO_PHOTO);
            }
        });

        /*
        *   Controla o fluxo de telas de login do fragment3.
        * */
        authStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){
                    onSignInInitialize(user.getDisplayName());
                }else{
                    onSignOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setLogo(R.drawable.icon)
                                    .setTheme(R.style.AppTheme)
                                    .setAvailableProviders(
                                            Arrays.asList(
                                                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                                                    new AuthUI.IdpConfig.EmailBuilder().build()
                                            )
                                    )
                                    .build(),
                            CODIGO_LOGAR);
                }
            }
        };

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        mFirebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onPause(){
        super.onPause();
        if(authStateListener != null){
            mFirebaseAuth.removeAuthStateListener(authStateListener);
        }
        detachDatabaseReadListener();
        listaMensagem.clear();
    }

    public void onSignInInitialize(String userName){
        mUsername = userName;
        SharedPreferences prefs = getActivity().getSharedPreferences("PREFS-NAME", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", mUsername);
        editor.commit();
        attackDatabaseReadListener();
    }

    public void onSignOutCleanUp(){
        mUsername = ANONYMOUS;
        listaMensagem.clear();
        detachDatabaseReadListener();
    }

    private void attackDatabaseReadListener(){
        if(mChildEventListener == null){
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Mensagem m = dataSnapshot.getValue(Mensagem.class);
                    listaMensagem.add(m);
                    adapter = new RecyclerMensagemAdapter(getContext(), listaMensagem);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            mReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener(){
        if(mChildEventListener != null){
            mReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CODIGO_LOGAR){
            if(resultCode == RESULT_OK){
                Toast.makeText(getContext(), "USUARIO AUTENTICADO", Toast.LENGTH_SHORT).show();
            }else if(resultCode == RESULT_CANCELED){
                //finish();
            }
        } else if(requestCode == CODIGO_PHOTO && resultCode == RESULT_OK){
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
