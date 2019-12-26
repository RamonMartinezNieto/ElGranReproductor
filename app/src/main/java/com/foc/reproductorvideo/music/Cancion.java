package com.foc.reproductorvideo.music;

/**
 * Clase para las canciones (se usa para las canciones de ejemplo)
 */
public class Cancion {

    String titulo;
    String subtitulo;
    int idCancion;
    int idMiniatura;

    public Cancion (String titulo, String subtitulo, int idCancion, int idMiniatura) {
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.idCancion = idCancion;
        this.idMiniatura = idMiniatura;
    }

    public String getTitulo () {
        return titulo;
    }

    public void setTitulo (String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo () {
        return subtitulo;
    }

    public void setSubtitulo (String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public int getIdCancion () {
        return idCancion;
    }

    public void setIdCancion (int idCancion) {
        this.idCancion = idCancion;
    }

    public void setIdMiniatura (int idMiniatura) {
        this.idMiniatura = idMiniatura;
    }

    public int getIdMiniatura () {
        return this.idMiniatura;
    }

}

