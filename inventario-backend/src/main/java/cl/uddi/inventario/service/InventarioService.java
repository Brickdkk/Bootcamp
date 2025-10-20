package cl.uddi.inventario.service;

import cl.uddi.inventario.domain.*;
import cl.uddi.inventario.repo.*;
import java.util.*;

public class InventarioService {
    private final RepositorioProductos productos;
    private final RepositorioMovimientos movimientos;

    public InventarioService(RepositorioProductos productos, RepositorioMovimientos movimientos) {
        this.productos = productos; this.movimientos = movimientos;
    }

    public List<Producto> listar() { return productos.listar(); }
    public Optional<Producto> buscarSku(int sku) { return productos.porSku(sku); }
    public List<Producto> alertasBajoStock() {
        List<Producto> out = new ArrayList<>();
        for (var p : productos.listar()) if (p.stock <= p.stockMin) out.add(p);
        return out;
    }

    public void venta(String rut, int sku, int cantidad, String doc, String nota) {
        var p = productos.porSku(sku).orElseThrow(() -> new RuntimeException("SKU inexistente"));
        if (!p.activo()) throw new RuntimeException("Producto INACTIVE");
        if (cantidad <= 0) throw new RuntimeException("Cantidad inválida");
        if (p.stock < cantidad) throw new RuntimeException("Stock insuficiente");

        Movimiento mv = new Venta(sku, cantidad, p.precio, doc, nota, rut);
        p.stock = mv.aplicar(p.stock);
        productos.upsert(p);
        movimientos.append(mv);
    }

    public void compra(String rut, int sku, int cantidad, int precioUnit, String doc, String nota) {
        var p = productos.porSku(sku).orElseThrow(() -> new RuntimeException("SKU inexistente"));
        if (cantidad <= 0 || precioUnit < 0) throw new RuntimeException("Valores inválidos");

        Movimiento mv = new Compra(sku, cantidad, precioUnit, doc, nota, rut);
        p.stock = mv.aplicar(p.stock);
        productos.upsert(p);
        movimientos.append(mv);
    }

    public void agregarProducto(Producto nuevo) {
        if (productos.porSku(nuevo.sku).isPresent()) throw new RuntimeException("SKU ya existe");
        if (nuevo.precio < 0 || nuevo.stock < 0 || nuevo.stockMin < 0) throw new RuntimeException("Valores inválidos");
        if (nuevo.estado == null || nuevo.estado.isBlank()) nuevo.estado = "ACTIVE";
        productos.upsert(nuevo);
    }

    public void inactivar(int sku) {
        var p = productos.porSku(sku).orElseThrow(() -> new RuntimeException("SKU inexistente"));
        p.estado = "INACTIVE"; productos.upsert(p);
    }

    public void reactivar(int sku) {
        var p = productos.porSku(sku).orElseThrow(() -> new RuntimeException("SKU inexistente"));
        p.estado = "ACTIVE"; productos.upsert(p);
    }
    
    public void eliminar(int sku) {
    var p = productos.porSku(sku).orElseThrow(() -> new RuntimeException("SKU inexistente"));
    if (p.stock > 0) throw new RuntimeException("No se puede eliminar: stock > 0");
    productos.eliminarPorSku(sku);
    }

}

