package com.foc.reproductorvideo.music.musica_cargar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ListView;
import com.foc.reproductorvideo.Funcionalidad;
import com.foc.reproductorvideo.R;
import java.util.ArrayList;
import java.util.HashMap;

public class activity_cargar_canciones_archivos extends AppCompatActivity {

    private final int ID_PERMISOS_READ_EXTERNAL = 9999;

    private ListView lvCargarMusica;
    private ArrayList<CancioneCargar> cancionesAlmacenadas = new ArrayList<>();
    private Funcionalidad funcCargarArchivos;
    private adaptador_musica_cargar amc;
    private MediaPlayer mpCargar;

    private boolean pausado = false;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_cargar_canciones_archivos);

        lvCargarMusica = (ListView) findViewById (R.id.listViewCargarMusica);
        funcCargarArchivos = new Funcionalidad (this, this);
        mpCargar = new MediaPlayer ();


        //pedir permisos
        if (ContextCompat.checkSelfPermission (this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //compruebo almacenamiento externo disponible
            if (isExternalStorageReadable ()) {
                // cargo el array
                cargarArchivosMusica (this);
                //cargo el adaptador
                amc = new adaptador_musica_cargar (this, cancionesAlmacenadas, mpCargar, this);
                //añado el adaptador al listView
                lvCargarMusica.setAdapter (amc);

            } else {
                funcCargarArchivos.dialogoPermisos ("Error con almacenamiento extero.","Su almacenamiento externo no está preparado. Si tiene SDCARD compruebe que está bien insertada");
            }
        }else {
            ActivityCompat.requestPermissions (this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, ID_PERMISOS_READ_EXTERNAL);
        }

    }

    /**
     * Respuesta a la petición de permisos de lectura
     *
     * @param requestCode sdf
     * @param permissions sdf
     * @param grantResult sdf
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResult);

        switch (requestCode) {
            case (ID_PERMISOS_READ_EXTERNAL):
                //En caso de que se le concedan los permisos se ejecutará el método para buscar el vídeo y cargarlo
                if (grantResult[0] == (PackageManager.PERMISSION_GRANTED)) {
                    // cargo el array
                    cargarArchivosMusica (this);
                    //cargo el adaptador
                    amc = new adaptador_musica_cargar (this, cancionesAlmacenadas, mpCargar, this);
                    //añado el adaptador al listView
                    lvCargarMusica.setAdapter (amc);

                } else {
                    funcCargarArchivos.dialogoPermisos ("Error con los permisos","Debes de conceder los permisos para poder buscar un archivo.");
                }
                break;
        }
    }

    /**
     * Método para cargar los archivos de música en el Array
     */
    private void cargarArchivosMusica(Context contexto){

        ContentResolver cancionesResolver = getContentResolver ();
        Uri cancionUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        //TODO query solo sirve para 8.0, tengo que cambiarlo
        Cursor cancionCursor  = cancionesResolver.query (cancionUri,null,null,null);
        String uri;

        if(cancionCursor != null && cancionCursor.moveToFirst ()){
            //extraigo las columnas
            int tituloColumna = cancionCursor.getColumnIndex (MediaStore.Audio.Media.TITLE);
            int idColumna = cancionCursor.getColumnIndex (MediaStore.Audio.Media._ID);
            int artistaColumna = cancionCursor.getColumnIndex (MediaStore.Audio.Media.ARTIST);

            do{
                uri = cancionUri.toString () + "/" + cancionCursor.getLong (idColumna);
                //voy sacando los datos del cursos y lo almaceno en el Array
                long id = cancionCursor.getLong (idColumna);
                String titulo = cancionCursor.getString (tituloColumna);
                String artista = cancionCursor.getString (artistaColumna);

                //Saco la imagen del album de los metadatos, le pongo uno por defecto
                Bitmap bm = BitmapFactory.decodeResource (getResources (),R.drawable.icono_musica_miniatura);
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(contexto,Uri.parse (uri));
                byte[] artBytes =  mmr.getEmbeddedPicture();
                if(artBytes!=null) {
                    bm = BitmapFactory.decodeByteArray(artBytes, 0, artBytes.length);
                }

                //todo

                cancionesAlmacenadas.add (new CancioneCargar (id,titulo,artista,uri,bm));

            }while(cancionCursor.moveToNext ());
        }
    }

    /**
     * Comprobar que el almacenamiento externa está montado y listo para leer
     * @return
     */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
            Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


    @Override
    protected void onPause() {
        super.onPause ();
        //busco el MediaPlayer en cuestión
        mpCargar = amc.getMediaPlayer ();

        if(mpCargar.isPlaying ()) {
            //lo pauso
            mpCargar.pause ();
            pausado = true;
        }

        ArrayList<HashMap<String, Button>> listaBotones = amc.getButtons ();

        for(int i = 0; i < listaBotones.size ();i++){
            HashMap<String,Button> boton = listaBotones.get (i);
            for(HashMap.Entry<String,Button> z : boton.entrySet()){
                if(z.getKey ().equals ("play")){
                    amc.botonEnable (z.getValue ());
                } else if(z.getKey ().equals ("stop")) {
                    amc.botonDissable (z.getValue ());
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume ();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy ();
        mpCargar = amc.getMediaPlayer ();
        if(mpCargar != null){
            mpCargar.release ();
            mpCargar = null;
        }
    }
}
