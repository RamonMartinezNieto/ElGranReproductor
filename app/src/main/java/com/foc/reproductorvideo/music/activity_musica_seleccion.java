package com.foc.reproductorvideo.music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.foc.reproductorvideo.R;
import com.foc.reproductorvideo.video.reproductor_video_seleccion;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

public class activity_musica_seleccion extends AppCompatActivity implements View.OnClickListener{

    MediaPlayer mpMain;
    Uri uri;

    Button botonSeleccionarMusica;
    Button botonPlay;
    Button botonPause;
    ProgressBar progressBar;
    TextView txtNombreCancion;
    TextView txtNombreAlbum;

    private final int ID_PERMISOS_READ_EXTERNAL = 9999;
    private final int ID_RESULTADO_ARCHIVO = 9999;

    Button buttonSelecionCancion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musica_seleccion);

        botonSeleccionarMusica = (Button) findViewById(R.id.buttonSelecionCancion);
        botonSeleccionarMusica.setOnClickListener(this);

        //botones
        botonPlay = (Button) findViewById(R.id.buttonPlayMusicSeleccion);
        botonPlay.setOnClickListener(this);
        botonPause = (Button) findViewById(R.id.buttonPauseMusicSeleccion);
        botonPause.setOnClickListener(this);

        txtNombreAlbum = (TextView) findViewById(R.id.textViewTituloSeleccion);
        txtNombreCancion = (TextView) findViewById(R.id.textViewSubtituloSeleccion);

        progressBar = (ProgressBar) findViewById(R.id.progressBarMusicaSeleccion);

        //Objeto media player a utilizar
        mpMain = new MediaPlayer();



    }

    @Override
    public void onClick(View v){

        switch (v.getId()){
            case(R.id.buttonSelecionCancion):

                if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ) {
                    if(mpMain.isPlaying()){
                        mpMain.stop();
                        mpMain.reset();
                    }
                    buscarArchivoCargar();
                } else {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},ID_PERMISOS_READ_EXTERNAL);
                }
                break;
                case(R.id.buttonPlayMusicSeleccion):
                    mpMain.start();
                    new ProgressBarAsyncTask(mpMain).execute(progressBar);
                break;

                case(R.id.buttonPauseMusicSeleccion):
                    if(mpMain.isPlaying()) {
                        mpMain.pause();
                    }
                break;
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if(mpMain.isPlaying()) {
            mpMain.stop();
            mpMain.reset();
        }
    }

    /**
     * Método para buscar el vídeo a cargar y ejecutarlo
     */
    private void buscarArchivoCargar(){
        //Intent para seleccionar un contenido
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        //selecciono cualqueir tipo de archivo si no es formáto vídeo saldrá un Dialog diciendo que no se ha podido reproducir (por defecto)
        i.setType("*/*");
        i.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(i, "Seleccione un vídeo"), ID_RESULTADO_ARCHIVO);
        } catch (ActivityNotFoundException anfe) {
            //Si no tiene explorador de archivos se le comunica que debe instalar uno
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Necesitas un explorador de archivos para poder seleccionar un vídeo.").setTitle("Error falta de explorador");
            //Botón neutral del dialog ¿realmente es necesario el OnClickListener)
            builder.setNeutralButton("Entendido", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //El botón no hace nada, solo es para que acepte y se vaya el AlertDialog
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    /**
     * Respuesta a la petición de permisos de lectura
     * @param requestCode
     * @param permissions
     * @param grantResult
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult){
        super.onRequestPermissionsResult(requestCode,permissions,grantResult);

        //Switch case para determinar que permiso se solicitó
        switch (requestCode){
            case(ID_PERMISOS_READ_EXTERNAL):
                //En caso de que se le concedan los permisos se ejecutará el método para buscar el vídeo y cargarlo
                if(grantResult[0] == (PackageManager.PERMISSION_GRANTED)){
                    buscarArchivoCargar();
                } else {
                    //Mensaje para que el usuario entienda que tiene que aceptar los permisos
                    //Construyo el dialog a través del builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Debes de conceder los permisos para poder buscar un archivo.").setTitle("Error con los Permisos");
                    //Botón neutral del dialog ¿realmente es necesario el OnClickListener)
                    builder.setNeutralButton("Entendido", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //El botón no hace nada, solo es para que acepte y se vaya el AlertDialog
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
        }
    }

    /**
     * Resultado del intent que busca el vídeo, devuelve datos que usaremos para coger el Uri
     * @param cod
     * @param resultado
     * @param datos
     */
    protected void onActivityResult(int cod, int resultado, Intent datos){

        switch (cod){
            case ID_RESULTADO_ARCHIVO:
                if(resultado == RESULT_OK){

                    //Para finalizar cargo el vídeo a través del URI
                    if(mpMain != null){

                        try {
                            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                            mmr.setDataSource(this, datos.getData());

                            String nombreAlbum = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                            String tituloCancion= mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

                            txtNombreAlbum.setText(nombreAlbum);
                            txtNombreCancion.setText(tituloCancion);

                            //cargo el media player con los datos devueltos
                            mpMain.setDataSource(this, datos.getData());

                            mpMain.prepare();
                            mpMain.start();

                            //Ejecuto tarea async para el progress bar le paso por constructor un MediaPLayer y el progressbar en los parámetros async
                            new ProgressBarAsyncTask(mpMain).execute(progressBar);

                        } catch(IOException ioe){
                            //todo
                        }
                    }
                }
                break;
        }
    }
}
