package com.jorge.sprint0_jorgelarrosaquesada;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


import com.androidnetworking.interfaces.JSONObjectRequestListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

//----------------------------------------------------
// Archivo: MainActivity.java
// J.Dec
//----------------------------------------------------

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Se declran las variables
//    String ip = "172.20.10.2";
    String ip = new LogicaFake().getIp();

    Boolean sesion;
    ImageView devices;
    private int ultimaMedida;
    private float major_datos;
    private int minor_datos;
    private int rssi2;
    private ImageView perfil;
    private ImageView information;
    private TextView distanciaSensor;
    private LocationManager locManager;
    FloatingActionButton fab;

    //Boton y Texto qr
    private ImageView buttonScan;
    private String UUIDScan;

    //    Notificaciones
    static final String CANAL_ID = "mi_canal";
    private PendingIntent pendingIntent;
    private ClaseLanzarNotificaciones notificaciones;


    private static Timer timer = new Timer();

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    //Sirve para guardar datos permanentes
    SharedPreferences myPreferences;
    SharedPreferences.Editor myEditor;

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private static final String ETIQUETA_LOG = ">>>>";
    private static final int CODIGO_PETICION_PERMISOS = 11223344;

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private BluetoothLeScanner elEscanner;
    private ScanCallback callbackDelEscaneo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainactivity);

        //Sirve para guardar valores de en la memoria interna del la app
        myPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        myEditor = myPreferences.edit();

        //Obtengo de la memoria interna el valor de la sesion
        sesion = myPreferences.getBoolean("sesion", false);

        Log.d("datos", ": " + myPreferences.getString("correo", "unknown"));
        Log.d("datos", ": " + myPreferences.getInt("ID_user", 0));
        Log.d("datos", ": " + myPreferences.getString("contrasenya", "unknown"));
        Log.d("datos", ": " + myPreferences.getInt("telefono", 0));
        Log.d("datos", ": " + myPreferences.getString("nombre", "unknown"));
        Log.d("datos", ": " + myPreferences.getString("apellidos", "unknown"));
        Log.d("datos", ": " + myPreferences.getString("estado", "unknown"));


        //mapa
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        supportMapFragment.getMapAsync(this);


        //Compruebo si la sesion este iniciada, si es falso te envía al login
        if (sesion == false) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }

        //Compruebo si el usuario tiene una placa asociada
        int ID_placa = myPreferences.getInt("ID_placa", 0);
        String UUID_user = myPreferences.getString("UUID_placa","TEST-GTI-TEST-00");
        Log.d("ID PLACA ON CREATE", String.valueOf(ID_placa));
        Log.d("UUID PLACA ON CREATE", myPreferences.getString("UUID_placa", "test"));



        //Librería encargada ser cocentarse con el Servidor
        AndroidNetworking.initialize(getApplicationContext());

        //Se solicitan los permisos para que el usuario seleccione si desea permitir el acceso al GPS del dispositivo:
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        //Se comprueba si se han concedido los permisos para mostrar los datos de latitud, longitud, altura y precisión del dispositivo
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Faltan los permisos", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            return;
        } else {
            //Te muestra un texto que está el gps activado
            Toast.makeText(getApplicationContext(), "GPS Activado", Toast.LENGTH_SHORT).show();
        }


        //Relaciono las variables con los id del layout
        perfil = findViewById(R.id.perfilImage);
        information = findViewById(R.id.imageInformation);
        buttonScan = findViewById(R.id.imageView8);
        devices = findViewById(R.id.profileImage);
        distanciaSensor = findViewById(R.id.distanciaDelSensor);

        //Botón que te lleva a la pestaña de EditPerfil.
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditarPerfil.class);
                startActivity(intent);
            }
        });

        //Botón que te lleva a la pestaña de InformacionAdicional.
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InformacionAdicional.class);
                startActivity(intent);
            }
        });

        //Botón para el escaneao del qr, Botón para llamar a la función botonLeerCodigoQR()
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botonLeerCodigoQR(view);
            }
        });

        //Botón que te lleva a la pestaña de Devices.
        devices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Devices.class);
                intent.putExtra("ID_user", myPreferences.getInt("ID_user", 0));
                intent.putExtra("ID_placa", myPreferences.getInt("ID_placa", 0));
                startActivity(intent);
            }
        });


        //Llama a la función inicializarBlueTooth()
        Log.d("placa", "onCreate: placa esta coño"+ myPreferences.getInt("ID_placa", 0));
        inicializarBlueTooth();

        ejecutarDelay();
        String stringuuid = "EPSG-GTI-PROY-3A";
        UUID uuid = Utilidades.stringToUUID(stringuuid);
        buscarEsteDispositivoBTLE(Utilidades.stringToUUID(stringuuid));

        Log.d(ETIQUETA_LOG, " onCreate(): termina ");

    } // onCreate()


    // .................................................................
    //  resultado: ScanResult
    //  -->
    // mostrarInformacionDispositivoBTLE()
    //
    // Esta función busca todos los dispositivo
    // .................................................................
    private void getDeviceInfo() {
        AndroidNetworking.get("http://" + ip + ":8080/buscarPlacaConId/" + myPreferences.getInt("ID_user", 0))
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("ID_USUARIO", String.valueOf(myPreferences.getInt("ID_user", 0)));

                            String uuid = response.getString("uuid");
                            int id = response.getInt("ID_placa");
                            Log.d("ID GETDEVICEINFO", String.valueOf(id));


                            myEditor.putInt("ID_placa", id);
                            myEditor.putString("UUID_placa", uuid);

                            myEditor.commit();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }


    // .................................................................
    //  resultado: ScanResult
    //  -->
    // mostrarInformacionDispositivoBTLE()
    //
    // Esta función busca todos los dispositivo
    // .................................................................
    @SuppressLint("MissingPermission")
    private void mostrarInformacionDispositivoBTLE(ScanResult resultado) {
        Log.d("entra", "mostrarInformacionDispositivoBTLE: entra");

        BluetoothDevice bluetoothDevice = resultado.getDevice();
        byte[] bytes = resultado.getScanRecord().getBytes();

        int rssi = resultado.getRssi();
        rssi2 = rssi;



        Log.d(ETIQUETA_LOG, "----------------------------------------------------");
        Log.d(ETIQUETA_LOG, "------------ DISPOSITIVO DETECTADO BTLE ------------");
        Log.d(ETIQUETA_LOG, "----------------------------------------------------");
        Log.d(ETIQUETA_LOG, " nombre = " + bluetoothDevice.getName());
        Log.d(ETIQUETA_LOG, " toString = " + bluetoothDevice.toString());

        ParcelUuid[] puuids = bluetoothDevice.getUuids();


        Log.d(ETIQUETA_LOG, " dirección = " + bluetoothDevice.getAddress());
        Log.d("rssi", " rssi = " + rssi2);

        Log.d(ETIQUETA_LOG, " bytes = " + new String(bytes));
        Log.d(ETIQUETA_LOG, " bytes (" + bytes.length + ") = " + Utilidades.bytesToHexString(bytes));

        TramaIBeacon tib = new TramaIBeacon(bytes);

        //Obtenemos los datos que se envían por el Major y el Minor
        major_datos = (Utilidades.bytesToInt(tib.getMajor()));
        minor_datos = (Utilidades.bytesToInt(tib.getMinor()));

        Log.d(ETIQUETA_LOG, " -------------------- DATOS -------------------- ");
        Log.d(ETIQUETA_LOG, "          advFlags = " + Utilidades.bytesToHexString(tib.getAdvFlags()));
        Log.d(ETIQUETA_LOG, "          advHeader = " + Utilidades.bytesToHexString(tib.getAdvHeader()));
        Log.d(ETIQUETA_LOG, "          companyID = " + Utilidades.bytesToHexString(tib.getCompanyID()));
        Log.d(ETIQUETA_LOG, "          iBeacon type = " + Integer.toHexString(tib.getiBeaconType()));
        Log.d(ETIQUETA_LOG, "          iBeacon length 0x = " + Integer.toHexString(tib.getiBeaconLength()) + " ( "
                + tib.getiBeaconLength() + " ) ");
        Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToHexString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " uuid2  = " + Utilidades.bytesToString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " major  = " + Utilidades.bytesToHexString(tib.getMajor()) + "( "
                + Utilidades.bytesToInt(tib.getMajor()) + " ) ");
        Log.d(ETIQUETA_LOG, " minor  = " + Utilidades.bytesToHexString(tib.getMinor()) + "( "
                + Utilidades.bytesToInt(tib.getMinor()) + " ) ");
        Log.d(ETIQUETA_LOG, " txPower  = " + Integer.toHexString(tib.getTxPower()) + " ( " + tib.getTxPower() + " )");
        Log.d(ETIQUETA_LOG, "----------------------------------");
        Log.d(ETIQUETA_LOG, "");

        int distance = 10^((tib.getTxPower() - rssi2 )/10*4 );
        Log.d("Distancia", distance + "nombre" +  Utilidades.bytesToString(tib.getUUID()));

    } // ()


    // .................................................................
    //  dispositivoBuscado: UUID
    //  -->
    // buscarEsteDispositivoBTLE()
    //
    // Esta función busca el dispositivo que le hemos pasado por valores
    // .................................................................

    @SuppressLint("MissingPermission")
    private void buscarEsteDispositivoBTLE(final UUID dispositivoBuscado) {
        Log.d(ETIQUETA_LOG, " buscarEsteDispositivoBTLE(): empieza ");

        Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): instalamos scan callback ");


        // super.onScanResult(ScanSettings.SCAN_MODE_LOW_LATENCY, result); para ahorro de energía

        this.callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult resultado) {
                super.onScanResult(callbackType, resultado);
                Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onScanResult() ");

                mostrarInformacionDispositivoBTLE(resultado);
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

        ScanFilter sf = new ScanFilter.Builder().setDeviceName(dispositivoBuscado.toString()).build();
        Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): empezamos a escanear buscando: " + dispositivoBuscado);
        //Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): empezamos a escanear buscando: " + dispositivoBuscado
        //      + " -> " + Utilidades.stringToUUID( dispositivoBuscado ) );

        this.elEscanner.startScan(this.callbackDelEscaneo);
    } // ()


    private void buscarEsteDispositivoBTLE2(final String dispositivoBuscado) {
        Log.d(ETIQUETA_LOG, " buscarEsteDispositivoBTLE(): empieza ");

        Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): instalamos scan callback ");


        // super.onScanResult(ScanSettings.SCAN_MODE_LOW_LATENCY, result); para ahorro de energía

        this.callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult resultado) {
                super.onScanResult(callbackType, resultado);
                Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onScanResult() ");

                mostrarInformacionDispositivoBTLE(resultado);
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

        ScanFilter sf = new ScanFilter.Builder().setDeviceName(dispositivoBuscado).build();
        Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): empezamos a escanear buscando: " + dispositivoBuscado);
        //Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): empezamos a escanear buscando: " + dispositivoBuscado
        //      + " -> " + Utilidades.stringToUUID( dispositivoBuscado ) );

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        this.elEscanner.startScan(this.callbackDelEscaneo);
    } // ()

    // .................................................................
    //
    // detenerBusquedaDispositivosBTLE()
    //
    // Esta función detiene la busqueda de Beacons
    // .................................................................

    @SuppressLint("MissingPermission")
    public void detenerBusquedaDispositivosBTLE() {

        if (this.callbackDelEscaneo == null) {
            return;
        }

        this.elEscanner.stopScan(this.callbackDelEscaneo);
        this.callbackDelEscaneo = null;


    } // ()

    // .................................................................
    //
    // inicializarBlueTooth()
    //
    // .................................................................
    @SuppressLint("MissingPermission")
    private void inicializarBlueTooth() {
        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): obtenemos adaptador BT ");

        BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): habilitamos adaptador BT ");

        bta.enable();

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): habilitado =  " + bta.isEnabled());

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): estado =  " + bta.getState());

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): obtenemos escaner btle ");

        this.elEscanner = bta.getBluetoothLeScanner();

        if (this.elEscanner == null) {
            Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): Socorro: NO hemos obtenido escaner btle  !!!!");

        }

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): voy a perdir permisos (si no los tuviera) !!!!");

        if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION},
                    CODIGO_PETICION_PERMISOS);
        } else {
            Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): parece que YA tengo los permisos necesarios !!!!");

        }
    } // ()

    // .................................................................
    // requestCode: R, permissions: <Texto>, grantResults: <R>
    // -->
    // onRequestPermissionsResult()
    //
    // .................................................................
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CODIGO_PETICION_PERMISOS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d(ETIQUETA_LOG, " onRequestPermissionResult(): permisos concedidos  !!!!");
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {

                    Log.d(ETIQUETA_LOG, " onRequestPermissionResult(): Socorro: permisos NO concedidos  !!!!");

                }
                return;
        }

        // Other 'case' lines to check for other
        // permissions this app might request.
    } // ()


    // .................................................................
    // valor: Z, sensor: R
    // -->
    // guardarMedida() <--
    // <--
    // Medida:
    // {valor: Z, tiempo: Texto, nombre_sensor: Texto, coordenada: Coordenada}
    //
    // Esta función crea un objeto medida mediante un constructor al cual se le pasan los valores mencionados arriba y lo devuelve.
    // .................................................................
    public Medida guardarMedida(float valor, int sensor) {
        Medida medida = new Medida();
        Float valurNull = new Float(valor);

        //Valor
        medida.setValor(valor);

        //Fecha actual
        int ahora = (int) System.currentTimeMillis();

        Log.d("AHORA TIME LONG", String.valueOf(ahora));
        medida.setTiempo(ahora);

        //Nombre del Sensor
        String nombre_sensor;
        if (sensor == 1) {
            nombre_sensor = "test";
        } else {
            nombre_sensor = "Ozono";
        }

        //        Calculando la diferencia de tiempo entre la ultima medida y la hora actual. Si el tiempo supera 2 minutos envia la notificacion
        long tMinTotal = System.currentTimeMillis() / (60 * 1000);
        long minMedida = medida.getTiempo() / (60*1000);
        long milliseconds = tMinTotal - minMedida;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        Log.d("minutos", "Minutos obtenidos " + String.valueOf(minutes));
        Log.d("minutos", "Minutos de la Medida " + String.valueOf(minMedida));
        Log.d("minutos", "Minutos de ahora " + String.valueOf(tMinTotal));

//        if(minutes>2) {
//            notificaciones = new ClaseLanzarNotificaciones(getApplicationContext());
//            notificaciones.crearNotificacion("Desconectado","El sensor esta desconectado o fuera del alcance");
//            insertarNotificacion("Desconectado","El sensor esta desconectado o fuera del alcance");
//        }

        if(valor > 60){
            notificaciones = new ClaseLanzarNotificaciones(getApplicationContext());
            notificaciones.crearNotificacion("Limite alcanzado","Alto nivel de ozono por la zona");
            insertarNotificacion("Limite alcanzado", "Alto nivel de ozono por la zona");
        }
//        else if(valurNull.equals(null) || ){
//            notificaciones = new ClaseLanzarNotificaciones(getApplicationContext());
//            notificaciones.crearNotificacion("Desconectado","El sensor esta desconectado o fuera del alcance");
//        }

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

    // .................................................................
    // view: View
    // -->
    // botonEnviarAlServidor()
    //
    // Esta función crea una medida con los valores que obtiene por los imputs, después hace un post al servidor node insertando los datos en l bd
    // .................................................................
    public void botonEnviarAlServidor() {
        //Medida medida = guardarMedida("Test",major_datos, minor_datos);
        Medida medida = guardarMedida(major_datos, minor_datos);
        ultimaMedida = (int) medida.getValor();

        //Envíar datos POST
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", null);
            jsonObject.put("valor", medida.getValor());
            jsonObject.put("fecha", medida.getTiempo());
            jsonObject.put("nombreSensor", medida.getNombre_sensor());
            jsonObject.put("longitud", medida.getCoordenada().getX());
            jsonObject.put("latitud", medida.getCoordenada().getY());
            jsonObject.put("ID_placa", myPreferences.getInt("ID_placa", 0));

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

        //Toast.makeText(getApplicationContext(), "Datos Enviados", Toast.LENGTH_SHORT).show();
        Log.d("DATOS ENVIADOS", "DATOS ENVIADOS");
    }

    // .................................................................
    // view: View
    // -->
    // botonLeerCodigoQR()
    //
    // Esta función lee el qr
    // .................................................................

    public void botonLeerCodigoQR(View view) {
        IntentIntegrator integrador = new IntentIntegrator(MainActivity.this);
        integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrador.setPrompt("lECTOR - cdp");
        integrador.setBeepEnabled(true);
        integrador.setBarcodeImageEnabled(true);
        integrador.initiateScan();
    }

    // .................................................................
    // requestCode: int, resultCode: int , data: Intent
    // -->
    // onActivityResult()
    //
    // Esta función comprueba si se ha escaneado bien el qr, si es correcto guarda el valor y llama a ejecutarDelay()
    // .................................................................

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult resultado = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (resultado != null) {
            if (resultado.getContents() == null) {
                Toast.makeText(this, "Lectura cancelada", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, resultado.getContents(), Toast.LENGTH_LONG).show();
                buscarEsteDispositivoBTLE(Utilidades.stringToUUID(resultado.getContents()));

                UUIDScan = resultado.getContents();
                enviarUUIDAlServidor();
                ejecutarDelay();
            }
        } else {

            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // .................................................................
    //
    // ejecutarDelay()
    //
    // Esta función llama a botonEnviarAlServidor() tras 10 segundos
    // .................................................................
    private void ejecutarDelay() {

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //what you want to do
                botonEnviarAlServidor();
                //buscarEsteDispositivoBTLE2(myPreferences.getString("UUID_placa", "null"));
            }
        }, 0, 10000);//wait 0 ms before doing the action and do it evry 10000ms (10second)
    }


    public void pararTimer(){
        timer.cancel();
    }


    @Override
    public void onMapReady(GoogleMap mapa) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mapa.setMyLocationEnabled(true);

        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Posteriormente, se asigna a la variable de tipo Location que accederá a la última posición conocida proporcionada por el proveedor
        @SuppressLint("MissingPermission") Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //LatLng real = new LatLng(0, 0);

        LatLng real = new LatLng(location.getLatitude(), location.getLongitude());

        mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(real,12));

    }


    //Métodos get y post sensor
    public void enviarUUIDAlServidor() {
        Medida medida = guardarMedida(major_datos, minor_datos);
        ultimaMedida = (int) medida.getValor();

        //Envíar datos POST
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID_placa", null);
            jsonObject.put("ID_user", myPreferences.getInt("ID_user", 0));
            jsonObject.put("uuid", UUIDScan);
            jsonObject.put("estadoPlaca", 1);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post("http://" + ip + ":8080/insertarPlaca")//Recordar cambiar ip cada vez que cambies de red
                .addJSONObjectBody(jsonObject) // posting json
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });

        Toast.makeText(getApplicationContext(),"Placa enviada",Toast.LENGTH_SHORT).show();
    }


    private void insertarNotificacion(String motivo, String mensaje){
        Date date = new Date();
        String fecha = ""+date.getDay()+"."+date.getMonth();

//        TODO conseguir ID_PLACA y ID_USER
        Notificacion notificacion = guardarNotificacione(motivo,mensaje,fecha,1,1);

        //Envíar datos POST
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID_notificacion", null);
            jsonObject.put("motivo", notificacion.getMotivo());
            jsonObject.put("mensaje", notificacion.getMensaje());
            jsonObject.put("fecha", notificacion.getFecha());
            jsonObject.put("ID_user", notificacion.getID_user());
            jsonObject.put("ID_placa", notificacion.getID_placa());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post("http://" + ip + ":8080/insertarNotificacion")//Recordar cambiar ip cada vez que cambies de red
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

//        Toast.makeText(getApplicationContext(),"Notificacion Creada",Toast.LENGTH_SHORT).show();
    }

    // .................................................................
    // motivo: Texto, mensaje: Texto, fecha: Texto, ID_user: R, ID_placa: R
    // -->
    // guardarNotificacione() <--
    // <--
    // Notificacion:
    // {motivo: Texto, mensaje: Texto, fecha: Texto, ID_user: R, ID_placa: R}
    //
    // Esta función crea un objeto usuario mediante un constructor al cual se le pasan los valores mencionados arriba y lo devuelve.
    // .................................................................

    private Notificacion guardarNotificacione(String motivo, String mensaje, String fecha, int ID_user, int ID_placa){
        Notificacion notificacion = new Notificacion();

        notificacion.setMotivo(motivo);
        notificacion.setMensaje(mensaje);
        notificacion.setFecha(fecha);
        notificacion.setID_user(ID_user);
        notificacion.setID_placa(ID_placa);

        return notificacion;
    }


}

