package sv.edu.itca.servicedesk360.model;

import java.util.Objects;

public class CuentaUsuario {
    private final Usuario usuario;
    private final String hashClave;

    public CuentaUsuario(Usuario usuario, String hashClave) {
        this.usuario = Objects.requireNonNull(usuario, "El usuario es obligatorio.");
        if (hashClave == null || hashClave.trim().isEmpty()) {
            throw new IllegalArgumentException("El hash es obligatorio.");
        }
        this.hashClave = hashClave;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getHashClave() {
        return hashClave;
    }
}