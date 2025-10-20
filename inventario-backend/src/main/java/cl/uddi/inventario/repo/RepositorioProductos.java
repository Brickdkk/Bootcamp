package cl.uddi.inventario.repo;

import cl.uddi.inventario.domain.Producto;
import java.util.*;

public interface RepositorioProductos {
    List<Producto> listar();
    Optional<Producto> porSku(int sku);
    void guardarTodos(List<Producto> items);
    void upsert(Producto p);
    void eliminarPorSku(int sku);
}
