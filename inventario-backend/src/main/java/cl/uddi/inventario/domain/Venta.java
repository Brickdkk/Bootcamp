package cl.uddi.inventario.domain;

public class Venta extends Movimiento {
    public Venta(int sku, int cantidad, int precioUnitario, String documento, String nota, String rutUsuario) {
        super(TipoMovimiento.VENTA, sku, cantidad, precioUnitario, documento, nota, rutUsuario);
    }

    @Override
    public int aplicar(int stockActual) {
        return stockActual - cantidad;
    }
}
