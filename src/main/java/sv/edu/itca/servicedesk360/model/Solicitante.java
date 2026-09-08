package sv.edu.itca.servicedesk360.model;

public class Solicitante extends Usuario {
    private String empresa;
    private String telefono;

    public Solicitante(long id, String nombreCompleto, String correo, String empresa, String telefono) {
        super(id, nombreCompleto, correo, RolUsuario.SOLICITANTE);
        this.empresa = normalizar(empresa);
        this.telefono = normalizar(telefono);
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = normalizar(empresa);
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = normalizar(telefono);
    }

    private static String normalizar(String valor) {
        return valor == null ? "" : valor.trim();
    }
}