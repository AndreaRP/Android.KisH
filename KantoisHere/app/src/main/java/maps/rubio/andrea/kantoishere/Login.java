package maps.rubio.andrea.kantoishere;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Login/registro a la aplicacion.
 */
public class Login extends ActionBarActivity {
    //private static final String TAG = "Login";
    private TextView mEmailView;
    private EditText mPasswordView;
    private TextView login_reg;
    private EditText pass_reg;
    private String result;
    private ArrayList<Bicho> bichos = new ArrayList<>();
    private int RESULT_CODE = 1;

    /**
     * Inicia los campos de login y password y los botones de logeo y registro.
     * En ambos botones se comprueba antes que los campos no estén vacios antes de mandar la peticióo.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*Campos de login*/
        mEmailView = (TextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        /*Campos de registro*/
        login_reg = (TextView) findViewById(R.id.nombre_usuario_registro);
        pass_reg = (EditText) findViewById(R.id.password_registro);


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = mEmailView.getText().toString();
                String pass = mPasswordView.getText().toString();
                /*comprobamos que ninguno de los campos esté vacío*/
                if (!login.trim().isEmpty() && !pass.trim().isEmpty()) {
                    LoginServer c = new LoginServer(Login.this, login, pass);
                    c.execute();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.campo_vacio,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button registrar = (Button) findViewById(R.id.registro_button);
        registrar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = login_reg.getText().toString();
                String pass = pass_reg.getText().toString();

                /*comprobamos que ninguno de los campos esté vacío*/
                if (!login.trim().isEmpty() && !pass.trim().isEmpty()) {
                    RegistroServer c = new RegistroServer(Login.this, login, pass);
                    c.execute();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.campo_vacio,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * Método del boton registrar
     * @param v
     */

    public void registrar(View v) {
        LinearLayout login = (LinearLayout) findViewById(R.id.email_login_form);
        login.setVisibility(View.GONE);
        LinearLayout registro = (LinearLayout) findViewById(R.id.email_register_form);
        registro.setVisibility(View.VISIBLE);

    }

    /**
     * Método del boton de login
     * @param v
     */
    public void login(View v) {
        LinearLayout registro = (LinearLayout) findViewById(R.id.email_register_form);
        registro.setVisibility(View.GONE);
        LinearLayout login = (LinearLayout) findViewById(R.id.email_login_form);
        login.setVisibility(View.VISIBLE);
    }

    /**
     * Metodo que llama a la actividad principal una vez el login o el registro han sido validados.
     * Manda los datos del usuario a la actividad {@link Principal}.
     * @param login
     * @param pass
     */
    private void startActivity(String login, String pass) {
        Intent principal = new Intent(this, Principal.class);
        principal.putParcelableArrayListExtra("bichos", bichos);
        principal.putExtra("login", login);
        principal.putExtra("pass", pass);
        startActivity(principal);
        finish();
    }

    /**
     * Tarea asincrona que realiza la petición de login al servidor mediante la clase {@link UsuarioDAO}.
     */
    private class LoginServer extends AsyncTask<String, Void, String> {
        Context contexto = null;
        String login = "";
        String pass = "";

        public LoginServer(Context context, String login, String pass) {
            contexto = context;
            this.login = login;
            this.pass = pass;
        }

        @Override
        protected String doInBackground(String... strings) {
            //Log.i(TAG, "doInBackground");
            String respuesta = "";
            try {
                UsuarioDAO c = new UsuarioDAO();
                respuesta = c.loginString(login, pass);
                //Log.i(TAG, respuesta);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return respuesta;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            /*si el login ha sido correcto se devuelve dl servidor un 'true'*/
            if (Boolean.parseBoolean(s)) {
                startActivity(login, pass);
            } else {
                Toast.makeText(getApplicationContext(), s,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Tarea asincrona que realiza la peticion de registro al servidor mediante la clase {@link UsuarioDAO}.
     */
    private class RegistroServer extends AsyncTask<String, Void, String> {
        Context contexto = null;
        String login = "";
        String pass = "";

        public RegistroServer(Context context, String login, String pass) {
            contexto = context;
            this.login = login;
            this.pass = pass;
        }

        @Override
        protected String doInBackground(String... strings) {
            //Log.i(TAG, "doInBackground");
            String respuesta = "";
            try {
                UsuarioDAO c = new UsuarioDAO();
                respuesta = c.registerString(login, pass);
                //Log.i(TAG, respuesta);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return respuesta;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(), s,
                    Toast.LENGTH_SHORT).show();
            if (!s.matches("Usuario ya existente")) {
                startActivity(login, pass);
            }
        }
    }
}



