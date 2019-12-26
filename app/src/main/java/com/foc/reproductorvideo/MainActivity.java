package com.foc.reproductorvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.foc.reproductorvideo.video.opciones_reproducir_video;
import com.foc.reproductorvideo.music.opciones_reproducir_musica;
/**
 * @author Ramón Martínez Nieto
 */

/**
 * Activity principal, tiene dos botones a través de los cuales vamos a reproducir vídeo o música
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnMusica;
    private Button btnVideo;

    Funcionalidad funcionalidad;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        //Cargo los botones y les añado el listener
        btnVideo = (Button) findViewById (R.id.buttonVideo);
        btnVideo.setOnClickListener (this);
        btnMusica = (Button) findViewById (R.id.buttonMusica);
        btnMusica.setOnClickListener (this);

        funcionalidad = new Funcionalidad (this, this);
    }

    /**
     * Evento onClick sobreescrito para añadir Funcionalidad a los botones
     *
     * @param v
     */
    @Override
    public void onClick (View v) {

        //Creo switch-case para identificar los eventos en los diferentes botones
        switch (v.getId ()) {
            case (R.id.buttonVideo):
                funcionalidad.cambioActivity (opciones_reproducir_video.class, v);
                break;
            case (R.id.buttonMusica):
                funcionalidad.cambioActivity (opciones_reproducir_musica.class, v);
                break;
        }
    }
}