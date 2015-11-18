package maps.rubio.andrea.kantoishere;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Actividad que contiene los fragmentos principal, web y lista de pokemon, asi como los metodos que se llaman desde ellos.
 *
 * @author Andrea
 */
public class Principal extends ActionBarActivity {
    private static final String TAG = "Principal";
    private String login;
    private String pass;
    private ArrayList<Bicho> bichos;
    private DrawerLayout drawerLayout;
    private ListView lvNavigationDrawer;
    private String[] opcionesMenu;// si no pita al buscar el elemento en una posicion
    private int fragmentoActual;
    private BichoItem[] lista;
    private int cuidarRequestCode = 1;

    /**
     * Se recogen los datos del usuario mandados desde la actividad {@link Login}.
     * Carga el navigation drawer con las opciones necesarias, carga el fragmento principal y descarga los bichos
     * que pertenecen al usuario.
     * Es necesario llamar de forma manual al metodo
     * onRestoreInstanceState ya que la configuración por defecto de los fragentos evita que se llame de forma automatica.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        onRestoreInstanceState(savedInstanceState);/*es necesario llamarlo manualmente ya que la configuracion por defecto de
        los fragmentos evita que se llame automaticamente*/
        rellenarDrawer();
        cargarFragmento(0);
        //bichos = getIntent().getParcelableArrayListExtra("bichos");
        login = getIntent().getStringExtra("login");
        pass = getIntent().getStringExtra("pass");
        cargarBichos();
        //Log.i(TAG, "onCreate");
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * Metodo del boton del fragmento principal. Comprueba que el GPS este activado y ofrece la posibilidad
     * de activarlo si no lo esta.
     *
     * @param v
     */

