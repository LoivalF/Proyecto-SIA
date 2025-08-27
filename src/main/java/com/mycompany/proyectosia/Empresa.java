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
public class Empresa {
    private String nombre ;
    ArrayList buses ;
    private Map<String, ArrayList> mapaBuses ;
    
    public Empresa(String nombre) {
        this.nombre = nombre ;
        this.buses = new ArrayList<>() ;
        this.mapaBuses = new HashMap<>() ;
    }
    //Getter
    public String getNombre() {return nombre ;}
    public ArrayList<Bus> getBuses() {return buses ;}
    public Map<String, ArrayList> getMapaBuses() {return mapaBuses ;}
    //Setter
    public void setNombre(String nombre){this.nombre = nombre ;}
    //crear mas metodos jiji
    
}
