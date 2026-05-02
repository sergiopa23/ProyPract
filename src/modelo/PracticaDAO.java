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

public class PracticaDAO {

    public void insertar(Practica p) {
        String sql = "INSERT INTO Practica (id_practica, fecha_inicio, fecha_fin, horas, estado, id_estudiante, id_docente, id_coordinador, id_institucion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, p.getIdPractica());
            stmt.setDate(2, p.getFechaInicio());
            stmt.setDate(3, p.getFechaFin());
            stmt.setInt(4, p.getHoras());
            stmt.setString(5, p.getEstado());
            stmt.setInt(6, p.getIdEstudiante());
            stmt.setInt(7, p.getIdDocente());
            stmt.setInt(8, p.getIdCoordinador());
            stmt.setInt(9, p.getIdInstitucion());
            stmt.executeUpdate();
            System.out.println("Práctica insertada correctamente.");
        } catch (SQLException ex) {
            System.out.println("Error al insertar práctica: " + ex.getMessage());
        }
    }

    public List<Practica> listar() {
        List<Practica> lista = new ArrayList<>();
        String sql = "SELECT * FROM Practica";
        try (Connection conn =(Connection) ConexionBD.getInstancia();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Practica p = new Practica(
                        rs.getInt("id_practica"),
                        rs.getDate("fecha_inicio"),
                        rs.getDate("fecha_fin"),
                        rs.getInt("horas"),
                        rs.getString("estado"),
                        rs.getInt("id_estudiante"),
                        rs.getInt("id_docente"),
                        rs.getInt("id_coordinador"),
                        rs.getInt("id_institucion")
                );
                lista.add(p);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar prácticas: " + ex.getMessage());
        }
        return lista;
    }

    public void actualizar(Practica p) {
        String sql = "UPDATE Practica SET fecha_inicio=?, fecha_fin=?, horas=?, estado=?, id_estudiante=?, id_docente=?, id_coordinador=?, id_institucion=? WHERE id_practica=?";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, p.getFechaInicio());
            stmt.setDate(2, p.getFechaFin());
            stmt.setInt(3, p.getHoras());
            stmt.setString(4, p.getEstado());
            stmt.setInt(5, p.getIdEstudiante());
            stmt.setInt(6, p.getIdDocente());
            stmt.setInt(7, p.getIdCoordinador());
            stmt.setInt(8, p.getIdInstitucion());
            stmt.setInt(9, p.getIdPractica());
            stmt.executeUpdate();
            System.out.println("Práctica actualizada correctamente.");
        } catch (SQLException ex) {
            System.out.println("Error al actualizar práctica: " + ex.getMessage());
        }
    }

    public void eliminar(int idPractica) {
        String sql = "DELETE FROM Practica WHERE id_practica=?";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPractica);
            stmt.executeUpdate();
            System.out.println("Práctica eliminada correctamente.");
        } catch (SQLException ex) {
            System.out.println("Error al eliminar práctica: " + ex.getMessage());
        }
    }
}