    public void lanzarMaps(View v) {
        /*se comprueba que se tenga el gps activado*/
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(R.string.gps_inactivo)
                    .setCancelable(false)
                    .setPositiveButton(R.string.si,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(callGPSSettingIntent);
                                }
                            });
            alertDialogBuilder.setNegativeButton(R.string.no,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        } else {
            Intent mapsActivity = new Intent(this, MapsActivity.class);
            mapsActivity.putExtra("login", login);
            startActivity(mapsActivity);
        }
    }

    /**
     * Crea el menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    /**
     * Segun la opcion seleccionada del menu lleva a cabo una opcion u otra.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.logOut:
                volverInicio();
                return true;
            case R.id.eliminarUser:
                pedirConfirmacion();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Desconecta al usuario de la aplicacion y lo lleva a la actividad de {@link Login}.
     */
    private void volverInicio() {
        Intent login = new Intent(this, Login.class);
        startActivity(login);
        finish();
    }

    /**
     * Pide la confirmacion de baja del usuario y lo elimina si se acepta.
     */
    private void pedirConfirmacion() {
        boolean confirmar;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.confirmacion_baja)
                .setCancelable(false)
                .setPositiveButton(R.string.confirmacion_baja_si,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                /*Eliminamos el usuario y volvemos a inicio*/
                                new BorrarUsuario().execute();
                                volverInicio();
                                dialog.cancel();
                            }
                        });
        alertDialogBuilder.setNegativeButton(R.string.confirmacion_baja_no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    /**
     * Rellena el NavigationDrawer
     */
    private void rellenarDrawer() {
        //rellenamos el drawer
        opcionesMenu = getResources().getStringArray(R.array.items_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        lvNavigationDrawer = (ListView) findViewById(R.id.left_drawer);
        lvNavigationDrawer.setAdapter(new ArrayAdapter<String>(
                getSupportActionBar().getThemedContext(),
                R.layout.navigation_drawer_item, opcionesMenu));
        lvNavigationDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view,
                                    int position, long id) {
                cargarFragmento(position);
                lvNavigationDrawer.setItemChecked(position, true);
                String tituloSeccion = opcionesMenu[position];
                getSupportActionBar().setTitle(tituloSeccion);
                drawerLayout.closeDrawer(lvNavigationDrawer);
            }
        });
    }

    /**
     * Carga los diferentes fragmentos segun la opcion seleccionada en el navigation drawer.
     *
     * @param position
     */
    public void cargarFragmento(int position) {
        fragmentoActual = position;
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new FragmentoPrincipal();
                break;
            case 1:
                fragment = new FragmentoLista();
                //cargarListaBichos();
                break;
            case 2:
                fragment = new FragmentoWeb();
                break;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment).commit();
    }


    /**
     * Llama a la tarea asincrona que carga los bichos del usuario.
     */
    private void cargarBichos() {
        new CargarBichos(this, login).execute();
    }

    //======================================FRAGMENT DE LA WEB==================================================

    /**
     * Carga el fragento de la web con el WebClient customizado.
     */
    public void cargarWeb() {
        WebView miWeb = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = miWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        miWeb.setWebViewClient(new MiWebView(this));
        miWeb.loadUrl("http://kantoishere.byethost6.com//");

    }


    /*==============================FRAGMENT CON LISTA=========================================================*/

    /**
     * Carga el fragmento de la lista con los bichos del usuario mediante la clase {@link UsuarioDAO}.
     */
    public void cargarListaBichos() {
        //
        // Log.i(TAG, "cargarListaBichos");
        //si hay bichos
        if (bichos.size() > 0) {
            // LISTVIEW
            //saco el id de las imagenes sprite correspondientes
            int[] imagenes = new int[bichos.size()];
            for (int i = 0; i < bichos.size(); i++) {
                String nombreGenerico = "sprite_" + bichos.get(i).getNombreGenerico().toLowerCase();
                imagenes[i] = getResources().getIdentifier(nombreGenerico, "drawable", getPackageName());
            }

        /*saco la experiencia*/
            int[] experiencia = new int[bichos.size()];
            for (int i = 0; i < bichos.size(); i++) {
                experiencia[i] = bichos.get(i).getHambre();
            }
            int longitud = bichos.size();

            // creo el array cat con los nombres de los bichos
            String[] nombres = new String[bichos.size()];
            for (int i = 0; i < bichos.size(); i++) {
                nombres[i] = bichos.get(i).getNombre();
            }

            final String[] nombresGenericos = new String[bichos.size()];
            for (int i = 0; i < bichos.size(); i++) {
                nombresGenericos[i] = bichos.get(i).getNombreGenerico();
            }

        /*cargo los valores de los arrays en el array de bichos que voy a mostrar*/
            lista = new BichoItem[longitud];
            for (int i = 0; i < longitud; i++) {
                lista[i] = new BichoItem(imagenes[i], experiencia[i], nombres[i]);
            }
            // cargo el adaptador con los datos en el listview
            AdaptadorItemBicho adapt = new AdaptadorItemBicho(this);
            ListView lvItemBicho = (ListView) findViewById(R.id.lvBichoItem);
            lvItemBicho.setAdapter(adapt);
            // añadimos el listener
            lvItemBicho.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Intent cuidar = new Intent(getApplicationContext(), Cuidar.class);
                    cuidar.putExtra("bicho", bichos.get(position));
                    cuidar.putExtra("posicion", position);
                    //cuidar.putExtra("nombreGenerico", nombresGenericos[position]);
                    cuidar.putExtra("login", login);
                    cuidar.putExtra("pass", pass);
                    startActivityForResult(cuidar, cuidarRequestCode);
                }
            });
        } else {

            Toast.makeText(getApplicationContext(), R.string.sin_bichos,
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Recibe los cambios realizados en el bicho que se ha mandado a la actividad Cuidar y
     * los sincroniza con la BD mediante la clase {@link UsuarioDAO}
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // comprobamos que se venga de cuidar, y si se ha usado o liberado un bicho
        if (requestCode == cuidarRequestCode && data != null) {
            /*recuperamos los datos del bicho*/
            int posicion = data.getIntExtra("posicion", -1);
            Bicho b = data.getParcelableExtra("bicho");
            if (resultCode == RESULT_OK) {
                if (posicion != -1) {
                /*sustituimos*/
                    bichos.set(posicion, b);
                } else
                    Log.e("TAG", "Error al recuperar los datos");
            } else if (resultCode == RESULT_CANCELED) {
                Log.i(TAG, "result canceled");
                /*el bicho ha sido eliminado, se quita de la lista*/
                bichos.remove(b);
            }
            cargarListaBichos();
        }
    }

    //============================================
    //              GUARDAR ESTADO
    //=============================================

    // guardo los datos al cambiar de posicion

    /**
     * Guarda los cambios cuando se pausa la actividad.
     * @param savedInstanceState
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("fragmento", fragmentoActual);
        savedInstanceState.putParcelableArrayList("bichos", bichos);
        //Log.i(TAG, "guardar "+bichos.size()+"");
        //Log.i(TAG, "onSave");
    }

    /**
     * Restaura los valores del bundle al reiniciar la actividad.
     * @param savedInstanceState
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            super.onRestoreInstanceState(savedInstanceState);
            // se recupera el fragmento que estaba cargado
            cargarFragmento(savedInstanceState.getInt("fragmento"));
            bichos = savedInstanceState.getParcelableArrayList("bichos");
            // Log.i(TAG, "cargar: "+bichos.size()+"");
            // Log.i(TAG, "onRestore");
        }
    }


    /*Si se ha salido de la actividad es posible que se hayan capturado nuevos bichos, de modo
    * que es necesario volver a actualizar la lista*/
    @Override
    protected void onRestart() {
        //Log.i(TAG, "onRestart");
        super.onRestart();
        cargarBichos();
    }


    // =============================================
    // ADAPTADOR (MODELO) DE LA CATEGORIA
    // =============================================

    static class ViewHolder {
        ImageView imagen;
        TextView nombre;
        TextView experiencia;
    }

    /**
     * Clase que permite cargar los item bicho en la lista
     */
    public class AdaptadorItemBicho extends ArrayAdapter<Object> {
        Activity contexto;

        public AdaptadorItemBicho(Activity contexto) {
            super(contexto, R.layout.layout_bicho_item, lista);
            this.contexto = contexto;
        }

        public View getView(int posicion, View convertView, ViewGroup padre) {
            View item = convertView;
            ViewHolder holder;
            if (item == null) {
                // si el layout no está inflado ya, se infla
                LayoutInflater inflador = contexto.getLayoutInflater();
                item = inflador.inflate(R.layout.layout_bicho_item, null);
                // cargamos en el holder los valores
                holder = new ViewHolder();
                holder.imagen = (ImageView) item.findViewById(R.id.ivSpriteBicho);
                holder.nombre = (TextView) item.findViewById(R.id.tvNombre);
                holder.experiencia = (TextView) item.findViewById(R.id.tvExperiencia);
                // se lo añadimos al item
                item.setTag(holder);
            } else {
                // si ya estaba cargado, se recupera el view
                holder = (ViewHolder) item.getTag();
            }
            // Se le da contenido al layout del holder
            holder.imagen.setImageResource(lista[posicion].getImagen());
            holder.nombre.setText(lista[posicion].getNombre());
            holder.experiencia.setText(lista[posicion].getExperiencia() + "");
            return (item);
        }
    }

    /*===================================== ASYNC TASKS========================================================*/

    /**
     * Tarea asincrona que borra un usuario de la BD mediante la clase {@link UsuarioDAO}.
     */
    private class BorrarUsuario extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            //Log.i(TAG, "BorrarUsuario: doInBackground");
            String respuesta = "";
            try {
                UsuarioDAO u = new UsuarioDAO();
                u.borrarUsuario(login);
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
     * Tarea asincrona que carga los bichos de un usuario mediante la clase {@link UsuarioDAO}.
     */
    private class CargarBichos extends AsyncTask<String, Void, String> {

        Context contexto = null;
        String login = "";

        public CargarBichos(Context context, String login) {
            contexto = context;
            this.login = login;
            //Log.i(TAG, login);
        }

        @Override
        protected String doInBackground(String... strings) {

            String respuesta = "";
            try {
                UsuarioDAO c = new UsuarioDAO();
                respuesta = c.cargarBichos(login);
                //Log.i(TAG, respuesta);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            //Log.i(TAG, "Cargar bichos: doInBackground terminado");
            return respuesta;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            /*si el login ha sido correcto se devuelve dl servidor un 'true'*/
            ObjectMapper mapper = new ObjectMapper();
            try {
                bichos = mapper.readValue(s, new TypeReference<ArrayList<Bicho>>() {
                });
                //Log.i(TAG, bichos.size() + "");
                //Log.i(TAG, "bichos mapeados");
            } catch (IOException e) {
                //Log.i(TAG, e.getMessage());
                //e.printStackTrace();
            }
        }
    }
}
