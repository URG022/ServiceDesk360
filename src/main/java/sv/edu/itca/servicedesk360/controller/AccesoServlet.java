package sv.edu.itca.servicedesk360.controller;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sv.edu.itca.servicedesk360.model.Usuario;
import sv.edu.itca.servicedesk360.service.Autenticador;

@WebServlet("/acceso")
public class AccesoServlet extends HttpServlet {

    private Autenticador obtenerAutenticador() {
        return (Autenticador) getServletContext().getAttribute("autenticador");
    }

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
        if (!ultimoUsuario.isEmpty()) {
            request.setAttribute("ultimoUsuario", ultimoUsuario);
        }

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String correo = request.getParameter("correo");
        String clave = request.getParameter("clave");

        Optional<Usuario> resultado = obtenerAutenticador().autenticar(correo, clave);

        if (!resultado.isPresent()) {
            request.setAttribute("mensajeError", "Correo o contraseña incorrectos.");
            request.setAttribute("ultimoUsuario", correo);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        HttpSession anterior = request.getSession(false);
        if (anterior != null) {
            anterior.invalidate();
        }

        Usuario usuario = resultado.get();
        HttpSession sesion = request.getSession(true);
        sesion.setMaxInactiveInterval(15 * 60);
        sesion.setAttribute("usuarioAutenticado", usuario);

        if ("si".equals(request.getParameter("recordar"))) {
            agregarCookieCorreo(request, response, usuario.getCorreo());
        } else {
            eliminarCookieCorreo(request, response);
        }

        response.sendRedirect(request.getContextPath() + "/panel");
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

    private String buscarCookie(HttpServletRequest request, String nombre) {
        if (request.getCookies() == null) return "";
        for (Cookie cookie : request.getCookies()) {
            if (nombre.equals(cookie.getName())) return cookie.getValue();
        }
        return "";
    }

    private String rutaCookie(HttpServletRequest request) {
        String contexto = request.getContextPath();
        return contexto.isEmpty() ? "/" : contexto;
    }
}