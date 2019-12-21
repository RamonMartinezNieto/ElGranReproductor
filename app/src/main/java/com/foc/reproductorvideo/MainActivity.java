package com.foc.reproductorvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.foc.reproductorvideo.video.opciones_reproducir_video;
import com.foc.reproductorvideo.music.opciones_reproducir_musica;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnMusica;
    Button btnVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Cargo los botones y les añado el listener
        btnVideo = (Button) findViewById(R.id.buttonVideo);
        btnVideo.setOnClickListener(this);

        btnMusica = (Button) findViewById(R.id.buttonMusica);
        btnMusica.setOnClickListener(this);

    }

    /**
     * Evento onClick sobreescrito para añadir funcionalidad a los botones
     * @param v
     */
    @Override
    public void onClick(View v){

        //Creo switch-case para identificar los eventos en los diferentes botones
        switch(v.getId()){
            case (R.id.buttonVideo):
                    cambioActivity(this, opciones_reproducir_video.class, v);
                break;
            case (R.id.buttonMusica):
                    cambioActivity(this, opciones_reproducir_musica.class, v);
                break;
        }
    }

    /**
     * todo refactorizar, copiado en las otras activities
     * @param c class a la que se va a hacer la transicción
     */
    private void cambioActivity(Context cont, Class c, View v){
        Intent i = new Intent(cont, c);

        //Compruebo si la versión es mayor al SDK 23 para tener una transicción mejor
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityOptions ao = ActivityOptions.makeClipRevealAnimation(v,0,0,v.getWidth(),v.getHeight());
            startActivity(i,ao.toBundle());
        } else {
            //Si el nivel es inferior se ejecutará esta transicción
            ActivityOptions ao = ActivityOptions.makeScaleUpAnimation(v, 0, 0, v.getWidth(), v.getHeight());
            startActivity(i);
        }
    }
}