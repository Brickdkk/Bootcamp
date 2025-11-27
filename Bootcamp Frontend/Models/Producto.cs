using System.Text.Json.Serialization;

namespace Bootcamp_Frontend.Models
{
    public class Producto
    {
        [JsonPropertyName("sku")]
        public int Sku { get; set; }

        [JsonPropertyName("nombre")]
        public string Nombre { get; set; }

        [JsonPropertyName("stock")]
        public int Stock { get; set; }

        [JsonPropertyName("precio")]
        public double Precio { get; set; }
    }
}
