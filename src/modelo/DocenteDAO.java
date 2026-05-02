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

public class DocenteDAO {

    public void insertar(Docente d) {
        String sql = "INSERT INTO Docente (id_docente, especialidad, id_usuario) VALUES (?, ?, ?)";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, d.getIdDocente());
            stmt.setString(2, d.getEspecialidad());
            stmt.setInt(3, d.getIdUsuario());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al insertar docente: " + ex.getMessage());
        }
    }

    public List<Docente> listar() {
        List<Docente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Docente";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Docente d = new Docente(
                        rs.getInt("id_docente"),
                        rs.getString("especialidad"),
                        rs.getInt("id_usuario")
                );
                lista.add(d);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar docentes: " + ex.getMessage());
        }
        return lista;
    }

    public void actualizar(Docente d) {
        String sql = "UPDATE Docente SET especialidad=?, id_usuario=? WHERE id_docente=?";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, d.getEspecialidad());
            stmt.setInt(2, d.getIdUsuario());
            stmt.setInt(3, d.getIdDocente());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al actualizar docente: " + ex.getMessage());
        }
    }

    public void eliminar(int idDocente) {
        String sql = "DELETE FROM Docente WHERE id_docente=?";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idDocente);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al eliminar docente: " + ex.getMessage());
        }
    }
}

