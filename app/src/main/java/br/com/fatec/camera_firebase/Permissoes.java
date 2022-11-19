package br.com.fatec.camera_firebase;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissoes {

    public static void validarPermissoes(String[] permissoes, Activity activity, int requestCode){
        if(Build.VERSION.SDK_INT >= 23){
            List<String> listaPermissoes = new ArrayList<>();

            // verifica permissoes ja concedidas
            for(String permissao: permissoes){
                boolean temPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if(!temPermissao){
                    listaPermissoes.add(permissao);
                }
            }
            // pede permissoes nao concedidas
            if(!listaPermissoes.isEmpty()) {
                String[] vetorPermissoes = new String[listaPermissoes.size()];
                listaPermissoes.toArray(vetorPermissoes);
                ActivityCompat.requestPermissions(activity, vetorPermissoes, requestCode);
            }
        }
    }
}
