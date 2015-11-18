package maps.rubio.andrea.kantoishere;

import android.util.Log;

import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * DAO de Usuario
 * @author Andrea
 */
public class UsuarioDAO {
    private static final String TAG = "UsuarioDAO";
    ObjectMapper mapper = new ObjectMapper();

    /**
     * Realiza la peticion de login mediante una pass y login concretos.
     *
     * @param login
     * @param pass
     * @return
     */
    public String loginString(String login, String pass) {
        String resp = "";
        try {
            //pass = PasswordHash.createHash(pass);
            Connection.Response response = null;
            //manda peticion
            response = Jsoup.connect("http://kantoishere.byethost6.com//login.php")
                    .data("campoLogin", login)
                    .data("campoPass", pass)
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .execute();
            resp = response.body();
            Log.d(TAG, resp);
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }
        return resp;
    }

    /**
     * Realiza la peticin de los bichos que posee un usuario.
     * @param login
     * @return
     */
    public String cargarBichos(String login) {
        String resp = "";
        try {
            Connection.Response response = null;
            //manda peticion
            response = Jsoup.connect("http://kantoishere.byethost6.com//cargarBichos.php")
                    .data("campoLogin", login)
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .execute();
            resp = response.body();
            Log.d(TAG, resp);
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }
        return resp;
    }

    /**
     * Realiza la peticion de update de un conjunto de bichos en la base de datos, mandandolos como
     * un array en json.
     * @param bichos
     * @return
     */
    public String actualizarBichos(String bichos) {
        String resp = "";
        try {
            //pass = PasswordHash.createHash(pass);
            Connection.Response response = null;
            //manda peticion
            response = Jsoup.connect("http://kantoishere.byethost6.com//actualizarBichos.php")
                    .data("bichos",bichos)
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .execute();
            resp = response.body();
            Log.d(TAG, "actualizarBichos: "+resp);
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }
        return resp;
    }

    /**
     * Realiza la peticion de eliminar de la BD al bicho dado.
     * @param bicho
     * @return
     */
    public String liberarBicho(String bicho) {
        String resp = "";
        try {
            Connection.Response response = null;
            response = Jsoup.connect("http://kantoishere.byethost6.com//liberarBicho.php")
                    .data("bicho",bicho)
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .execute();
            resp = response.body();
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }
        return resp;
    }

    /**
     * Realiza la peticion de registro con el nombre y la pass dados.
     * @param login
     * @param pass
     * @return
     */
    public String registerString(String login, String pass) {
        String resp = "";
        try {
            Connection.Response response = null;
            response = Jsoup.connect("http://kantoishere.byethost6.com//insertUsuarios.php")
                    .data("campoLogin", login)
                    .data("campoPass", pass)
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .execute();
            resp = response.body();
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }
        return resp;
    }

    /**
     * Realiza la peticion de eliminar al usuario dado de la BD.
     * @param login
     * @return
     */

    public String borrarUsuario(String login) {
        String resp = "";
        try {
            Connection.Response response = null;
            response = Jsoup.connect("http://kantoishere.byethost6.com//borrarUsuario.php")
                    .data("campoLogin", login)
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .execute();
            resp = response.body();
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }
        return resp;
    }
}
