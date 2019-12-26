package com.foc.reproductorvideo.video;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.MediaController;
import android.widget.VideoView;
import java.util.ArrayList;

import com.foc.reproductorvideo.R;

/**
 * @author Ramón Martínez Nieto
 */
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

        // Cargar Video principal Path del mp4 como recurso, en cualquier modo.
        String pathVideoFileMuestra = "android.resource://" + getPackageName() + "/" + R.raw.video1;

        vvMuestra = (VideoView) findViewById(R.id.videoViewMuestra);

        vvMuestra.setMediaController(new MediaController(vvMuestra.getContext()));

        vvMuestra.setVideoURI(Uri.parse(pathVideoFileMuestra));

        vvMuestra.start();

        //IMPORTANTE: El GridView solo se cargará en modo PORTRAIT
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            //GridView para ver los vídeo de ejemplo
            gvEjemplos = (GridView) findViewById(R.id.gridViewEjemplos);

            //Lista de vídeos cargada manualmente, lo suyo sería leer lo que hay en el directorio, pero como
            //son vídeos de ejemplo se puede hacer así.
            final ArrayList<Video> misVideos = new ArrayList<>();
            misVideos.add(new Video("Cascadas", R.raw.video1));
            misVideos.add(new Video("Café", R.raw.video2));
            misVideos.add(new Video("Disco", R.raw.video3));
            misVideos.add(new Video("Partículas", R.raw.video4));

            //Llamo a mi adaptador
            adaptador_videos_ejemplo ave = new adaptador_videos_ejemplo(this, R.layout.adaptador_videos_ejemplos, misVideos);
            gvEjemplos.setAdapter(ave);
            gvEjemplos.requestFocus();

            //Asigno OnItemClickListener para cuando pulse un vídeo se cargue ese vídeo
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
    }
}
