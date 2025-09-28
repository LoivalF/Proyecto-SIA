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
    private int presupuesto;
    
    public Empresa(String nombre) {
        this.nombre = nombre ;
        this.buses = new ArrayList() ;
        this.mapaBuses = new HashMap() ;
        this.presupuesto = 50000000;
    }
    //SETTERS
    public void setNombre(String nombre){this.nombre = nombre ;}
    public void setBus(Bus b) { if (!buses.contains(b)) { buses.add(b);}}
    public void setMapaBuses(Map mapaBuses) {
        if (mapaBuses == null) { this.mapaBuses = new HashMap(); }
        else { this.mapaBuses = new HashMap(mapaBuses); }}
    public void setPresupuesto(int presupuesto) { this.presupuesto = presupuesto; }
    
    //GETTERS
    public String getNombre() { return nombre ;}
    public ArrayList getBuses() { return new ArrayList(buses) ;}
    public Map getMapaBuses() { return new HashMap(mapaBuses) ;}
    public int getPresupuesto() { return presupuesto; }
    
    // ================== MÉTODOS ==================

    // Overload agregado para compatibilidad con comprarBus(...)
    public void agregarBus(Bus b) {
        // Reutiliza tu método original tomando el destino ya seteado en el Bus
        agregarBus(b, b.getDestino());
    }

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
                // remover de la lista global
                buses.remove(i);
                // remover del mapa por destino (evita NPE)
                ArrayList lista = (ArrayList) mapaBuses.get(b.getDestino());
                if (lista != null) {
                    lista.remove(b);
                    if (lista.isEmpty()) { mapaBuses.remove(b.getDestino()); }
                }
                System.out.println("Bus con patente " + patente + " eliminado.");
                return true;
            }
        }
        System.out.println("No se encontró ningún bus con patente " + patente);
        return false;
    }
    
    public boolean modificarDestinoBus(String patente, String destino) {
        for (int i = 0 ; i < buses.size() ; i++) {
            Bus b = (Bus) buses.get(i);
            if (b.getPatente().equalsIgnoreCase(patente)) {
                ArrayList listaAntigua = (ArrayList) mapaBuses.get(b.getDestino());
                if (listaAntigua != null) {
                    listaAntigua.remove(b);
                    if (listaAntigua.isEmpty()) { mapaBuses.remove(b.getDestino()); }
                }
                b.setDestino(destino);
                agregarBus(b, destino);
                System.out.println("Bus con patente " + patente + " ahora tiene el destino " + destino);
                return true;
            }
        }
        System.out.println("No se encontró ningún bus con patente " + patente);
        return false;
    }
    public Bus buscarBusPatente(String patente) {
        for (int i = 0 ; i < buses.size() ; i++) {
            Bus b = (Bus) buses.get(i);
            if (b.getPatente().equalsIgnoreCase(patente)) { return b; }    
        }
        return null;
    }
    public Bus asignarPasajeroDisponible(Pasajero p) throws CapacidadLlenaException, PasajeroDuplicadoException{
        if (!mapaBuses.containsKey(p.getDestino())) {
            throw new CapacidadLlenaException("No hay buses con destino a " + p.getDestino());
        }

        ArrayList lista = (ArrayList) mapaBuses.get(p.getDestino());

        // Probar buses del destino hasta lograr cupo
        CapacidadLlenaException ultimoCapacidad = null;
        for (int i = 0; i < lista.size(); i++) {
            Bus b = (Bus) lista.get(i);
            try {
                if (b.agregarPasajero(p)) {
                    // éxito (si hay duplicado/cupo insuficiente, ya se lanzó arriba)
                    return b;
                }
            } catch (CapacidadLlenaException e) {
                ultimoCapacidad = e; // intentamos siguiente bus
            }
        }
        // Ningún bus tuvo cupo
        if (ultimoCapacidad != null) throw ultimoCapacidad;
        throw new CapacidadLlenaException("No hay cupos disponibles para " + p.getDestino());
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
            ArrayList lista = (ArrayList) mapaBuses.get(destino); 
            System.out.println("Buses con destino a " + destino + ":");
            for (int i = 0; i < lista.size(); i++) {
                Bus b = (Bus) lista.get(i); 
                System.out.println("| Patente: " + b.getPatente() + " | Capacidad: " + b.getCapacidad());
            }
        } else {
            System.out.println("No hay buses registrados para el destino: " + destino);
        }
    }

    // ===================== NUEVOS MÉTODOS – Empresa =====================

    /** Devuelve una copia de la lista de buses (por si la UI prefiere no usar getBuses()). */
    public ArrayList getBusesEmpresa() {
        return new ArrayList(this.buses);
    }

    /** Devuelve una copia de la lista de buses para un destino (a diferencia de obtenerBusesDestino() que imprime). */
    public ArrayList getBusesPorDestino(String destino) {
        if (destino == null) return new ArrayList();
        if (this.mapaBuses == null) return new ArrayList();
        ArrayList lista = (ArrayList) this.mapaBuses.get(destino);
        return (lista == null) ? new ArrayList() : new ArrayList(lista);
    }

    /** Busca un pasajero por RUT en todos los buses y lo retorna. Null si no existe. */
    public Pasajero buscarPasajeroPorRut(String rut) {
        if (rut == null) return null;
        for (int i = 0; i < this.buses.size(); i++) {
            Bus b = (Bus) this.buses.get(i);
            Pasajero p = b.buscarPasajeroPorRut(rut); // helper en Bus
            if (p != null) return p;
        }
        return null;
    }

    /** Devuelve el BUS donde se encuentra ese RUT, o null si no está en ninguno. */
    public Bus buscarBusDePasajeroPorRut(String rut) {
        if (rut == null) return null;
        for (int i = 0; i < this.buses.size(); i++) {
            Bus b = (Bus) this.buses.get(i);
            Pasajero p = b.buscarPasajeroPorRut(rut);
            if (p != null) return b;
        }
        return null;
    }

    public int consolidarPorDestino(String destino, int umbralUtilidad) {
        ArrayList lista = getBusesPorDestino(destino);
        if (lista.isEmpty()) return 0;

        // Ordenar: más llenos primero (más vendidos)
        Collections.sort(lista, new Comparator() {
            public int compare(Object o1, Object o2) {
                Bus a = (Bus) o1, b = (Bus) o2;
                return Integer.compare(b.asientosVendidos(), a.asientosVendidos());
            }
        });

        int reasignados = 0;

        // Llenar receptores desde donantes (final de la lista)
        for (int i = 0; i < lista.size(); i++) {
            Bus receptor = (Bus) lista.get(i);
            if (receptor.estaLleno()) continue;

            for (int j = lista.size() - 1; j > i; j--) {
                Bus donante = (Bus) lista.get(j);
                if (donante.asientosVendidos() == 0) continue;
                if (receptor.estaLleno()) break;

                int cupos = receptor.cuposDisponibles();
                int mov = receptor.moverPasajerosDesde(donante, cupos);
                reasignados += mov;
            }
        }

        // Cancelar buses sin pasajeros (y limpiar mapa)
        ArrayList paraEliminar = new ArrayList();
        for (int k = 0; k < lista.size(); k++) {
            Bus b = (Bus) lista.get(k);
            boolean sinPax = (b.asientosVendidos() == 0);
            if (sinPax) {
                paraEliminar.add(b);
            }
        }

        if (!paraEliminar.isEmpty()) {
            ArrayList enMapa = (ArrayList) this.mapaBuses.get(destino);
            for (int i = 0; i < paraEliminar.size(); i++) {
                Bus del = (Bus) paraEliminar.get(i);
                this.buses.remove(del);
                if (enMapa != null) enMapa.remove(del);
            }
            if (enMapa != null && enMapa.isEmpty()) {
                this.mapaBuses.remove(destino);
            }
        }

        return reasignados;
    }

    public boolean cancelarViaje(String patente, int umbralUtilidad) {
        Bus aCancelar = buscarBusPatente(patente);
        if (aCancelar == null) return false;

        String destino = aCancelar.getDestino();
        ArrayList lista = getBusesPorDestino(destino);
        // quitarse a sí mismo de la lista local de trabajo
        for (int i = 0; i < lista.size(); i++) {
            Bus b = (Bus) lista.get(i);
            if (b == aCancelar) { lista.remove(i); break; }
        }

        // Reasignar todos los pasajeros del bus a cancelar
        Collections.sort(lista, new Comparator() {
            public int compare(Object o1, Object o2) {
                Bus a = (Bus) o1, b = (Bus) o2;
                return Integer.compare(b.asientosVendidos(), a.asientosVendidos());
            }
        });

        int porMover = aCancelar.asientosVendidos();
        for (int i = 0; i < lista.size() && porMover > 0; i++) {
            Bus receptor = (Bus) lista.get(i);
            int cupos = receptor.cuposDisponibles();
            if (cupos <= 0) continue;
            int mov = receptor.moverPasajerosDesde(aCancelar, cupos);
            porMover -= mov;
        }

        if (aCancelar.asientosVendidos() == 0) {
            this.buses.remove(aCancelar);
            ArrayList enMapa = (ArrayList) this.mapaBuses.get(destino);
            if (enMapa != null) {
                enMapa.remove(aCancelar);
                if (enMapa.isEmpty()) this.mapaBuses.remove(destino);
            }
            return true;
        }
        return false; // quedaron pax sin cupo, no se cancela
    }

        // Promueve un bus a BusPremium sin tocar CSV ni ProyectoSia
    public boolean promoverABusPremium(String patente, int recargoServicio) {
        Bus base = buscarBusPatente(patente);
        if (base == null) return false;

        // Crear premium a partir del base
        BusPremium premium = new BusPremium(base, recargoServicio);

        // Reemplazar referencia en la lista principal
        for (int i = 0; i < buses.size(); i++) {
            if (buses.get(i) == base) { buses.set(i, premium); break; }
        }

        // Reemplazar referencia en el mapa por destino
        ArrayList lista = (ArrayList) mapaBuses.get(base.getDestino());
        if (lista != null) {
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i) == base) { lista.set(i, premium); break; }
            }
        }
        return true;
    }

    // Promueve un pasajero a PasajeroFrecuente (reemplazando uno por otro)
    public boolean promoverPasajeroAFrecuente(String rut, int puntos) {
        if (rut == null || rut.trim().isEmpty()) return false;
        Bus b = buscarBusDePasajeroPorRut(rut);
        if (b == null) return false;

        Pasajero p = b.buscarPasajeroPorRut(rut);
        if (p == null) return false;

        // Eliminar original -> capacity++ interno
        b.eliminarPasajero(rut);

        PasajeroFrecuente pf = new PasajeroFrecuente(p.getNombre(), p.getRut(), p.getDestino(), puntos);
        try {
            b.agregarPasajero(pf);
            return true;
        } catch (CapacidadLlenaException | PasajeroDuplicadoException ex) {

            try {
                b.agregarPasajero(p); 
            } catch (Exception ignore) {

            }
            return false;
        }
    }
}
