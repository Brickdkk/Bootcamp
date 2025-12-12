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
    
    // NUEVO: Para el historial
    public List<Movimiento> listarMovimientos() {
        return repositorioMovimientos.listar();
    }

    public Optional<Producto> buscarProductoPorSku(int sku) {
        return repositorioProductos.buscarPorSku(sku);
    }

    public List<Producto> obtenerAlertasBajoStock() {
        List<Producto> productosBajoStock = new ArrayList<>();
        List<Producto> todos = repositorioProductos.listar();
        for (Producto p : todos) {
            if (p.stock <= p.stockMinimo) {
                productosBajoStock.add(p);
            }
        }
        return productosBajoStock;
    }

    public void registrarVenta(String rutUsuario, int sku, int cantidad, String documento, String nota) {
        Optional<Producto> op = repositorioProductos.buscarPorSku(sku);
        if (op.isEmpty()) {
            throw new RuntimeException("SKU inexistente");
        }
        Producto producto = op.get();
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
        Optional<Producto> op = repositorioProductos.buscarPorSku(sku);
        if (op.isEmpty()) {
            throw new RuntimeException("SKU inexistente");
        }
        Producto producto = op.get();
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
        if (nuevoProducto.precio < 0) nuevoProducto.precio = 0;
        if (nuevoProducto.estado == null || nuevoProducto.estado.equals("")) {
            nuevoProducto.estado = "ACTIVE";
        }
        repositorioProductos.actualizarOInsertar(nuevoProducto);
    }

    public void inactivarProducto(int sku) {
        Optional<Producto> op = repositorioProductos.buscarPorSku(sku);
        if (op.isPresent()) {
            Producto p = op.get();
            p.estado = "INACTIVE";
            repositorioProductos.actualizarOInsertar(p);
        }
    }

    public void reactivarProducto(int sku) {
        Optional<Producto> op = repositorioProductos.buscarPorSku(sku);
        if (op.isPresent()) {
            Producto p = op.get();
            p.estado = "ACTIVE";
            repositorioProductos.actualizarOInsertar(p);
        }
    }

    public void eliminarProducto(int sku) {
        Optional<Producto> op = repositorioProductos.buscarPorSku(sku);
        if (op.isPresent()) {
            repositorioProductos.eliminarPorSku(sku);
        } else {
            throw new RuntimeException("No existe producto para eliminar");
        }
    }
}