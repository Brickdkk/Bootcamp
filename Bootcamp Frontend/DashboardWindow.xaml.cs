using System.Windows;
using System.Windows.Input;
using Microsoft.Win32;
using System.IO;
using System.Linq;
using Bootcamp_Frontend.Services;
using Microsoft.VisualBasic;

namespace Bootcamp_Frontend
{
    public partial class DashboardWindow : Window
    {
        private readonly ApiService _apiService = new ApiService();

        public DashboardWindow()
        {
            InitializeComponent();
            CargarTabla();
        }

        public async void CargarTabla()
        {
            var productos = await _apiService.ObtenerProductosAsync();
            ProductosDataGrid.ItemsSource = productos;
            MessageBox.Show($"Productos cargados: {productos.Count}");
        }

        private async void ImportarProductos_Click(object sender, RoutedEventArgs e)
        {
            OpenFileDialog dialog = new OpenFileDialog { Filter = "Archivos de texto (*.txt)|*.txt" };
            dialog.ShowDialog();
            
            var lineas = File.ReadAllLines(dialog.FileName);
            
            foreach (var linea in lineas)
            {
                var partes = linea.Split('|');
                var producto = new Models.Producto
                {
                    Sku = int.Parse(partes[0]),
                    Nombre = partes[1],
                    Stock = int.Parse(partes[2]),
                    Precio = double.Parse(partes[3])
                };
                await _apiService.AgregarProductoAsync(producto);
            }
            
            MessageBox.Show("Productos importados");
            CargarTabla();
        }

        private async void ImportarMovimientos_Click(object sender, RoutedEventArgs e)
        {
            OpenFileDialog dialog = new OpenFileDialog { Filter = "Archivos de texto (*.txt)|*.txt" };
            dialog.ShowDialog();
            
            var lineas = File.ReadAllLines(dialog.FileName);
            
            foreach (var linea in lineas)
            {
                var partes = linea.Split('|');
                string rut = partes[0];
                int sku = int.Parse(partes[1]);
                int cantidad = int.Parse(partes[2]);
                
                await _apiService.RegistrarVentaAsync(rut, sku, cantidad);
            }
            
            MessageBox.Show("Movimientos importados");
            CargarTabla();
        }

        private async void ExportarProductos_Click(object sender, RoutedEventArgs e)
        {
            SaveFileDialog dialog = new SaveFileDialog { Filter = "Archivos de texto (*.txt)|*.txt", FileName = "productos.txt" };
            dialog.ShowDialog();
            
            var productos = await _apiService.ObtenerProductosAsync();
            var lineas = productos.Select(p => $"{p.Sku}|{p.Nombre}|{p.Stock}|{p.Precio}");
            
            File.WriteAllText(dialog.FileName, string.Join("\n", lineas));
            MessageBox.Show("Productos exportados");
        }

        private async void ExportarMovimientos_Click(object sender, RoutedEventArgs e)
        {
            SaveFileDialog dialog = new SaveFileDialog { Filter = "Archivos de texto (*.txt)|*.txt", FileName = "movimientos.txt" };
            dialog.ShowDialog();
            
            var productos = await _apiService.ObtenerProductosAsync();
            var lineas = productos.Select(p => $"{p.Sku}|{p.Nombre}|{p.Stock}");
            
            File.WriteAllText(dialog.FileName, string.Join("\n", lineas));
            MessageBox.Show("Movimientos exportados");
        }

        private void AnadirProducto_Click(object sender, MouseButtonEventArgs e)
        {
            new AnadirProductoWindow(_apiService, this).ShowDialog();
        }

        private async void EliminarProducto_Click(object sender, MouseButtonEventArgs e)
        {
            string sku = Interaction.InputBox("Ingresa el SKU del producto a eliminar:", "Eliminar");
            await _apiService.EliminarProductoAsync(int.Parse(sku));
            MessageBox.Show("Producto eliminado");
            CargarTabla();
        }

        private async void ModificarStock_Click(object sender, MouseButtonEventArgs e)
        {
            string sku = Interaction.InputBox("SKU del producto:", "Modificar Stock");
            string cantidad = Interaction.InputBox("Cantidad a agregar:", "Modificar Stock");
            string precio = Interaction.InputBox("Precio de compra:", "Modificar Stock");
            
            await _apiService.ModificarStockAsync(int.Parse(sku), int.Parse(cantidad), int.Parse(precio));
            MessageBox.Show("Stock modificado");
            CargarTabla();
        }

        private void RepositorioMovimientos_Click(object sender, MouseButtonEventArgs e)
        {
            new MovimientosWindow(_apiService).ShowDialog();
        }
    }
}
