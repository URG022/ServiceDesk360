package sv.edu.itca.servicedesk360.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/panel")
public class PanelServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("usuarioAutenticado") == null) {
            response.sendRedirect(request.getContextPath() + "/acceso?estado=sesion");
            return;
        }
        request.getRequestDispatcher("/panel.jsp").forward(request, response);
    }
}