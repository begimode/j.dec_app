package com.jorge.sprint0_jorgelarrosaquesada;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

//----------------------------------------------------
// Archivo: MainActivity.java
// Jorge Larrosa Quesada
// Sprint 0
//----------------------------------------------------

public class MainActivity extends AppCompatActivity {

    String ip = "192.168.43.164";

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private static final String ETIQUETA_LOG = ">>>>";
    private static final int CODIGO_PETICION_PERMISOS = 11223344;

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private BluetoothLeScanner elEscanner;
    private ScanCallback callbackDelEscaneo = null;

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private float major_datos;
    private int minor_datos;

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private Button enviarDatos;
    private Button enviarDatosFake;


    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private LocationManager locManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Librería encargada ser cocentarse con el Servidor
        AndroidNetworking.initialize(getApplicationContext());

        //Se solicitan los permisos para que el usuario seleccione si desea permitir el acceso al GPS del dispositivo:
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        //Se comprueba si se han concedido los permisos para mostrar los datos de latitud, longitud, altura y precisión del dispositivo
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(getApplicationContext(),"Faltan los permisos",Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            return;
        }else {

            Toast.makeText(getApplicationContext(),"GPS Activado",Toast.LENGTH_SHORT).show();
        }

        //Botón que envía los datos al Servidor
        enviarDatos = findViewById(R.id.enviarDatos);
        enviarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botonEnviarAlServidor(view);
            }
        });

        enviarDatosFake = findViewById(R.id.button_fake);
        enviarDatosFake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botonEnviarAlServidorFake(view);
            }
        });

        Log.d(ETIQUETA_LOG, " onCreate(): empieza ");

        inicializarBlueTooth();

        Log.d(ETIQUETA_LOG, " onCreate(): termina ");

    } // onCreate()


    // --------------------------------------------------------------
    // --------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private void buscarTodosLosDispositivosBTLE() {
        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empieza ");

        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): instalamos scan callback ");

        this.callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult( int callbackType, ScanResult resultado ) {
                super.onScanResult(callbackType, resultado);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanResult() ");

                mostrarInformacionDispositivoBTLE( resultado );
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onBatchScanResults() ");

            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanFailed() ");

            }
        };

        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empezamos a escanear ");

        this.elEscanner.startScan( this.callbackDelEscaneo);

    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private void mostrarInformacionDispositivoBTLE(ScanResult resultado ) {

        BluetoothDevice bluetoothDevice = resultado.getDevice();
        byte[] bytes = resultado.getScanRecord().getBytes();
        int rssi = resultado.getRssi();

        Log.d(ETIQUETA_LOG, "----------------------------------------------------");
        Log.d(ETIQUETA_LOG, "------------ DISPOSITIVO DETECTADO BTLE ------------");
        Log.d(ETIQUETA_LOG, "----------------------------------------------------");
        Log.d(ETIQUETA_LOG, " nombre = " + bluetoothDevice.getName());
        //.d(ETIQUETA_LOG, " toString = " + bluetoothDevice.toString());

        /*
        ParcelUuid[] puuids = bluetoothDevice.getUuids();
        if ( puuids.length >= 1 ) {
            //Log.d(ETIQUETA_LOG, " uuid = " + puuids[0].getUuid());
           // Log.d(ETIQUETA_LOG, " uuid = " + puuids[0].toString());
        }*/

        //Log.d(ETIQUETA_LOG, " dirección = " + bluetoothDevice.getAddress());
        //Log.d(ETIQUETA_LOG, " rssi = " + rssi );

        //Log.d(ETIQUETA_LOG, " bytes = " + new String(bytes));
        //Log.d(ETIQUETA_LOG, " bytes (" + bytes.length + ") = " + Utilidades.bytesToHexString(bytes));

        TramaIBeacon tib = new TramaIBeacon(bytes);

        //Obtenemos los datos que se envían por el Major y el Minor
        major_datos = (Utilidades.bytesToInt(tib.getMajor()));
        minor_datos = (Utilidades.bytesToInt(tib.getMinor()));

        Log.d(ETIQUETA_LOG, " -------------------- DATOS -------------------- ");
        //Log.d(ETIQUETA_LOG, " prefijo  = " + Utilidades.bytesToHexString(tib.getPrefijo()));
        //Log.d(ETIQUETA_LOG, "          advFlags = " + Utilidades.bytesToHexString(tib.getAdvFlags()));
        //Log.d(ETIQUETA_LOG, "          advHeader = " + Utilidades.bytesToHexString(tib.getAdvHeader()));
        //Log.d(ETIQUETA_LOG, "          companyID = " + Utilidades.bytesToHexString(tib.getCompanyID()));
        //Log.d(ETIQUETA_LOG, "          iBeacon type = " + Integer.toHexString(tib.getiBeaconType()));
        //Log.d(ETIQUETA_LOG, "          iBeacon length 0x = " + Integer.toHexString(tib.getiBeaconLength()) + " ( "
        //        + tib.getiBeaconLength() + " ) ");
        //Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToHexString(tib.getUUID()));
        //Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " major  = " + Utilidades.bytesToHexString(tib.getMajor()) + "( "
                + Utilidades.bytesToInt(tib.getMajor()) + " ) ");
        Log.d(ETIQUETA_LOG, " minor  = " + Utilidades.bytesToHexString(tib.getMinor()) + "( "
                + Utilidades.bytesToInt(tib.getMinor()) + " ) ");
        //Log.d(ETIQUETA_LOG, " txPower  = " + Integer.toHexString(tib.getTxPower()) + " ( " + tib.getTxPower() + " )");
        Log.d(ETIQUETA_LOG, "----------------------------------");
        Log.d(ETIQUETA_LOG, "");


    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private void buscarEsteDispositivoBTLE(final String dispositivoBuscado ) {
        Log.d(ETIQUETA_LOG, " buscarEsteDispositivoBTLE(): empieza ");

        Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): instalamos scan callback ");


        // super.onScanResult(ScanSettings.SCAN_MODE_LOW_LATENCY, result); para ahorro de energía

        this.callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult( int callbackType, ScanResult resultado ) {
                super.onScanResult(callbackType, resultado);
                Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onScanResult() ");

                mostrarInformacionDispositivoBTLE( resultado );
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onBatchScanResults() ");

            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onScanFailed() ");

            }
        };

        ScanFilter sf = new ScanFilter.Builder().setDeviceName( dispositivoBuscado ).build();

        Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): empezamos a escanear buscando: " + dispositivoBuscado );
        //Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): empezamos a escanear buscando: " + dispositivoBuscado
        //      + " -> " + Utilidades.stringToUUID( dispositivoBuscado ) );

        this.elEscanner.startScan( this.callbackDelEscaneo );
    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private void detenerBusquedaDispositivosBTLE() {

        if ( this.callbackDelEscaneo == null ) {
            return;
        }

        this.elEscanner.stopScan( this.callbackDelEscaneo );
        this.callbackDelEscaneo = null;

    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public void botonBuscarDispositivosBTLEPulsado( View v ) {
        Log.d(ETIQUETA_LOG, " boton buscar dispositivos BTLE Pulsado" );
        this.buscarTodosLosDispositivosBTLE();
    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public void botonBuscarNuestroDispositivoBTLEPulsado( View v ) {
        Log.d(ETIQUETA_LOG, " boton nuestro dispositivo BTLE Pulsado" );
        //this.buscarEsteDispositivoBTLE( Utilidades.stringToUUID( "EPSG-GTI-PROY-3A" ) );

        //this.buscarEsteDispositivoBTLE( "EPSG-GTI-PROY-3A" );
        this.buscarEsteDispositivoBTLE( "GTI-JDEC" );

    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public void botonDetenerBusquedaDispositivosBTLEPulsado( View v ) {
        Log.d(ETIQUETA_LOG, " boton detener busqueda dispositivos BTLE Pulsado" );
        this.detenerBusquedaDispositivosBTLE();
    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    @SuppressLint("MissingPermission")
    private void inicializarBlueTooth() {
        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): obtenemos adaptador BT ");

        BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): habilitamos adaptador BT ");

        bta.enable();

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): habilitado =  " + bta.isEnabled() );

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): estado =  " + bta.getState() );

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): obtenemos escaner btle ");

        this.elEscanner = bta.getBluetoothLeScanner();

        if ( this.elEscanner == null ) {
            Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): Socorro: NO hemos obtenido escaner btle  !!!!");

        }

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): voy a perdir permisos (si no los tuviera) !!!!");

        if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        )
        {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION},
                    CODIGO_PETICION_PERMISOS);
        }
        else {
            Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): parece que YA tengo los permisos necesarios !!!!");

        }
    } // ()


    // --------------------------------------------------------------
    // --------------------------------------------------------------


    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults);

        switch (requestCode) {
            case CODIGO_PETICION_PERMISOS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d(ETIQUETA_LOG, " onRequestPermissionResult(): permisos concedidos  !!!!");
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                }  else {

                    Log.d(ETIQUETA_LOG, " onRequestPermissionResult(): Socorro: permisos NO concedidos  !!!!");

                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    } // ()

    //-------------------------------------------------------------------------
    //-------------------------------------------------------------------------
    //Guardamos la Medida
    public Medida guardarMedida(float valor, int sensor){
        Medida medida = new Medida();

        //Valor
        medida.setValor(valor);

        //Fecha actual
        long ahora = System.currentTimeMillis();
        Date fecha = new Date(ahora);

        medida.setTiempo(fecha.toString());

        //Nombre del Sensor
        String nombre_sensor;
        if(sensor == 1){
            nombre_sensor = "test";
        }else {
            nombre_sensor = "Ozono";
        }

        medida.setNombre_sensor(nombre_sensor);

        //Coordenadas
        //Se asigna a la clase LocationManager el servicio a nivel de sistema a partir del nombre
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Posteriormente, se asigna a la variable de tipo Location que accederá a la última posición conocida proporcionada por el proveedor
        @SuppressLint("MissingPermission") Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        Coordenada coordenada = new Coordenada();
        coordenada.setX((float) location.getLongitude());
        coordenada.setY((float) location.getLatitude());

        medida.setCoordenada(coordenada);
        return medida;
    }

    //-------------------------------------------------------------------------
    //-------------------------------------------------------------------------
    //Enviar Datos al Servidor

    public void botonEnviarAlServidor(View view) {
        //Medida medida = guardarMedida("Test",major_datos, minor_datos);
        Medida medida = guardarMedida(major_datos, minor_datos);

        //Envíar datos POST
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", null);
            jsonObject.put("valor", medida.getValor());
            jsonObject.put("fecha", medida.getTiempo());
            jsonObject.put("nombreSensor", medida.getNombre_sensor());
            jsonObject.put("longitud", medida.getCoordenada().getX());
            jsonObject.put("latitud", medida.getCoordenada().getY());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post("http://" + ip + ":8080/insertarMedicion")//Recordar cambiar ip cada vez que cambies de red
                .addJSONObjectBody(jsonObject) // posting json
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });

        Toast.makeText(getApplicationContext(),"Datos Enviados",Toast.LENGTH_SHORT).show();
    }

    public void botonEnviarAlServidorFake(View view) {
        //Medida medida = guardarMedida("Test",major_datos, minor_datos);
        Medida medida = guardarMedida(2, 2);

        //Log.d("medida", "medida: " + medida.getValor() + " " + medida.getTiempo() + " " + medida.getNombre_sensor() + " " + medida.getCoordenada().getX() + " " + medida.getCoordenada().getY());

        //Envíar datos POST
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", null);
            jsonObject.put("valor", medida.getValor());
            jsonObject.put("fecha", medida.getTiempo());
            jsonObject.put("nombreSensor", medida.getNombre_sensor());
            jsonObject.put("longitud", medida.getCoordenada().getX());
            jsonObject.put("latitud", medida.getCoordenada().getY());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post("http://" + ip + ":8080/insertarMedicion")//Recordar cambiar ip cada vez que cambies de red
                .addJSONObjectBody(jsonObject) // posting json
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });

        Toast.makeText(getApplicationContext(),"Datos Enviados",Toast.LENGTH_SHORT).show();
    }


}