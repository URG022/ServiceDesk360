package sv.edu.itca.servicedesk360.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ValidadorRegistro {
    private static final Pattern CORREO = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    public List<String> validar(String nombre, String correo, String rol, String clave, String confirmacion) {
        List<String> errores = new ArrayList<>();
        if (nombre == null || nombre.trim().length() < 3) {
            errores.add("El nombre debe contener al menos 3 caracteres.");
        }
        if (correo == null || !CORREO.matcher(correo.trim()).matches()) {
            errores.add("Ingrese un correo válido.");
        }
        if (!"SOLICITANTE".equalsIgnoreCase(rol) && !"TECNICO".equalsIgnoreCase(rol) && !"ADMINISTRADOR".equalsIgnoreCase(rol)) {
            errores.add("Seleccione un rol permitido.");
        }
        if (clave == null || clave.length() < 8 || !contieneNumero(clave)) {
            errores.add("La contraseña debe tener 8 caracteres y un número.");
        }
        if (clave == null || !clave.equals(confirmacion)) {
            errores.add("Las contraseñas no coinciden.");
        }
        return errores;
    }

    private boolean contieneNumero(String texto) {
        for (char caracter : texto.toCharArray()) {
            if (Character.isDigit(caracter)) {
                return true;
            }
        }
        return false;
    }
}