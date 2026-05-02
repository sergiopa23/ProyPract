/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author parra
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdministradorDAO {

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
