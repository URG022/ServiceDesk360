package sv.edu.itca.servicedesk360.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sv.edu.itca.servicedesk360.service.ServicioRegistro;

@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    private ServicioRegistro obtenerServicioRegistro() {
        return (ServicioRegistro) getServletContext().getAttribute("servicioRegistro");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/registro.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String rol = request.getParameter("rol");
        String clave = request.getParameter("clave");
        String confirmar = request.getParameter("confirmarClave");

        List<String> errores = obtenerServicioRegistro().registrar(nombre, correo, rol, clave, confirmar);

        if (!errores.isEmpty()) {
            request.setAttribute("mensajeError", String.join(" ", errores));
            request.getRequestDispatcher("/registro.jsp").forward(request, response);
            return;
        }

        HttpSession sesion = request.getSession();
        sesion.setAttribute("mensajeFlash", "Cuenta registrada correctamente. Inicie acceso.");
        response.sendRedirect(request.getContextPath() + "/acceso");
    }
}