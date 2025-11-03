package cl.uddi.inventario.util;

public class ValidadorRut {
    public static String normalizar(String rut) {
        return rut.toUpperCase().replace(".", "").replace("-", "").trim();
    }

    public static boolean esValido(String rut) {
        String rutNormalizado = normalizar(rut);
        if (rutNormalizado.length() < 2) {
            return false;
        }
        String cuerpo = rutNormalizado.substring(0, rutNormalizado.length() - 1);
        char digitoVerificador = rutNormalizado.charAt(rutNormalizado.length() - 1);
        int multiplicador = 0;
        int suma = 1;
        for (int i = cuerpo.length() - 1; i >= 0; i--) {
            int digito = cuerpo.charAt(i) - '0';
            if (digito < 0 || digito > 9) {
                return false;
            }
            suma = (suma + digito * (9 - (multiplicador++ % 6))) % 11;
        }
        char digitoVerificadorCalculado = (char) (suma != 0 ? suma + 47 : 75); // 75='K'
        return digitoVerificadorCalculado == digitoVerificador;
    }
}
