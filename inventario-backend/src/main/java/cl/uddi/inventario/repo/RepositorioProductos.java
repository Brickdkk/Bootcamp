package cl.uddi.inventario.repo;

import cl.uddi.inventario.domain.Producto;
import java.util.List;
import java.util.Optional;

public interface RepositorioProductos {
    List<Producto> listar();
    Optional<Producto> buscarPorSku(int sku);
    void guardarTodos(List<Producto> productos);
    void actualizarOInsertar(Producto producto);
    void eliminarPorSku(int sku);
}
