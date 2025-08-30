/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectosia ;
import java.util.* ;
/**
 *
 * @author pauli felip
 */
public class Empresa { 
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
    
    public void agregarBus(Bus b, String destino) {
        b.setDestino(destino);
        if (!buses.contains(b)) {
            buses.add(b);
            System.out.println("Bus agregado: patente " + b.getPatente() + " con destino " + destino);
        }
        if (!mapaBuses.containsKey(destino)) {
            mapaBuses.put(destino, new ArrayList()); 
        }
        ArrayList lista = mapaBuses.get(destino);  //se me olvido quitar los diamantes XD
        if (!lista.contains(b)) { 
            lista.add(b);
        }
    }

    
    public boolean asignarPasajeroABus(Pasajero p, String patente) {
        for (int i = 0; i < buses.size(); i++) {
            Bus b = buses.get(i);
            if (b.getPatente().equalsIgnoreCase(patente)) {
                if (!p.getDestino().equalsIgnoreCase(b.getDestino())) {
                    System.out.println("El destino del pasajero no coincide con el del bus.");
                    return false;
                }
                // delega en el bus (él valida capacidad y descuenta)
                if (b.agregarPasajero(p)) {
                    System.out.println("Pasajero " + p.getNombre() + " asignado al bus " + b.getPatente());
                    return true;
                } else {
                    System.out.println("El bus " + b.getPatente() + " está lleno.");
                    return false;
                }
            }
        }
        System.out.println("No existe un bus con la patente: " + patente);
        return false;
    }
    public void mostrarPasajerosDeBus(String patente) {
        for (int i = 0; i < buses.size(); i++) {
            Bus b = (Bus) buses.get(i); // casteo a Bus
            if (b.getPatente().equalsIgnoreCase(patente)) {
                System.out.println("Pasajeros del bus con patente " + patente + ":");
    
                ArrayList pasajeros = b.getListaPasajeros(); // sin <>
                for (int j = 0; j < pasajeros.size(); j++) {
                    Pasajero p = (Pasajero) pasajeros.get(j); // casteo a Pasajero
                    System.out.println("| " + p.getNombre() + " | RUT: " + p.getRut() + " | Destino: " + p.getDestino());
                }
                return;
            }
        }
        System.out.println("No se encontró ningún bus con la patente: " + patente);
    }

    public void obtenerBusesDestino(String destino) {
        if (mapaBuses.containsKey(destino)) {  // containsKey es de Map
            ArrayList lista = mapaBuses.get(destino); // 
            System.out.println("Buses con destino a " + destino + ":");

            for (int i = 0; i < lista.size(); i++) {
                Bus b = (Bus) lista.get(i); // casteo a Bus
                System.out.println("| Patente: " + b.getPatente() + " | Capacidad: " + b.getCapacidad());
            }
        } else {
            System.out.println("No hay buses registrados para el destino: " + destino);
        }
    }
}
