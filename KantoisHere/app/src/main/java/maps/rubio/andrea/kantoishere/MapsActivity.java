package maps.rubio.andrea.kantoishere;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Clase que gestiona la posicion en el mapa del usuario, la coteja con las zonas y puntos de la base de datos
 * y inicia la actividad capturar si se dan las condiciones adecuadas (dentro de una zona de hierba alta y en un punto activo)
 *
 * @author Andrea
 */
public class MapsActivity extends FragmentActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    // para almacenar en el bundle
    private final static String ultimoValor = "location-key";
    private static final String TAG = "Maps";
    //para la localizacion
    private static final long updateDefault = 5000;//refresco cada 5 segundos
    private static final long updateMax = updateDefault / 2;//maxima frecuencia de refresco, 2.5 sg
    private String nombreGenerico;
    private int codigoBicho;
    //private boolean activo = false;
    private int capturaRequestCode = 1;
    //mapa (null si no hay conexion con google services)
    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;//instancia para google play services
    private Location mLastLocation; // ultima localizacion conocida
    private Location mCurrentLocation;// localizacion actual
    //private Marker puntero;
    private LocationRequest mLocationRequest;
    //server
    private ArrayList<Zona> zonas;
    private ArrayList<Bicho> bichos;
    private ArrayList<Punto> puntos;
    private CircleOptions circulo = new CircleOptions().visible(false);
    private Zona zonaActual = new Zona();
    private Punto puntoActual = new Punto();
    private Handler handler = new Handler();
    private Comprobador comprobador;
    private boolean hayCirculo = false;
    private String login;
    private int bichoRandomInt;

    /**
     * Al iniciarse se recogen los datos que le ha pasado la actvidad principal, se cargan las zonas posibles en un arrayList
     * y comprueba que la localizacion esta activada. Si lo esta se continua con la ejecucion, si no se finaliza la actividad.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        zonaActual.setCodigo(0);
        login = getIntent().getStringExtra("login");
        /*Cargamos las posibles zonas*/
        CargarZonas c = new CargarZonas();
        c.execute();

        /*se comprueba que la localización está activada*/
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            setUpMapIfNeeded();//muestra el mapa
            /*se recupera el valor del bundle, si lo hay*/
            updateValuesFromBundle(savedInstanceState);
            buildGoogleApiClient();//crea la instancia de google play services
        } else {
            //voy a la actividad main
            //Log.i(TAG, "Home");
            finish();
        }
    }

    /**
     * Crea la conexion a google play services (en onStop se desconecta)
     */
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    /**
     * Al reiniciar se carga el mapa que ya existiera y se comienza a actualizar la posicion.
     */
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    /**
     * Al pausar se paran los updates, pero no se desconecta de Google play services.
     */
    @Override
    protected void onPause() {
        super.onPause();
        // Quita los updates, pero no se desconecta.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    /**
     * Al parar se desconecta del cliente de google play services.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    /*===============================================================MAPS SETUP================================================================*/

    /**
     * Crea la instancia de google play services y añade la api de localizacion.
     * Despues comienza a localizar.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();//
    }

    /**
     * Crea un mapa o lo carga si ya existe.
     */
    private void setUpMapIfNeeded() {
        // Si el mapa aun no se ha creado
        if (mMap == null) {
            // Se optiene el mapa del support fragment
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Si se consiguió sacar el mapa del suportfragment, se muestra el mapa
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * Se crea un puntero en la posicion default 0,0 si no hay localización previa
     * si la hay, se establece en ella.
     * Aqui se incluyen los Markers o lineas, se ponen los listener a la camara. Solo debe llamarse
     * cuando se ha comprobado que {@link #mMap} no es null.
     */
    private void setUpMap() {

        if (mLastLocation == null) {
            //Log.i(TAG, "mLastLocation == null");
            //puntero = mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        } else {
            //Log.i(TAG, "mLastLocation != null");
            //puntero = mMap.addMarker(new MarkerOptions().
            //       position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())).title("Marker"));
            // puntero.setPosition(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        }
        //se hace zoom para que aparezcan las calles
        //mMap.animateCamera(CameraUpdateFactory.zoomIn());
    }

    /**
     * Cuando el cliente esta listo coge la longitud y latitud
     *
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        //Log.i(TAG, "onConnected");
        if (mCurrentLocation == null) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        marcarPosicion();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    /**
     * Crea el location request, estableciendo el intervalo por defecto y maximo de refresco.
     * Ademas establece la prioridad de llamadas.
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(updateDefault);
        mLocationRequest.setFastestInterval(updateMax);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Actualiza la posicion utilizando la instancia de google play services (mGoogleApiClient),
     * en la variable mLocationRequest y el listener de la clase
     */
    protected void startLocationUpdates() {
        //Log.i(TAG, "startLocationUpdates");
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * Coloca la camara para que el usuario pueda ver la zona del mapa que tiene hierba alta.
     */
    private void colocarCamara() {
        CameraUpdate cameraPos = CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 15);
        mMap.animateCamera(cameraPos);
    }

    /**
     * Marca la posicion actual en el mapa
     */
    private void marcarPosicion() {
        mMap.setMyLocationEnabled(true);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    /**
     * Método abstracto de location listener, es llamado desde startLocationUpdates
     */
    @Override
    public void onLocationChanged(Location location) {
        // mMap.addCircle(circulo);
        Log.i(TAG, "onLocationChanged");
        mCurrentLocation = location;
        colocarCamara();
        actualizaZona();
    }

    /*====================================================== LOGICA DE LA APP ============================================================*/

    /**
     * Comprueba si las coordenadas actuales se encuentran dentro de alguna de las zonas marcadas.
     * Si estamos dentro comprueba si es la primera vez que se entra, si lo es hace tres cosas:
     * - descarga del servidor los puntos correspondientes a esa zona (activa una tarea asincrona
     * independiente, que cada 2 min comprueba si los puntos han cambiado)
     * - descarga del servidor los bichos que se pueden encontrar en esa zona
     * - pinta en el mapa la zona, para dar informacion visual al usuario en todo momento
     * Si no lo es pero aun estamos en la zona, dejara de comprobar (no se puede estar en dos zonas a la vez)
     * Al salir de la zona comprobara si aun sigue marcada en el mapa y creara una nueva vacia, poniendo su codigo a 0
     * Ademas, elimina la cola del handler que actualizaba los puntos cada 2 min
     */
    private void actualizaZona() {
        //en cada cambio de posicion se comprueba si se está en una zona
        boolean zonaEncontrada = false;
        for (int i = 0; i < zonas.size() && !zonaEncontrada; i++) {
            if (zonas.get(i).estaDentro(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())) {//al entrar en una zona
                if (zonaActual.getCodigo() == 0 || zonaActual != zonas.get(i)) {
                    //se actualiza la zonaActual a la nueva zona
                    zonaActual = zonas.get(i);
                }
                if (!circulo.isVisible()) {//si no estaba ya pintado, se pinta
                    pintarZona(zonaActual.getLat(), zonaActual.getLongitud(), zonaActual.getRadio());
                    new CargarBichos().execute();//se cargan los posibles bichos (solo una vez)
                    new CargarPuntos().execute();//se bajan los puntos activos (solo una vez)(empieza a correr la async task)
                }
                /*miramos si estamos en posicion de un pokemon (al cargar en tarea asincrona, las primeras veces
                * aun no habra cargado y sera nulo*/
                if (puntos != null)
                    hayBicho();

                zonaEncontrada = true;//impido que siga evaluando el resto de las zonas
            } else {//si no se está en una zona
                /*si el circulo esta pintado aunque ya no se este en la zona marcada como actual
                * se borra*/
                if (circulo.isVisible() && !zonaActual.estaDentro(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())) {//comprobamos si aun esta pintada y la borramos (la primera vez)
                    borrarZona();
                    /*se quita la zona actual*/
                    zonaActual = new Zona();
                    zonaActual.setCodigo(0);
                    //myasyntask.cancel();//paro la que haya
                    handler.removeCallbacks(comprobador);//elimino la cola del handler
                }
            }
        }
    }

    /**
     * Dibuja un circulo sobre la zona en la que se encuentra el usuario actualmente.
     *
     * @param lat
     * @param longi
     * @param radio
     */
    private void pintarZona(double lat, double longi, int radio) {
        //creamos el circulo
        Log.i(TAG, "pintarCirculo");
        circulo.center(new LatLng(lat, longi))
                .radius(radio) // In meters
                .fillColor(Color.argb(80, 0, 124, 0));
        circulo.visible(true);
        if (!hayCirculo) {
            mMap.addCircle(circulo);
            hayCirculo = true;
            //avisamos al usuario
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
            Toast.makeText(getApplicationContext(), R.string.entra_zona,
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Elimina el circulo que representq la zona en la que se encuetra el usuario.
     */
    private void borrarZona() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
        Toast.makeText(getApplicationContext(), R.string.sale_zona,
                Toast.LENGTH_LONG).show();
        //borramos el circulo
        circulo.visible(false);
    }

    /**
     * Comprueba si nos encontramos en un punto de aparicion de pkmn, si lo estamos, escoge uno de los posibles
     * al azar y lanza la aplicacion de captura, esperando la respuesta.
     */
    private void hayBicho() {
        Log.i(TAG, "hayBicho");
        boolean encontrado = false;
        // int puntoActual;
        Log.i(TAG, "puntoActual: " + mCurrentLocation.getLatitude() + " " + mCurrentLocation.getLongitude());
        for (int i = 0; i < puntos.size() && !encontrado; i++) {
            if (puntos.get(i).enPunto(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())) {//al entrar en un punto
                Log.i(TAG, "estoy en punto");
                Log.i(TAG, "puntoActual: " + mCurrentLocation.getLatitude() + " " + mCurrentLocation.getLongitude());
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(500);
                Toast.makeText(getApplicationContext(), R.string.bicho_salvaje,
                        Toast.LENGTH_LONG).show();
                // puntoActual = i;
                encontrado = true;
                /*se decide aleatoriamente el bicho que va a salir*/
                Random r = new Random();
                bichoRandomInt = r.nextInt(bichos.size());
                Bicho b = bichos.get(bichoRandomInt);
                codigoBicho = b.getnPokemon();
                puntoActual = puntos.get(i);
                /*quitamos el punto del array en memoria y mandamos al server la info de que esta inactivo*/
                new DesactivarPunto(puntos.get(i).getCodigo()).execute();
                Log.i(TAG, "desactivarPunto lanzado");
                puntos.remove(i);
                /*Indicamos el nombre generico para poder acceder*/
                nombreGenerico = b.getNombreGenerico().toLowerCase();
                /*Lanzamos la actividad vuforia para capturar el bicho, pasandole el nombre del bicho
                * que ha aparecido*/
                Intent intent = new Intent("com.AR.KisHCaptura.UnityPlayerNativeActivity");
                intent.putExtra("nombreGenerico", b.getNombreGenerico().toLowerCase());
                startActivityForResult(intent, capturaRequestCode);
            }
        }
    }

    /**
     * Se recoge la info de si se ha capturado o no. Si se ha captuado se pide al usuario que le ponga nombre.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // comprobamos que se venga de capturar, y si se ha usado o liberado un bicho
        Log.i(TAG, "code: " + capturaRequestCode);
        if (requestCode == capturaRequestCode && data != null) {
            /*recuperamos los datos de la captura*/
            boolean atrapado = data.getBooleanExtra("capturado", true);
            //int posicion = data.getIntExtra("posicion", -1);
            if (atrapado) {
                String text = String.format(getResources().getString(R.string.conseguido), nombreGenerico);
                Toast.makeText(getApplicationContext(),
                        text, Toast.LENGTH_SHORT)
                        .show();
                ponerNombre();

            } else {
                String text = String.format(getResources().getString(R.string.huyo), nombreGenerico);
                Toast.makeText(getApplicationContext(),
                        text, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    /**
     * Se pregunta el mote que se le quiere dar al pokemon recien capturado.
     * Una vez elegido, se hace una peticion al servidro para insertarlo en la BD mediante la clase {@link MapsDAO}.
     */
    private void ponerNombre() {
        //Log.i(TAG, "ponerNombre");
        /*preguntamos el nombre del bicho mediante un alert dialog*/
        String mensaje = String.format(getResources().getString(R.string.poner_mote), nombreGenerico);
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.layout_nombre_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        TextView tv = (TextView) promptsView.findViewById(R.id.tvPonerMote);
        tv.setText(mensaje);
        final EditText result = (EditText) promptsView.findViewById(R.id.etMote);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Listo",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                aniadirBicho(result.getText().toString());
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Paso",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                aniadirBicho(nombreGenerico);
                                dialog.cancel();
                            }
                        });

        // crea el Alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // lo muestra
        alertDialog.show();

    }

    /**
     * Inicia la tarea asincrona que guarda en bicho en la BD.
     *
     * @param nombre
     */
    private void aniadirBicho(String nombre) {
        Log.i(TAG, "aniadirBicho");
          /*guardamos en el servidor*/
        new AniadirBicho(nombre).execute();
    }
/*================================================ESTABILIDAD=======================================================================*/

    /**
     * Guarda los parametros en el bundle para que puedan recuperarse si la actividad debe volver a crearse.
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(ultimoValor, mCurrentLocation);
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("bichos", bichos);
        savedInstanceState.putParcelableArrayList("zonas", zonas);
        savedInstanceState.putParcelableArrayList("puntos", puntos);
        savedInstanceState.putParcelable("circulo", circulo);
        savedInstanceState.putParcelable("zonaActual", zonaActual);
        savedInstanceState.putParcelable("puntoActual", puntoActual);
        savedInstanceState.putBoolean("hayCirculo", hayCirculo);
        savedInstanceState.putString("nombreGenerico", nombreGenerico);
        savedInstanceState.putString("login", login);
        savedInstanceState.putInt("bichoRandomInt", bichoRandomInt);
        savedInstanceState.putInt("codigoBicho", codigoBicho);
    }

    /**
     * Recupera los valores del bundle si vuelve a crearse la actividad.
     *
     * @param savedInstanceState
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        Log.i(TAG, "Updating values from bundle");
        if (savedInstanceState != null) {
            /*actualiza el valor del mCurrentLocation desde el bundle y actualiza el
            * puntero para mostrar la posicion*/
            if (savedInstanceState.keySet().contains(ultimoValor)) {
                // Since ultimoValor was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(ultimoValor);
                bichos = savedInstanceState.getParcelableArrayList("bichos");
                puntos = savedInstanceState.getParcelableArrayList("puntos");
                zonas = savedInstanceState.getParcelableArrayList("zonas");
                circulo = savedInstanceState.getParcelable("circulo");
                zonaActual = savedInstanceState.getParcelable("zonaActual");
                hayCirculo = savedInstanceState.getBoolean("hayCirculo");
                nombreGenerico = savedInstanceState.getString("nombreGenerico");
                login = savedInstanceState.getString("login");
                bichoRandomInt = savedInstanceState.getInt("bichoRandomInt");
                codigoBicho = savedInstanceState.getInt("codigoBicho");
                puntoActual = savedInstanceState.getParcelable("puntoActual");
                if (circulo.isVisible()) {
                    mMap.addCircle(circulo);
                }
            }
            //actualizaPuntero();
        }
    }
        /*===============================================TAREAS ASINCRONAS============================================*/

    /**
     * Tarea asincrona que recarga los puntos activos mediante la clase {@link MapsDAO}.
 */
    private class Comprobador implements Runnable {
        @Override
        public void run() {
            new CargarPuntos().execute();
        }
    }

    /**
     * Tarea asíncrona que carga los puntos activos.
     */
    private class CargarPuntos extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... arg0) {
            Log.i(TAG, "CargarPuntos: doInBackground");
            String respuesta = "";
            try {
                MapsDAO c = new MapsDAO();
                respuesta = c.cargarPuntos(zonaActual.getCodigo());
                //Log.i(TAG, respuesta);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return respuesta;
        }

        protected void onPostExecute(String result) {
            //cargar los puntos
            ObjectMapper mapper = new ObjectMapper();
            try {
                puntos = mapper.readValue(result, new TypeReference<ArrayList<Punto>>() {
                });
                handler = new Handler();
                comprobador = new Comprobador();
                handler.postDelayed(comprobador, 5 * 60 * 1000);//cada 5 min
            } catch (IOException e) {
                Log.i(TAG, e.getMessage());
                //e.printStackTrace();
            }
        }
    }

    /**
     * Taera asincrona que desactiva un punto que ha sido "usado" mediante la clase {@link MapsDAO}.
     */
    private class DesactivarPunto extends AsyncTask<String, Void, String> {
        int codigoPunto;

        DesactivarPunto(int codigoPunto) {
            this.codigoPunto = codigoPunto;
        }

        @Override
        protected String doInBackground(String... strings) {
            String respuesta = "";
            try {
                respuesta = new MapsDAO().desactivarPunto(codigoPunto);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return respuesta;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    /**
     * Tarea asincrona que inserta un bicho a la BD mediante la clase {@link MapsDAO}.
     */
    public class AniadirBicho extends AsyncTask<String, Void, String> {
        private String nombre;

        public AniadirBicho(String nombre) {
            this.nombre = nombre;
        }

        @Override
        protected String doInBackground(String... strings) {
            String respuesta = "";
            try {
                respuesta = new MapsDAO().aniadirBicho(login, codigoBicho, puntoActual.getCodigo(), nombre);
                bichoRandomInt = 0;//reseteamos el codigo
                puntoActual = new Punto();//reseteamos el puntoActual
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return respuesta;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    /**
     * Tarea asincrona que carga las zonas que existen mediante la clase {@link MapsDAO}.
     */
    private class CargarZonas extends AsyncTask<String, Void, String> {
        //Context contexto = null;
        //public CargarZonas(Context context) {
        //  contexto = context;
        //}

        @Override
        protected String doInBackground(String... strings) {
            Log.i(TAG, "CargarZonas: doInBackground");
            String respuesta = "";
            try {
                MapsDAO c = new MapsDAO();
                respuesta = c.cargarZonas();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return respuesta;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            /**/
            ObjectMapper mapper = new ObjectMapper();
            try {
                zonas = mapper.readValue(s, new TypeReference<ArrayList<Zona>>() {
                });
                Log.i(TAG, "zonas.size: " + zonas.size() + "");
            } catch (IOException e) {
                Log.i(TAG, e.getMessage());
            }

        }
    }

    /**
     * Tarea asincrona que carga los posibles bichos de una zona mediante la clase {@link MapsDAO}.
     */
    private class CargarBichos extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            Log.i(TAG, "CargarBichos: doInBackground");
            String respuesta = "";
            try {
                respuesta = new MapsDAO().cargarBichos(zonaActual.getCodigo());
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return respuesta;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ObjectMapper mapper = new ObjectMapper();
            try {
                bichos = mapper.readValue(s, new TypeReference<ArrayList<Bicho>>() {
                });
            } catch (IOException e) {
                Log.i(TAG, e.getMessage());
            }
        }
    }
}
