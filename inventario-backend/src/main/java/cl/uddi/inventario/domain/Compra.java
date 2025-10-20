package cl.uddi.inventario.domain;

public class Compra extends Movimiento {
    public Compra(int sku, int cantidad, int precioUnit, String doc, String nota, String rut) {
        super(MovementType.COMPRA, sku, cantidad, precioUnit, doc, nota, rut);
    }
    @Override public int aplicar(int stockActual) { return stockActual + cantidad; }
}

