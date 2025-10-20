package cl.uddi.inventario.app;

import cl.uddi.inventario.domain.Producto;
import cl.uddi.inventario.service.InventarioService;
import java.util.Scanner;

public class MenuAdmin {
    private final InventarioService svc;
    private final String rut;
    public MenuAdmin(InventarioService svc, String rut){ this.svc=svc; this.rut=rut; }

    public void run(Scanner sc){
        while(true){
            System.out.println("\n== ADMIN ==");
            System.out.println("1) Agregar producto");
            System.out.println("2) Inactivar producto");
            System.out.println("3) Reactivar producto");
            System.out.println("4) Eliminar producto");
            System.out.println("9) Ir a menú cliente");
            System.out.println("0) Salir");
            System.out.print("> ");
            String op = sc.nextLine().trim();
            try{
                switch(op){
                    case "1" -> {
                        Producto p = new Producto();
                        System.out.print("SKU: "); p.sku = Integer.parseInt(sc.nextLine());
                        System.out.print("Nombre: "); p.nombre = sc.nextLine();
                        System.out.print("Categoría: "); p.categoria = sc.nextLine();
                        System.out.print("Precio: "); p.precio = Integer.parseInt(sc.nextLine());
                        System.out.print("Stock: "); p.stock = Integer.parseInt(sc.nextLine());
                        System.out.print("Stock mínimo: "); p.stockMin = Integer.parseInt(sc.nextLine());
                        p.estado = "ACTIVE";
                        svc.agregarProducto(p);
                        System.out.println("Producto agregado.");
                    }
                    case "2" -> { System.out.print("SKU: "); int sku = Integer.parseInt(sc.nextLine()); svc.inactivar(sku); System.out.println("Inactivado."); }
                    case "3" -> { System.out.print("SKU: "); int sku = Integer.parseInt(sc.nextLine()); svc.reactivar(sku); System.out.println("Reactivado."); }
                    case "4" -> { System.out.print("SKU: "); int sku = Integer.parseInt(sc.nextLine()); svc.eliminar(sku); System.out.println("Eliminado.");}
                    case "9" -> new MenuCliente(svc, rut).run(sc);
                    case "0" -> { return; }
                    default -> System.out.println("Opción inválida");
                }
            }catch(NumberFormatException e){ System.out.println("Error: "+e.getMessage()); }
        }
    }
}

