package com.mycompany.proyectosia;

import java.io.*;
import java.util.*;

/**
 * Métodos utilitarios para cargar y guardar datos de Empresa desde/hacia CSV
 * @author LoivalF
 */
public class ProyectoSia {

    // ===== Cargar Empresa desde CSV =====
    public static Empresa cargarCSV(String archivo, String nombreEmpresa) throws IOException {
        Empresa empresa = new Empresa(nombreEmpresa);
        Map<String, Bus> busesPorPatente = new HashMap<>();

        // try-with-resources: si falla abrir/leer, lanzará IOException hacia la UI
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea = br.readLine(); // cabecera
            if (linea == null) {
                return empresa; // CSV vacío
            }

            String row;
            while ((row = br.readLine()) != null) {
                if (row.trim().isEmpty()) continue;

                String[] partes = row.split(",", -1);

                // Variables con defaults
                String patente = "";
                int capacidad = 0;
                String destinoBus = "Desconocido";
                String nombrePasajero = "";
                String rutPasajero = "";
                String destinoPasajero = "";

                int precio = 0;
                int costoViaje = 0; // mapea a costoMantencion en Bus
                int montoGenerado = 0;

                // Cargar campos base si existen
                if (partes.length > 0) patente = partes[0].trim();
                if (partes.length > 1) {
                    String capStr = partes[1].trim();
                    if (!capStr.isEmpty()) {
                        try { capacidad = Integer.parseInt(capStr); } catch (NumberFormatException ignore) { capacidad = 0; }
                    }
                }
                if (partes.length > 2) {
                    destinoBus = partes[2].trim();
                    if (destinoBus.isEmpty()) destinoBus = "Desconocido";
                }
                if (partes.length > 3) nombrePasajero = partes[3].trim();
                if (partes.length > 4) rutPasajero = partes[4].trim();
                if (partes.length > 5) destinoPasajero = partes[5].trim();

                // Económicos opcionales
                if (partes.length > 6) {
                    String precioStr = partes[6].trim();
                    if (!precioStr.isEmpty()) {
                        try { precio = Integer.parseInt(precioStr); } catch (NumberFormatException ignore) { precio = 0; }
                    }
                }
                if (partes.length > 7) {
                    String costoStr = partes[7].trim();
                    if (!costoStr.isEmpty()) {
                        try { costoViaje = Integer.parseInt(costoStr); } catch (NumberFormatException ignore) { costoViaje = 0; }
                    }
                }
                if (partes.length > 8) {
                    String montoStr = partes[8].trim();
                    if (!montoStr.isEmpty()) {
                        try { montoGenerado = Integer.parseInt(montoStr); } catch (NumberFormatException ignore) { montoGenerado = 0; }
                    }
                }

                // Si no hay patente, saltamos la fila
                if (patente.isEmpty()) continue;

                // Crear o recuperar el bus por patente
                Bus bus = busesPorPatente.get(patente);
                if (bus == null) {
                    bus = new Bus(patente, capacidad, destinoBus, new ArrayList());
                    bus.setPrecio(precio);
                    bus.setCostoMantencion(costoViaje);
                    bus.setMontoGenerado(montoGenerado);

                    busesPorPatente.put(patente, bus);
                    empresa.agregarBus(bus, destinoBus);
                } else {
                    // Si en filas posteriores vienen valores > 0, actualiza
                    if (precio > 0) bus.setPrecio(precio);
                    if (costoViaje > 0) bus.setCostoMantencion(costoViaje);
                    if (montoGenerado > 0) bus.setMontoGenerado(montoGenerado);
                }

                // Si la fila trae pasajero válido, agrégalo
                if (!nombrePasajero.isEmpty() && !rutPasajero.isEmpty()) {
                    String destPax = (destinoPasajero == null || destinoPasajero.isEmpty()) ? destinoBus : destinoPasajero;
                    Pasajero p = new Pasajero(nombrePasajero, rutPasajero, destPax);
                    try {
                        bus.agregarPasajero(p); // ahora lanza CapacidadLlenaException / PasajeroDuplicadoException
                    } catch (PasajeroDuplicadoException e) {
                        // Si quieres loguear: System.out.println("Duplicado CSV ignorado: " + e.getMessage());
                    } catch (CapacidadLlenaException e) {
                        // Si quieres loguear: System.out.println("Sin cupo CSV ignorado: " + e.getMessage());
                    }
                }
            }
        } // <- se cierra solo; cualquier IOException se propaga

        return empresa;
    }

    // ===== Guardar Empresa en CSV =====
    public static void guardarCSV(Empresa empresa, String archivo) throws IOException {
        String[] header = new String[]{
            "patente","capacidad","destino","nombrePasajero","rutPasajero","destinoPasajero",
            "precio","costoViaje","montoGenerado"
        };

        // try-with-resources: si falla escribir, lanzará IOException hacia la UI
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            pw.println(String.join(",", header));

            ArrayList buses = empresa.getBuses();
            for (int i = 0; i < buses.size(); i++) {
                Bus bus = (Bus) buses.get(i);
                ArrayList pax = bus.getListaPasajeros();
                int capacidadTotal = bus.getCapacidadTotal();

                if (!pax.isEmpty()) {
                    for (int j = 0; j < pax.size(); j++) {
                        Pasajero p = (Pasajero) pax.get(j);
                        pw.println(
                            bus.getPatente() + "," +
                            capacidadTotal + "," +
                            safe(bus.getDestino()) + "," +
                            safe(p.getNombre()) + "," +
                            safe(p.getRut()) + "," +
                            safe(p.getDestino()) + "," +
                            bus.getPrecio() + "," +
                            bus.getCostoMantencion() + "," +
                            bus.getMontoGenerado()
                        );
                    }
                } else {
                    // placeholder sin pasajero
                    pw.println(
                        bus.getPatente() + "," +
                        capacidadTotal + "," +
                        safe(bus.getDestino()) + "," +
                        "" + "," + "" + "," + "" + "," +
                        bus.getPrecio() + "," +
                        bus.getCostoMantencion() + "," +
                        bus.getMontoGenerado()
                    );
                }
            }
        } // <- se cierra solo; IOException se propaga
    }

    private static String safe(String s) {
        if (s == null) return "";
        return s.replace(",", " ");
    }

    // Lanzador UI
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new PrincipalWindow().setVisible(true);
        });
    }
}
