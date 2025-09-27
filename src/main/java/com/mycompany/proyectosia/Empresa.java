package com.mycompany.proyectosia ;
import java.util.* ;
/**
 *
 * @author pauli felip LoivalF
 */
public class Empresa { 
    private String nombre ;
    private ArrayList buses ;
    private Map mapaBuses ;
    
    public Empresa(String nombre) {
        this.nombre = nombre ;
        this.buses = new ArrayList() ;
        this.mapaBuses = new HashMap() ;
    }
    
    //SETTERS
    public void setNombre(String nombre){this.nombre = nombre ;}
    public void setBus(Bus b) { if (!buses.contains(b)) { buses.add(b);}}
    public void setMapaBuses(Map mapaBuses) {
        if (mapaBuses == null) { this.mapaBuses = new HashMap(); }
        else { this.mapaBuses = new HashMap(mapaBuses); }}
    
    //GETTERS
    public String getNombre() { return nombre ;}
    public ArrayList getBuses() { return new ArrayList(buses) ;}
    public Map getMapaBuses() { return new HashMap(mapaBuses) ;}
    
    //MÉTODOS
    public void agregarBus(Bus b, String destino) {
        b.setDestino(destino);
        if (!buses.contains(b)) {
            buses.add(b);
            System.out.println("Bus agregado: Patente " + b.getPatente() + " con destino " + destino);
        }
        if (!mapaBuses.containsKey(destino)) {
            mapaBuses.put(destino, new ArrayList()); 
        }
        ArrayList lista = (ArrayList) mapaBuses.get(destino); 
        if (!lista.contains(b)) { 
            lista.add(b);
        }
    }
    
    public boolean eliminarBus(String patente) {
        for (int i = 0 ; i < buses.size() ; i++) {
            Bus b = (Bus) buses.get(i);
            if (b.getPatente().equalsIgnoreCase(patente)) {
                buses.remove(i);
            }
            ArrayList lista = (ArrayList) mapaBuses.get(b.getDestino());
            if (lista != null) { lista.remove(b); }
            if (lista.isEmpty()) { mapaBuses.remove(b.getDestino()); }
            System.out.println("Bus con patente " + patente + " eliminado.");
            return true;
        }
        System.out.println("No se encontró ningún bus con patente" + patente);
        return false;
    }
    
    public boolean modificarDestinoBus(String patente, String destino) {
        for (int i = 0 ; i < buses.size() ; i++) {
            Bus b = (Bus) buses.get(i);
            if (b.getPatente().equalsIgnoreCase(patente)) {
                ArrayList listaAntigua = (ArrayList) mapaBuses.get(b.getDestino());
                if (listaAntigua != null) { listaAntigua.remove(b); }
                if (listaAntigua.isEmpty()) { mapaBuses.remove(b.getDestino()); }
                
                b.setDestino(destino);
                
                agregarBus(b, destino);
                System.out.println("Bus con patente " + patente + " ahora tiene el destino " + destino);
                return true;
            }
        }
        System.out.println("No se encontró ningún bus con patente" + patente);
        return false;
    }
        
    public Bus buscarBusPatente(String patente) {
        for (int i = 0 ; i < buses.size() ; i++) {
            Bus b = (Bus) buses.get(i);
            if (b.getPatente().equalsIgnoreCase(patente)) { return b; }    
        }
        return null;
    }
    
    public boolean asignarPasajeroABus(Pasajero p, String patente) {
        for (int i = 0; i < buses.size(); i++) {
            Bus b = (Bus) buses.get(i);
            if (b.getPatente().equalsIgnoreCase(patente)) {
                if (!p.getDestino().equalsIgnoreCase(b.getDestino())) {
                    System.out.println("El destino del pasajero no coincide con el del bus.");
                    return false;
                }
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
            Bus b = (Bus) buses.get(i); 
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
        if (mapaBuses.containsKey(destino)) { 
            ArrayList lista = (ArrayList) mapaBuses.get(destino); // 
            System.out.println("Buses con destino a " + destino + ":");

            for (int i = 0; i < lista.size(); i++) {
                Bus b = (Bus) lista.get(i); 
                System.out.println("| Patente: " + b.getPatente() + " | Capacidad: " + b.getCapacidad());
            }
        } 
        else {
            System.out.println("No hay buses registrados para el destino: " + destino);
        }
    } 
}
