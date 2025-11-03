package cl.uddi.inventario.repo;

import cl.uddi.inventario.domain.Movimiento;
import java.util.List;

public interface RepositorioMovimientos {
    void agregar(Movimiento movimiento);
    List<Movimiento> listarPorSku(int sku);
}
