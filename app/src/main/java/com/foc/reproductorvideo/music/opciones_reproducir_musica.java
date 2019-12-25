package com.foc.reproductorvideo.music;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.foc.reproductorvideo.R;

/**
 * @author Ramón Martínez Nieto
 * Dni: 43188174-A
 * Programación multimedia de Dispositivos Móviles
 * Tarea 4
 */
public class opciones_reproducir_musica extends AppCompatActivity implements View.OnClickListener {

    private Button btnMusicaEjemplo;
    private Button btnMusicaSeleccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproducir_musica);

        btnMusicaEjemplo = (Button) findViewById(R.id.buttonMusicaEjemplo);
        btnMusicaEjemplo.setOnClickListener(this);

        btnMusicaSeleccion = (Button) findViewById(R.id.buttonMusicaSeleccion);
        btnMusicaSeleccion.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.buttonMusicaEjemplo):
                cambioActivity(this, activity_musica_ejemplo.class, v);
                break;
            case (R.id.buttonMusicaSeleccion):
                cambioActivity(this, activity_musica_seleccion.class, v);
                break;
        }
    }


    /**
     * TODO - Éste método está repetido, no he conseguido extraerlo para que sea común, startActivity()
     * TODO - aun que le pase todo por parámetros.
     * Ejecuta una transicción para versiones más modernas y otra para versiones más antiguas (son muy similares)
     *
     * @param c class a la que se va a hacer la transicción
     */
    private void cambioActivity(Context cont, Class c, View v) {
        Intent i = new Intent(cont, c);

        //Compruebo si la versión es mayor al SDK 23 para tener una transicción mejor
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityOptions ao = ActivityOptions.makeClipRevealAnimation(v, 0, 0, v.getWidth(), v.getHeight());
            startActivity(i, ao.toBundle());
        } else {
            //Si el nivel es inferior se ejecutará esta transicción
            ActivityOptions ao = ActivityOptions.makeScaleUpAnimation(v, 0, 0, v.getWidth(), v.getHeight());
            startActivity(i);
        }

    }
}
