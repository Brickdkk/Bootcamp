package cl.uddi.inventario.app;

import cl.uddi.inventario.domain.Producto;
import cl.uddi.inventario.service.InventarioService;
import java.util.Scanner;

public class MenuAdmin {
    private final InventarioService inventarioService;
    private final String rut;

    public MenuAdmin(InventarioService inventarioService, String rut) {
        this.inventarioService = inventarioService;
        this.rut = rut;
    }

    public void ejecutar(Scanner scanner) {
        while (true) {
            System.out.println("\n== ADMIN ==");
            System.out.println("1) Agregar producto");
            System.out.println("2) Inactivar producto");
            System.out.println("3) Reactivar producto");
            System.out.println("4) Eliminar producto");
            System.out.println("9) Ir a menú cliente");
            System.out.println("0) Salir");
            System.out.print("> ");
            String opcion = scanner.nextLine().trim();
            try {
                switch (opcion) {
                    case "1" -> {
                        Producto nuevoProducto = new Producto();
                        System.out.print("SKU: ");
                        nuevoProducto.sku = Integer.parseInt(scanner.nextLine());
                        System.out.print("Nombre: ");
                        nuevoProducto.nombre = scanner.nextLine();
                        System.out.print("Categoría: ");
                        nuevoProducto.categoria = scanner.nextLine();
                        System.out.print("Precio: ");
                        nuevoProducto.precio = Integer.parseInt(scanner.nextLine());
                        System.out.print("Stock: ");
                        nuevoProducto.stock = Integer.parseInt(scanner.nextLine());
                        System.out.print("Stock mínimo: ");
                        nuevoProducto.stockMinimo = Integer.parseInt(scanner.nextLine());
                        nuevoProducto.estado = "ACTIVE";
                        inventarioService.agregarProducto(nuevoProducto);
                        System.out.println("Producto agregado correctamente.");
                    }
                    case "2" -> {
                        System.out.print("SKU: ");
                        int sku = Integer.parseInt(scanner.nextLine());
                        inventarioService.inactivarProducto(sku);
                        System.out.println("Producto inactivado correctamente.");
                    }
                    case "3" -> {
                        System.out.print("SKU: ");
                        int sku = Integer.parseInt(scanner.nextLine());
                        inventarioService.reactivarProducto(sku);
                        System.out.println("Producto reactivado correctamente.");
                    }
                    case "4" -> {
                        System.out.print("SKU: ");
                        int sku = Integer.parseInt(scanner.nextLine());
                        inventarioService.eliminarProducto(sku);
                        System.out.println("Producto eliminado correctamente.");
                    }
                    case "9" -> new MenuCliente(inventarioService, rut).ejecutar(scanner);
                    case "0" -> {
                        return;
                    }
                    default -> System.out.println("Opción inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
