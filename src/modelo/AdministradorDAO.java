/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * Clase AdministradorDAO
 * 
 * Esta clase implementa el patrón DAO (Data Access Object) para la entidad Administrador.
 * Se encarga de gestionar las operaciones CRUD (Crear, Leer, Actualizar y Eliminar)
 * en la base de datos, permitiendo la comunicación entre la aplicación y la tabla Administrador.
 * 
 * Utiliza JDBC para la conexión y ejecución de sentencias SQL.
 * 
 * @author parra
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdministradorDAO {
 /**
     * Inserta un nuevo administrador en la base de datos.
     * 
     * @param a Objeto Administrador que contiene los datos a insertar.
     */
    public void insertar(Administrador a) {
        String sql = "INSERT INTO Administrador (id_admin, id_usuario) VALUES (?, ?)";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, a.getIdAdmin());
            stmt.setInt(2, a.getIdUsuario());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al insertar administrador: " + ex.getMessage());
        }
    }
  /**
     * Obtiene una lista de todos los administradores registrados en la base de datos.
     * 
     * @return Lista de objetos Administrador.
     */
    public List<Administrador> listar() {
        List<Administrador> lista = new ArrayList<>();
        String sql = "SELECT * FROM Administrador";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Administrador a = new Administrador(
                        rs.getInt("id_admin"),
                        rs.getInt("id_usuario")
                );
                lista.add(a);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar administradores: " + ex.getMessage());
        }
        return lista;
    }
  /**
     * Actualiza los datos de un administrador existente en la base de datos.
     * 
     * @param a Objeto Administrador con los nuevos datos a actualizar.
     */
    public void actualizar(Administrador a) {
        String sql = "UPDATE Administrador SET id_usuario=? WHERE id_admin=?";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, a.getIdUsuario());
            stmt.setInt(2, a.getIdAdmin());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al actualizar administrador: " + ex.getMessage());
        }
    }
  /**
     * Elimina un administrador de la base de datos según su ID.
     * 
     * @param idAdmin Identificador del administrador a eliminar.
     */
    public void eliminar(int idAdmin) {
        String sql = "DELETE FROM Administrador WHERE id_admin=?";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAdmin);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al eliminar administrador: " + ex.getMessage());
        }
    }
}
