package com.example.proyecto_v2;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;

public class EmomDesafio extends AppCompatActivity implements View.OnClickListener {
    //Declaración de variables que almacenan los elementos de la interfaz de usuario.
    private ImageView iconoBienIV;
    private ImageView iconoMalIV;
    private TextView palabraMostradaTV;
    private TextView palabraCorregidaTV;

    private Handler handler = new Handler();//Instanciación obje para modificar elementos del threadUI desde un thread secundario.
    private HashMap<String, Integer> listaDesafio = InstruccionesEmom.listaDesafio;//lista que almacena las palabras que se muestran en el desafío.

    //declaración variables utilizadas para implementar el comportamiento de la clase.
    private String palabraVisualizada = null;
    private Integer valorPalabraVisualizada = null;

    private Context context = this;//Declaración de variable que almacena el contexto de la clase.
    private int mejorRacha = 0;//Declaración variable que almacena la racha de aciertos.
    private int aux = 0;//Declaración variable auxiliar para almacenar la racha de aciertos.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emom_desafio);
        initUI();//Llamada al método que inicializa los elementos de la interfaz de usuario.
        initListeners();//Llamada al método que implementa el comportamiento en función de las acciones del usuario.
    }//final class onCreate

    @Override
    protected void onResume(){
        super.onResume();
        initTemporizador();//iniciar el hilo secundario.
    }//final onResume

    private void initUI(){
        iconoBienIV = (ImageView) findViewById(R.id.icono_bien_image_view);
        iconoMalIV = (ImageView) findViewById(R.id.icono_mal_image_view);
        palabraMostradaTV = (TextView) findViewById(R.id.palabra_mostrada_text_view);
        palabraCorregidaTV = (TextView) findViewById(R.id.palabra_corregida_text_view);
    }//final initUI.

    private void initListeners(){
        iconoBienIV.setOnClickListener(this);
        iconoMalIV.setOnClickListener(this);
    }//final initListeners

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch(id){
            case R.id.icono_bien_image_view:
                if(valorPalabraVisualizada == 0){
                    palabraCorregidaTV.setText("CORRECTO");
                    aux++;
                } else {
                    palabraCorregidaTV.setText("INCORRECTO");
                    if (aux > mejorRacha){
                        mejorRacha = aux;
                    }//final if
                    aux = 0;
                }
                break;
            case R.id.icono_mal_image_view:
                if (valorPalabraVisualizada == 1){
                    palabraCorregidaTV.setText("CORRECTO");
                    aux++;
                } else {
                    palabraCorregidaTV.setText("INCORRECTO");
                    if (aux > mejorRacha){
                        mejorRacha = aux;
                    }//final if
                    aux = 0;
                }
                break;
        }//final switch filtra el comportamiento en función de las acciones del usuario.
    }//final onClick

    private void initTemporizador(){
        //Se lanza un hilo secundario encargado de implementar el cronómetro del examen.
        new Thread((new Runnable() {
            @Override
            public void run() {

                for (Map.Entry<String, Integer> i : listaDesafio.entrySet()) {
                    palabraCorregidaTV.post(new Runnable() {
                        @Override
                        public void run() {
                            palabraCorregidaTV.setText("");
                        }
                    });
                    palabraVisualizada = i.getKey();
                    valorPalabraVisualizada = i.getValue();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            palabraMostradaTV.setText(palabraVisualizada);
                        }
                    });

                    try {
                        Thread.sleep(4000);//Se detiene el hilo durante 4 segundos
                    } catch(InterruptedException e){}//final try - catch que captura posibles excepciones que se puedan producir.
                }//final for

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        palabraMostradaTV.setText("FINAL");
                    }
                });

                try {
                    Thread.sleep(2500);//Se detiene el hilo durante 4 segundos
                } catch(InterruptedException e){}//final try - catch que captura posibles excepciones que se puedan producir.

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                int record_emom = sp.getInt("record_emom", 0);//Se consulta la puntuación almacenada.

                if(mejorRacha > record_emom){
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("record_emom", mejorRacha);//Se almacena el nuevo récord en las preferencias.
                    editor.apply();
                }//final if comprueba que haya un nuevo récord

                Intent in = new Intent(context, MainActivity.class);
                startActivity(in);
            }//final run hilo secundario
        })).start();//inicialización del hilo secundario.
    }//final initTemporizador

}//final class Desafios