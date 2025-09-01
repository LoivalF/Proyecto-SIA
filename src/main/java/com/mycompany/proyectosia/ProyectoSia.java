/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyectosia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
/**
 *
 * @author gueon felip pauli
 */
public class ProyectoSia {

    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        // SIA1.4: Datos iniciales
        Empresa empresa = new Empresa("TransPUCV");

        Bus b1 = new Bus("ABCD-11", 3, "Valparaiso", new ArrayList());
        Bus b2 = new Bus("XYZ-22", 2, "Santiago", new ArrayList());
        empresa.agregarBus(b1, "Valparaiso");
        empresa.agregarBus(b2, "Santiago");

        Pasajero p1 = new Pasajero("Juan Perez", "11.111.111-1", "Valparaiso");
        Pasajero p2 = new Pasajero("Ana Diaz", "22.222.222-2", "Santiago");
        empresa.asignarPasajeroABus(p1, "ABCD-11");
        empresa.asignarPasajeroABus(p2, "XYZ-22");

        // Menú SIA1.8 (todo por consola)
        while (true) {
            System.out.println("\n===== SISTEMA DE GESTIÓN DE BUSES Y PASAJEROS =====");
            System.out.println("1) Insertar PASAJERO en un BUS (manual)");
            System.out.println("2) Mostrar PASAJEROS de un BUS por patente");
            System.out.println("3) Agregar BUS nuevo");
            System.out.println("4) Listar BUSES por DESTINO");
            System.out.println("5) Listar TODOS los BUSES");
            System.out.println("6) Eliminar pasajero de un BUS por RUT");
            System.out.println("0) Salir");
            System.out.print("Opción: ");

            String op = in.readLine();
            if (op == null) break;

            switch (op.trim()) {
                case "1":
                    opcionInsertarPasajeroEnBus(empresa);
                    break;
                case "2":
                    opcionMostrarPasajerosDeBus(empresa);
                    break;
                case "3":
                    opcionAgregarBus(empresa);
                    break;
                case "4":
                    opcionListarBusesPorDestino(empresa);
                    break;
                case "5":
                    opcionListarTodosLosBuses(empresa);
                    break;
                case "6":
                    opcionEliminarPasajeroDeBus(empresa);
                    break;
                case "0":
                    System.out.println("Adiós 👋");
                    return;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    // ----- Opciones -----

    // SIA1.8 (1) Inserción manual en la 2ª colección (pasajeros dentro de bus)
    private static void opcionInsertarPasajeroEnBus(Empresa empresa) throws IOException {
        System.out.println("\n--- Insertar pasajero en bus ---");
        System.out.print("Patente del bus: ");
        String patente = in.readLine();

        // 1) Buscar el bus por patente (sin <> y con casteo)
        ArrayList buses = empresa.getBuses();
        Bus busEncontrado = null;
        for (int i = 0; i < buses.size(); i++) {
            Bus b = (Bus) buses.get(i);
            if (b.getPatente().equalsIgnoreCase(patente)) {
                busEncontrado = b;
                break;
            }
        }
        if (busEncontrado == null) {
            System.out.println("No se encontró el bus con patente " + patente);
            return;
        }

        System.out.print("Nombre pasajero: ");
        String nombre = in.readLine();

        System.out.print("RUT pasajero: ");
        String rut = in.readLine();

        System.out.print("Destino pasajero (debe coincidir con el bus): ");
        String destino = in.readLine();

        // 2) Verificar DUPLICADO por RUT en ese bus
        ArrayList lista = busEncontrado.getListaPasajeros(); // sin <>
        for (int j = 0; j < lista.size(); j++) {
            Pasajero existente = (Pasajero) lista.get(j);
            if (existente.getRut().equalsIgnoreCase(rut)) {
                System.out.println("Ya existe un pasajero con RUT " + rut + " en este bus.");
                return; // no seguimos
            }
        }

        // Validar destino aquí para avisar antes:
        if (!destino.equalsIgnoreCase(busEncontrado.getDestino())) {
            System.out.println("El destino del pasajero no coincide con el del bus (" 
                               + busEncontrado.getDestino() + ").");
            // puedes return; si quieres bloquear aquí. Si no, dejar que Empresa valide igual.
            // return;
        }

        // 3) Crear y asignar
        Pasajero p = new Pasajero(nombre, rut, destino);
        boolean ok = empresa.asignarPasajeroABus(p, patente);
        if (!ok) {
            System.out.println("No se pudo asignar el pasajero (capacidad, destino o duplicado).");
        }
    }


    // SIA1.8 (2) Mostrar listado de elementos de la 2ª colección (pasajeros del bus)
    private static void opcionMostrarPasajerosDeBus(Empresa empresa) throws IOException {
        System.out.println("\n--- Mostrar pasajeros de un bus ---");
        System.out.print("Patente del bus: ");
        String patente = in.readLine();
        empresa.mostrarPasajerosDeBus(patente);
    }

    private static void opcionAgregarBus(Empresa empresa) throws IOException {
        System.out.println("\n--- Agregar bus ---");
        System.out.print("Patente: ");
        String patente = in.readLine();

        int capacidad = leerEntero("Capacidad (cupos disponibles): ");

        System.out.print("Destino: ");
        String destino = in.readLine();

        Bus b = new Bus(patente, capacidad, destino, new ArrayList());
        empresa.agregarBus(b, destino);
    }

    private static void opcionListarBusesPorDestino(Empresa empresa) throws IOException {
        System.out.println("\n--- Listar buses por destino ---");
        System.out.print("Destino: ");
        String destino = in.readLine();
        empresa.obtenerBusesDestino(destino);
    }

    private static void opcionListarTodosLosBuses(Empresa empresa) {
        System.out.println("\n--- Lista de todos los buses ---");
        ArrayList buses = empresa.getBuses(); // sin <>
        if (buses.isEmpty()) {
            System.out.println("(no hay buses)");
            return;
        }
        for (int i = 0; i < buses.size(); i++) {
            Bus b = (Bus) buses.get(i); // casteo
            System.out.println("- Patente: " + b.getPatente()
                    + " | Destino: " + b.getDestino()
                    + " | Capacidad (disp): " + b.getCapacidad()
                    + " | Pasajeros: " + b.pasajerosActuales());
        }
    }

    private static void opcionEliminarPasajeroDeBus(Empresa empresa) throws IOException {
        System.out.println("\n--- Eliminar pasajero de un bus ---");
        System.out.print("Patente del bus: ");
        String patente = in.readLine();
        System.out.print("RUT del pasajero: ");
        String rut = in.readLine();

        // buscar bus y eliminar pasajero por RUT desde el bus
        ArrayList buses = empresa.getBuses();
        for (int i = 0; i < buses.size(); i++) {
            Bus b = (Bus) buses.get(i);
            if (b.getPatente().equalsIgnoreCase(patente)) {
                b.eliminarPasajero(rut);
                return;
            }
        }
        System.out.println("No se encontró el bus con patente " + patente);
    }

    // ----- Utilidades -----
    private static int leerEntero(String prompt) throws IOException {
        System.out.print(prompt);
        String s = in.readLine();
        return Integer.parseInt(s.trim()); // si el usuario no pone un número, el programa se cae
    }

}
