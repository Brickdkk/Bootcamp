using System.Windows;
using Bootcamp_Frontend.Services;

namespace Bootcamp_Frontend
{
    public partial class MovimientosWindow : Window
    {
        private readonly ApiService _apiService;

        public MovimientosWindow(ApiService apiService)
        {
            InitializeComponent();
            _apiService = apiService;
            CargarMovimientos();
        }

        private async void CargarMovimientos()
        {
            var movimientos = await _apiService.ObtenerMovimientosAsync();
            MovimientosDataGrid.ItemsSource = movimientos;
        }
    }
}
