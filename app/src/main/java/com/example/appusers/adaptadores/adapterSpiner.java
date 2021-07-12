package com.example.appusers.adaptadores;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.appusers.R;
import com.example.appusers.modelos.roles;
import com.example.appusers.retrofit.httpCall;
import com.example.appusers.retrofit.onResponseRoles;

import java.util.ArrayList;
import java.util.List;

public class adapterSpiner {
    private Context contexto;
    private String listType;
    private Spinner spinControl;
    private ArrayList data;
    public adapterSpiner(Context context, Spinner spinControl, ArrayList data){
        this.contexto = context;
        this.spinControl = spinControl;
        this.data =data;
    }
    public void fillSpiner() {
        ArrayAdapter<String> adapterRol = new ArrayAdapter<String>(contexto, R.layout.bg_spin_text, data);
        adapterRol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinControl.setAdapter(adapterRol);
    }


}
