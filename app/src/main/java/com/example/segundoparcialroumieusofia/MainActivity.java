package com.example.segundoparcialroumieusofia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Handler.Callback , SearchView.OnQueryTextListener{

    List<Usuario> usuarios =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Sofia - on create", String.valueOf(usuarios));
        this.usuarios = this.recuperarUsuarios();
        this.actualizarTextView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem search =menu.findItem(R.id.buscar);
        SearchView searchView= (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.agregar_usuario){
            Ventana v = new Ventana("Usuario no encontrado", null, "Guardar", "Cerrar", null, true ,this.usuarios, this);
            v.show(getSupportFragmentManager(), "ventana");
        }
        return super.onOptionsItemSelected(item);
    }
    @NonNull
    private List<Usuario> recuperarUsuarios() {

        SharedPreferences prefs = this.getSharedPreferences("miListita", Context.MODE_PRIVATE);
        String usuariosString = prefs.getString("usuarios", "sinUsuarios");

        Log.d("Sofia - listaaaa", usuariosString);
        if ("sinUsuarios".equals(usuariosString) ) {
            Handler handler = new Handler(this);
            Thread t1 = new Thread(new HiloConexion(handler));
            t1.start();

            return new ArrayList<Usuario>();
        }
        else {
            Log.d("Sofia - lista llena", usuariosString);
            return this.parserJsonUsuarios(usuariosString);
        }
    }

    private List<Usuario> parserJsonUsuarios(String string) {
        Log.d("Sofia - parserJson", string);
        List<Usuario> usuarios = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(string);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Integer id = (jsonObject.has("id"))?Integer.valueOf(jsonObject.getString("id")):0;
                String username = (jsonObject.has("username")) ? jsonObject.getString("username")
                                 :(jsonObject.has("nombre")) ? jsonObject.getString("nombre") : "";
                String rol = (jsonObject.has("rol"))?jsonObject.getString("rol"):"Supervisor";
                Boolean admin = (jsonObject.has("username"))?Boolean.valueOf(jsonObject.getString("admin")):false;

                Usuario usuario = new Usuario(id, username, rol, admin);
                usuarios.add(usuario);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        this.usuarios = this.parserJsonUsuarios(msg.obj.toString());
        Log.d("Sofia - Lista", String.valueOf(usuarios));
        this.actualizarTextView();
        this.actualizarSharedPreferences();

        return false;
    }

    //carga de datos en pantalla
    public void actualizarTextView() {
        TextView textView = this.findViewById(R.id.usuarios);
        textView.setText(this.usuarios.toString());
    }
    public void actualizarSharedPreferences() {
        SharedPreferences prefs = this.getSharedPreferences("miListita", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("usuarios", this.usuarios.toString());
        editor.commit();
    }


    //Buscador
    @Override
    public boolean onQueryTextSubmit(String query) {
        for (int i = 0; i < this.usuarios.size(); i++) {
            Usuario usuario = this.usuarios.get(i);

            if (query.equals(usuario.getUsername())) {
                Log.d("usuario encontrado", query);
                String mensaje = "El rol del usuario es ".concat(usuario.getRol());
                Ventana dialog = new Ventana("Usuario encontrado", mensaje,  null, "Cerrar",null, false,null, null);
                dialog.show(this.getSupportFragmentManager(), "Dialog encontró usuario");
                return false;
            }
        }

        Ventana dialog = new Ventana("Usuario no encontrado", "El usuario ".concat(query).concat(" no esta dentro de la lista"), null, "Cerrar", null, false,null, null );
        dialog.show(this.getSupportFragmentManager(), "Dialog NO encontró usuario");
        return false;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}