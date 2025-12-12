using System;
using System.Text.Json.Serialization;

namespace Bootcamp_Frontend.Models
{
    public class Movimiento
    {
        [JsonPropertyName("sku")]
        public int Sku { get; set; }

        [JsonPropertyName("cantidad")]
        public int Cantidad { get; set; }

        [JsonPropertyName("precioUnitario")]
        public int PrecioUnitario { get; set; }

        [JsonPropertyName("rutUsuario")]
        public string RutUsuario { get; set; }

        [JsonPropertyName("documento")]
        public string Documento { get; set; }

        [JsonPropertyName("nota")]
        public string Nota { get; set; }

        [JsonPropertyName("tipo")]
        public string Tipo { get; set; }

        [JsonPropertyName("fecha")]
        public string Fecha { get; set; }

        public int Total => Cantidad * PrecioUnitario;
    }
}
