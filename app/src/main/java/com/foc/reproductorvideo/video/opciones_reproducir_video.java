package com.foc.reproductorvideo.video;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.foc.reproductorvideo.Funcionalidad;
import com.foc.reproductorvideo.R;

public class opciones_reproducir_video extends AppCompatActivity implements View.OnClickListener{

    Button btnVideoEjemplo;
    Button btnVideoSeleccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reproducir_video);
        btnVideoEjemplo = (Button) findViewById(R.id.buttonVideoEjemplo);
        btnVideoEjemplo.setOnClickListener(this);

        btnVideoSeleccion = (Button) findViewById(R.id.buttonVideoSeleccion);
        btnVideoSeleccion.setOnClickListener(this);

    }


    /**
     * Método sobreescrito onClick
     * @param v
     */
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case(R.id.buttonVideoEjemplo):
                cambioActivity(this, reproductor_video_ejemplo.class,v);
                break;
            case(R.id.buttonVideoSeleccion):
                cambioActivity(this, reproductor_video_seleccion.class,v);
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
