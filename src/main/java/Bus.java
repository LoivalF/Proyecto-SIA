/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sia.proyectosia;

/**
 *
 * @author felip
 */
public class Bus {
    private String patente;
    private int capacidad;

    public Bus(String patente, int capacidad) {
        this.patente = patente;
        this.capacidad = capacidad;
    }

    public void setPatente(String patente){ this.patente = patente; }
    public String getPatente(){ return patente; }

    public void setCapacidad(int capacidad){ this.capacidad = capacidad; }
    public int getCapacidad(){ return capacidad; }

    public boolean estaOcupado() {
        return capacidad == 0;
    }
}
