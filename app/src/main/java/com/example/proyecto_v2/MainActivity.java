package com.example.proyecto_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaración de los elementos del layout pertenecientes a esta clase.
    private Button examenBtn, desafiosBtn, recordsBtn;
    private BaseDatos bd= new BaseDatos(this);//declaración obj BaseDatos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(bd.getPalabra("0").equals("false")){
            bd.initTabla(this);//se crean los registros de la tabla con las palabras de los ficheros txt.
            Toast.makeText(this, "TABLA INICIALIZADA", Toast.LENGTH_LONG).show();
        }//final if comprueba existencia tabla contenga registros.
        initUI();//Llamada al método que inicializa los componentes de la interfaz de usuario.
        initListeners();//Llamada al método que inicializa los elementos que responden ante las acciones del usuario.
    }//final onCreate.

    private void initUI(){
        examenBtn = (Button) findViewById(R.id.examen_btn);
        desafiosBtn = (Button) findViewById(R.id.desafios_btn);
        recordsBtn = (Button) findViewById(R.id.records_btn);
    }//final initUI

    private void initListeners(){
        examenBtn.setOnClickListener(this);
        desafiosBtn.setOnClickListener(this);
        recordsBtn.setOnClickListener(this);
    }//final initListeners

    @Override
    public void onClick(View view) {
        int idBtnPulsado = view.getId();
        switch (idBtnPulsado){
            case R.id.examen_btn:
                Intent instruccionesExamen = new Intent(this, InstruccionesExamen.class);
                startActivity(instruccionesExamen);
                break;
            case R.id.desafios_btn:
                Intent menuDesafios = new Intent(this, MenuDesafios.class);
                startActivity(menuDesafios);
                break;
            case R.id.records_btn:
                Intent records = new Intent(this, Estadisticas.class);
                startActivity(records);
                break;
        }//final switch que filtra las distintas acciones en función del elemento pulsado por el usuario.
    }//final onClick
}//final class MainActivity