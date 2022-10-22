package com.example.proyecto_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;

public class InstruccionesEmom extends AppCompatActivity {

    private Button empezarEmomBtn;//Declaración variable que almacena el botón para empezar el desafío.
    private NumeroAleatorio nA = new NumeroAleatorio();//Instanciación de un objeto de clase NumeroAleatorio para generar números aleatorios.
    private BaseDatos bd = new BaseDatos(this);//Instanciación de un objeto de clase BaseDatos para realizar consultas a la BB.DD.
    //Declaración de variables para implementar el funcionamiento de la aplicación.
    private int porcentajePalabrasCorrectas;
    private int porcentajePalabrasIncorrectas;
    private ArrayList<Integer> listaPosicionesPares = new ArrayList<Integer>();
    private ArrayList<Integer> listaPosicionesImpares = new ArrayList<Integer>();
    private ArrayList<String> listaPalabrasCorrectas = new ArrayList<String>();
    private ArrayList<String> listaPalabrasIncorrectas = new ArrayList<String>();
    public static HashMap<String, Integer> listaDesafio = new HashMap<String, Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrucciones_emom);
        initUI();//Llamada al método que inicializa las variables que almacenan los elementos de la interfaz de usuario.
        obtenerPorcentajePalabras();//Llamada al método que obtiene el número de palabras correctas e incorrectas que van a mostrarse en el desafío.
        rellenarListasPosiciones();//Llamada al método que genera las posiciones de las palabras que vas a ser mostradas en el desafío.
        rellenarListasPalabras();//Llamada al método que obtiene las palabras correspondientes a las posiciones generadas en el método anterior.
        generarListaDesafio();//Llamada al método que genera la lista con las palbras correctas e incorrectas mezcladas.
    }//final onCreate

    public void empezarDesafio(View view) {
        //Método que lanza el activity para empezar el desafío.
        Intent desafio = new Intent(this, EmomDesafio.class);
        startActivity(desafio);
    }//final empezarDesafio

    private void initUI(){
        empezarEmomBtn = (Button) findViewById(R.id.btn_empezar_emom);
        empezarEmomBtn.setEnabled(false);
    }//final initUI

    private void obtenerPorcentajePalabras(){
        /*
         * Este método establece, el número de palabras correctas e incorrectasde, de las 100 que consta un examen.
         */
        porcentajePalabrasCorrectas = nA.generarNumeroAleatorio(60, 40);//obtención del número de palabras correctas que se van a mostrar.
        porcentajePalabrasIncorrectas = 100 - porcentajePalabrasCorrectas;//obtención del número de palabras incorrectas que se van a mostrar.
    }//final obtenerPorcentajePalabras.

    private void rellenarListasPosiciones(){
        listaPosicionesPares = nA.generarListaNummerosPares(porcentajePalabrasCorrectas);//obtener números con las posiciones, en el fichero, de las palabras correctas que se van a mostrar.
        listaPosicionesImpares = nA.generarListaNummerosImpares(porcentajePalabrasIncorrectas);//obtener números con las posiciones, en el fichero, de las palabras incorrectas que se van a mostrar.
    }//final rellenarListaPosicionesPalabras

    private void rellenarListasPalabras(){
        for(int i = 0; i < listaPosicionesPares.size(); i++ ){
            listaPalabrasCorrectas.add(i, bd.getPalabra(String.valueOf(listaPosicionesPares.get(i))));//generación lista con las palabras correctas que se van a mostrar.
        }//final for recorrey el array posicionesPares

        for(int i = 0; i < listaPosicionesImpares.size(); i++ ){
            listaPalabrasIncorrectas.add(i, bd.getPalabra(String.valueOf(listaPosicionesImpares.get(i))));//generación lista con las palabras incorrectas que se van a mostrar.
        }//final for recorrey el array posicionesImpares
    }//final rellenarListasPalabras

    private void generarListaDesafio(){
        //Declaración de variables de control para salir del bucle while.
        boolean continue1 = true;
        boolean continue2 = true;

        while ((continue1 == true || continue2 == true)){
            int numAlCo = nA.generarNumeroAleatorio(5,1);//generar número que indica la cantidad de accesos seguidos al fichero.
            for (int i = 0; i < numAlCo; i++){
                if(!listaPalabrasCorrectas.isEmpty()){
                    String aux = listaPalabrasCorrectas.get(0);//Se obtiene la palabra.
                    listaDesafio.put(aux, 0);//se añade a la lista definitiva del desafío.
                    listaPalabrasCorrectas.remove(0);//se elimina la palabra añadida para que no se repita.
                } else {
                    continue1 = false;
                }//final if - else
            }//final for

            int numAlIn = nA.generarNumeroAleatorio(5,1);//generar número que indica la cantidad de accesos seguidos al fichero.
            for (int i = 0; i < numAlIn; i++){
                if(!listaPalabrasIncorrectas.isEmpty()) {
                    String aux = listaPalabrasIncorrectas.get(0);//Se obtiene la palabra.
                    listaDesafio.put(aux, 1);//se añade a la lista definitiva del desafío.
                    listaPalabrasIncorrectas.remove(0);//se elimina la palabra añadida para que no se repita.
                }else{
                    continue2 = false;
                }//final if - else
            }//final for
        }//final while
        empezarEmomBtn.setEnabled(true);//se activa el botón para poder acceder al desafío.
    }//final generarListaExamen

}//final class InstruccionesDesafio