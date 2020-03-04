# El Gran Reproductor (The greater reproductor)

Reproductor de vídeo y música.


SDK min: 26
SDK objecitve: 29 

If you want to change SDK min, only will need change a query in the file "activity_cargar_canciones_archivos" into the folder "musica_cargar" in method "cargarArchivosMusica(Context contexto)". In this method I made a Query with ContentResolver and this one is not supported for lower Android sdk. If you want is possible change this method.
