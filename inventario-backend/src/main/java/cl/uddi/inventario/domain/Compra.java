package cl.uddi.inventario.domain;

public class Compra extends Movimiento {
    public Compra(int sku, int cantidad, int precioUnitario, String documento, String nota, String rutUsuario) {
        super(TipoMovimiento.COMPRA, sku, cantidad, precioUnitario, documento, nota, rutUsuario);
    }

    @Override
    public int aplicar(int stockActual) {
        return stockActual + cantidad;
    }
}
