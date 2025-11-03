package cl.uddi.inventario.repo;

import cl.uddi.inventario.domain.Producto;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TxtProductosRepo implements RepositorioProductos {
    private final Path rutaArchivo;
    private List<Producto> cache = new ArrayList<>();

    public TxtProductosRepo(String nombreArchivo) {
        this.rutaArchivo = Paths.get(nombreArchivo);
        cargar();
    }

    private void cargar() {
        cache.clear();
        if (!Files.exists(rutaArchivo)) return;
        try (BufferedReader br = Files.newBufferedReader(rutaArchivo)) {
            for (String linea; (linea = br.readLine()) != null; ) {
                if (linea.isBlank()) continue;
                String[] campos = linea.split("\\|", -1);
                if (campos.length < 7) continue; // SKU|NOMBRE|CATEGORIA|PRECIO|STOCK|STOCK_MIN|ESTADO
                Producto p = new Producto();
                p.sku = Integer.parseInt(campos[0]);
                p.nombre = campos[1];
                p.categoria = campos[2];
                p.precio = Integer.parseInt(campos[3]);
                p.stock = Integer.parseInt(campos[4]);
                p.stockMinimo = Integer.parseInt(campos[5]);
                p.estado = campos[6];
                cache.add(p);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    

    private void guardar() {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(rutaArchivo))) {
            for (Producto p : cache) {
                pw.printf("%d|%s|%s|%d|%d|%d|%s%n",
                        p.sku, p.nombre, p.categoria, p.precio, p.stock, p.stockMinimo, p.estado);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Producto> listar() {
        return new ArrayList<>(cache);
    }

    @Override
    public Optional<Producto> buscarPorSku(int sku) {
        return cache.stream().filter(p -> p.sku == sku).findFirst();
    }

    @Override
    public void guardarTodos(List<Producto> productos) {
        cache = new ArrayList<>(productos);
        guardar();
    }

    @Override
    public void actualizarOInsertar(Producto producto) {
        buscarPorSku(producto.sku).ifPresentOrElse(
                antiguo -> {
                    for (int i = 0; i < cache.size(); i++) {
                        if (cache.get(i).sku == producto.sku) {
                            cache.set(i, producto);
                            break;
                        }
                    }
                },
                () -> cache.add(producto)
        );
        guardar();
    }

    @Override
    public void eliminarPorSku(int sku) {
        cache.removeIf(p -> p.sku == sku);
        guardar();
    }
}
