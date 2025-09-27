package com.mycompany.proyectosia;

import java.io.*;
import java.util.*;

/**
 * Métodos utilitarios para cargar y guardar datos de Empresa desde/hacia CSV
 * 
 * @author LoivalF
 */
public class ProyectoSia {

    // ===== Cargar Empresa desde CSV =====
    public static Empresa cargarCSV(String archivo, String nombreEmpresa) {
        Empresa empresa = new Empresa(nombreEmpresa);
        Map<String, Bus> mapa = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea = br.readLine(); // saltar cabecera
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",", -1);
                String patente = partes[0];
                int capacidad = Integer.parseInt(partes[1]);
                String destino = partes[2];
                String nombrePasajero = partes[3];
                String rutPasajero = partes[4];
                String destinoPasajero = partes[5];

                Bus bus = mapa.get(patente);
                if (bus == null) {
                    bus = new Bus(patente, capacidad, destino, new ArrayList());
                    empresa.agregarBus(bus, destino);
                    mapa.put(patente, bus);
                }

                if (!nombrePasajero.isEmpty() && !rutPasajero.isEmpty()) {
                    Pasajero p = new Pasajero(nombrePasajero, rutPasajero, destinoPasajero);
                    bus.agregarPasajero(p);
                }
            }
            System.out.println("Datos cargados desde " + archivo);
        } catch (IOException e) {
            System.out.println("Error al cargar CSV: " + e.getMessage());
        }
        return empresa;
    }

    // ===== Guardar Empresa en CSV =====
    public static void guardarCSV(Empresa empresa, String archivo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            pw.println("patente,capacidad,destino,nombre_pasajero,rut_pasajero,destino_pasajero");

            for (Object objBus : empresa.getBuses()) {
                Bus bus = (Bus) objBus;
                ArrayList pasajeros = bus.getListaPasajeros();

                if (pasajeros.isEmpty()) {
                    pw.println(bus.getPatente() + "," +
                               bus.getCapacidad() + "," +
                               bus.getDestino() + ",,,");
                } else {
                    for (Object objPasajero : pasajeros) {
                        Pasajero p = (Pasajero) objPasajero;
                        pw.println(bus.getPatente() + "," +
                                   bus.getCapacidad() + "," +
                                   bus.getDestino() + "," +
                                   p.getNombre() + "," +
                                   p.getRut() + "," +
                                   p.getDestino());
                    }
                }
            }
            System.out.println("Datos guardados en " + archivo);
        } catch (IOException e) {
            System.out.println("Error al guardar CSV: " + e.getMessage());
        }
    }

    // ===== Puente: lanzar la ventana principal =====
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}
