package com.example.hudson.tadszap.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.hudson.tadszap.Modelo.Mensagem;
import com.example.hudson.tadszap.R;
import com.example.hudson.tadszap.ViewHolderClass.MensagemViewHolder;

import java.util.List;

public class RecyclerMensagemAdapter extends RecyclerView.Adapter{

    Context context;
    List<Mensagem> lista;

    public RecyclerMensagemAdapter(Context context, List<Mensagem> lista) {
        this.context = context;
        this.lista = lista;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup container, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_message, container, false);
        MensagemViewHolder viewHolder = new MensagemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MensagemViewHolder holder = (MensagemViewHolder) viewHolder;
        Mensagem msEscolhida = lista.get(position);
        holder.getTextoMensagem().setText(msEscolhida.getTexto());
        holder.getTextoAutor().setText(msEscolhida.getAutor());
        holder.getTextoHoraData().setText(msEscolhida.getDataTime());

        if(msEscolhida.getPhotoUri() == null) {
            holder.getImageStorage().setVisibility(View.GONE);
            holder.getTextoMensagem().setVisibility(View.VISIBLE);
            return;
        } else{
            holder.getTextoMensagem().setVisibility(View.GONE);
            holder.getImageStorage().setVisibility(View.VISIBLE);
            Glide.with(holder.getImageStorage().getContext())
                    .load(msEscolhida.getPhotoUri())
                    .into(holder.getImageStorage());
        }
    }

    @Override
    public int getItemCount() {
        return lista == null ? 0: lista.size();
    }
}
