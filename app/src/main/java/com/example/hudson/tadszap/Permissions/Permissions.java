package com.example.hudson.tadszap.Permissions;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissions {

    public static boolean validarPermissao(String[]permissoes, Activity fragment, int requestcode){
        //verifica se o usuario esta usando a versao marshmallow em diante

        if(Build.VERSION.SDK_INT >= 23){
            List<String> listPermissoes = new ArrayList<>();
            /*Percorre as permissões passadas verificando uma a uma se já existe alguma permissão liberada*/
            for (String permissao : permissoes) {
                boolean tempermissao = ContextCompat.checkSelfPermission(fragment, permissao) == PackageManager.PERMISSION_GRANTED;
                if(!tempermissao){
                    listPermissoes.add(permissao);
                }
            }
            //caso a lista esteja vazia eh necessario solicitar a permissao
            if(listPermissoes.isEmpty()){
                return  true;
            }

            String[] novaspermissoes = new String[listPermissoes.size()];
            listPermissoes.toArray(novaspermissoes);
            //solicita permissao
            ActivityCompat.requestPermissions(fragment, novaspermissoes, requestcode);

        }
        return true;
    }

}
