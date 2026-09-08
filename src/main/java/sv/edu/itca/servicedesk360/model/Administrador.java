package sv.edu.itca.servicedesk360.model;

public class Administrador extends Usuario {
    public Administrador(long id, String nombreCompleto, String correo) {
        super(id, nombreCompleto, correo, RolUsuario.ADMINISTRADOR);
    }
}