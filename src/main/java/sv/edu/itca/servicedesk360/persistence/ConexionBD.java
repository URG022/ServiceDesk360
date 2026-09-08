package sv.edu.itca.servicedesk360.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ConexionBD {
    private static final Properties CONFIG = cargarConfiguracion();

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("No se pudo registrar el driver MySQL: " + e.getMessage());
        }
    }

    private ConexionBD() {}

    private static Properties cargarConfiguracion() {
        Path ruta = Path.of(System.getProperty("user.home"), ".servicedesk360", "db.properties");
        Properties p = new Properties();
        try (InputStream in = Files.newInputStream(ruta)) {
            p.load(in);
            return p;
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo cargar la configuración JDBC externa desde: " + ruta, ex);
        }
    }

    public static Connection abrir() throws SQLException {
        return DriverManager.getConnection(
            CONFIG.getProperty("db.url"),
            CONFIG.getProperty("db.user"),
            CONFIG.getProperty("db.password")
        );
    }
}