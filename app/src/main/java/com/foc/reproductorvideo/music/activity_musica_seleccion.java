package com.foc.reproductorvideo.music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

import com.foc.reproductorvideo.Funcionalidad;
import com.foc.reproductorvideo.R;

public class activity_musica_seleccion extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mpMain;
    private Uri uri;
    private Button botonSeleccionarMusica;
    private Button botonPlay;
    private Button botonPause;
    private ProgressBar progressBar;
    private TextView txtNombreCancion;
    private TextView txtNombreAlbum;

    private boolean pausado = false;

    private final int ID_PERMISOS_READ_EXTERNAL = 9999;
    private final int ID_RESULTADO_ARCHIVO = 8888;

    Funcionalidad funcMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_musica_seleccion);

        botonSeleccionarMusica = (Button) findViewById (R.id.buttonSelecionCancion);
        botonSeleccionarMusica.setOnClickListener (this);

        //botones
        botonPlay = (Button) findViewById (R.id.buttonPlayMusicSeleccion);
        botonPlay.setOnClickListener (this);
        botonPause = (Button) findViewById (R.id.buttonPauseMusicSeleccion);
        botonPause.setOnClickListener (this);

        txtNombreAlbum = (TextView) findViewById (R.id.textViewTituloSeleccion);
        txtNombreCancion = (TextView) findViewById (R.id.textViewSubtituloSeleccion);

        progressBar = (ProgressBar) findViewById (R.id.progressBarMusicaSeleccion);

        //Objeto media player a utilizar
        mpMain = new MediaPlayer ();

        //Instancio objeto Funcionalidad dónde tengo varios métodos
        funcMusic = new Funcionalidad (this, this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId ()) {
            case (R.id.buttonSelecionCancion):
                if (ContextCompat.checkSelfPermission (this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    if (mpMain.isPlaying ()) {
                        mpMain.stop ();
                        mpMain.reset ();
                    }
                    //llamo al método para buscar el archivo a cargar
                    funcMusic.buscarArchivoCargar ();
                } else {
                    ActivityCompat.requestPermissions (this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ID_PERMISOS_READ_EXTERNAL);
                }
                break;

            case (R.id.buttonPlayMusicSeleccion):
                mpMain.start ();
                new ProgressBarAsyncTask (mpMain).execute (progressBar);
                ;

                break;

            case (R.id.buttonPauseMusicSeleccion):
                if (mpMain.isPlaying ()) {
                    mpMain.pause ();
                    pausado = true;
                }
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause ();
        if (mpMain.isPlaying ()) {
            mpMain.pause ();
            //todo tengo que controlar mejor la barra de progreso
            //progressBar.setProgress(0);
            pausado = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume ();
        if (pausado) {
            mpMain.start ();
            pausado = false;
        }
    }

    /**
     * Respuesta a la petición de permisos de lectura
     *
     * @param requestCode sdf
     * @param permissions sdf
     * @param grantResult sdf
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResult);

        //Switch case para determinar que permiso se solicitó
        switch (requestCode) {
            case (ID_PERMISOS_READ_EXTERNAL):
                //En caso de que se le concedan los permisos se ejecutará el método para buscar el vídeo y cargarlo
                if (grantResult[0] == (PackageManager.PERMISSION_GRANTED)) {
                    //llamo al método para buscar el archivo a cargar
                    funcMusic.buscarArchivoCargar ();
                } else {
                    funcMusic.dialogoPermisos ("Error con los permisos","Debes de conceder los permisos para poder buscar un archivo.");
                }
                break;
        }
    }

    /**
     * Resultado del intent que busca el vídeo, devuelve datos que usaremos para coger el Uri
     *
     * @param cod
     * @param resultado
     * @param datos
     */
    protected void onActivityResult(int cod, int resultado, Intent datos) {

        switch (cod) {
            case ID_RESULTADO_ARCHIVO:
                if (resultado == RESULT_OK) {

                    //Para finalizar cargo el vídeo a través del URI
                    if (mpMain != null) {
                        try {
                            MediaMetadataRetriever mmr = new MediaMetadataRetriever ();
                            mmr.setDataSource (this, datos.getData ());

                            String nombreAlbum = mmr.extractMetadata (MediaMetadataRetriever.METADATA_KEY_ALBUM);
                            String tituloCancion = mmr.extractMetadata (MediaMetadataRetriever.METADATA_KEY_TITLE);

                            txtNombreAlbum.setText (nombreAlbum);
                            txtNombreCancion.setText (tituloCancion);

                            //cargo el media player con los datos devueltos todo:por algún motivo a petado aquí una vez
                            mpMain.setDataSource (this, datos.getData ());

                            mpMain.prepare ();
                            mpMain.start ();

                            //Ejecuto tarea async para el progress bar le paso por constructor un MediaPLayer y el progressbar en los parámetros async
                            new ProgressBarAsyncTask (mpMain).execute (progressBar);

                        } catch (IOException ioe) {
                            //todo
                        }
                    }
                }
                break;
        }
    }
}