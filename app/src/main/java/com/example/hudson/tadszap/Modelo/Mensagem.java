package com.example.hudson.tadszap.Modelo;

import android.net.Uri;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Mensagem {

    private String texto;
    private Date horaData;
    private String autor;
    private String photoUri;

    public Mensagem(String texto, Date horaData, String autor, String photoUri){
        this.texto = texto;
        this.horaData = horaData;
        this.autor = autor;
        this.photoUri = photoUri;
    }

    /*
    * FIREBASE REQUIRED CONSTRUCTOR EMPTY
    * */
    public Mensagem(){
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Date getHoraData() {
        return horaData;
    }

    public void setHoraData(Date horaData) {
        this.horaData = horaData;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    /*
    *  Método para conversão da data e hora do sistema
    * */
    public  String getDataTime(){
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return format.format(horaData);
    }

}
