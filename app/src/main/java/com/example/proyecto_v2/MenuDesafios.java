package com.example.proyecto_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuDesafios extends AppCompatActivity implements View.OnClickListener {
    //Declaración de las variables que almacenan los elementos de la UI correspondientes a esta clase.
    private Button emomBtn;
    private Button amrapBtn;
    private Button forTimeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_desafios);
        initUI();//Llamada al método que inicializa los elementos de la interfaz de usuario.
        initListeners();//Llamada al método que inicializa los escuchadores que responden ante las acciones del usuario.
    }//final onCreate

    private void initUI(){
        emomBtn = (Button) findViewById(R.id.emom_btn_cardview);
        amrapBtn = (Button) findViewById(R.id.amrap_btn_cardview);
        forTimeBtn = (Button) findViewById(R.id.fortime_btn_cardview);
    }//final initUI

    private void initListeners(){
        emomBtn.setOnClickListener(this);
        amrapBtn.setOnClickListener(this);
        forTimeBtn.setOnClickListener(this);
    }//final initListeners

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id){
            case R.id.emom_btn_cardview:
                Intent desafioEmom = new Intent(this, InstruccionesEmom.class );
                startActivity(desafioEmom);
                break;
            case R.id.amrap_btn_cardview:
                Intent desafioAmrap = new Intent(this, InstruccionesAmrap.class );
                startActivity(desafioAmrap);
                break;
            case R.id.fortime_btn_cardview:
                Intent desafioForTime = new Intent(this, InstruccionesForTime.class );
                startActivity(desafioForTime);
                break;
            default:
        }//final switch que filtra las acciones en función del elemento pulsado por el usuario.
    }//final onClick
}//final class MenuDesafio