package com.example.proyecto_v2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Examen extends AppCompatActivity {
    //Declaración variable que almacena el btn_corregir_examen
    private Button corregirExamen;

    //Declaración de variables para la utilización del RecyclerView
    private RecyclerView rvLista;
    private RecyclerAdapter adapter;
    private List<ItemList> items;
    private HashMap<String, Integer>lista = InstruccionesExamen.listaExamen;
    public static HashMap<String, Integer>listaRespuestas = new HashMap<String, Integer>();

    //Declaración de variables para la utilización del hilo que implementa el cronómetro.
    private Handler handler = new Handler();
    private TextView cronoTextView;
    private int segundos = 0;
    private int minutos = 0;
    private boolean continuar = true;

    //Declaración de variables para obtener y almacenar la nota del examen
    private int aciertos = 0;
    private int fallos = 0;
    private String nota = null;
    private Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examen);
        //Llamadas a los métodos que implementa el comportamiento del activity.
        initUI();
        initValues();
        initListeners();
    }//final onCreate.

    @Override
    protected void onResume(){
        super.onResume();
        initCronometro();//Se inicializa el cronómetro.
    }//final onResume

    private void initUI(){
        //Se inicializan los elementos de la UI
        rvLista = findViewById(R.id.rvLista);
        cronoTextView = (TextView) findViewById(R.id.cronometro_text_view);
        corregirExamen = (Button) findViewById(R.id.btn_corregir_examen);
        corregirExamen.setEnabled(false);
    }//final initUI

    private void initValues(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvLista.setLayoutManager(manager);
        items = getItems();
        adapter = new RecyclerAdapter(items);
        rvLista.setAdapter(adapter);
    }//final initValues

    private void initListeners(){
        //Se pone a la escucha el botón corregirExamen para que ejecute el método corregirExamen() cuando sea pulsado.
        corregirExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                corregirExamen();//llamada al método corregir examen
            }
        });
    }//final initListener

    private List<ItemList> getItems(){
        int i = 1;
        List<ItemList> itemLists = new ArrayList<>();
        for (Map.Entry<String,Integer> pos: lista.entrySet()) {
            itemLists.add(new ItemList(String.valueOf(i+". "), pos.getKey(), R.drawable.ic_svgbien, R.drawable.ic_svgmal));
            i++;
        }//final for ecah que recorre el hasmap (lista examen).
        return itemLists;
    }//final getItems

    private void initCronometro(){
        //Se lanza un hilo secundario encargado de implementar el cronómetro del examen.
        new Thread((new Runnable() {
            @Override
            public void run() {
                while (continuar){
                    try {
                        Thread.sleep(1000);//Se detiene el hilo durante 1 segundo
                    } catch(InterruptedException e){}//final try - catch que captura posibles excepciones que se puedan producir.

                    segundos++;//se aumenta, un valor, la variable que contabiliza los segundos.

                    if(segundos == 59){
                        minutos++;
                        segundos = 0;
                    }//final if que reinicia los segundos cuando estos llegan a 59

                    if(minutos > 0){
                        continuar = false;//se modifica el valor de la variable para salir del bucle que contenido en el método run del hilo secundario.
                        corregirExamen.post(new Runnable() {
                            @Override
                            public void run() {
                                corregirExamen.setEnabled(true);//se deshabilita el botón "corregir examen" para impedir que pueda ser pulsado.
                            }//final run.
                        });//final del post que sincroniza los hilos primario y secundario.

                        for (Map.Entry<String, Integer> i : listaRespuestas.entrySet()) {
                            String palabra = i.getKey();//se obtiene la palabra respondida por el usuario
                            Integer respuesta = i.getValue();//se obtiene el valor asociado a la palabra respondida por el usuario.
                            Integer valor = lista.get(palabra);//se busca en la lista del examen la plabra respondida por el usuario

                            if(respuesta == valor){
                                aciertos++;//se aumenta una unidad en la variable que contabiliza los aciertos del usuario
                            }else{
                                fallos++;//se aumenta una unidad en la variable que contabiliza los fallos del usuario
                            }//final if - else que compara los valores de las palabras de la lista del examen con las palabras respondidas por el usuario.
                        }//final for each que recorre lista con las respuestas usuario.

                        //Se realizan las operaciones matemáticas para obtener la nota del examen.
                        float fallosDiv2 = (float)fallos/2;
                        float aciertosMenosFallos = (float) aciertos - fallosDiv2;
                        float aciertosMenosFallosDivCien = (float) aciertosMenosFallos / 100;
                        float aciertosMenosFallosDivCienPorDiez = (float) aciertosMenosFallosDivCien * 10;

                        nota = String.format("%.2f", aciertosMenosFallosDivCienPorDiez);//se da formato a la nota del examen para mostrar solo dos decimales.

                        /*SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                        float record_examen = sp.getFloat("record_examen", 0.00f);//Se consulta la puntuación almacenada.

                        if(Float.valueOf(nota) > record_examen){
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putFloat("record_examen", Float.valueOf(nota));//Se almacena el nuevo récord en las preferencias.
                            editor.apply();
                        }//final if comprueba que haya un nuevo récord

                        Intent in = new Intent(context, MainActivity.class);
                        startActivity(in);*/
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                String min = "", seg = "";//declaración de las variables utilizadas para mostrar el cronómetro.

                                if (segundos < 10){
                                    seg = "0" + segundos;
                                }else {
                                    seg = "" + segundos;
                                }//final if que controla los segundos

                                if (minutos < 10){
                                    min = "0" + minutos;
                                }else {
                                    min = "" + minutos;
                                }//final if que controla los minutos

                                cronoTextView.setText(min + ":" + seg);//se modifica el textview donde el usuario visualiza el cronómetro.

                            }//final run
                        });//final handler que implementa el cronómetro del examen.
                    }//final if - else que controla la duración del examen y muestra la nota del examen al terminarse el tiempo.
                }//final while

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        cronoTextView.setText("FINAL");
                    }//final run
                });//final del handler que sincroniza los hilos para manipular un elemento del hiloUI desde el hilo secundario.
            }//final run
        })).start();//inicialización del hilo secundario.
    }//final initCronometro

    public void corregirExamen() {
        //continuar = false;//se modifica el valor de la variable para detener el bucle del hilo contenido en el método run del hilo secundario.

        /*for (Map.Entry<String, Integer> i : listaRespuestas.entrySet()) {
            String palabra = i.getKey();//se obtiene la palabra respondida por el usuario
            Integer respuesta = i.getValue();//se obtiene el valor de la palabra respondida por el usuario
            Integer valor = lista.get(palabra);//se obtiene el valor de la palabra de la lista del examen coincidente con la respondida por el usuario.

            if(respuesta == valor){
                aciertos++;//se aumenta una unidad la variable que contabiliza los aciertos del usuario
            }else{
                fallos++;//se aumenta una unidad la variable que contabiliza los fallos del usuario
            }//final if - else que compara los valores de la palabra respondida por el usuario con la palabra del examen.
        }//final for recorre lista con las respuestas usuario

        //Se realizan las operaciones matemáticas para obtener la nota del examen.
        float fallosDiv2 = (float)fallos/2;
        float aciertosMenosFallos = (float) aciertos - fallosDiv2;
        float aciertosMenosFallosDivCien = (float) aciertosMenosFallos / 100;
        float aciertosMenosFallosDivCienPorDiez = (float) aciertosMenosFallosDivCien * 10;

        nota = String.format("%.2f", aciertosMenosFallosDivCienPorDiez);//se da formato a la nota del examen para mostrar solo dos decimales.*/

        cronoTextView.setText(nota);//Se muestra la nota en el textview utilizado anteriormente para mostrar el crono

        corregirExamen.setEnabled(false);//se desactiva el botón para impedir que se presione más veces.

        try {
            Thread.sleep(3000);//Se detiene el hilo durante 1 segundo
        } catch(InterruptedException e){
            Log.d("ERROR","Fallo en el hilo que implementa el cronómetro del examen");
        }//final try - catch que captura posibles excepciones que se puedan producir.

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        float record_examen = sp.getFloat("record_examen", 0.00f);//Se consulta la puntuación almacenada.

        if(Float.valueOf(nota) > record_examen){
            SharedPreferences.Editor editor = sp.edit();
            editor.putFloat("record_examen", Float.valueOf(nota));//Se almacena el nuevo récord en las preferencias.
            editor.apply();
        }//final if comprueba que haya un nuevo récord

        aciertos = 0;
        fallos = 0;
        nota = null;

        Intent in = new Intent(context, MainActivity.class);
        startActivity(in);

    }//final corregirExamen
}//final class Examen.