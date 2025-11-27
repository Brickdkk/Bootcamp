package cl.uddi.inventario.controller;

import cl.uddi.inventario.domain.Producto;
import cl.uddi.inventario.service.InventarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Permite que tu aplicación WPF (C#) se conecte sin bloqueos
public class InventarioController {

    private final InventarioService service;

    // Inyección de dependencias del Servicio
    public InventarioController(InventarioService service) {
        this.service = service;
    }

    // ==========================================
    // 1. ENDPOINT DE PRODUCTOS (Para el Dashboard)
    // URL: GET http://localhost:8080/api/productos
    // ==========================================
    @GetMapping("/productos")
    public List<Producto> obtenerProductos() {
        return service.listarProductos();
    }

    // ==========================================
    // 2. ENDPOINT DE LOGIN (Para LoginWindow)
    // URL: POST http://localhost:8080/api/login
    // ==========================================
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        // Aquí simulamos la validación.
        // Si el usuario es "admin" y la clave "admin123", retornamos éxito (200 OK).
        if ("admin".equalsIgnoreCase(request.username) && "admin123".equals(request.password)) {
            return ResponseEntity.ok("LOGIN_EXITOSO");
        }
        
        // Si no, retornamos error 401 (Unauthorized)
        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }

    // ==========================================
    // 3. ENDPOINT DE VENTA (Para futura funcionalidad)
    // URL: POST http://localhost:8080/api/venta
    // ==========================================
    @PostMapping("/venta")
    public ResponseEntity<String> registrarVenta(@RequestBody VentaRequest request) {
        try {
            // Llamamos a tu lógica original de Java para descontar stock
            service.registrarVenta(
                request.rut, 
                request.sku, 
                request.cantidad, 
                "BOLETA-API", 
                "Venta desde WPF"
            );
            return ResponseEntity.ok("Venta realizada con éxito");
        } catch (RuntimeException e) {
            // Si falta stock o el producto no existe, devolvemos el error
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // ==========================================
    // CLASES AUXILIARES (DTOs)
    // Sirven para recibir los datos JSON que envía C#
    // ==========================================
    
    // Estructura de datos para el Login
    static class LoginRequest {
        public String username;
        public String password;
    }

    // Estructura de datos para la Venta
    static class VentaRequest {
        public String rut;
        public int sku;
        public int cantidad;
    }
}