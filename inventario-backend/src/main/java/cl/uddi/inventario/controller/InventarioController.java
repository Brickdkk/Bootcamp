package cl.uddi.inventario.controller;

import cl.uddi.inventario.domain.Movimiento;
import cl.uddi.inventario.domain.Producto;
import cl.uddi.inventario.service.InventarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") 
public class InventarioController {

    private final InventarioService service;

    public InventarioController(InventarioService service) {
        this.service = service;
    }

    @GetMapping("/productos")
    public List<Producto> obtenerProductos() {
        return service.listarProductos();
    }

    // NUEVO: Endpoint para borrar producto
    @DeleteMapping("/productos/{sku}")
    public ResponseEntity<String> eliminarProducto(@PathVariable int sku) {
        try {
            service.eliminarProducto(sku);
            return ResponseEntity.ok("Producto eliminado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // NUEVO: Endpoint para agregar producto
    @PostMapping("/productos")
    public ResponseEntity<String> agregarProducto(@RequestBody Producto p) {
        try {
            service.agregarProducto(p);
            return ResponseEntity.ok("Producto agregado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        if ("admin".equalsIgnoreCase(request.username) && "admin123".equals(request.password)) {
            return ResponseEntity.ok("LOGIN_EXITOSO");
        }
        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }

    @PostMapping("/venta")
    public ResponseEntity<String> registrarVenta(@RequestBody VentaRequest request) {
        try {
            service.registrarVenta(request.rut, request.sku, request.cantidad, "BOLETA-API", "Venta desde WPF");
            return ResponseEntity.ok("Venta OK");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // NUEVO: Endpoint para Modificar Stock (Compra)
    @PostMapping("/compra")
    public ResponseEntity<String> registrarCompra(@RequestBody CompraRequest request) {
        try {
            // Usamos "admin" como rut genérico ya que la ventana de modificar stock no pide rut
            service.registrarCompra("admin", request.sku, request.cantidad, request.precio, "FACTURA-API", "Compra desde WPF");
            return ResponseEntity.ok("Stock modificado");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // NUEVO: Endpoint para el Historial
    @GetMapping("/movimientos")
    public List<Movimiento> obtenerMovimientos() {
        return service.listarMovimientos();
    }

    // CLASES DTO (Para recibir JSON)
    static class LoginRequest {
        public String username;
        public String password;
    }

    static class VentaRequest {
        public String rut;
        public int sku;
        public int cantidad;
    }
    
    static class CompraRequest {
        public int sku;
        public int cantidad;
        public int precio;
    }
}