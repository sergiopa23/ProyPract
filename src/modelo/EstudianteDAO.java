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

public class EstudianteDAO {

    public void insertar(Estudiante e) {
        String sql = "INSERT INTO Estudiante (id_estudiante, codigo, programa, id_usuario) VALUES (?, ?, ?, ?)";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, e.getIdEstudiante());
            stmt.setString(2, e.getCodigo());
            stmt.setString(3, e.getPrograma());
            stmt.setInt(4, e.getIdUsuario());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al insertar estudiante: " + ex.getMessage());
        }
    }

    public List<Estudiante> listar() {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT * FROM Estudiante";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Estudiante e = new Estudiante(
                        rs.getInt("id_estudiante"),
                        rs.getString("codigo"),
                        rs.getString("programa"),
                        rs.getInt("id_usuario")
                );
                lista.add(e);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar estudiantes: " + ex.getMessage());
        }
        return lista;
    }

    public void actualizar(Estudiante e) {
        String sql = "UPDATE Estudiante SET codigo=?, programa=?, id_usuario=? WHERE id_estudiante=?";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, e.getCodigo());
            stmt.setString(2, e.getPrograma());
            stmt.setInt(3, e.getIdUsuario());
            stmt.setInt(4, e.getIdEstudiante());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al actualizar estudiante: " + ex.getMessage());
        }
    }

    public void eliminar(int idEstudiante) {
        String sql = "DELETE FROM Estudiante WHERE id_estudiante=?";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEstudiante);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error al eliminar estudiante: " + ex.getMessage());
        }
    }
}

