package com.foc.reproductorvideo.music;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.widget.ProgressBar;

public class ProgressBarAsyncTask extends AsyncTask<ProgressBar, Integer, Void> {

    private int progreso = 0;
    private ProgressBar pb;
    private double duracionMP = 0;
    boolean enProceso = true;
    private MediaPlayer mp;

    public ProgressBarAsyncTask (MediaPlayer mp) {
        super ();
        this.mp = mp;
    }

    @Override
    protected void onPreExecute () {
        progreso = 0;
        enProceso = true;
        //compruebo la duración en milisegundos
        duracionMP = (double) mp.getDuration ();
    }

    @Override
    protected Void doInBackground (ProgressBar... params) {

        this.pb = (ProgressBar) params[0];
        //Duración en milisegundos partido 100
        double currentPosMP;

        enProceso = true;

        while (enProceso) {
            while (mp.isPlaying ()) {
                //recupero la posición actual en milisengudos
                currentPosMP = mp.getCurrentPosition ();
                //Calculo los milisengudos para la barra de 100
                progreso = (int) (currentPosMP / this.duracionMP * 100);
                //publico el progreso
                publishProgress (progreso);
            }
            enProceso = false;
            publishProgress (progreso);
        }
        return null;
    }

    @Override
    protected void onProgressUpdate (Integer... values) {
        if (pb != null) {
            pb.setProgress (values[0]);
        }
    }

    @Override
    protected void onPostExecute (Void result) {
        pb.setProgress (progreso);
    }
}
