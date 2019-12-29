package com.foc.reproductorvideo.music.musica_cargar;

import android.graphics.Bitmap;

public class CancioneCargar {

    private long id;
    private String titulo;
    private String artista;
    private String uriCancion;
    private Bitmap iconoMusica;


    public CancioneCargar(long songID, String songTitle, String songArtist,String path,Bitmap iconoMusica) {
        this.id=songID;
        this.titulo=songTitle;
        this.artista=songArtist;
        this.iconoMusica = iconoMusica;
        this.uriCancion = path;
    }

    public long getIdCancion(){return this.id;}
    public String getTitulo(){return this.titulo;}
    public String getArtista(){return this.artista;}
    public String getPath(){return this.uriCancion;}
    public Bitmap getIconoMusica(){ return this.iconoMusica; }

}
