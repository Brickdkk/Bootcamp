package cl.uddi.inventario.domain;

public class Venta extends Movimiento {
    public Venta(int sku, int cantidad, int precioUnit, String doc, String nota, String rut) {
        super(MovementType.VENTA, sku, cantidad, precioUnit, doc, nota, rut);
    }
    @Override public int aplicar(int stockActual) { return stockActual - cantidad; }
}

