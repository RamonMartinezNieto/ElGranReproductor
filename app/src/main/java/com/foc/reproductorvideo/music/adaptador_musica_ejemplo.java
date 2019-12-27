package com.foc.reproductorvideo.music;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.foc.reproductorvideo.R;


/**
 * @author Ramón Martínez Nieto
 */

/**
 * Clase Adaptador para cargar el ListView con vídeos de ejemplo
 */
public class adaptador_musica_ejemplo extends BaseAdapter {

    private Context contexto;
    private Activity activity;
    private ArrayList<Cancion> miMusica;

    private MediaPlayer mediaplayer;
    private int idCancionPausada;
    private int idCancionPlayed;

    private Button botonAnteriorPlay;
    private Button botonAnteriorPause;

    //HashMap para saber el estado de las canciones (key-idCancion / map-currentPosition
    private HashMap<Integer, Integer> cancionesEnMarcha = new HashMap<> ();
    private boolean segundaVez = false;

    //Constructor del adaptador
    public adaptador_musica_ejemplo (Context c, ArrayList<Cancion> listaCanciones, MediaPlayer mp, Activity activity) {
        this.contexto = c;
        this.miMusica = listaCanciones;
        this.mediaplayer = mp;
        this.activity = activity;

        this.botonAnteriorPlay = null;
        this.botonAnteriorPause = null;
    }

    @Override
    public int getCount () {
        return this.miMusica.size ();
    }

    @Override
    public Object getItem (int position) {
        return this.miMusica.get (position);
    }

    @Override
    public long getItemId (int id) {
        return id;
    }

    /**
     * Construcción del adaptador personalizado
     *
     * @param posicion
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView (int posicion, View convertView, ViewGroup parent) {

        //cargo la lista
        View v = convertView;

        //inflo la vista que nos llega a través del layout personalizado
        LayoutInflater li = LayoutInflater.from (this.contexto);
        v = li.inflate (R.layout.adaptador_musica_ejemplos, null);

        //extraigo los datos (esto es por cada iteración)
        String tituloActual = miMusica.get (posicion).getTitulo ();
        final int idCancion = miMusica.get (posicion).getIdCancion ();
        int idMiniatura = miMusica.get (posicion).getIdMiniatura ();
        String subtitulo = miMusica.get (posicion).getSubtitulo ();


        //Busco el textView que hay en mi layout personalizado y el VideoView
        TextView tvTituloCancion = v.findViewById (R.id.textViewTitulo);
        TextView tvSubtituloCancion = v.findViewById (R.id.textViewSubtitulo);
        ImageView ivMiniatura = v.findViewById (R.id.imageViewMiniatura);

        //cargo el ProgressBar como final para tener acceso desde los onClick
        final ProgressBar progressBarMusicaEjemplo = v.findViewById (R.id.progressBarMusicaEjemplo);

        //almaceno todas las canciones
        cancionesEnMarcha.put (idCancion,1);

        //asigno los datos al TextView
        tvTituloCancion.setText (tituloActual);
        tvSubtituloCancion.setText (subtitulo);

        //cargo la imagen en el ImageView a través de setImageResource con el id pasado
        ivMiniatura.setImageResource (idMiniatura);

        //Cargo el toggleButton y le asigno un listener para el botón (ojo que es para cada botón)

        final Button btStopMusica = (Button) v.findViewById (R.id.buttonPauseMusic);
        //desactivo el botón de stop
        botonDissable (btStopMusica);

        final Button btPlayMusica = (Button) v.findViewById (R.id.buttonPlayMusic);
        botonEnable (btPlayMusica);

        btPlayMusica.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick (View v) {

                //Compruebo los botones anteriores para devolverlos a su estado original
                if(botonAnteriorPlay != null && botonAnteriorPause != null){
                    botonEnable (botonAnteriorPlay);
                    botonDissable (botonAnteriorPause);
                }

                //cargar canción
                String pathMusicaFileMuestra = "android.resource://com.foc.reproductorvideo/" + idCancion;

                if (idCancionPausada == idCancion) {

                    mediaplayer.start ();
                    botonDissable(btPlayMusica);
                    botonEnable (btStopMusica);

                    //Ejecuto tarea async para el progress bar le paso por constructor un MediaPLayer y el progressbar en los parámetros async
                    new ProgressBarAsyncTask (mediaplayer).execute (progressBarMusicaEjemplo);

                } else {
                    //guardo el estado de la canción que se estaba reproduciendo anteirormente
                    if(segundaVez) {
                        cancionesEnMarcha.put (idCancionPlayed, mediaplayer.getCurrentPosition ());
                    }

                    //Paro y vuelvo al estado Idle
                    mediaplayer.stop ();
                    mediaplayer.reset ();


                    try {
                        //Indico la dirección del recurso
                        mediaplayer.setDataSource (v.getContext (), Uri.parse (pathMusicaFileMuestra));
                        mediaplayer.prepare ();

                    } catch (IOException ioe) {
                       Log.e ("IOExceptionMusic", "error al cargar el archivo de música");
                    }

                    idCancionPlayed = idCancion;
                    idCancionPausada = 0;

                    //Seteo la posición de la canción en cuestión
                    for(HashMap.Entry<Integer,Integer> i : cancionesEnMarcha.entrySet ()){
                        if(idCancion == i.getKey ()){
                            mediaplayer.seekTo (i.getValue ());
                        }
                    }

                    mediaplayer.start ();
                    botonDissable(btPlayMusica);
                    botonEnable (btStopMusica);

                    //Ejecuto tarea async para el progress bar le paso por constructor un MediaPLayer y el progressbar en los parámetros async
                    new ProgressBarAsyncTask (mediaplayer).execute (progressBarMusicaEjemplo);

                    //Me guardo en una variable el botón
                    botonAnteriorPlay = btPlayMusica;
                    botonAnteriorPause = btStopMusica;
                    segundaVez = true;
                }
            }
        });

        btStopMusica.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (mediaplayer != null) {
                    if (mediaplayer.isPlaying () && idCancionPlayed == idCancion) {
                        mediaplayer.pause ();
                        idCancionPausada = idCancion;

                        cancionesEnMarcha.put (idCancion,mediaplayer.getCurrentPosition ());
                    }
                }
                botonEnable (btPlayMusica);
                botonDissable (btStopMusica);

            }
        });

        //Al completarse la canción
        mediaplayer.setOnCompletionListener (new MediaPlayer.OnCompletionListener (){
            @Override
            public void onCompletion (MediaPlayer mp) {
                botonEnable (botonAnteriorPlay);
                botonDissable (botonAnteriorPause);
                Toast.makeText (contexto,"onCompletion",Toast.LENGTH_SHORT).show ();
                //Reestablezco la posición a 0 cuando se ha completado la canción
                for(HashMap.Entry<Integer,Integer> i : cancionesEnMarcha.entrySet ()){
                    if(idCancion == i.getKey ()){
                        mediaplayer.seekTo (0);
                    }
                }
            }
        });
        //devuelvo la vista
        return v;
    }

    /**
     * Para deshabilitar un botón
     * @param bt
     */
    public void botonDissable(Button bt){
        bt.getBackground().setColorFilter(ContextCompat.getColor(contexto, R.color.colorButtonDissable), PorterDuff.Mode.MULTIPLY);
        bt.setClickable (false);
    }

    /**
     * Para habilitar un botón
     * @param bt
     */
    public void botonEnable(Button bt){
        bt.getBackground().setColorFilter(ContextCompat.getColor(contexto, R.color.colorButtonEnable), PorterDuff.Mode.MULTIPLY);
        bt.setClickable (true);
    }

}
