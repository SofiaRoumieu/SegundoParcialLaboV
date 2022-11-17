package com.example.segundoparcialroumieusofia;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.List;

public class Ventana extends DialogFragment {
    private Activity activity;
    private String titulo;
    private String mensaje;
    private String btnPositivo;
    private String btnNegativo;
    private String btnNeutral;
    private Boolean view;
    private List<Usuario> usuarios;


    public Ventana(String titulo, String mensaje, String btnPositivo, String btnNegativo,
                         String btnNeutral, Boolean view, List<Usuario> usuarios, Activity activity) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.btnPositivo = btnPositivo;
        this.btnNegativo = btnNegativo;
        this.btnNeutral = btnNeutral;
        this.view = view;
        this.usuarios=usuarios;
        this.activity=activity;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
       // builder.setTitle("Crear usuario");

        View view = LayoutInflater.from(getContext()).inflate(R.layout.ventana,null);

        ClickDialog clickListener = new ClickDialog(view, usuarios, activity);

        if (!("".equals(this.titulo)) && this.titulo != null)
            builder.setTitle(this.titulo);
        if (!("".equals(this.mensaje)) && this.mensaje != null)
            builder.setMessage(this.mensaje);
        if (!("".equals(this.btnPositivo)) && this.btnPositivo != null)
            builder.setPositiveButton(this.btnPositivo, clickListener);
        if (!("".equals(this.btnNegativo)) && this.btnNegativo != null)
            builder.setNegativeButton(this.btnNegativo, clickListener);
        if (this.view == true)
            builder.setView(view);

        CompoundButton tglAdmin = view.findViewById(R.id.admin);
        Spinner slcRol = view.findViewById(R.id.rol);
        tglAdmin.setOnCheckedChangeListener(clickListener);
        slcRol.setOnItemSelectedListener(clickListener);

        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getContext(), R.array.roles_array, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        slcRol.setAdapter(adapterSpinner);


       // builder.setPositiveButton("Guardar",clickListener);
       // builder.setNegativeButton("Cerrar",clickListener);

        return builder.create();
    }
}
