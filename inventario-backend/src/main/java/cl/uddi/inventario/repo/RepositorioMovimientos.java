package cl.uddi.inventario.repo;

import cl.uddi.inventario.domain.Movimiento;
import java.util.*;

public interface RepositorioMovimientos {
    void append(Movimiento m);
    List<Movimiento> listarPorSku(int sku);
}

