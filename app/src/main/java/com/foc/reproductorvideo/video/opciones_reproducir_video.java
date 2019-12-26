package com.foc.reproductorvideo.video;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.foc.reproductorvideo.Funcionalidad;
import com.foc.reproductorvideo.R;

/**
 * @author Ramón Martínez Nieto
 */

/**
 * Clase que contiene dos botones, uno para reproducir videos de ejemplo y otro para seleccionar el vídeo que se desee
 * implementa View.onClickListener
 */
public class opciones_reproducir_video extends AppCompatActivity implements View.OnClickListener {

    private Button btnVideoEjemplo;
    private Button btnVideoSeleccion;

    Funcionalidad funcionalidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_reproducir_video);

        //Busco los botones y les asigno un listener
        btnVideoEjemplo = (Button) findViewById (R.id.buttonVideoEjemplo);
        btnVideoEjemplo.setOnClickListener (this);
        btnVideoSeleccion = (Button) findViewById (R.id.buttonVideoSeleccion);
        btnVideoSeleccion.setOnClickListener (this);

        funcionalidad = new Funcionalidad (this, this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId ()) {
            case (R.id.buttonVideoEjemplo):
                funcionalidad.cambioActivity (reproductor_video_ejemplo.class, v);
                break;

            case (R.id.buttonVideoSeleccion):
                funcionalidad.cambioActivity (reproductor_video_seleccion.class, v);
                break;

        }
    }
}