using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using Bootcamp_Frontend.Models;

namespace Bootcamp_Frontend.Services
{
    public class ApiService
    {
        private static readonly HttpClient _httpClient = new HttpClient { BaseAddress = new Uri("http://localhost:8080") };

        public async Task<bool> LoginAsync(string username, string password)
        {
            var request = new { username, password };
            var response = await _httpClient.PostAsJsonAsync("/api/login", request);
            return response.IsSuccessStatusCode;
        }

        public async Task<List<Producto>> ObtenerProductosAsync()
        {
            return await _httpClient.GetFromJsonAsync<List<Producto>>("/api/productos");
        }

        public async Task AgregarProductoAsync(Producto producto)
        {
            await _httpClient.PostAsJsonAsync("/api/productos", producto);
        }

        public async Task EliminarProductoAsync(int sku)
        {
            await _httpClient.DeleteAsync($"/api/productos/{sku}");
        }

        public async Task ModificarStockAsync(int sku, int cantidad, int precio)
        {
            var request = new { sku, cantidad, precio };
            await _httpClient.PostAsJsonAsync("/api/compra", request);
        }

        public async Task RegistrarVentaAsync(string rut, int sku, int cantidad)
        {
            var request = new { rut, sku, cantidad };
            await _httpClient.PostAsJsonAsync("/api/venta", request);
        }

        public async Task<List<Movimiento>> ObtenerMovimientosAsync()
        {
            return await _httpClient.GetFromJsonAsync<List<Movimiento>>("/api/movimientos");
        }
    }
}