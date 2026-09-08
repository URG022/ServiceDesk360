package sv.edu.itca.servicedesk360.persistence;

import java.sql.SQLException;

public class PruebaTransaccion {
    public static void main(String[] args) {
        OperacionesJDBC op = new OperacionesJDBC();

        // 1. Prueba de lectura parametrizada segura
        try {
            ClienteRegistro cliente = op.buscarClientePorCorreo("ana.lopez@demo.local");
            System.out.println("Cliente encontrado: " + (cliente != null ? cliente.getNombre() : "No existe"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 2. Transacción Exitosa
        try {
            long ticketId = op.registrarTicketConSeguimiento(
                1L, null, 1L, 1L,
                "Error al ingresar al portal",
                "El usuario recibe un mensaje de error al autenticar.",
                "MEDIA",
                "Ticket registrado desde la prueba JDBC"
            );
            System.out.println("Transacción exitosa. Ticket ID: " + ticketId);
        } catch (SQLException e) {
            System.err.println("Error en transacción: " + e.getMessage());
        }

        // 3. Prueba de Rollback controlado (id_categoria 99999 inexistente)
        try {
            System.out.println("Iniciando prueba de Rollback con FK inválida...");
            op.registrarTicketConSeguimiento(
                1L, null, 1L, 99999L,
                "Ticket que fallará",
                "Debe hacer rollback completo",
                "ALTA",
                "Detalle fallido"
            );
        } catch (SQLException e) {
            System.out.println("Rollback comprobado con éxito. Error capturado: " + e.getMessage());
        }
    }
}