/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectosia ;
import java.util.* ;
/**
 *
 * @author pauli
 */
public class Empresa { //hola
    private String nombre ;
    private ArrayList<Bus> buses ;
    private Map<String, ArrayList<Bus>> mapaBuses ;
    
    public Empresa(String nombre) {
        this.nombre = nombre ;
        this.buses = new ArrayList<>() ;
        this.mapaBuses = new HashMap<>() ;
    }
    //Getter
    public String getNombre() {return nombre ;}
    public ArrayList<Bus> getBuses() {return buses ;}
    public Map<String, ArrayList<Bus>> getMapaBuses() {return mapaBuses ;}
    //Setter
    public void setNombre(String nombre){this.nombre = nombre ;}
    //crear mas metodos jiji
    
    public void agregarBus (Bus b, String destino) { //metodo que agrega un bus con destino
        b.setDestino(destino) ; //destino del bus
        if (!buses.contains(b)){ //agregar a lista de buses
            buses.add(b);
            System.out.println("Hay un bus tanto, con patente tanto con destino tanto");
        }
        if (!mapaBuses.containsKey(destino)) { //Agrega al mapa por destino....
            mapaBuses.put(destino, new ArrayList<>()) ; // cada destino tiene su lista de buses
        }
        mapaBuses.get(destino).add(b) ;
    }
    
    public boolean asignarPasajeroABus (Pasajero p, String patente) {
        for (Bus b : buses) { //buscar bus por patente
            if (b.getPatente().equals(patente)) {
                if (!p.getDestino().equals(b.getDestino())) { //verifica que el destinto del pasajero y el bus concuerde
                    System.out.println("el destino del pasajeno no coincide con el bus") ;
                    return false ;
                }
                if (b.getCapacidad() > 0) { //verifica la capacidad del bus ;;;ppp
                    b.setCapacidad(b.getCapacidad() - 1); //reduce el cupo del bus
                    System.out.println("pasajero : sunombre, asignado al bus : patente");
                    return true ;
                } else {
                    System.out.println("El bus patente esta lleno");
                    return false ;
                }
            }
        }
        System.out.println("No existe un bus con la patente, patente");
        return false ;
    }
}
