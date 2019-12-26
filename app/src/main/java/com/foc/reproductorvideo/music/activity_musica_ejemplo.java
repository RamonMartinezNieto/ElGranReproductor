package com.foc.reproductorvideo.music;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;
import com.foc.reproductorvideo.R;


public class activity_musica_ejemplo extends AppCompatActivity  {


    ListView lvListaMusica;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musica_ejemplo);

        lvListaMusica = (ListView) findViewById(R.id.listViewEjemplos);


        ArrayList<Cancion> listaCanciones = new ArrayList<>();
        listaCanciones.add(new Cancion("Grupo 1","Nombre canción 1", R.raw.cancion1, R.drawable.cancion1 ));
        listaCanciones.add(new Cancion("Grupo 2","Nombre canción 2", R.raw.cancion2, R.drawable.cancion2 ));
        listaCanciones.add(new Cancion("Grupo 3","Nombre canción 3", R.raw.cancion3, R.drawable.cancion3 ));
        listaCanciones.add(new Cancion("Grupo 4","Nombre canción 4", R.raw.cancion4, R.drawable.cancion5 ));

        adaptador_musica_ejemplo ame = new adaptador_musica_ejemplo(this, listaCanciones);
        //Nota, el ListView pierde el foco y lo ganan los botones de reproducción
        lvListaMusica.setAdapter(ame);




    }

}