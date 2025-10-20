package cl.uddi.inventario.repo;

import cl.uddi.inventario.domain.Producto;
import java.io.*; import java.nio.file.*; import java.util.*;

public class TxtProductosRepo implements RepositorioProductos {
    private final Path path;
    private List<Producto> cache = new ArrayList<>();

    public TxtProductosRepo(String fileName) {
        this.path = Paths.get(fileName);
        cargar();
    }

    private void cargar() {
        cache.clear();
        if (!Files.exists(path)) return;
        try (BufferedReader br = Files.newBufferedReader(path)) {
            for (String line; (line = br.readLine()) != null; ) {
                if (line.isBlank()) continue;
                String[] t = line.split("\\|", -1);
                if (t.length < 7) continue; // SKU|NOMBRE|CATEGORIA|PRECIO|STOCK|STOCK_MIN|ESTADO
                Producto p = new Producto();
                p.sku = Integer.parseInt(t[0]);
                p.nombre = t[1];
                p.categoria = t[2];
                p.precio = Integer.parseInt(t[3]);
                p.stock = Integer.parseInt(t[4]);
                p.stockMin = Integer.parseInt(t[5]);
                p.estado = t[6];
                cache.add(p);
            }
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    private void guardar() {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(path))) {
            for (Producto p : cache) {
                pw.printf("%d|%s|%s|%d|%d|%d|%s%n",
                        p.sku, p.nombre, p.categoria, p.precio, p.stock, p.stockMin, p.estado);
            }
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    @Override public List<Producto> listar() { return new ArrayList<>(cache); }

    @Override public Optional<Producto> porSku(int sku) {
        return cache.stream().filter(p -> p.sku == sku).findFirst();
    }

    @Override public void guardarTodos(List<Producto> items) {
        cache = new ArrayList<>(items);
        guardar();
    }

    @Override public void upsert(Producto p) {
        porSku(p.sku).ifPresentOrElse(
            old -> { for (int i = 0; i < cache.size(); i++) if (cache.get(i).sku == p.sku) { cache.set(i, p); break; } },
            () -> cache.add(p)
        );
        guardar();
    }
    @Override public void eliminarPorSku(int sku){ // ← NUEVO
        cache.removeIf(p -> p.sku == sku);
        guardar();
    }
}

