using System;
using System.Text.Json.Serialization;

namespace Bootcamp_Frontend.Models
{
    public class Movimiento
    {
        [JsonPropertyName("id")]
        public int Id { get; set; }

        [JsonPropertyName("fecha")]
        public string Fecha { get; set; }

        [JsonPropertyName("tipo")]
        public string Tipo { get; set; }

        [JsonPropertyName("rut")]
        public string Rut { get; set; }

        [JsonPropertyName("sku")]
        public int Sku { get; set; }

        [JsonPropertyName("cantidad")]
        public int Cantidad { get; set; }

        [JsonPropertyName("precio")]
        public double Precio { get; set; }
    }
}
