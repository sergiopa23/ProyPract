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

public class CoordinadorDAO {

    public void insertar(Coordinador c) {
        String sql = "INSERT INTO Coordinador (id_coordinador, id_usuario) VALUES (?, ?)";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, c.getIdCoordinador());
            stmt.setInt(2, c.getIdUsuario());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al insertar coordinador: " + ex.getMessage());
        }
    }

    public List<Coordinador> listar() {
        List<Coordinador> lista = new ArrayList<>();
        String sql = "SELECT * FROM Coordinador";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Coordinador c = new Coordinador(
                        rs.getInt("id_coordinador"),
                        rs.getInt("id_usuario")
                );
                lista.add(c);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar coordinadores: " + ex.getMessage());
        }
        return lista;
    }

    public void actualizar(Coordinador c) {
        String sql = "UPDATE Coordinador SET id_usuario=? WHERE id_coordinador=?";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, c.getIdUsuario());
            stmt.setInt(2, c.getIdCoordinador());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al actualizar coordinador: " + ex.getMessage());
        }
    }

    public void eliminar(int idCoordinador) {
        String sql = "DELETE FROM Coordinador WHERE id_coordinador=?";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCoordinador);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al eliminar coordinador: " + ex.getMessage());
        }
    }
}

