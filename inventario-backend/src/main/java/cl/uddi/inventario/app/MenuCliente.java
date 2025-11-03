package cl.uddi.inventario.app;

import cl.uddi.inventario.service.InventarioService;
import java.util.Scanner;

public class MenuCliente {
    private final InventarioService inventarioService;
    private final String rut;

    public MenuCliente(InventarioService inventarioService, String rut) {
        this.inventarioService = inventarioService;
        this.rut = rut;
    }

    public void ejecutar(Scanner scanner) {
        while (true) {
            System.out.println("\n== CLIENTE ==");
            System.out.println("1) Listar productos");
            System.out.println("2) Buscar producto por SKU");
            System.out.println("3) Registrar venta de un producto");
            System.out.println("4) Registrar compra de un producto");
            System.out.println("5) Ver alertas de bajo stock");
            System.out.println("0) Salir");
            System.out.print("> ");
            String opcion = scanner.nextLine().trim();
            try {
                switch (opcion) {
                    case "1" -> inventarioService.listarProductos().forEach(System.out::println);
                    case "2" -> {
                        System.out.print("SKU: ");
                        int sku = Integer.parseInt(scanner.nextLine());
                        inventarioService.buscarProductoPorSku(sku)
                                .ifPresentOrElse(System.out::println, () -> System.out.println("No existe un producto con ese SKU"));
                    }
                    case "3" -> {
                        System.out.print("SKU: ");
                        int sku = Integer.parseInt(scanner.nextLine());
                        System.out.print("Cantidad: ");
                        int cantidad = Integer.parseInt(scanner.nextLine());
                        inventarioService.registrarVenta(rut, sku, cantidad, "TICKET", "venta mostrador");
                        System.out.println("Venta registrada correctamente.");
                    }
                    case "4" -> {
                        System.out.print("SKU: ");
                        int sku = Integer.parseInt(scanner.nextLine());
                        System.out.print("Cantidad: ");
                        int cantidad = Integer.parseInt(scanner.nextLine());
                        System.out.print("Precio Unitario: ");
                        int precioUnitario = Integer.parseInt(scanner.nextLine());
                        inventarioService.registrarCompra(rut, sku, cantidad, precioUnitario, "OC", "reposicion");
                        System.out.println("Compra registrada correctamente.");
                    }
                    case "5" -> inventarioService.obtenerAlertasBajoStock().forEach(p ->
                            System.out.printf("ALERTA -> SKU %d (%s) stock=%d min=%d%n", p.sku, p.nombre, p.stock, p.stockMinimo));
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
