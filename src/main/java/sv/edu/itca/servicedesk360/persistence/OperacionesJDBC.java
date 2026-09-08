package sv.edu.itca.servicedesk360.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

public class OperacionesJDBC {

    public ClienteRegistro buscarClientePorCorreo(String correo) throws SQLException {
        String sql = "SELECT id_cliente, nombre, correo, activo FROM clientes WHERE correo = ?";
        try (Connection cn = ConexionBD.abrir();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return new ClienteRegistro(
                    rs.getLong("id_cliente"),
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    rs.getBoolean("activo")
                );
            }
        }
    }

    public long insertarCliente(String nombre, String correo) throws SQLException {
        String sql = "INSERT INTO clientes (nombre, correo) VALUES (?, ?)";
        try (Connection cn = ConexionBD.abrir();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.setString(2, correo);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new SQLException("MySQL no devolvió la clave generada.");
                }
                return keys.getLong(1);
            }
        }
    }

    public long registrarTicketConSeguimiento(
            long idCliente, Long idEquipo, Long idTecnico,
            long idCategoria, String titulo, String descripcion,
            String prioridad, String detalleInicial) throws SQLException {

        String sqlTicket = "INSERT INTO tickets (id_cliente, id_equipo, id_tecnico, id_categoria, titulo, descripcion, prioridad) "
                         + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlSeg = "INSERT INTO seguimientos (id_ticket, detalle) VALUES (?, ?)";

        try (Connection cn = ConexionBD.abrir()) {
            cn.setAutoCommit(false);
            try {
                long idTicket;
                try (PreparedStatement ps = cn.prepareStatement(sqlTicket, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, idCliente);
                    if (idEquipo == null) ps.setNull(2, Types.BIGINT);
                    else ps.setLong(2, idEquipo);
                    if (idTecnico == null) ps.setNull(3, Types.BIGINT);
                    else ps.setLong(3, idTecnico);
                    ps.setLong(4, idCategoria);
                    ps.setString(5, titulo);
                    ps.setString(6, descripcion);
                    ps.setString(7, prioridad);
                    ps.executeUpdate();
                    try (ResultSet k = ps.getGeneratedKeys()) {
                        if (!k.next()) throw new SQLException("Sin id_ticket generado");
                        idTicket = k.getLong(1);
                    }
                }

                try (PreparedStatement ps = cn.prepareStatement(sqlSeg)) {
                    ps.setLong(1, idTicket);
                    ps.setString(2, detalleInicial);
                    ps.executeUpdate();
                }

                cn.commit();
                return idTicket;
            } catch (SQLException ex) {
                cn.rollback();
                throw ex;
            } finally {
                cn.setAutoCommit(true);
            }
        }
    }
}