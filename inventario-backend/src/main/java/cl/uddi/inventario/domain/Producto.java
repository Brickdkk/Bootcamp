package cl.uddi.inventario.domain;

public class Producto {
    public int sku;
    public String nombre;
    public String categoria;
    public int precio;
    public int stock;
    public int stockMinimo;
    public String estado; // ACTIVE | INACTIVE

    public boolean estaActivo() {
        return "ACTIVE".equalsIgnoreCase(estado);
    }

    @Override
    public String toString() {
        return sku + " | " + nombre + " | " + categoria + " | $" + precio + " | stock=" + stock + "/" + stockMinimo + " | " + estado;
    }
}
