package com.mycompany.proyectosia ;
import java.util.*;


/**
 *
 * @author LoivalF
 */
public class Bus {
    private String patente;
    private int capacidad;
    private String destino;
    private ArrayList listaPasajeros;
    private int costoMantencion;
    private int precio;
    private int montoGenerado;
    
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
    public void setListaPasajeros(ArrayList lista) { 
        if (lista == null) { this.listaPasajeros = new ArrayList(); }
        else { this.listaPasajeros = new ArrayList(lista); }}
    public void setCostoMantencion(int costoMantencion) { this.costoMantencion = costoMantencion; }
    public void setPrecio(int precio) { this.precio = precio; }
    public void setMontoGenerado(int montoGenerado) { this.montoGenerado = montoGenerado; }

    //GETTERS
    public String getPatente() { return patente; }
    public int getCapacidad() { return capacidad; }
    public String getDestino() { return destino; }
    public ArrayList getListaPasajeros() { return new ArrayList(listaPasajeros); }
    public int getCostoMantencion() { return costoMantencion; }
    public int getPrecio() { return precio; }
    public int getMontoGenerado() { return montoGenerado; }
    
    //MÉTODOS
    public boolean agregarPasajero(Pasajero p) {
        if (this.capacidad == 0) { 
            return false; 
        }
              
        listaPasajeros.add(p);
        capacidad--;
        return true;
    }
    public boolean agregarPasajero(String nombre, String rut, String destino) {
        if (this.capacidad == 0) { 
            return false; 
        }
        
        Pasajero p = new Pasajero(nombre, rut, destino);
        listaPasajeros.add(p);
        capacidad--;
        return true;
    }
    
    public void eliminarPasajero(String rut) {
        for (int i = 0 ; i < listaPasajeros.size() ; i++) {
            Pasajero p = (Pasajero) listaPasajeros.get(i);
            
            if (p.getRut().equalsIgnoreCase(rut)) {
                listaPasajeros.remove(i);
                System.out.println("Pasajero de RUT "+rut+" ha sido eliminado correctamente");
                this.capacidad++;
                return;
            }
        }
        System.out.println("No se encontró el pasajero con RUT "+rut);
    }
    
    public int pasajerosActuales() {
        return listaPasajeros.size();
    }

    
}
