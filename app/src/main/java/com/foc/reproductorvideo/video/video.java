package com.foc.reproductorvideo.video;

public class video {

    private int idVideo;
    private String nombre;

    public video(String nombre, int idVideo){
        this.idVideo = idVideo;
        this.nombre = nombre;
    }

    public int getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(int idVideo) {
        this.idVideo = idVideo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
