using System.Windows;
using Bootcamp_Frontend.Services;

namespace Bootcamp_Frontend
{
    public partial class AnadirProductoWindow : Window
    {
        private readonly ApiService _apiService;
        private readonly DashboardWindow _dashboard;

        public AnadirProductoWindow(ApiService apiService, DashboardWindow dashboard)
        {
            InitializeComponent();
            _apiService = apiService;
            _dashboard = dashboard;
        }

        private async void Guardar_Click(object sender, RoutedEventArgs e)
        {
            var producto = new Models.Producto
            {
                Sku = int.Parse(SkuTextBox.Text),
                Nombre = NombreTextBox.Text,
                Stock = int.Parse(StockTextBox.Text),
                Precio = double.Parse(PrecioTextBox.Text)
            };

            await _apiService.AgregarProductoAsync(producto);
            MessageBox.Show("Producto agregado");
            _dashboard.CargarTabla();
            this.Close();
        }

        private void Cerrar_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
    }
}
