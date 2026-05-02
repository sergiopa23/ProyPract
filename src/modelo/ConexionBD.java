package modelo;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL =
            "jdbc:oracle:thin:@//localhost:1521/XE";

    private static final String USER =
            "proyprac";

    private static final String PASSWORD =
            "proyprac4";

    // Constructor privado
    private ConexionBD() {
    }

    // Siempre devuelve una conexión nueva
    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

            DatabaseMetaData meta = connection.getMetaData();

            System.out.println(
                    "✅ Conectado a Oracle: "
                    + meta.getDatabaseProductName()
            );

            return connection;

        } catch (SQLException ex) {
            throw new RuntimeException(
                    "❌ Error de conexión: "
                    + ex.getMessage()
            );
        }
    }
}