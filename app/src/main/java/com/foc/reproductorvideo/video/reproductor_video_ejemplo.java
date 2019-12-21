package com.foc.reproductorvideo.video;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.icu.text.UnicodeSetSpanner;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.foc.reproductorvideo.MainActivity;
import com.foc.reproductorvideo.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para reproducir un vídeo de ejemplo como recurso, esta clase diferencía entre el modo
 * PORTRAIT Y LANDSCAPE, bisacamente si está en LANDSCAPE se ejecuta un vídeo de ejemplo. Si está
 * en modo PORTRAIT se creará un GridView con 4 vídeos de ejemplo que se pueden cargar a través del
 * onItemClickLister, el GridView tiene un adapter personalizado.
 */
public class reproductor_video_ejemplo extends AppCompatActivity {

    VideoView vvMuestra = null;
    GridView gvEjemplos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor_video_ejemplo);

        //El GridView solo se cargará en modo protrait
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            //GridView para ver los vídeo de ejemplo
            gvEjemplos = (GridView) findViewById(R.id.gridViewEjemplos);

            //Lista de vídeos cargada manualmente, lo suyo sería leer lo que hay en el directorio, pero como
            //son vídeos de ejemplo se puede hacer así.
            final ArrayList<video> misVideos = new ArrayList<>();
            misVideos.add(new video("Cascadas", R.raw.video1));
            misVideos.add(new video("Café", R.raw.video2));
            misVideos.add(new video("Disco", R.raw.video3));
            misVideos.add(new video("Partículas", R.raw.video4));

            //Llamo a mi adaptador
            adaptador_videos_ejemplo ave = new adaptador_videos_ejemplo(this, R.layout.adaptador_videos_ejemplos, misVideos);
            gvEjemplos.setAdapter(ave);
            gvEjemplos.requestFocus();

            gvEjemplos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Cojo el vídeo según la posición devuelta por el ItemClickListener y lo cargo en el VideoView
                    String pathVideoFileMuestra2 = "android.resource://" + getPackageName() + "/" + misVideos.get(position).getIdVideo();
                    vvMuestra.setVideoURI(Uri.parse(pathVideoFileMuestra2));
                    vvMuestra.start();
                }
            });
    }

        // Cargar Video principal Path del mp4 como recurso
        String pathVideoFileMuestra = "android.resource://" + getPackageName() + "/" + R.raw.video1;

        vvMuestra = (VideoView) findViewById(R.id.videoViewMuestra);

        vvMuestra.setMediaController(new MediaController(vvMuestra.getContext()));

        vvMuestra.setVideoURI(Uri.parse(pathVideoFileMuestra));

        vvMuestra.start();

    }
}
