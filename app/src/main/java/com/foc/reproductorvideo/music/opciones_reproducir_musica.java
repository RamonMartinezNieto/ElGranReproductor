package com.foc.reproductorvideo.music;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.foc.reproductorvideo.Funcionalidad;
import com.foc.reproductorvideo.R;


/**
 * @author Ramón Martínez Nieto
 */
public class opciones_reproducir_musica extends AppCompatActivity implements View.OnClickListener {

    private Button btnMusicaEjemplo;
    private Button btnMusicaSeleccion;

    Funcionalidad funcionalidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_reproducir_musica);

        btnMusicaEjemplo = (Button) findViewById (R.id.buttonMusicaEjemplo);
        btnMusicaEjemplo.setOnClickListener (this);

        btnMusicaSeleccion = (Button) findViewById (R.id.buttonMusicaSeleccion);
        btnMusicaSeleccion.setOnClickListener (this);

        funcionalidad = new Funcionalidad (this, this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId ()) {
            case (R.id.buttonMusicaEjemplo):
                funcionalidad.cambioActivity (activity_musica_ejemplo.class, v);
                break;
            case (R.id.buttonMusicaSeleccion):
                funcionalidad.cambioActivity (activity_musica_seleccion.class, v);
                break;
        }
    }
}
