package cl.uddi.inventario.repo;

import cl.uddi.inventario.domain.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TxtMovimientosRepo implements RepositorioMovimientos {
    private final Path rutaArchivo;

    public TxtMovimientosRepo(String nombreArchivo) {
        this.rutaArchivo = Paths.get(nombreArchivo);
    }

    @Override
    public void agregar(Movimiento movimiento) {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                rutaArchivo, StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            // FECHA_ISO|TIPO|SKU|CANTIDAD|PRECIO_UNIT|DOC|NOTA|RUT
            pw.printf("%s|%s|%d|%d|%d|%s|%s|%s%n",
                    movimiento.fecha.toString(), movimiento.tipo.name(), movimiento.sku, movimiento.cantidad, movimiento.precioUnitario,
                    movimiento.documento == null ? "" : movimiento.documento, movimiento.nota == null ? "" : movimiento.nota, movimiento.rutUsuario);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Movimiento> listarPorSku(int sku) {
        if (!Files.exists(rutaArchivo)) return List.of();
        List<Movimiento> movimientos = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(rutaArchivo)) {
            for (String linea; (linea = br.readLine()) != null; ) {
                String[] campos = linea.split("\\|", -1);
                if (campos.length < 8 || Integer.parseInt(campos[2]) != sku) continue;
                TipoMovimiento tipo = TipoMovimiento.valueOf(campos[1]);
                int cantidad = Integer.parseInt(campos[3]);
                int precioUnitario = Integer.parseInt(campos[4]);
                String documento = campos[5];
                String nota = campos[6];
                String rut = campos[7];
                Movimiento m = switch (tipo) {
                    case VENTA -> new Venta(sku, cantidad, precioUnitario, documento, nota, rut);
                    case COMPRA -> new Compra(sku, cantidad, precioUnitario, documento, nota, rut);
                };
                try {
                    m.fecha = Instant.parse(campos[0]);
                } catch (Exception ignored) {
                }
                movimientos.add(m);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return movimientos;
    }
}
