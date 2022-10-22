package com.example.proyecto_v2;

import java.util.ArrayList;

public class NumeroAleatorio {

    public NumeroAleatorio(){}//Constructor

    public int generarNumeroAleatorio(int maximo, int minimo) {
        /*
         * Este método genera un número aleatorio entre los números que se le pasen como parámetros.
         */

        int numAle = (int)(Math.random()*(maximo - minimo + 1) + minimo);

        return numAle;
    }//final generarNumeroAleatorio.

    public ArrayList<Integer> generarListaNummerosImpares(int cantidad){
        ArrayList<Integer> listaImpares = new ArrayList<Integer>();
        int numIm;
        for(int i = 0; i < cantidad; i++){
            do{
                numIm = generarNumeroAleatorio(199, 0);
            }while (numIm%2 == 0 || listaImpares.contains(numIm));
            listaImpares.add(numIm);
        }//final for
        return listaImpares;
    }//generarListaNumerosImpares

    public ArrayList<Integer> generarListaNummerosPares(int cantidad){
        ArrayList<Integer>listaPares = new ArrayList<Integer>();
        int numPar;
        for(int i = 0; i < cantidad; i++){
            do{
                numPar = generarNumeroAleatorio(199, 0);
            }while (numPar%2 != 0 || listaPares.contains(numPar));
            listaPares.add(numPar);
        }//final for
        return listaPares;
    }//generarListaNumerosPares

}//final class NumeroAleatorio
