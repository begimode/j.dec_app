package com.jorge.sprint0_jorgelarrosaquesada;


import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


public class ClaseLanzarNotificaciones {

    //Se declran las variables
    String ip = new LogicaFake().getIp();


    private final Context context;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;
    private PendingIntent pendingIntent;


    /**
     * Metodo que llama a la función crearNotificationChannel()
     * @param context le pasamos el contexto deseado
     */
    public ClaseLanzarNotificaciones(Context context){
        this.context = context;
        createNotificationChannel();
    }


    // -----------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------

    /**
     * Metodo para crear notificaciones
     * @param mensaje le pasamos el mensajes que visualizará la notificacion
     * @param titulo el titulo de la notificacion
     *
     * Texto, Texto->crearNotificacion()
     */
    public void crearNotificacion(String titulo, String mensaje){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        //builder.setContentTitle("Angle Corp");
        builder.setContentTitle(titulo);
        //builder.setContentText("Bateria baja del sensor");
        builder.setContentText(mensaje);
        builder.setColor(Color.BLUE);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        //builder.setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());

//        insertarNotificacion(titulo, mensaje);
    }


    // -----------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------


    /**
     * La funcion setPendingIntent() es una funcion la cual utilizarems en un futuro, redirigir al usuario a un fragment al pulsar la notificacion.
     * setPendingIntent()
     */
    /*private void setPendingIntent(){
        Intent intent = new Intent(getContext(), NotificacionesFragment.class );
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
        //stackBuilder.addParentStack(NotificacionesFragment.class);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
        //Log.d("pene","pending");
    }*/


    // -----------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------

    /**
     * La funcion createNotificationChannel() es una funcion la cual maneja la creacion de la notificacion.
     * createNotificationChannel()
     */
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){ //verifica la version del SDK
            CharSequence name = "Noticacion";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }


    // -----------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------

    private Boolean isNotifiacionAlive(){
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        return notificationManagerCompat.areNotificationsEnabled();
    }
//
//    private void insertarNotificacion(String motivo, String mensaje){
//        Date date = new Date();
//        String fecha = ""+date.getDay()+"."+date.getMonth();
//
////        TODO conseguir ID_PLACA y ID_USER
//        Notificacion notificacion = guardarNotificacione(motivo,mensaje,fecha,1,1);
//
//        //Envíar datos POST
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("ID_notificacion", null);
//            jsonObject.put("motivo", notificacion.getMotivo());
//            jsonObject.put("mensaje", notificacion.getMensaje());
//            jsonObject.put("fecha", notificacion.getFecha());
//            jsonObject.put("ID_user", notificacion.getID_user());
//            jsonObject.put("ID_placa", notificacion.getID_placa());
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        AndroidNetworking.post("http://" + ip + ":8080/insertarNotificacion")//Recordar cambiar ip cada vez que cambies de red
//                .addJSONObjectBody(jsonObject) // posting json
//                .setTag("test")
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONArray(new JSONArrayRequestListener() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        // do anything with response
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
//                        // handle error
//                    }
//                });
//
////        Toast.makeText(getApplicationContext(),"Notificacion Creada",Toast.LENGTH_SHORT).show();
//    }
//
//    // .................................................................
//    // motivo: Texto, mensaje: Texto, fecha: Texto, ID_user: R, ID_placa: R
//    // -->
//    // guardarNotificacione() <--
//    // <--
//    // Notificacion:
//    // {motivo: Texto, mensaje: Texto, fecha: Texto, ID_user: R, ID_placa: R}
//    //
//    // Esta función crea un objeto usuario mediante un constructor al cual se le pasan los valores mencionados arriba y lo devuelve.
//    // .................................................................
//
//    private Notificacion guardarNotificacione(String motivo, String mensaje, String fecha, int ID_user, int ID_placa){
//        Notificacion notificacion = new Notificacion();
//
//        notificacion.setMotivo(motivo);
//        notificacion.setMensaje(mensaje);
//        notificacion.setFecha(fecha);
//        notificacion.setID_user(ID_user);
//        notificacion.setID_placa(ID_placa);
//
//        return notificacion;
//    }

}

