package cl.uddi.inventario.app;

import cl.uddi.inventario.service.InventarioService;
import java.util.Scanner;

public class MenuCliente {
    private final InventarioService svc;
    private final String rut;
    public MenuCliente(InventarioService svc, String rut){ this.svc=svc; this.rut=rut; }

    public void run(Scanner sc){
        while(true){
            System.out.println("\n== CLIENTE ==");
            System.out.println("1) Listar");
            System.out.println("2) Buscar por SKU");
            System.out.println("3) Venta de un producto");
            System.out.println("4) Compra de un producto");
            System.out.println("5) Alertas bajo stock");
            System.out.println("0) Salir");
            System.out.print("> ");
            String op = sc.nextLine().trim();
            try{
                switch(op){
                    case "1" -> svc.listar().forEach(System.out::println);
                    case "2" -> {
                        System.out.print("SKU: "); int sku = Integer.parseInt(sc.nextLine());
                        svc.buscarSku(sku).ifPresentOrElse(System.out::println, ()-> System.out.println("No existe"));
                    }
                    case "3" -> {
                        System.out.print("SKU: "); int sku = Integer.parseInt(sc.nextLine());
                        System.out.print("Cantidad: "); int qty = Integer.parseInt(sc.nextLine());
                        svc.venta(rut, sku, qty, "TICKET", "venta mostrador");
                        System.out.println("Venta OK.");
                    }
                    case "4" -> {
                        System.out.print("SKU: "); int sku = Integer.parseInt(sc.nextLine());
                        System.out.print("Cantidad: "); int qty = Integer.parseInt(sc.nextLine());
                        System.out.print("Precio Unitario: "); int pu = Integer.parseInt(sc.nextLine());
                        svc.compra(rut, sku, qty, pu, "OC", "reposicion");
                        System.out.println("Compra OK.");
                    }
                    case "5" -> svc.alertasBajoStock().forEach(p ->
                        System.out.printf("ALERTA -> SKU %d (%s) stock=%d min=%d%n", p.sku, p.nombre, p.stock, p.stockMin));
                    case "0" -> { return; }
                    default -> System.out.println("Opción inválida");
                }
            }catch(NumberFormatException e){ System.out.println("Error: "+e.getMessage()); }
        }
    }
}

