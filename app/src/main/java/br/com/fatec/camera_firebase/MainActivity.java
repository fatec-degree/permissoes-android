package br.com.fatec.camera_firebase;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final int CAMERA = 1;
    private final int GALERIA = 2;

    private ImageView imageViewUsuario;
    private EditText editTextNome;
    private Button buttonInserir;
    private String[] permissoes = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
    AlertDialog.Builder msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.msg = new AlertDialog.Builder(MainActivity.this);
        msg.setNegativeButton("Cancelar", null);
        msg.setMessage("Você precisa conceder ao menos uma permissão.");


        this.imageViewUsuario = findViewById(R.id.imageViewUsuario);
        this.editTextNome = findViewById(R.id.editTextTextPersonName);
        this.buttonInserir = findViewById(R.id.buttonInserir);

        imageViewUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Permissoes.validarPermissoes(permissoes, MainActivity.this, 1);
                int permissionCamera = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
                int permissionGaleria = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if(permissionCamera == PackageManager.PERMISSION_GRANTED){
                    msg.setPositiveButton("Câmera", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intentCamera, CAMERA);
                        }
                    });
                }

                if(permissionCamera == PackageManager.PERMISSION_GRANTED){
                    msg.setPositiveButton("Câmera", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intentCamera, CAMERA);
                        }
                    });
                }

                if(permissionCamera == PackageManager.PERMISSION_GRANTED){
                    msg.setNeutralButton("Galeria", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intentGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intentGaleria, GALERIA);
                        }
                    });
                }

                if(permissionCamera == PackageManager.PERMISSION_GRANTED && permissionGaleria == PackageManager.PERMISSION_GRANTED){
                    msg.show();
                }
            }
        });

        buttonInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Inserir no Firebase
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bitmap bitmap = null;
            if(requestCode == CAMERA){
                bitmap = (Bitmap) data.getExtras().get("data");
            } else if(requestCode == GALERIA){
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Erro ao carregar imagem", Toast.LENGTH_SHORT).show();
            }
            if(bitmap != null){
                this.imageViewUsuario.setImageBitmap(bitmap);
            }
        } else {
            Toast.makeText(this, "Erro ao carregar imagem", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int i = 0; i < permissions.length; i++){
            boolean permissaoFoiConcedida = permissions[i].equals("android.permission.CAMERA") && grantResults[i] == 0;
            if(permissaoFoiConcedida){
                msg.setPositiveButton("Câmera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intentCamera, CAMERA);
                    }
                });
            };

            permissaoFoiConcedida = permissions[i].equals("android.permission.READ_EXTERNAL_STORAGE") && grantResults[i] == 0;
            if(permissaoFoiConcedida){
                msg.setNeutralButton("Galeria", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intentGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intentGaleria, GALERIA);
                    }
                });
            };
            msg.show();
        }
    }
}