package com.example.proyecto_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;

public class InstruccionesExamen extends AppCompatActivity {

    private Button empezarExamen;//Declaración de la variable que almacena el botón de acceso al desafío.
    private NumeroAleatorio nA = new NumeroAleatorio();//Instanciación de objeto de clase NumeroAleatorio para obtener números aleatorios.
    private BaseDatos bd = new BaseDatos(this);//Instanciación de objeto de clase BaseDatos para realizar operaciones en la BB.DD.

    //Declaración de las variables necesarias para implementar el comportamiento del examen.
    private int porcentajePalabrasCorrectas;
    private int porcentajePalabrasIncorrectas;
    private ArrayList<Integer> listaPosicionesPares = new ArrayList<Integer>();
    private ArrayList<Integer> listaPosicionesImpares = new ArrayList<Integer>();
    private ArrayList<String> listaPalabrasCorrectas = new ArrayList<String>();
    private ArrayList<String> listaPalabrasIncorrectas = new ArrayList<String>();
    public static HashMap<String, Integer> listaExamen = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrucciones_examen);
        initUI();//Llamada al método que inicia los elementos de la interfaz de usuario.
        obtenerPorcentajePalabras();//Llamada al método que genera el número de palabras correctas e incorrectas del examen.
        rellenarListasPosiciones();//Llamada al método que genera la posición de las palabras que se van a mostrar en el examen.
        rellenarListasPalabras();//Llamada al método que obtiene las palabras respectivas a las posiciones generadas anteriormente.
        generarListaExamen();//Llamada al método que genera la lista de palabras definitivas para el examen.
    }//final onCreate.

    private void initUI(){
        empezarExamen = (Button) findViewById(R.id.btn_empezar_examen);
        empezarExamen.setEnabled(false);
    }//final initUI

    public void empezarExamen(View view) {
        //Método que lanza la activity del examen.
        Intent examen = new Intent(this, Examen.class);
        startActivity(examen);
    }//final empezarExamen

    private void obtenerPorcentajePalabras(){
        /*
         * Este método establece, el número de palabras correctas e incorrectasde, de las 100 que consta un examen.
         */
        porcentajePalabrasCorrectas = nA.generarNumeroAleatorio(60, 40);//generar número palabras correctas.
        porcentajePalabrasIncorrectas = 100 - porcentajePalabrasCorrectas;//generar número palabras incorrectas.
    }//final obtenerPorcentajePalabras.

    private void rellenarListasPosiciones(){
        listaPosicionesPares = nA.generarListaNummerosPares(porcentajePalabrasCorrectas);//obtener posición palabras correctas que se van a mostrar.
        listaPosicionesImpares = nA.generarListaNummerosImpares(porcentajePalabrasIncorrectas);//obtener posición palabras incorrectas que se van a mostrar.
    }//final rellenarListaPosicionesPalabras

    private void rellenarListasPalabras(){
        for(int i = 0; i < listaPosicionesPares.size(); i++ ){
            listaPalabrasCorrectas.add(i, bd.getPalabra(String.valueOf(listaPosicionesPares.get(i))));//obtener palabras correctas correspondientes a posiciones generadas anteriormente.
        }//final for recorrey el array posicionesPares

        for(int i = 0; i < listaPosicionesImpares.size(); i++ ){
            listaPalabrasIncorrectas.add(i, bd.getPalabra(String.valueOf(listaPosicionesImpares.get(i))));//obtener palabras incorrectas correspondientes a posiciones generadas anteriormente.
        }//final for recorrey el array posicionesImpares
    }//final rellenarListasPalabras

    private void generarListaExamen(){
        //Declaración variables control para detener el bucle while.
        boolean continue1 = true;
        boolean continue2 = true;

        while ((continue1 == true || continue2 == true)){
            int numAlCo = nA.generarNumeroAleatorio(5,1);//número accesos seguidos a lista de palabras correctas.
            for (int i = 0; i < numAlCo; i++){
                if(!listaPalabrasCorrectas.isEmpty()){
                    String aux = listaPalabrasCorrectas.get(0);//Obtener palabra
                    listaExamen.put(aux, 0);//Añadir palabra a la lista examen
                    listaPalabrasCorrectas.remove(0);//eliminar lista palabra añadida
                } else {
                    continue1 = false;
                }//final if - else
            }//final for

            int numAlIn = nA.generarNumeroAleatorio(5,1);//número accesos seguidos a lista de palabras incorrectas.
            for (int i = 0; i < numAlIn; i++){
                if(!listaPalabrasIncorrectas.isEmpty()) {
                    String aux = listaPalabrasIncorrectas.get(0);//Obtener palabra
                    listaExamen.put(aux, 1);//Añadir palabra a la lista examen
                    listaPalabrasIncorrectas.remove(0);//eliminar lista palabra añadida
                }else{
                    continue2 = false;
                }//final if - else
            }//final for
        }//final while
        empezarExamen.setEnabled(true);//se habilita el botón para acceder al examen.
    }//final generarListaExamen

}//final class InstruccionesExamen