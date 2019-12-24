package com.foc.reproductorvideo.music;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.foc.reproductorvideo.R;


import java.util.ArrayList;


/**
 * @author Ramón Martínez Nieto
 * Dni: 43188174-A
 * Programación multimedia de Dispositivos Móviles
 * Tarea 4
 */

/**
 * Clase Adaptador para cargar el ListView con vídeos de ejemplo
 */
public class adaptador_musica_ejemplo extends BaseAdapter {

    private Context contexto;
    private ArrayList<cancion> miMusica;

    //Constructor del adaptador
    public adaptador_musica_ejemplo(Context c, ArrayList<cancion> listaCanciones){
        this.contexto = c;
        this.miMusica = listaCanciones;
    }

    @Override
    public int getCount() {
        return this.miMusica.size();
    }

    @Override
    public Object getItem(int position) {
        return this.miMusica.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    /**
     * Construcción del adaptador personalizado
     * @param posicion
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int posicion, View convertView, ViewGroup parent) {

        //cargo la lista
        View v = convertView;

        //inflo la vista que nos llega a través del layout personalizado
        LayoutInflater li = LayoutInflater.from(this.contexto);
        v = li.inflate(R.layout.adaptador_musica_ejemplos,null);

        //extraigo los datos (esto es por cada iteración)
        String tituloActual = miMusica.get(posicion).getTitulo();
        int idCancion = miMusica.get(posicion).getIdCancion();
        int idMiniatura = miMusica.get(posicion).getIdMiniatura();
        String subtitulo = miMusica.get(posicion).getSubtitulo();


        //cargar canción
        String pathVideoFileMuestra = "android.resource://com.foc.reproductorvideo/" + idCancion;

        //Busco el textView que hay en mi layout personalizado y el VideoView
        TextView tvTituloCancion = v.findViewById(R.id.textViewTitulo);
        TextView tvSubtituloCancion = v.findViewById(R.id.textViewSubtitulo);
        ImageView ivMiniatura = v.findViewById(R.id.imageViewMiniatura);


        //asigno los datos al TextView
        tvTituloCancion.setText(tituloActual);
        tvSubtituloCancion.setText(subtitulo);

        //todo- como seteo la imagen?
        ivMiniatura.setImageResource(R.drawable.cancion1);


        //todo - aquí va el código de reproducción

        //devuelvo la vista
        return v;
    }
}
