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
            try
            {
                var request = new LoginRequest { Username = username, Password = password };
                var response = await _httpClient.PostAsJsonAsync("/api/login", request);
                return response.IsSuccessStatusCode;
            }
            catch
            {
                throw new Exception("Error al conectar con el servidor. Verifica que el backend este corriendo.");
            }
        }

        public async Task<List<Producto>> ObtenerProductosAsync()
        {
            try
            {
                var productos = await _httpClient.GetFromJsonAsync<List<Producto>>("/api/productos");
                return productos ?? new List<Producto>();
            }
            catch
            {
                throw new Exception("Error al obtener productos. Verifica que el backend este corriendo.");
            }
        }
    }
}
