    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectosia;
import java.util.ArrayList;
// @author Ashborned9

public class BusPremium extends Bus {
    private int recargoServicio; // monto extra por cada pasajero

    // Constructor que "promueve" un Bus ya existente (no altera CSV)
    public BusPremium(Bus base, int recargoServicio) {
        super(base.getPatente(), base.getCapacidad(), base.getDestino(), base.getListaPasajeros());
        this.setPrecio(base.getPrecio());
        this.setCostoMantencion(base.getCostoMantencion());
        this.setMontoGenerado(base.getMontoGenerado());
        this.recargoServicio = recargoServicio;
    }

    // También puedes ofrecer este ctor “directo” si lo necesitas
    public BusPremium(String patente, int capacidadRestante, String destino, ArrayList listaPasajeros,
                      int precio, int costoMantencion, int recargoServicio) {
        super(patente, capacidadRestante, destino, listaPasajeros);
        this.setPrecio(precio);
        this.setCostoMantencion(costoMantencion);
        this.setMontoGenerado(0);
        this.recargoServicio = recargoServicio;
    }

    // ======= SOBRESCRITURAS =======

    // Ingreso = ingreso base + (recargo por pasajero * vendidos)
    @Override
    public int ingresoCalculado() {
        return super.ingresoCalculado() + (asientosVendidos() * recargoServicio);
    }

    // Asegura que el TOTAL sea correcto (total = vendidos + cupos restantes)
    @Override
    public int getCapacidadTotal() {
        return getCapacidad() + pasajerosActuales();
    }

    @Override
    public String toString() {
        return "[Bus Premium] Patente: " + this.getPatente() +
               ", Destino: " + this.getDestino() +
               ", Capacidad restante: " + this.getCapacidad() +
               ", Precio base: $" + this.getPrecio() +
               ", Recargo servicio: $" + recargoServicio;
    }

    // Getter/Setter del recargo (si quieres exponerlos en UI)
    public int getRecargoServicio() { return recargoServicio; }
    public void setRecargoServicio(int recargoServicio) { this.recargoServicio = recargoServicio; }
}