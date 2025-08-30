/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectosia ;
import java.util.*;
import java.io.*;

/**
 *
 * @author LoivalF
 */
public class Bus {
    private String patente;
    private int capacidad;
    private String destino;
    private ArrayList listaPasajeros;
    
    //CONSTRUCTORES
    public Bus(String patente, int capacidad, String destino, ArrayList listaPasajeros) {
        this.patente = patente;
        this.capacidad = capacidad;
        this.destino = destino;
        this.listaPasajeros = listaPasajeros;
    }
    public Bus() {
        this.patente = "Desconocido";
        this.capacidad = 0;
        this.destino = "Desconocido";
        this.listaPasajeros = new ArrayList();
    }
    
    //SETTERS
    public void setPatente(String patente) { this.patente = patente; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    public void setDestino(String destino) { this.destino = destino; }
    public void setListaPasajeros(ArrayList listaPasajeros) { this.listaPasajeros = listaPasajeros; }

    //GETTERS
    public String getPatente() { return patente; }
    public int getCapacidad() { return capacidad; }
    public String getDestino() { return destino; }
    public ArrayList getListaPasajeros() { return listaPasajeros; }
    
    //MÉTODOS
    public void agregarPasajero(Pasajero p) {
        if (this.capacidad == 0) { 
            System.out.println("No quedan asientos disponibles en el bus. ¡Lo sentimos!");
            return; 
        }
              
        listaPasajeros.add(p);
        capacidad--;
        System.out.println("¡Pasajero añadido correctamente!");
    }
    public void agregarPasajero(String nombre, String rut, String destino) {
        if (this.capacidad == 0) { 
            System.out.println("No quedan asientos disponibles en el bus. ¡Lo sentimos!");
            return; 
        }
        
        Pasajero p = new Pasajero(nombre, rut, destino);
        listaPasajeros.add(p);
        capacidad--;
        System.out.println("¡Pasajero añadido correctamente!");
    }
    
    public void eliminarPasajero(String rut) {
        for (int i = 0 ; i < listaPasajeros.size() ; i++) {
            Pasajero p = (Pasajero) listaPasajeros.get(i);
            
            if (p.getRut().equalsIgnoreCase(rut)) {
                listaPasajeros.remove(i);
                System.out.println("Pasajero de RUT "+rut+" ha sido eliminado correctamente");
                return;
            }
        }
        System.out.println("No se encontró el pasajero con RUT "+rut);
    }
    
    public int pasajerosActuales() {
        return listaPasajeros.size();
    }
}
