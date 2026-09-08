package sv.edu.itca.servicedesk360.storage;

import java.util.Optional;
import sv.edu.itca.servicedesk360.model.CuentaUsuario;

public interface BuscadorCuentas {
    Optional<CuentaUsuario> buscarPorCorreo(String correo);
    boolean existeCorreo(String correo);
}