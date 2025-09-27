package com.mycompany.proyectosia ;
/**
 *
 * @author LoivalF
 */
public class Pasajero { 
    private String nombre;
    private String rut;
    private String destino;
       
    //CONSTRUCTORES
    public Pasajero() {
        this.nombre = ("Desconocido");
        this.rut = "Desconocido";
        this.destino = "Desconocido";
    }
    public Pasajero(String nombre, String rut, String destino) {
        this.nombre = nombre;
        this.rut = rut;
        this.destino = destino;
    }
    
    //SETTERS
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setRut(String rut) { this.rut = rut; } 
    public void setDestino(String destino) { this.destino = destino; }
    
    //GETTERS
    public String getNombre() { return this.nombre; }
    public String getRut() { return this.rut; }
    public String getDestino() { return destino; } 
    
    //MÉTODOS
    public String toString() {
        return "Pasajero: " + nombre + ", RUT: " + rut + ", Destino: " + destino;
    }
}
