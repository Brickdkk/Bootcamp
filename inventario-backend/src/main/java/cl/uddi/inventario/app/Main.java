package cl.uddi.inventario.app;

import cl.uddi.inventario.repo.*;
import cl.uddi.inventario.service.InventarioService;
import cl.uddi.inventario.util.RutValidator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var sc = new Scanner(System.in);

        // 1) Preguntar nombres de archivos
        System.out.print("Nombre del archivo de productos (ej: productos.txt): ");
        String fileProductos = sc.nextLine().trim();
        if (fileProductos.isBlank()) fileProductos = "productos.txt";

        System.out.print("Nombre del archivo de movimientos (ej: movimientos.txt, este archvio sera creado): ");
        String fileMovs = sc.nextLine().trim();
        if (fileMovs.isBlank()) fileMovs = "movimientos.txt";

        // 2) Crear repos a partir de esos nombres
        RepositorioProductos repoProd = new TxtProductosRepo(fileProductos);
        RepositorioMovimientos repoMov = new TxtMovimientosRepo(fileMovs);
        var svc = new InventarioService(repoProd, repoMov);

        // 3) Login simple (rol + RUT)
        System.out.println("Rol: 1) Cliente  2) Admin");
        String opRol = sc.nextLine().trim();
        Role role = "2".equals(opRol) ? Role.ADMIN : Role.CLIENTE;

        System.out.print("RUT: ");
        String rut = sc.nextLine().trim();
        if (!RutValidator.esValido(rut)) { System.out.println("RUT inválido"); return; }
        rut = RutValidator.normalizar(rut);

        // 4) Menú por rol
        if (role == Role.CLIENTE) new MenuCliente(svc, rut).run(sc);
        else                      new MenuAdmin(svc, rut).run(sc);
    }
}


