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
    private ArrayList listaPasajeros;   // Se mantiene sin genéricos para no romper tu código existente
    private int costoMantencion;        // Usaremos esto como costo por viaje (costo fijo del servicio)
    private int precio;                 // Precio por pasaje
    private int montoGenerado;  
    private int capacidadTotal;

    // ===== CONSTRUCTORES =====
    public Bus(String patente, int capacidad, String destino, ArrayList listaPasajeros) {
        this.patente = patente;
        this.capacidad = capacidad;
        this.destino = destino;
        this.listaPasajeros = listaPasajeros;
        this.capacidadTotal = capacidad;
    }
    public Bus() {
        this.patente = "Desconocido";
        this.capacidad = 0;
        this.destino = "Desconocido";
        this.listaPasajeros = new ArrayList();
        this.capacidadTotal= 0;
    }

    // ===== SETTERS =====
    public void setPatente(String patente) { this.patente = patente; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    public void setDestino(String destino) { this.destino = destino; }
    public void setListaPasajeros(ArrayList lista) {
        if (lista == null) { this.listaPasajeros = new ArrayList(); }
        else { this.listaPasajeros = new ArrayList(lista); }
    }
    public void setCostoMantencion(int costoMantencion) { this.costoMantencion = costoMantencion; }
    public void setPrecio(int precio) { this.precio = precio; }
    public void setMontoGenerado(int montoGenerado) { this.montoGenerado = montoGenerado; }
    public void setCapacidadTotal(int total) {
        if (total < 0) total = 0;
        this.capacidadTotal = total;
        int vendidos = pasajerosActuales();
        this.capacidad = Math.max(0, this.capacidadTotal - vendidos); // mantiene cupos coherentes
    }

    // ===== GETTERS =====
    public String getPatente() { return patente; }
    public int getCapacidad() { return capacidad; }              // cupos restantes según tu lógica
    public String getDestino() { return destino; }
    public ArrayList getListaPasajeros() { return new ArrayList(listaPasajeros); }
    public int getCostoMantencion() { return costoMantencion; }
    public int getPrecio() { return precio; }
    public int getMontoGenerado() { return montoGenerado; }
    public int getCapacidadTotal() {
    if (capacidadTotal <= 0) {
        return this.capacidad + pasajerosActuales();
    }
    return capacidadTotal;
}

    // ===== MÉTODOS EXISTENTES (NO MODIFICADOS) =====
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

    // ===== MÉTODOS NUEVOS (NO ROMPEN TU LÓGICA) =====

    /** Cupos disponibles (alias semántico de tu campo capacidad). */
    public int cuposDisponibles() {
        return this.capacidad;
    }

    /** ¿Está lleno? Basado en tu manejo de capacidad decreciente. */
    public boolean estaLleno() {
        return this.capacidad <= 0;
    }

    /** Por claridad de negocio (wrapper de tu método existente). */
    public int asientosVendidos() {
        return pasajerosActuales();
    }

    /** Verifica si el RUT ya está en el bus (apoya validación UI sin cambiar tu agregarPasajero). */
    public boolean contieneRut(String rut) {
        if (rut == null) return false;
        for (int i = 0; i < listaPasajeros.size(); i++) {
            Pasajero p = (Pasajero) listaPasajeros.get(i);
            if (rut.equalsIgnoreCase(p.getRut())) return true;
        }
        return false;
    }

    /** Busca pasajero por RUT y lo retorna (opcional para Empresa/UI). */
    public Pasajero buscarPasajeroPorRut(String rut) {
        if (rut == null) return null;
        for (int i = 0; i < listaPasajeros.size(); i++) {
            Pasajero p = (Pasajero) listaPasajeros.get(i);
            if (rut.equalsIgnoreCase(p.getRut())) return p;
        }
        return null;
    }

    /** ¿El destino ingresado coincide con el del bus? (útil para validar antes de agregar) */
    public boolean destinoCoincide(String destino) {
        return destino != null && this.destino != null && this.destino.equalsIgnoreCase(destino);
    }

    // ---------- Economía del viaje ----------
    /** Ingreso calculado en vivo: asientos vendidos * precio. (No toca tu montoGenerado) */
    public int ingresoCalculado() {
        return asientosVendidos() * this.precio;
    }

    /** Costo por viaje: usamos costoMantencion como costo fijo del servicio. */
    public int costoViaje() {
        return this.costoMantencion;
    }

    /** Utilidad = ingreso calculado - costo viaje. */
    public int utilidad() {
        return ingresoCalculado() - costoViaje();
    }

    /** ¿Es rentable? umbral 0 => no perder plata; puedes pasar otro umbral desde Empresa. */
    public boolean esRentable(int umbral) {
        return utilidad() >= umbral;
    }

    /** Texto corto para mostrar en tablas o reportes. */
    public String resumenEconomico() {
        String signo = utilidad() >= 0 ? "+" : "-";
        return "Vendidos=" + asientosVendidos() +
               ", Ingreso=$" + ingresoCalculado() +
               ", Costo=$" + costoViaje() +
               ", Utilidad=" + signo + Math.abs(utilidad());
    }

    // ---------- Apoyo a consolidación entre buses del mismo destino ----------
    /**
     * Extrae hasta 'cantidad' pasajeros del FINAL de la lista (estrategia simple)
     * y los retorna para reasignación. No altera la lógica de agregar/eliminar que ya tienes,
     * solo reutiliza tu incremento de capacidad al remover uno por uno.
     */
    public List extraerPasajerosHasta(int cantidad) {
        List movidos = new ArrayList();
        if (cantidad <= 0) return movidos;
        int aMover = Math.min(cantidad, listaPasajeros.size());
        for (int i = 0; i < aMover; i++) {
            // remover desde el final para evitar corrimientos costosos
            int idx = listaPasajeros.size() - 1;
            Pasajero p = (Pasajero) listaPasajeros.remove(idx);
            this.capacidad++; // coherente con tu eliminarPasajero
            movidos.add(p);
        }
        return movidos;
    }

    /**
     * Intenta mover 'cantidad' pasajeros desde 'origen' hacia este bus.
     * No mueve nada si los destinos no coinciden o si no hay cupos.
     * Devuelve cuántos pasajeros efectivamente movió.
     */
    public int moverPasajerosDesde(Bus origen, int cantidad) {
        if (origen == null || cantidad <= 0) return 0;
        if (!this.destinoCoincide(origen.getDestino())) return 0;
        if (this.estaLleno()) return 0;

        int cupos = this.cuposDisponibles();
        int maxMov = Math.min(cantidad, cupos);
        List movidos = origen.extraerPasajerosHasta(maxMov);
        int realizados = 0;
        for (int i = 0; i < movidos.size(); i++) {
            Pasajero p = (Pasajero) movidos.get(i);
            // Reutilizamos tu agregarPasajero(Pasajero) para mantener coherencia en capacidad--
            boolean ok = this.agregarPasajero(p);
            if (ok) {
                realizados++;
            } else {
                // si por alguna razón no cupo, devolvemos el pasajero al origen para no perderlo
                origen.agregarPasajero(p);
            }
        }
        return realizados;
    }
}
