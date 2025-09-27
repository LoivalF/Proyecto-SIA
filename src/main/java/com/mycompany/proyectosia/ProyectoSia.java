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
        Map<String, Bus> busesPorPatente = new HashMap<>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(archivo));
            String linea = br.readLine(); // cabecera
            if (linea == null) {
                return empresa;
            }

            String row;
            while ((row = br.readLine()) != null) {
                if (row.trim().isEmpty()) {
                    continue;
                }

                String[] partes = row.split(",", -1);

                // Variables con defaults
                String patente = "";
                int capacidad = 0;
                String destinoBus = "Desconocido";
                String nombrePasajero = "";
                String rutPasajero = "";
                String destinoPasajero = "";

                int precio = 0;
                int costoViaje = 0;       // se mapea a costoMantencion en Bus
                int montoGenerado = 0;

                // Cargar campos base si existen
                if (partes.length > 0) {
                    patente = partes[0].trim();
                }
                if (partes.length > 1) {
                    String capStr = partes[1].trim();
                    if (!capStr.isEmpty()) {
                        try {
                            capacidad = Integer.parseInt(capStr);
                        } catch (NumberFormatException e) {
                            capacidad = 0;
                        }
                    }
                }
                if (partes.length > 2) {
                    destinoBus = partes[2].trim();
                    if (destinoBus.isEmpty()) {
                        destinoBus = "Desconocido";
                    }
                }
                if (partes.length > 3) {
                    nombrePasajero = partes[3].trim();
                }
                if (partes.length > 4) {
                    rutPasajero = partes[4].trim();
                }
                if (partes.length > 5) {
                    destinoPasajero = partes[5].trim();
                }

                // Campos económicos opcionales
                if (partes.length > 6) {
                    String precioStr = partes[6].trim();
                    if (!precioStr.isEmpty()) {
                        try {
                            precio = Integer.parseInt(precioStr);
                        } catch (NumberFormatException e) {
                            precio = 0;
                        }
                    }
                }
                if (partes.length > 7) {
                    String costoStr = partes[7].trim();
                    if (!costoStr.isEmpty()) {
                        try {
                            costoViaje = Integer.parseInt(costoStr);
                        } catch (NumberFormatException e) {
                            costoViaje = 0;
                        }
                    }
                }
                if (partes.length > 8) {
                    String montoStr = partes[8].trim();
                    if (!montoStr.isEmpty()) {
                        try {
                            montoGenerado = Integer.parseInt(montoStr);
                        } catch (NumberFormatException e) {
                            montoGenerado = 0;
                        }
                    }
                }

                // Si no hay patente, saltamos la fila
                if (patente.isEmpty()) {
                    continue;
                }

                // Crear o recuperar el bus por patente
                Bus bus = busesPorPatente.get(patente);
                if (bus == null) {
                    // OJO: tu constructor de Bus recibe listaPasajeros
                    bus = new Bus(patente, capacidad, destinoBus, new ArrayList());
                    bus.setPrecio(precio);
                    bus.setCostoMantencion(costoViaje);
                    bus.setMontoGenerado(montoGenerado);

                    busesPorPatente.put(patente, bus);
                    // Registrar en Empresa usando tu método que actualiza mapaBuses
                    empresa.agregarBus(bus, destinoBus);
                } else {
                    // Si vienen valores económicos en filas posteriores, actualiza si son >0
                    if (precio > 0) {
                        bus.setPrecio(precio);
                    }
                    if (costoViaje > 0) {
                        bus.setCostoMantencion(costoViaje);
                    }
                    if (montoGenerado > 0) {
                        bus.setMontoGenerado(montoGenerado);
                    }
                }

                // Si la fila trae pasajero válido, agrégalo
                boolean tieneNombre = !nombrePasajero.isEmpty();
                boolean tieneRut = !rutPasajero.isEmpty();
                if (tieneNombre && tieneRut) {
                    // Si no trae destino de pasajero, usa el del bus
                    String destPax = destinoPasajero;
                    if (destPax == null || destPax.isEmpty()) {
                        destPax = destinoBus;
                    }

                    Pasajero p = new Pasajero(nombrePasajero, rutPasajero, destPax);
                    // Usa tu lógica actual (capacidad-- al agregar)
                    bus.agregarPasajero(p);
                }
            }

            System.out.println("Datos cargados desde " + archivo);
        } catch (IOException e) {
            System.out.println("Error al cargar CSV: " + e.getMessage());
        } finally {
            if (br != null) {
                try { br.close(); } catch (IOException ignored) {}
            }
        }

        return empresa;
    }
    // ===== Guardar Empresa en CSV =====
    public static void guardarCSV(Empresa empresa, String archivo) {
        
        String[] header = new String[]{
            "patente","capacidad","destino","nombrePasajero","rutPasajero","destinoPasajero",
            "precio","costoViaje","montoGenerado"
        };

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            // cabecera
            pw.println(String.join(",", header));

            ArrayList buses = empresa.getBuses();
            for (int i = 0; i < buses.size(); i++) {
                Bus bus = (Bus) buses.get(i);
                ArrayList pax = bus.getListaPasajeros();
                int capacidadTotal = bus.getCapacidadTotal();
                // Si tiene pasajeros: una fila por pasajero
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
                    // Sin pasajeros: escribimos fila “placeholder” para no perder el bus
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
            System.out.println("Datos guardados en " + archivo);
        } catch (IOException e) {
            System.out.println("Error al guardar CSV: " + e.getMessage());
        }
    }
    private static String safe(String s) {
    if (s == null) return "";
    // Evita comas crudas en CSV si no estás usando comillas
    return s.replace(",", " ");
}

    // ===== Puente: lanzar la ventana principal =====
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}
