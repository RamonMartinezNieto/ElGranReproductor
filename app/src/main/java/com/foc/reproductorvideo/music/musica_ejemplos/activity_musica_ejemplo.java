package com.foc.reproductorvideo.music.musica_ejemplos;

import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import com.foc.reproductorvideo.R;


/**
 * Clase activity que carga el ListView con ejemplos de canciones
 */
public class activity_musica_ejemplo extends AppCompatActivity {

    private ListView lvListaMusica;
    private MediaPlayer mpMain;
    private Boolean pausado = false;
    private adaptador_musica_ejemplo ame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_musica_ejemplo);

        lvListaMusica = (ListView) findViewById (R.id.listViewEjemplos);
        mpMain = new MediaPlayer ();

        //Ejemplos añadidos manualmente, todo: lo correcto sería leer los arhicvos de la aplicación
        ArrayList<Cancion> listaCanciones = new ArrayList<> ();
        listaCanciones.add (new Cancion ("Grupo 1", "Nombre canción 1", R.raw.cancion1, R.drawable.cancion1));
        listaCanciones.add (new Cancion ("Grupo 2", "Nombre canción 2", R.raw.cancion2, R.drawable.cancion2));
        listaCanciones.add (new Cancion ("Grupo 3", "Nombre canción 3", R.raw.cancion3, R.drawable.cancion3));
        listaCanciones.add (new Cancion ("Grupo 4", "Nombre canción 4", R.raw.cancion4, R.drawable.cancion5));

         ame = new adaptador_musica_ejemplo (this, listaCanciones, mpMain,this);
        //Nota, el ListView pierde el foco y lo ganan los botones de reproducción
        lvListaMusica.setAdapter (ame);



    }

    @Override
    protected void onPause() {
        super.onPause ();
        //busco el MediaPlayer en cuestión
        mpMain = ame.getMediaPlayer ();

        if(mpMain.isPlaying ()) {
            //lo pauso
            mpMain.pause ();
            pausado = true;
        }

        ArrayList<HashMap<String,Button>> listaBotones = ame.getButtons ();

        for(int i = 0; i < listaBotones.size ();i++){
            HashMap<String,Button> boton = listaBotones.get (i);
            for(HashMap.Entry<String,Button> z : boton.entrySet()){
                if(z.getKey ().equals ("play")){
                    ame.botonEnable (z.getValue ());
                } else if(z.getKey ().equals ("stop")) {
                    ame.botonDissable (z.getValue ());
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume ();


    }

    @Override
    protected void onDestroy(){
        super.onDestroy ();
        mpMain = ame.getMediaPlayer ();
        if(mpMain != null){
            mpMain.release ();
            mpMain = null;
        }
    }
}