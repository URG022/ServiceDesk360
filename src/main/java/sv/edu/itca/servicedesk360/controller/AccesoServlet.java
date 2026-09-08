package sv.edu.itca.servicedesk360.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/acceso")
public class AccesoServlet extends HttpServlet {

    private static final Pattern CORREO_VALIDO = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sesion = request.getSession(false);
        if (sesion != null && sesion.getAttribute("mensajeFlash") != null) {
            request.setAttribute("mensajeExito", sesion.getAttribute("mensajeFlash"));
            sesion.removeAttribute("mensajeFlash");
        }

        String estado = request.getParameter("estado");
        if ("cerrada".equals(estado)) {
            request.setAttribute("mensajeExito", "La sesión fue cerrada correctamente.");
        } else if ("sesion".equals(estado)) {
            request.setAttribute("mensajeError", "Debe iniciar acceso para abrir el panel.");
        }

        String ultimoUsuario = buscarCookie(request, "ultimoUsuario");
        if (CORREO_VALIDO.matcher(ultimoUsuario).matches()) {
            request.setAttribute("ultimoUsuario", ultimoUsuario);
        }

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String correo = normalizar(request.getParameter("correo")).toLowerCase();
        String clave = valorSeguro(request.getParameter("clave"));

        // Ejercicio Complementario 2: Control de intentos fallidos
        HttpSession sesionActual = request.getSession(true);
        Integer intentos = (Integer) sesionActual.getAttribute("intentosFallidos");
        if (intentos == null) {
            intentos = 0;
        }

        if (intentos >= 3) {
            request.setAttribute("mensajeError", "Demasiados intentos fallidos. Espere unos minutos para reintentar.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        Map<String, Map<String, String>> usuarios = obtenerUsuarios();
        Map<String, String> datosUsuario = usuarios.get(correo);

        boolean credencialesValidas = datosUsuario != null
                && datosUsuario.get("hash").equals(generarHash(clave));

        if (!credencialesValidas) {
            intentos++;
            sesionActual.setAttribute("intentosFallidos", intentos);
            request.setAttribute("mensajeError", "Correo o contraseña incorrectos. Intento " + intentos + " de 3.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Credenciales correctas: limpiar contador e invalidar sesión previa
        sesionActual.invalidate();

        HttpSession nuevaSesion = request.getSession(true);
        nuevaSesion.setAttribute("usuarioNombre", datosUsuario.get("nombre"));
        nuevaSesion.setAttribute("usuarioCorreo", correo);
        nuevaSesion.setAttribute("usuarioRol", datosUsuario.get("rol"));
        nuevaSesion.setAttribute("usuarioRolDescripcion", describirRol(datosUsuario.get("rol")));
        nuevaSesion.setMaxInactiveInterval(15 * 60);

        // Gestión de la cookie de preferencia de correo
        if ("si".equals(request.getParameter("recordar"))) {
            agregarCookieCorreo(request, response, correo);
        } else {
            eliminarCookieCorreo(request, response);
        }

        // Ejercicio 3: Cookie de preferencia no sensible (tema de interfaz)
        agregarCookieTema(request, response, "claro");

        response.sendRedirect(request.getContextPath() + "/panel");
    }

    // Ejercicio 5: Rol ADMINISTRADOR agregado al switch
    private String describirRol(String rol) {
        switch (rol) {
            case "SOLICITANTE":
                return "Solicitante de soporte";
            case "TECNICO":
                return "Técnico de soporte";
            case "ADMINISTRADOR":
                return "Administrador del sistema";
            default:
                return "Rol no identificado";
        }
    }

    private void agregarCookieCorreo(HttpServletRequest request, HttpServletResponse response, String correo) {
        Cookie cookie = new Cookie("ultimoUsuario", correo);
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setHttpOnly(true);
        cookie.setSecure(request.isSecure());
        cookie.setPath(rutaCookie(request));
        response.addCookie(cookie);
    }

    private void eliminarCookieCorreo(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("ultimoUsuario", "");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(request.isSecure());
        cookie.setPath(rutaCookie(request));
        response.addCookie(cookie);
    }

    // Ejercicio 3: Metodo auxiliar para cookie de tema
    private void agregarCookieTema(HttpServletRequest request, HttpServletResponse response, String tema) {
        Cookie cookie = new Cookie("temaInterfaz", tema);
        cookie.setMaxAge(30 * 24 * 60 * 60);
        cookie.setHttpOnly(false);
        cookie.setSecure(request.isSecure());
        cookie.setPath(rutaCookie(request));
        response.addCookie(cookie);
    }

    private String buscarCookie(HttpServletRequest request, String nombre) {
        if (request.getCookies() == null) {
            return "";
        }
        for (Cookie cookie : request.getCookies()) {
            if (nombre.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return "";
    }

    private String rutaCookie(HttpServletRequest request) {
        String contexto = request.getContextPath();
        return contexto.isEmpty() ? "/" : contexto;
    }

    private String normalizar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private String valorSeguro(String valor) {
        return valor == null ? "" : valor;
    }

    private String generarHash(String valor) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(valor.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("No fue posible procesar la contraseña.", ex);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Map<String, String>> obtenerUsuarios() {
        Object existente = getServletContext().getAttribute("usuarios");
        if (existente == null) {
            synchronized (getServletContext()) {
                existente = getServletContext().getAttribute("usuarios");
                if (existente == null) {
                    existente = new ConcurrentHashMap<String, Map<String, String>>();
                    getServletContext().setAttribute("usuarios", existente);
                }
            }
        }
        return (Map<String, Map<String, String>>) existente;
    }
}