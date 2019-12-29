package com.foc.reproductorvideo;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import androidx.appcompat.app.AlertDialog;

public class Funcionalidad {

    private final int ID_RESULTADO_ARCHIVO = 8888;
    Activity activity;
    private Context context;

    public Funcionalidad(Context c, Activity a) {
        this.context = c;
        this.activity = a;
    }

    /**
     * Método para buscar el vídeo a cargar y ejecutarlo
     */
    public void buscarArchivoCargar(String tipo) {
        //Intent para seleccionar un contenido
        Intent i = new Intent ();
        i.setAction (Intent.ACTION_GET_CONTENT);
        //selecciono cualqueir tipo de archivo si no es formáto vídeo saldrá un Dialog diciendo que no se ha podido reproducir (por defecto)
        i.setType (tipo+"/*");
        i.addCategory (Intent.CATEGORY_OPENABLE);

        try {
            activity.startActivityForResult (Intent.createChooser (i, "Seleccione un vídeo"), ID_RESULTADO_ARCHIVO);

        } catch (ActivityNotFoundException anfe) {
            //Si no tiene explorador de archivos se le comunica que debe instalar uno
            AlertDialog.Builder builder = new AlertDialog.Builder (this.context);

            builder.setMessage ("Necesitas un explorador de archivos para poder seleccionar un vídeo.").setTitle ("Error falta de explorador");
            //Botón neutral del dialog ¿realmente es necesario el OnClickListener)
            builder.setNeutralButton ("Entendido", new DialogInterface.OnClickListener () {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //El botón no hace nada, solo es para que acepte y se vaya el AlertDialog
                }
            });
            AlertDialog dialog = builder.create ();
            dialog.show ();
        }
    }

    /**
     * Mensje de advertencia al usuario, se le comunica que se requieren los permisos para poder
     * cargar archivos
     */
    public void dialogoPermisos(String titulo, String mensaje) {
        //Construyo el dialog a través del builder
        AlertDialog.Builder builder = new AlertDialog.Builder (this.context);
        builder.setMessage (mensaje).setTitle(titulo);
        //Botón neutral del dialog ¿realmente es necesario el OnClickListener)
        builder.setNeutralButton ("Entendido", new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //El botón no hace nada, solo es para que acepte y se vaya el AlertDialog
            }
        });
        AlertDialog dialog = builder.create ();
        dialog.show ();
    }

    /**
     * Ejecuta una transicción para versiones más modernas y otra para versiones más antiguas (son muy similares)
     *
     * @param c class a la que se va a hacer la transicción
     */
    public void cambioActivity(Class c, View v) {
        Intent i = new Intent (this.context, c);

        //Compruebo si la versión es mayor al SDK 23 para tener una transicción mejor
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityOptions ao = ActivityOptions.makeClipRevealAnimation (v, 0, 0, v.getWidth (), v.getHeight ());
            activity.startActivity (i, ao.toBundle ());
        } else {
            //Si el nivel es inferior se ejecutará esta transicción
            ActivityOptions ao = ActivityOptions.makeScaleUpAnimation (v, 0, 0, v.getWidth (), v.getHeight ());
            activity.startActivity (i);
        }
    }
}
