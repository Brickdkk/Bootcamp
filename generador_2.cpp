#include <iostream>
#include <fstream>
#include <cstdlib>
#include <ctime>
#include <string>

using namespace std;

int main() {
    const char* categorias[8] = {
        "lacteos y quesos", "frutas y verduras", "despensa", "carnes y pescados",
        "panaderia y pasteleria", "congelados", "snacks dulces", "snacks salados"
    };

    const char* productos[8][6] = {
        {"Leche", "Queso", "Yogur", "Manteca", "Crema", "Ricotta"},
        {"Manzana", "Banana", "Pera", "Naranja", "Sandia", "Tomate"},
        {"Arroz", "Fideos", "Azucar", "Sal", "Aceite", "Porotos"},
        {"Pollo", "Carne Vacuno", "Pescado", "Cerdo", "Cordero", "Jamon"},
        {"Pan", "Croissant", "Torta", "Donas", "Facturas", "Budin"},
        {"Helado", "Papas Fritas", "Hamburguesa", "Nuggets", "Pizza", "Verduras"},
        {"Chocolate", "Galletas", "Caramelos", "Gomitas", "Alfajor", "Turron"},
        {"Papas Lays", "Nachos", "Mani", "Almendras", "Pistachos", "Aceitunas"}
    };

    int skuBase[8] = {100, 200, 300, 400, 500, 600, 700, 800};
    int usados[48] = {0}; 
    srand(time(0));

    ofstream archivo("productos.txt"); 
    
    if (!archivo.is_open()) {
        cout << "Error al crear el archivo!" << endl;
        return 1;
    }

    int total = 0;
    int cantidad;
    cout << "Cuantos productos desea generar (max 48)?: ";
    cin >> cantidad;
    if (cantidad > 48) cantidad = 48;

    while (total < cantidad) {
        int cat = rand() % 8; 
        int prod = rand() % 6; 
        int idx = cat * 6 + prod;
        
        if (usados[idx]) continue;
        usados[idx] = 1;
        
        int precio = 1000 + rand() % (50000 - 1000 + 1);
        int stock = 1 + rand() % 200;
        int elSku = skuBase[cat] + prod;

        // FORMATO JAVA: SKU|NOMBRE|CATEGORIA|PRECIO|STOCK|STOCK_MIN|ESTADO
        archivo << elSku << "|" 
                << productos[cat][prod] << "|" 
                << categorias[cat] << "|" 
                << precio << "|" 
                << stock << "|" 
                << "5" << "|"        
                << "ACTIVE" << endl; 
        
        total++;
    }
    
    archivo.close();
    cout << "Archivo productos.txt generado correctamente." << endl;
    return 0;
}