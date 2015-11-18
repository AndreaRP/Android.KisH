package maps.rubio.andrea.kantoishere;

import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Clase que permite interactuar con el bicho seleccionado.
 *
 * @author Andrea
 */
public class Cuidar extends ActionBarActivity {
    //private static String TAG = "Cuidar";
    private Bicho bicho;
    private int posicion;
    private int result = 1;
    private String login;

    /**
     * Se recogen los valores del intent. El nombre generico sirve para cargar la imagen con ese nombre
     * en el web view.
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuidar);
        WebView imagen = (WebView) findViewById(R.id.wvGifBicho);
        TextView nombre = (TextView) findViewById(R.id.tvCuidarNombre);
        /*recogemos los valores del intent*/
        bicho = getIntent().getParcelableExtra("bicho");
        posicion = getIntent().getIntExtra("posicion", -1);
        login = getIntent().getStringExtra("login");
        nombre.setText(bicho.getNombre());
        String archivo = bicho.getNombreGenerico().toLowerCase() + "";
        String html = "<div style=\"text-align: center;\"><img style=\"height:auto; width:75%;\" src='file:///android_asset/" + archivo + ".gif\'/></div>";
        imagen.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
        //imagen.loadUrl(url);
    }

    /**
     * Crea el menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cuidar, menu);
        return true;
    }

    /**
     * Cuando se presiona sobre el menu se hace la peticion al servidor de eliminar ese bicho.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.liberar:
                new LiberarBicho().execute();
                Resources res = getResources();
                String text = String.format(res.getString(R.string.despedida), bicho.getNombre());
                Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();
                Intent principal = new Intent(getApplicationContext(), Principal.class);
                principal.putExtra("login",login);
                startActivity(principal);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Aumenta el atributo hambre y hace sonar un clip de audio
     * @param v
     */
    public void darComer(View v) {
        //aumentamos la comida
        bicho.setHambre(bicho.getHambre() + 1);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.comer);
        mp.start();
    }

    /**
     * Se actualiza la BD y se pasan los datos actualizados a la actividad principal.
     */
    @Override
    public void onBackPressed() {
        Intent principal = new Intent();
        principal.putExtra("bicho", bicho);
        principal.putExtra("posicion", posicion);
            setResult(RESULT_OK, principal);
            new ActualizarBichos().execute();
        finish();
    }

    /**
     * Tarea asincrona que realiza la peticion al servidor de actualizar los parametros del bicho
     * mediante la clase {@link UsuarioDAO}.
     */
    private class ActualizarBichos extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String respuesta = "";
            try {
                ObjectMapper mapper = new ObjectMapper();
                String bichoJson = mapper.writeValueAsString(bicho);
                UsuarioDAO u = new UsuarioDAO();
                u.actualizarBichos(bichoJson);

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
     * Tare asincrona que elimina el bicho capturado del servidor mediante la clase {@link UsuarioDAO}.
     */
    private class LiberarBicho extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            //Log.i(TAG, "BorrarUsuario: doInBackground");
            String respuesta = "";
            try {
                ObjectMapper mapper = new ObjectMapper();
                String bichoJson = mapper.writeValueAsString(bicho);
                UsuarioDAO u = new UsuarioDAO();
                u.liberarBicho(bichoJson);

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
}
