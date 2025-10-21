package cl.uddi.inventario.app;

public class Sesion {
    public final String rut;
    public final Rol rol;

    public Sesion(String rut, Rol rol) {
        this.rut = rut;
        this.rol = rol;
    }
}
