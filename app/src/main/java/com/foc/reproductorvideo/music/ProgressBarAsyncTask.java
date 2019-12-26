package com.foc.reproductorvideo.music;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.widget.ProgressBar;

public class ProgressBarAsyncTask extends AsyncTask<ProgressBar, Integer, Void> {

        int progreso = 0;
        ProgressBar pb;
        double duracionMP = 0;

        MediaPlayer mp;

        public ProgressBarAsyncTask(MediaPlayer mp){
            super();

            this.mp = mp;
        }

        @Override
        protected void onPreExecute(){
            progreso = 0;
            duracionMP =  (double) mp.getDuration();
        }


        @Override
        protected Void doInBackground(ProgressBar... params){

            this.pb = (ProgressBar) params[0];
            //Duración en milisegundos partido 100
            double currentPosMP;

            while(progreso < 100){
                currentPosMP =  mp.getCurrentPosition();

                progreso = (int) ( currentPosMP /  this.duracionMP * 100);

                //publico el progreso
                publishProgress(progreso);

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            if(pb != null) {

                pb.setProgress(values[0]);
            }
        }

        @Override
        protected void onPostExecute(Void result){
            pb.setProgress(0);
        }

}
