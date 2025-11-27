package cl.uddi.inventario.config;

import cl.uddi.inventario.repo.*;
import cl.uddi.inventario.service.InventarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    // 1. Enseñamos a Spring a crear el Repositorio de Productos
    @Bean
    public RepositorioProductos repositorioProductos() {
        return new TxtProductosRepo("productos.txt");
    }

    // 2. Enseñamos a Spring a crear el Repositorio de Movimientos
    @Bean
    public RepositorioMovimientos repositorioMovimientos() {
        return new TxtMovimientosRepo("movimientos.txt");
    }

    // 3. Enseñamos a Spring a crear el Servicio (¡Este es el que te falta!)
    @Bean
    public InventarioService inventarioService(RepositorioProductos repoProd, RepositorioMovimientos repoMov) {
        return new InventarioService(repoProd, repoMov);
    }
}