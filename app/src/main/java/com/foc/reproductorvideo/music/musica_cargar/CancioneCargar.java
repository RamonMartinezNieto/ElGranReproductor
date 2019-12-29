package com.foc.reproductorvideo.music.musica_cargar;

import android.net.Uri;

public class CancioneCargar {

    private long id;
    private String titulo;
    private String artista;
    private String uriCancion;


    public CancioneCargar(long songID, String songTitle, String songArtist,String path) {
        id=songID;
        titulo=songTitle;
        artista=songArtist;
        this.uriCancion = path;
    }

    public long getIdCancion(){return id;}
    public String getTitulo(){return titulo;}
    public String getArtista(){return artista;}
    public String getPath(){return uriCancion;}
}
