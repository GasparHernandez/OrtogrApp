package com.example.proyecto_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class Estadisticas extends AppCompatActivity {

    private TextView recordExamenTV;
    private TextView recordFortimeTV;
    private TextView recordAmwapTV;
    private TextView recordEmomTV;
    private float recordExamen;
    private int recordAmwap;
    private int recordEmom;
    private String recordForTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);
        initUI();//Llamada que inicializa los elementos de la interfaz de usuario.
        consultarRecords();//Método que consulta las preferencias.
        mostrarRecords();//Método que muestra las preferencias.
    }//final onCreate

    private void initUI(){
        recordAmwapTV = findViewById(R.id.record_amwap_tv);
        recordEmomTV = findViewById(R.id.record_emom_tv);
        recordExamenTV = findViewById(R.id.record_examen_tv);
        recordFortimeTV = findViewById(R.id.record_fortime_tv);
    }//final initUI

    private void consultarRecords(){
        //Método que consulta los récords almacenados en las preferencias.
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this);
        recordExamen = datos.getFloat("record_examen", 0.00f);
        recordAmwap = datos.getInt("record_amwap", 0);
        recordEmom = datos.getInt("record_emom", 0);
        recordForTime = datos.getString("record_fortime", "00:00");
    }//final consultarRecords

    private void mostrarRecords(){
        //Método que muestra los récords almacenados en las preferencias.
        recordExamenTV.setText(String.valueOf(recordExamen));
        recordAmwapTV.setText(String.valueOf(recordAmwap));
        recordEmomTV.setText(String.valueOf(recordEmom));
        recordFortimeTV.setText(recordForTime);
    }//final mostrarRecords

}//final class Estadisticas