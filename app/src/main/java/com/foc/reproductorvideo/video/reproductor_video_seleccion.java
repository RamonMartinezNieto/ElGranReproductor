package com.foc.reproductorvideo.video;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.foc.reproductorvideo.R;
import com.foc.reproductorvideo.funcionalidad;

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
    private final int ID_RESULTADO_ARCHIVO = 8888;
    private final int ID_PERMISOS_READ_EXTERNAL = 9999;
    VideoView vv = null;
    Button btnSeleccionTexto;
    //Solo para landscape
    Button btnSeleccionFlotante;

    //para control del fondo
    ImageView imageViewFondo;
    FrameLayout frameLayoutImagenFondo;
    ImageView imageViewFondoLand;
    FrameLayout frameLayoutImagenFondoLand;
    funcionalidad funcVideo;
    Uri uri = null;

    //Variable que controla X del botón
    int btnSeleccionTextoX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor_video_seleccion);

        funcVideo = new funcionalidad(this, this);

        //Busco el fondo para tratarlo posteriormente
        imageViewFondo = (ImageView) findViewById(R.id.imageViewFondo);
        imageViewFondoLand = (ImageView) findViewById(R.id.imagenViewFondoLandscape);
        //Busco el FrameLayout para tratarlo posteriormnete
        frameLayoutImagenFondo = (FrameLayout) findViewById(R.id.frameLayoutImagenFondo);
        frameLayoutImagenFondoLand = (FrameLayout) findViewById(R.id.frameLayoutImagenFondoLandscape);


        //Botón flotante grande
        btnSeleccionTexto = (Button) findViewById(R.id.buttonSeleccionarTexto);
        btnSeleccionTexto.setOnClickListener(this);
        //Variable que controla X del botón para las transicciones entre botones
        btnSeleccionTextoX = 0;

        vv = (VideoView) findViewById(R.id.videoViewSeleccion);


        /**
         * Acciones a realizar si está en modo LANDSCAPE (solo en este modo habrá dos botones)
         */
            //Cargo el botón flotante pequeño que hay en el layout LANDSCAPE
            btnSeleccionFlotante = (Button) findViewById(R.id.buttonSeleccionarFlotante);
            btnSeleccionFlotante.setOnClickListener(this);
            //Le pongo una X fuera de la pantalla, para que no se vea al inicio
            btnSeleccionFlotante.setX(400);

            /**
             * Listener para indicar que el VideoView está cargado para reproducirse cambiará los botones a
             * través de una animación
             */
            vv.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {

                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        //Si está en modo RENDERING_START se ejecuta la animación de cambio al botón pequeño
                        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                            //Animación para que se vaya el botón con texto
                            transiccionSalidaBotonGrande();
                            return true;
                        }
                    } else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                            //Animación para que se vaya el botón con texto
                            if(uri != null){
                                int rotacion = saberRotacion(uri);

                                //Ojo, si el botón pequeño no estaba en su posición hay que ponersela
                                if(rotacion == 90 || rotacion == 270){
                                    transiccionSalidaBotonGrande();
                                } else if (btnSeleccionFlotante.getX() != 400) {
                                    transiccionEntradaBotonGrande();
                                }
                            }
                            return true;
                        }
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

                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        //Animación para que vuelva el botón con texto
                        transiccionEntradaBotonGrande();
                    }else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                        //Animación para que vuelva el botón con texto
                        int rotacion = saberRotacion(uri);

                        if(saberRotacion(uri) == 90 || saberRotacion(uri) == 270) {
                            transiccionEntradaBotonGrande();
                        }
                    }
                }
            });

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
            // buscarArchivoCargar();
             funcVideo.buscarArchivoCargar();

         } else {
             //Si no tengo permisos de lectura se los pido
             ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},ID_PERMISOS_READ_EXTERNAL);
         }
    }

    /**
     * Transicción salida botón grande y entrada pequeño
     */
    private void transiccionSalidaBotonGrande(){
        for (int i = 0; i < btnSeleccionTexto.getWidth() + btnSeleccionTexto.getX(); i++) {
            btnSeleccionTexto.animate().translationX(i);
            btnSeleccionTextoX++;
        }
        //Animación para que aparezca el botón pequeño
        for (int i = 300; i > 0; i--) {
            btnSeleccionFlotante.animate().translationX(-i);
        }
    }

    /**
     * Transicción entrada botón grande y salida pequeño
     */
    private void transiccionEntradaBotonGrande(){
        for (int i = btnSeleccionTextoX; i > 0; i--) {
            btnSeleccionTexto.animate().translationX(-i);
        }
        //Animación para que se vaya el botón pequeño
        for (int i = 0; i < btnSeleccionFlotante.getX(); i++) {
            btnSeleccionFlotante.animate().translationX(i);
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
                    //buscarArchivoCargar();
                    funcVideo.buscarArchivoCargar();
                } else {
                    funcVideo.dialogoPermisos();
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
                    //Cargo el URI de la respuesta del intent
                    uri = datos.getData();
                    //Para finalizar cargo el vídeo a través del URI
                    if(vv != null){
                        vv.setVideoURI(uri);

                        //Establezco un MediaController
                        vv.setMediaController(new MediaController(this));

                        //Diferente configuración según orientación
                        configuracionSegunOrientacion(uri);

                        //El VideoView tendrá el foco
                        vv.requestFocus();
                        //Ejecuto el vídeo
                        vv.start();
                    }
                }
                break;
        }
    }


    /**
     * Método que realiza diferentes accioens según la rotación del vídeo
     * @param uri
     */
    private void configuracionSegunOrientacion(Uri uri){

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            //El vídeo está en modo portrait quito el fondo y lo pongo negro por si hay "espacios" en los lados y en la parte inferior
            if(saberRotacion(uri) == 90 || saberRotacion(uri) == 270){
                imageViewFondo.setVisibility(View.INVISIBLE);
                frameLayoutImagenFondo.setBackgroundColor(Color.parseColor("#000000"));
            } else if (saberRotacion(uri) == 0 || saberRotacion(uri) == 180){
                imageViewFondo.setVisibility(View.VISIBLE);
                frameLayoutImagenFondo.setBackgroundColor(0x00FFFFFF);
            }
        }

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            //El vídeo está en modo landscape quito el fondo y lo pongo negro por si hay "espacios" en los lados y en la parte inferior

                imageViewFondoLand.setVisibility(View.INVISIBLE);
                frameLayoutImagenFondoLand.setBackgroundColor(Color.parseColor("#000000"));

        }
    }

    /**
     * Método para saber los grados en los que está grabado el vídeo
     * @param path
     * @return
     */
    private int saberRotacion(Uri path){
        int videoRotation = 0;
        MediaMetadataRetriever md = new MediaMetadataRetriever();
        md.setDataSource(this,path);
        videoRotation = Integer.parseInt(md.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
        return videoRotation;
    }
}