package com.example.segundoparcialroumieusofia;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class ClickDialog  implements DialogInterface.OnClickListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener  {
    View v;
    private List<Usuario> usuarios;
    private Activity activity;
    private Usuario usuario;
    public ClickDialog(View v, List<Usuario> usuarios, Activity activity){
        this.v=v;
        this.usuarios=usuarios;
        this.activity=activity;
        this.usuario = new Usuario();
        this.usuario.setAdmin(false);
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if(DialogInterface.BUTTON_POSITIVE==i){
            Log.d("Sofia - click", "click positivo");
            if (validarCampos()) {
                Log.d("Sofia - vieja lista", String.valueOf(usuarios));

                String username = ((EditText) v.findViewById(R.id.nombre_usuario)).getText().toString();
                usuario.setId(generarId());
                usuario.setUsername(username);
                usuarios.add(usuario);

                Log.d("Sofia - nueva lista", String.valueOf(usuarios));

                ((MainActivity) activity).actualizarSharedPreferences();
               ((MainActivity) activity).actualizarTextView();
            }
            else {
                Log.d("Sofia - campos invali", "no pasa validacion");
                Toast.makeText(v.getContext(), "Los campos no pueden estar vacíos", Toast.LENGTH_LONG).show();
            }
        }else{
            Log.d("Sofia - click", "click negativo");
        }
    }

    private boolean validarCampos() {
        EditText inputUsername = v.findViewById(R.id.nombre_usuario);
        if (inputUsername.getText().toString().isEmpty()) {
            return false;
        }

        return true;
    }

    private int generarId() {
        int id_anterior = 0;
        for (int i = 0; i < this.usuarios.size(); i++) {
            if (this.usuarios.get(i).getId() == null)
                continue;
            int id = this.usuarios.get(i).getId();
            if (id > id_anterior)
                id_anterior = id;
        }
        return id_anterior + 1;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0)
            this.usuario.setRol("Supervisor");
        else if (i == 1)
            this.usuario.setRol("Construction Manager");
        else if (i == 2)
            this.usuario.setRol("Project Manager");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        this.usuario.setAdmin(Boolean.valueOf(b));
        Log.d("Sofia - cambiando admin", String.valueOf(this.usuario.getAdmin()));
    }
}
