package com.foc.reproductorvideo.video;
/**
 * @author Ramón Martínez Nieto
 * Dni: 43188174-A
 * Programación multimedia de Dispositivos Móviles
 * Tarea 4
 */
/**
 * Clase video al que se le asigna un id (se le pasa como R.raw.videoX
 * y un nombre que será el título
 */
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
