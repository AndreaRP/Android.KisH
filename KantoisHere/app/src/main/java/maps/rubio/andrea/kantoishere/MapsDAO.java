package maps.rubio.andrea.kantoishere;

import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * DAO de la actividad MapsActivity
 * @author Andrea
 */
public class MapsDAO {
    private static final String TAG = "MapsDAO";
    //ObjectMapper mapper = new ObjectMapper();

    /**
     * Envia una peticion de todas las zonas que se encuentran en la BD. Recibe un array en json.
     *
     * @return
     */
    public String cargarZonas() {
        String resp = "";
        try {
            Connection.Response response = null;
            //manda peticion
            response = Jsoup.connect("http://kantoishere.byethost6.com//cargarZonas.php")
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .execute();
            resp = response.body();
            //Log.d(TAG, resp);
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }
        return resp;
    }

    /**
     * Envia una peticion de todos los puntos activos de una zona dada. Recibe un array en json.
     * @param codigoZona
     * @return
     */
    public String cargarPuntos(int codigoZona){
        String resp = "";
        try {
            Connection.Response response = null;
            //manda peticion
            response = Jsoup.connect("http://kantoishere.byethost6.com//cargarPuntos.php")
                    .data("zonaActual", codigoZona+"")
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .execute();
            resp = response.body();
           // Log.d(TAG, resp);
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }
        return resp;
    }

    /**
     * Envia una peticion de los bichos que pueden aparecer en una zona. Recibe un array en json.
     * @param codigoZona
     * @return
     */
    public String cargarBichos(int codigoZona){
        String resp = "";
        try {
            Connection.Response response = null;
            //manda peticion
            response = Jsoup.connect("http://kantoishere.byethost6.com//cargarBichoZona.php")
                    .data("zonaActual", codigoZona+"")
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .execute();
            resp = response.body();
           // Log.d(TAG, resp);
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }
        return resp;
    }

    /**
     * Envia una peticion de desactivar un punto con un codigo concreto.
     * @param codigoPunto
     * @return
     */
    public String desactivarPunto(int codigoPunto){
        String resp = "";
        try {
            Connection.Response response = null;
            //manda peticion
            response = Jsoup.connect("http://kantoishere.byethost6.com//desactivarPunto.php")
                    .data("campoPunto", codigoPunto+"")
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .execute();
            resp = response.body();
            // Log.d(TAG, resp);
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }
        return resp;
    }

    /**
     * Envia una peticion para introducir un bicho en la BD.
     * @param login
     * @param codigoBicho
     * @param codigoPunto
     * @param nombre
     * @return
     */
    public String aniadirBicho(String login, int codigoBicho, int codigoPunto, String nombre) {
        String resp = "";
        try {
            Connection.Response response = null;
            //manda peticion
            response = Jsoup.connect("http://kantoishere.byethost6.com//aniadirBicho.php")
                    .data("campoLogin", login)
                    .data("codigoBicho", codigoBicho + "")
                    .data("codigoPunto", codigoPunto + "")
                    .data("nombre", nombre + "")
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .execute();
            resp = response.body();
            Log.d(TAG, "anidirBichoMaps: " + resp);
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }
        return resp;
    }
}
