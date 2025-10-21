package cl.uddi.inventario.app;

import cl.uddi.inventario.repo.RepositorioMovimientos;
import cl.uddi.inventario.repo.RepositorioProductos;
import cl.uddi.inventario.repo.TxtMovimientosRepo;
import cl.uddi.inventario.repo.TxtProductosRepo;
import cl.uddi.inventario.service.InventarioService;
import cl.uddi.inventario.util.ValidadorRut;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);

        // 1) Preguntar nombres de archivos
        System.out.print("Nombre del archivo de productos (ej: productos.txt): ");
        String archivoProductos = scanner.nextLine().trim();
        if (archivoProductos.isBlank()) {
            archivoProductos = "productos.txt";
        }

        System.out.print("Nombre del archivo de movimientos (ej: movimientos.txt, este archivo será creado): ");
        String archivoMovimientos = scanner.nextLine().trim();
        if (archivoMovimientos.isBlank()) {
            archivoMovimientos = "movimientos.txt";
        }

        // 2) Crear repositorios a partir de esos nombres
        RepositorioProductos repositorioProductos = new TxtProductosRepo(archivoProductos);
        RepositorioMovimientos repositorioMovimientos = new TxtMovimientosRepo(archivoMovimientos);
        var inventarioService = new InventarioService(repositorioProductos, repositorioMovimientos);

        // 3) Login simple (rol + RUT)
        System.out.println("Rol: 1) Cliente  2) Admin");
        String opcionRol = scanner.nextLine().trim();
        Rol rol = "2".equals(opcionRol) ? Rol.ADMIN : Rol.CLIENTE;

        System.out.print("RUT: ");
        String rut = scanner.nextLine().trim();
        if (!ValidadorRut.esValido(rut)) {
            System.out.println("RUT inválido");
            return;
        }
        rut = ValidadorRut.normalizar(rut);

        // 4) Menú por rol
        if (rol == Rol.CLIENTE) {
            new MenuCliente(inventarioService, rut).ejecutar(scanner);
        } else {
            new MenuAdmin(inventarioService, rut).ejecutar(scanner);
        }
    }
}
