package com.example.proyecto_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InstruccionesAmrap extends AppCompatActivity {

    private Button empezarAmrapBtn;//Declaración de la variable que almacena el botón para empezar el desafío.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrucciones_amrap);
        initUI();//Llamada al método que inicializa la variable que almacena el botón para empezar el desafío.
    }//final onCreate

    private void initUI(){
        empezarAmrapBtn = (Button) findViewById(R.id.btn_empezar_amrap);
    }//final initUI

    public void empezarAmrap(View view) {
        Intent amrap = new Intent(this, AmwapDesafio.class);
        startActivity(amrap);
    }//final empezarAmrap que lanza el activity correspondiente para empezar el desafío.

}//final class InstruccionesAmrap