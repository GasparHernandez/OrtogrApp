package com.example.proyecto_v2;

public class ItemList {
    //Declaración variables que contienen los valores de cada elemento CardView del RecyclerView
    private String numeroPalabra;
    private String palabra;
    private int svgBien;
    private int svgMal;

    //Constructor de cada elemento CardView del RecyclerView
    public ItemList(String numeroPalabra, String palabra, int svgBien, int svgMal) {
        this.numeroPalabra = numeroPalabra;
        this.palabra = palabra;
        this.svgBien = svgBien;
        this.svgMal = svgMal;
    }//final constructor

    //Métodos get para obtener los valores de cada variable de cada elemento CardView del RecyclerView
    public String getNumeroPalabra() {
        return numeroPalabra;
    }//final getNumeroPalabra

    public String getPalabra() {
        return palabra;
    }//final getPalabra

    public int getSvgBien() {
        return svgBien;
    }//final getSvgBien

    public int getSvgMal() { return svgMal; }//final getSvgMal

}//final class ItemList
