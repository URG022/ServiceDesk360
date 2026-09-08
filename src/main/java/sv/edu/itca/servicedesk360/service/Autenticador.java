package sv.edu.itca.servicedesk360.service;

import java.util.Optional;
import sv.edu.itca.servicedesk360.model.Usuario;

public interface Autenticador {
    Optional<Usuario> autenticar(String correo, String clave);
}