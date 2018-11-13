package com.example.hudson.tadszap.ViewHolderClass;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hudson.tadszap.R;

public class MensagemViewHolder extends RecyclerView.ViewHolder {

    private TextView textoMensagem;
    private TextView textoAutor;
    private TextView textoHoraData;
    private ImageView imageStorage;

    public MensagemViewHolder(View itemView) {
        super(itemView);
        textoMensagem = itemView.findViewById(R.id.textoMensagem);
        textoAutor = itemView.findViewById(R.id.textoAutor);
        textoHoraData = itemView.findViewById(R.id.textoDataHora);
        imageStorage = itemView.findViewById(R.id.imageStorage);
    }

    public TextView getTextoMensagem() {
        return textoMensagem;
    }

    public TextView getTextoAutor() {
        return textoAutor;
    }

    public TextView getTextoHoraData() {
        return textoHoraData;
    }

    public ImageView getImageStorage(){
        return imageStorage;
    }
}
