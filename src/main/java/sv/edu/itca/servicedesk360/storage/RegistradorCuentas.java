package sv.edu.itca.servicedesk360.storage;

import sv.edu.itca.servicedesk360.model.CuentaUsuario;

public interface RegistradorCuentas {
    void guardar(CuentaUsuario cuenta);
}