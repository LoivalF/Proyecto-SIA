/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectosia;

/**
 *
 * @author felip
 */
public class PasajeroFrecuente extends Pasajero {
    private int puntos;

    public PasajeroFrecuente(String nombre, String rut, String destino, int puntos) {
        super(nombre, rut, destino);
        this.puntos = puntos;
    }

    @Override
    public String toString() {
        return super.toString() + " [Frecuente, puntos: " + puntos + "]";
    }

    public int getPuntos() { return puntos; }
    public void setPuntos(int puntos) { this.puntos = puntos; }
}
