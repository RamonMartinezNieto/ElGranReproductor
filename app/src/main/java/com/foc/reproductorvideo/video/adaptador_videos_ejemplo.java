package com.foc.reproductorvideo.video;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.VideoView;

import com.foc.reproductorvideo.R;

import java.util.ArrayList;

public class adaptador_videos_ejemplo extends BaseAdapter {

    private Context contexto;
    private int layout;
    private ArrayList<video> misVideos;

    public adaptador_videos_ejemplo(Context c, int layout, ArrayList<video> listaVideos){
        this.contexto = c;
        this.layout = layout;
        this.misVideos = listaVideos;
    }

    @Override
    public int getCount() {
        return this.misVideos.size();
    }

    @Override
    public Object getItem(int position) {
        return this.misVideos.get(position);
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

        //inflo la vista que nos llega con el layout personalizado
        LayoutInflater li = LayoutInflater.from(this.contexto);
        v = li.inflate(R.layout.adaptador_videos_ejemplos,null);

        //extraigo los datos (esto es por cada iteración)
        String nombreActual = misVideos.get(posicion).getNombre();
        int idVideo = misVideos.get(posicion).getIdVideo();

        //Path del mp4 como recurso
        // Cargar Video principal Path del mp4 como recurso
        String pathVideoFileMuestra = "android.resource://com.foc.reproductorvideo/" + idVideo;

        //Busco los textView que hay en mi layout personalizado
        TextView tvEjemplosNombresVideos = v.findViewById(R.id.tvEjemplosNombresVideos);
        VideoView vvEjemplosAdaptador = v.findViewById(R.id.vvEjemplosAdaptador);

        //asigno los datos a los diferentes TextView
        tvEjemplosNombresVideos.setText(nombreActual);

        //VideoView
        vvEjemplosAdaptador.setVideoURI(Uri.parse(pathVideoFileMuestra));
        vvEjemplosAdaptador.start();

        //Hago que pierda el foco para que lo tenga el GridView
        vvEjemplosAdaptador.setFocusable(false);

        //devuelvo la vista
        return v;
    }
}
