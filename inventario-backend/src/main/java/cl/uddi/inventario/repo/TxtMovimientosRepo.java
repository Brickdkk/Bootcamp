package cl.uddi.inventario.repo;

import cl.uddi.inventario.domain.*;
import java.io.*; import java.nio.file.*; import java.time.Instant; import java.util.*;

public class TxtMovimientosRepo implements RepositorioMovimientos {
    private final Path path;

    public TxtMovimientosRepo(String fileName) {
        this.path = Paths.get(fileName);
    }

    @Override public void append(Movimiento m) {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                path, StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            // FECHA_ISO|TIPO|SKU|CANTIDAD|PRECIO_UNIT|DOC|NOTA|RUT
            pw.printf("%s|%s|%d|%d|%d|%s|%s|%s%n",
                    m.fecha.toString(), m.tipo.name(), m.sku, m.cantidad, m.precioUnit,
                    m.doc == null ? "" : m.doc, m.nota == null ? "" : m.nota, m.rutUsuario);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    @Override public List<Movimiento> listarPorSku(int sku) {
        if (!Files.exists(path)) return List.of();
        List<Movimiento> out = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(path)) {
            for (String line; (line = br.readLine()) != null; ) {
                String[] t = line.split("\\|", -1);
                if (t.length < 8 || Integer.parseInt(t[2]) != sku) continue;
                MovementType tipo = MovementType.valueOf(t[1]);
                int cant = Integer.parseInt(t[3]);
                int pu   = Integer.parseInt(t[4]);
                String doc = t[5], nota = t[6], rut = t[7];
                Movimiento m = switch (tipo) {
                    case VENTA  -> new Venta(sku, cant, pu, doc, nota, rut);
                    case COMPRA -> new Compra(sku, cant, pu, doc, nota, rut);
                    default     -> new Compra(sku, 0, 0, "", "NOOP", rut); // para no romper
                };
                try { m.fecha = Instant.parse(t[0]); } catch (Exception ignored) {}
                out.add(m);
            }
        } catch (IOException e) { throw new RuntimeException(e); }
        return out;
    }
}

