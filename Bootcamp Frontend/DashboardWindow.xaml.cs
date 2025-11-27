using System;
using System.Windows;
using System.Windows.Input;
using Microsoft.Win32;
using System.IO;
using Bootcamp_Frontend.Services;

namespace Bootcamp_Frontend
{
    public partial class DashboardWindow : Window
    {
        private readonly ApiService _apiService = new ApiService();

        public DashboardWindow()
        {
            InitializeComponent();
        }

        private void ImportarProductos_Click(object sender, RoutedEventArgs e)
        {
            OpenFileDialog dialog = new OpenFileDialog { Filter = "Archivos de texto (*.txt)|*.txt" };
            if (dialog.ShowDialog() == true)
            {
                string contenido = File.ReadAllText(dialog.FileName);
                MessageBox.Show($"Archivo: {dialog.FileName}\nLineas: {contenido.Split('\n').Length}", "Importar Productos");
            }
        }

        private void ImportarMovimientos_Click(object sender, RoutedEventArgs e)
        {
            OpenFileDialog dialog = new OpenFileDialog { Filter = "Archivos de texto (*.txt)|*.txt" };
            if (dialog.ShowDialog() == true)
            {
                string contenido = File.ReadAllText(dialog.FileName);
                MessageBox.Show($"Archivo: {dialog.FileName}\nLineas: {contenido.Split('\n').Length}", "Importar Movimientos");
            }
        }

        private void ExportarProductos_Click(object sender, RoutedEventArgs e)
        {
            SaveFileDialog dialog = new SaveFileDialog { Filter = "Archivos de texto (*.txt)|*.txt", FileName = "productos.txt" };
            if (dialog.ShowDialog() == true)
            {
                File.WriteAllText(dialog.FileName, "ID|Nombre|Cantidad|Precio\n1|Laptop|10|1200.00\n2|Mouse|50|25.00");
                MessageBox.Show($"Exportado: {dialog.FileName}", "Exportar Productos");
            }
        }

        private void ExportarMovimientos_Click(object sender, RoutedEventArgs e)
        {
            SaveFileDialog dialog = new SaveFileDialog { Filter = "Archivos de texto (*.txt)|*.txt", FileName = "movimientos.txt" };
            if (dialog.ShowDialog() == true)
            {
                File.WriteAllText(dialog.FileName, "Fecha|Tipo|Producto|Cantidad\n2025-01-20|Entrada|Laptop|5\n2025-01-20|Salida|Mouse|10");
                MessageBox.Show($"Exportado: {dialog.FileName}", "Exportar Movimientos");
            }
        }

        private void AnadirProducto_Click(object sender, MouseButtonEventArgs e)
        {
            new AnadirProductoWindow().ShowDialog();
        }

        private void EliminarProducto_Click(object sender, MouseButtonEventArgs e)
        {
            MessageBox.Show("Eliminar Producto", "InvenPro");
        }

        private void ConsultarStock_Click(object sender, MouseButtonEventArgs e)
        {
            MessageBox.Show("Consultar Stock", "InvenPro");
        }

        private void ModificarStock_Click(object sender, MouseButtonEventArgs e)
        {
            MessageBox.Show("Modificar Stock", "InvenPro");
        }

        private async void ConsultarInventario_Click(object sender, MouseButtonEventArgs e)
        {
            try
            {
                var productos = await _apiService.ObtenerProductosAsync();
                MessageBox.Show($"Productos obtenidos: {productos.Count}\n\nEjemplo:\n{productos[0].Nombre} - Stock: {productos[0].Stock}", "Consultar Inventario");
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message, "Error");
            }
        }

        private void RepositorioMovimientos_Click(object sender, MouseButtonEventArgs e)
        {
            MessageBox.Show("Repositorio de Movimientos", "InvenPro");
        }
    }
}
