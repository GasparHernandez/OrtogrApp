package com.example.proyecto_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InstruccionesForTime extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrucciones_fortime);
    }//final onCreate()

    public void empezarForTime(View view) {
        //Método que lanza el activity para empezar el desafío.
        Intent forTime = new Intent(this, ForTimeDesafio.class);
        startActivity(forTime);
    }//final empezarForTime

}//final class InstruccionesForTime