package cl.uddi.inventario.service;

import cl.uddi.inventario.domain.*;
import cl.uddi.inventario.repo.RepositorioMovimientos;
import cl.uddi.inventario.repo.RepositorioProductos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InventarioService {
    private final RepositorioProductos repositorioProductos;
    private final RepositorioMovimientos repositorioMovimientos;

    public InventarioService(RepositorioProductos repositorioProductos, RepositorioMovimientos repositorioMovimientos) {
        this.repositorioProductos = repositorioProductos;
        this.repositorioMovimientos = repositorioMovimientos;
    }

    public List<Producto> listarProductos() {
        return repositorioProductos.listar();
    }

    public Optional<Producto> buscarProductoPorSku(int sku) {
        return repositorioProductos.buscarPorSku(sku);
    }

    public List<Producto> obtenerAlertasBajoStock() {
        List<Producto> productosBajoStock = new ArrayList<>();
        for (var producto : repositorioProductos.listar()) {
            if (producto.stock <= producto.stockMinimo) {
                productosBajoStock.add(producto);
            }
        }
        return productosBajoStock;
    }

    public void registrarVenta(String rutUsuario, int sku, int cantidad, String documento, String nota) {
        Producto producto = repositorioProductos.buscarPorSku(sku)
                .orElseThrow(() -> new RuntimeException("SKU inexistente"));
        if (!producto.estaActivo()) {
            throw new RuntimeException("Producto INACTIVO");
        }
        if (cantidad <= 0) {
            throw new RuntimeException("Cantidad inválida");
        }
        if (producto.stock < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }

        Movimiento venta = new Venta(sku, cantidad, producto.precio, documento, nota, rutUsuario);
        producto.stock = venta.aplicar(producto.stock);
        repositorioProductos.actualizarOInsertar(producto);
        repositorioMovimientos.agregar(venta);
    }

    public void registrarCompra(String rutUsuario, int sku, int cantidad, int precioUnitario, String documento, String nota) {
        Producto producto = repositorioProductos.buscarPorSku(sku)
                .orElseThrow(() -> new RuntimeException("SKU inexistente"));
        if (cantidad <= 0 || precioUnitario < 0) {
            throw new RuntimeException("Valores inválidos");
        }

        Movimiento compra = new Compra(sku, cantidad, precioUnitario, documento, nota, rutUsuario);
        producto.stock = compra.aplicar(producto.stock);
        repositorioProductos.actualizarOInsertar(producto);
        repositorioMovimientos.agregar(compra);
    }

    public void agregarProducto(Producto nuevoProducto) {
        if (repositorioProductos.buscarPorSku(nuevoProducto.sku).isPresent()) {
            throw new RuntimeException("SKU ya existe");
        }
        if (nuevoProducto.precio < 0 || nuevoProducto.stock < 0 || nuevoProducto.stockMinimo < 0) {
            throw new RuntimeException("Valores inválidos");
        }
        if (nuevoProducto.estado == null || nuevoProducto.estado.isBlank()) {
            nuevoProducto.estado = "ACTIVE";
        }
        repositorioProductos.actualizarOInsertar(nuevoProducto);
    }

    public void inactivarProducto(int sku) {
        Producto producto = repositorioProductos.buscarPorSku(sku)
                .orElseThrow(() -> new RuntimeException("SKU inexistente"));
        producto.estado = "INACTIVE";
        repositorioProductos.actualizarOInsertar(producto);
    }

    public void reactivarProducto(int sku) {
        Producto producto = repositorioProductos.buscarPorSku(sku)
                .orElseThrow(() -> new RuntimeException("SKU inexistente"));
        producto.estado = "ACTIVE";
        repositorioProductos.actualizarOInsertar(producto);
    }

    public void eliminarProducto(int sku) {
        Producto producto = repositorioProductos.buscarPorSku(sku)
                .orElseThrow(() -> new RuntimeException("SKU inexistente"));
        if (producto.stock > 0) {
            throw new RuntimeException("No se puede eliminar: stock > 0");
        }
        repositorioProductos.eliminarPorSku(sku);
    }
}
