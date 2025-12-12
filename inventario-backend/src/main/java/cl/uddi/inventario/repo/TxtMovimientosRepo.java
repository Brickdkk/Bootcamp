package cl.uddi.inventario.repo;

import cl.uddi.inventario.domain.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class TxtMovimientosRepo implements RepositorioMovimientos {
    private final Path rutaArchivo;
    private final List<Movimiento> cache = new ArrayList<>();

    public TxtMovimientosRepo(String nombreArchivo) {
        this.rutaArchivo = Paths.get(nombreArchivo);
        cargar();
    }

    private void cargar() {
        cache.clear();
        if (!Files.exists(rutaArchivo)) return;
        
        try (BufferedReader br = Files.newBufferedReader(rutaArchivo)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) continue;
                String[] campos = linea.split("\\|", -1);
                // Formato esperado: TIPO|SKU|CANTIDAD|PRECIO|DOCUMENTO|NOTA|RUT
                if (campos.length < 7) continue;

                String tipo = campos[0];
                int sku = Integer.parseInt(campos[1]);
                int cant = Integer.parseInt(campos[2]);
                int precio = Integer.parseInt(campos[3]);
                String doc = campos[4];
                String nota = campos[5];
                String rut = campos[6];

                Movimiento m;
                if ("VENTA".equalsIgnoreCase(tipo)) {
                    m = new Venta(sku, cant, precio, doc, nota, rut);
                } else {
                    m = new Compra(sku, cant, precio, doc, nota, rut);
                }
                cache.add(m);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar movimientos", e);
        }
    }

    private void guardar() {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(rutaArchivo))) {
            for (Movimiento m : cache) {
                String tipo = (m instanceof Venta) ? "VENTA" : "COMPRA";
                // Asumimos que los campos son públicos o tienen getters, 
                // ajustamos a acceso directo por ser del mismo paquete/diseño simple
                pw.printf("%s|%d|%d|%d|%s|%s|%s%n",
                        tipo,
                        m.sku,
                        m.cantidad,
                        m.precioUnitario,
                        m.documento,
                        m.nota,
                        m.rutUsuario
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar movimientos", e);
        }
    }

    @Override
    public void agregar(Movimiento movimiento) {
        cache.add(movimiento);
        guardar();
    }

    @Override
    public List<Movimiento> listarPorSku(int sku) {
        List<Movimiento> resultado = new ArrayList<>();
        for (Movimiento m : cache) {
            if (m.sku == sku) {
                resultado.add(m);
            }
        }
        return resultado;
    }

    // ESTE ES EL MÉTODO QUE FALTABA Y QUE SOLUCIONA TU ERROR
    @Override
    public List<Movimiento> listar() {
        return new ArrayList<>(cache);
    }
}