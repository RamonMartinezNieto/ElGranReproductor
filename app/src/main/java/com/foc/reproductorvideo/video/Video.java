package com.foc.reproductorvideo.video;
/**
 * @author Ramón Martínez Nieto
 * Dni: 43188174-A
 * Programación multimedia de Dispositivos Móviles
 * Tarea 4
 */
/**
 * Clase Video al que se le asigna un id (se le pasa como R.raw.videoX
 * y un nombre que será el título
 */
public class Video {

    private int idVideo;
    private String nombre;

    public Video(String nombre, int idVideo){
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
