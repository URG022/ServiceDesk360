package sv.edu.itca.servicedesk360.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import sv.edu.itca.servicedesk360.service.Autenticador;
import sv.edu.itca.servicedesk360.service.ServicioAutenticacion;
import sv.edu.itca.servicedesk360.service.ServicioRegistro;
import sv.edu.itca.servicedesk360.service.ServicioTickets;
import sv.edu.itca.servicedesk360.service.ValidadorRegistro;
import sv.edu.itca.servicedesk360.storage.DirectorioCuentasEnMemoria;
import sv.edu.itca.servicedesk360.storage.DirectorioTicketsEnMemoria;

@WebListener
public class AplicacionListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent evento) {
        DirectorioCuentasEnMemoria directorioCuentas = new DirectorioCuentasEnMemoria();
        ServicioRegistro registro = new ServicioRegistro(directorioCuentas, directorioCuentas, new ValidadorRegistro());
        Autenticador autenticador = new ServicioAutenticacion(directorioCuentas);

        DirectorioTicketsEnMemoria directorioTickets = new DirectorioTicketsEnMemoria();
        ServicioTickets servicioTickets = new ServicioTickets(directorioTickets, directorioTickets);

        ServletContext contexto = evento.getServletContext();
        contexto.setAttribute("servicioRegistro", registro);
        contexto.setAttribute("autenticador", autenticador);
        contexto.setAttribute("servicioTickets", servicioTickets);
    }

    @Override
    public void contextDestroyed(ServletContextEvent evento) {}
}