package com.foc.reproductorvideo.video;

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
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.foc.reproductorvideo.R;

/**
 * @author Ramón Martínez Nieto
 * Dni: 43188174-A
 * Programación multimedia de Dispositivos Móviles
 * Tarea 4
 */
/**
 * Clase que permite seleccionar el vídeo que queremos ejecutar
 */
public class reproductor_video_seleccion extends AppCompatActivity implements View.OnClickListener{

    //Variables con ID del resultado de permisos y de la carga de vídeo
    private final int ID_RESULTADO_VIDEO = 8888;
    private final int ID_PERMISOS_READ_EXTERNAL = 9999;

    VideoView vv = null;

    Button btnSeleccionTexto;
    Button btnSeleccionFlotante;

    //Variable que controla X del botón
    int btnSeleccionTextoX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor_video_seleccion);

        //Botón flotante grande
        btnSeleccionTexto = (Button) findViewById(R.id.buttonSeleccionarTexto);
        btnSeleccionTexto.setOnClickListener(this);
        //Variable que controla X del botón para las transicciones entre botones
        btnSeleccionTextoX = 0;

        vv = (VideoView) findViewById(R.id.videoViewSeleccion);


        /**
         * Acciones a realizar si está en modo LANDSCAPE (solo en este modo habrá dos botones)
         */
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            //Cargo el botón flotante pequeño que hay en el layout LANDSCAPE
            btnSeleccionFlotante = (Button) findViewById(R.id.buttonSeleccionarFlotante);
            btnSeleccionFlotante.setOnClickListener(this);
            //Le pongo una X fuera de la pantalla, para que no se vea al inicio
            btnSeleccionFlotante.setX(300);

            /**
             * Listener para indicar que el VideoView está cargado para reproducirse cambiará los botones a
             * través de una animación
             */
            vv.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {

                    //Si está en modo RENDERING_START se ejecuta la animación de cambio al botón pequeño
                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                        //Animación para que se vaya el botón con texto
                        for (int i = 0; i < btnSeleccionTexto.getX(); i++) {
                            btnSeleccionTexto.animate().translationX(i);
                            btnSeleccionTextoX++;
                        }
                        //Animación para que aparezca el botón pequeño
                        for (int i = 300; i > 0; i--) {
                            btnSeleccionFlotante.animate().translationX(-i);
                        }
                        return true;
                    }
                    return false;
                }
            });

            /**
             * Listener para indicar que el VideoView está completado
             */
            vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                        //Animación para que vuelva el botón con texto
                        for (int i = btnSeleccionTextoX; i > 0; i--) {
                            btnSeleccionTexto.animate().translationX(-i);
                        }
                        //Animación para que se vaya el botón pequeño
                        for (int i = 0; i < btnSeleccionFlotante.getX(); i++) {
                            btnSeleccionFlotante.animate().translationX(i);
                        }
                }
            });
        }
    }

    /**
     * Ambos botones tienen la misma acción, por eso no es necesario un swithc-case
     * @param v View
     */
    @Override
    public void onClick(View v){
         //Necesito los permisos si no los tiene se le solicitarán y continuará por onRequestPermissionsResult
         if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
             //Llamo al método con el que buscaremos y ejecutaremos el vídeo
             buscarVideoCargar();
         } else {
             //Si no tengo permisos de lectura se los pido
             ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},ID_PERMISOS_READ_EXTERNAL);
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
                    buscarVideoCargar();
                } else {
                    //Mensaje para que el usuario entienda que tiene que aceptar los permisos
                    //Construyo el dialog a través del builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Debes de conceder los permisos para poder buscar un vídeo.").setTitle("Error con los Permisos");
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
     * Método para buscar el vídeo a cargar y ejecutarlo
     */
    private void buscarVideoCargar(){
        //Intent para seleccionar un contenido
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        //selecciono cualqueir tipo de archivo si no es formáto vídeo saldrá un Dialog diciendo que no se ha podido reproducir (por defecto)
        i.setType("*/*");
        i.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(i, "Seleccione un vídeo"), ID_RESULTADO_VIDEO);
        } catch (ActivityNotFoundException anfe) {
            //Si no tiene explorador de archivos se le comunica que debe instalar uno
            Toast.makeText(this, "Debes de instalar un explorador de archivos", Toast.LENGTH_SHORT).show();
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
            case ID_RESULTADO_VIDEO:
                if(resultado == RESULT_OK){
                    //Cargo el URI de la respuesta del intent
                    Uri uri = datos.getData();

                    //Para finalizar cargo el vídeo a través del URI
                    if(vv != null){
                        vv.setVideoURI(uri);
                        //Establezco un MediaController
                        vv.setMediaController(new MediaController(this));
                        //Ejecuto el vídeo
                        vv.start();
                    }
                }
                break;
        }
    }




    /**
     * todo el método funciona y oculta la barra ede estado, pero no se muy bien como integrarlo para toda la activity
     * todo - ya que al realizar cualquier acción vuelve a ser visible
     * @deprecated
     */
    public void ocultarBarraSuperior(){
        //Oculto la barra superior
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else{
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }
}
