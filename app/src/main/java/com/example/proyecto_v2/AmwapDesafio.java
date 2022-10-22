package com.example.proyecto_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AmwapDesafio extends AppCompatActivity implements View.OnClickListener {

    //Declaración variables para obtener y mostrar las palabras al usuario
    private NumeroAleatorio nA = new NumeroAleatorio();
    private BaseDatos bd = new BaseDatos(this);
    private TextView palabraEvaluarTV;
    private TextView resultadoTV;
    private String palabraVisualizada = null;
    private Handler handler = new Handler();
    private Thread h;

    //Declaración de variables para capturar implementar las respuestas del usuario
    private ImageView imgBienIV;
    private ImageView imgMalIV;
    private Integer valorPalabraVisualizada = null;

    //Declaración cariable almacenar los aciertos del usuario y salir del bucle cuando llegue a 100.
    private int aciertos = 0;
    private TextView textViewNumAciertos;

    //Declaración de variables para la utilización del hilo que implementa el cronómetro.
    private Handler handlerCrono = new Handler();
    private TextView cronoTextView;
    private int segundos = 0;
    private int minutos = 0;
    private boolean continuar = true;

    private Context context = this;//variable que almacena el contexto


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amwap_desafio);
        initUI();//Llamada al método que inicializa los elementos de la interfaz de usuario
        initListeners();//Llamada al método que inicia los escuchadores para las acciones del usuario.
    }//final onCreate

    @Override
    protected void onResume(){
        super.onResume();
        initThread();//se inicializa un hilo secundario
        initCronometro();//se inicializa otro hilo secundario
    }//final onResume

    private void initUI(){
        imgBienIV = (ImageView) findViewById(R.id.acierto_img_amwap);
        imgMalIV = (ImageView) findViewById(R.id.error_img_amwap);
        palabraEvaluarTV = (TextView) findViewById(R.id.textView_palabraMostrada_amwap);
        resultadoTV = (TextView) findViewById(R.id.textView_resultado_amwap);
        cronoTextView = (TextView) findViewById(R.id.textView_crono_amwap);
        textViewNumAciertos = (TextView) findViewById(R.id.textView_numAciertos_amwap);
    }//final initUI.

    private void initListeners(){
        imgBienIV.setOnClickListener(this);
        imgMalIV.setOnClickListener(this);
    }//final initListeners

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch(id){
            case R.id.acierto_img_amwap:
                if(valorPalabraVisualizada == 0){
                    resultadoTV.setText("CORRECTO");
                    aciertos++;
                    textViewNumAciertos.setText(String.valueOf(aciertos));
                }else{
                    resultadoTV.setText("INCORRECTO");
                }
                synchronized (h){
                    h.notify();
                }
                break;
            case R.id.error_img_amwap:
                if(valorPalabraVisualizada == 1){
                    resultadoTV.setText("CORRECTO");
                    aciertos++;
                    textViewNumAciertos.setText(String.valueOf(aciertos));
                }else{
                    resultadoTV.setText("INCORRECTO");
                }
                synchronized (h){
                    h.notify();
                }
                break;
            default:
        }//final switch
    }//onClick

    private void initThread(){
        //Se lanza un hilo secundario encargado de implementar el
        new Thread((new Runnable() {
            @Override
            public void run() {
                h = Thread.currentThread();

                while(continuar){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) { }

                    resultadoTV.post(new Runnable() {
                        @Override
                        public void run() {
                            resultadoTV.setText("");
                        }
                    });

                    if(continuar){
                        int numeroAleatorio = nA.generarNumeroAleatorio(199, 0);

                        if ((numeroAleatorio % 2 == 0)){
                            valorPalabraVisualizada = 0;
                        }else{
                            valorPalabraVisualizada = 1;
                        }//final del if que determina si el número aleatorio generado es par o impar para saber si es una palabra correcta o incorrecta.
                        palabraVisualizada = bd.getPalabra(String.valueOf(numeroAleatorio));

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                palabraEvaluarTV.setText(palabraVisualizada);
                            }
                        });

                        synchronized (h){
                            try {
                                h.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }//final try - catch
                        }//final synchronized
                    }//final if
                }//final while


                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                int record_amwap = sp.getInt("record_amwap", 0);//Se consulta la puntuación almacenada.

                if(aciertos > record_amwap){
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("record_amwap", aciertos);//Se almacena el nuevo récord en las preferencias.
                    editor.apply();
                }//final if comprueba que haya un nuevo récord

                Intent in = new Intent(context, MainActivity.class);
                startActivity(in);

            }//final run hilo secundario
        })).start();//inicialización del hilo secundario.
    }//final initThread

    private void initCronometro(){
        //Se lanza un hilo secundario encargado de implementar el cronómetro del examen.
        new Thread((new Runnable() {
            @Override
            public void run() {
                while (minutos < 1){
                    try {
                        Thread.sleep(1000);//Se detiene el hilo durante 1 segundo
                    } catch(InterruptedException e){ }//final try - catch que captura posibles excepciones que se puedan producir.

                    if(minutos < 1){
                        segundos++;//se aumenta, un valor, la variable que contabiliza los segundos.

                        if(segundos == 59){
                            minutos++;
                            segundos = 0;
                        }//final if que reinicia los segundos cuando estos llegan a 59

                        handlerCrono.post(new Runnable() {
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
                    }//final if
                }//final while
                handlerCrono.post(new Runnable() {
                    @Override
                    public void run() {
                        cronoTextView.setText("FINAL");
                    }
                });
                continuar = false;
            }//final run
        })).start();//inicialización del hilo secundario.
    }//final initCronometro

}//final class AmrapDesafio