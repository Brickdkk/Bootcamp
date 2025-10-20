package cl.uddi.inventario.util;

public class RutValidator {
    public static String normalizar(String rut) {
        return rut.toUpperCase().replace(".", "").replace("-", "").trim();
    }
    public static boolean esValido(String rut) {
        String s = normalizar(rut);
        if (s.length() < 2) return false;
        String cuerpo = s.substring(0, s.length() - 1);
        char dv = s.charAt(s.length() - 1);
        int m = 0, suma = 1;
        for (int i = cuerpo.length() - 1; i >= 0; i--) {
            int d = cuerpo.charAt(i) - '0';
            if (d < 0 || d > 9) return false;
            suma = (suma + d * (9 - (m++ % 6))) % 11;
        }
        char dvCalc = (char) (suma != 0 ? suma + 47 : 75); // 75='K'
        return dvCalc == dv;
    }
}
