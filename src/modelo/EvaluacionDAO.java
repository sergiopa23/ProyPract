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

public class EvaluacionDAO {

    public void insertar(Evaluacion e) {
        String sql = "INSERT INTO Evaluacion (id_evaluacion, nota, observaciones, fecha, id_practica, id_docente, id_estudiante) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, e.getIdEvaluacion());
            stmt.setDouble(2, e.getNota());
            stmt.setString(3, e.getObservaciones());
            stmt.setDate(4, e.getFecha());
            stmt.setInt(5, e.getIdPractica());
            stmt.setInt(6, e.getIdDocente());
            stmt.setInt(7, e.getIdEstudiante());
            stmt.executeUpdate();
            System.out.println("Evaluación insertada correctamente.");
        } catch (SQLException ex) {
            System.out.println("Error al insertar evaluación: " + ex.getMessage());
        }
    }

    public List<Evaluacion> listar() {
        List<Evaluacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM Evaluacion";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Evaluacion e = new Evaluacion(
                        rs.getInt("id_evaluacion"),
                        rs.getDouble("nota"),
                        rs.getString("observaciones"),
                        rs.getDate("fecha"),
                        rs.getInt("id_practica"),
                        rs.getInt("id_docente"),
                        rs.getInt("id_estudiante")
                );
                lista.add(e);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar evaluaciones: " + ex.getMessage());
        }
        return lista;
    }

    public void actualizar(Evaluacion e) {
        String sql = "UPDATE Evaluacion SET nota=?, observaciones=?, fecha=?, id_practica=?, id_docente=?, id_estudiante=? WHERE id_evaluacion=?";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, e.getNota());
            stmt.setString(2, e.getObservaciones());
            stmt.setDate(3, e.getFecha());
            stmt.setInt(4, e.getIdPractica());
            stmt.setInt(5, e.getIdDocente());
            stmt.setInt(6, e.getIdEstudiante());
            stmt.setInt(7, e.getIdEvaluacion());
            stmt.executeUpdate();
            System.out.println("Evaluación actualizada correctamente.");
        } catch (SQLException ex) {
            System.out.println("Error al actualizar evaluación: " + ex.getMessage());
        }
    }

    public void eliminar(int idEvaluacion) {
        String sql = "DELETE FROM Evaluacion WHERE id_evaluacion=?";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEvaluacion);
            stmt.executeUpdate();
            System.out.println("Evaluación eliminada correctamente.");
        } catch (SQLException ex) {
            System.out.println("Error al eliminar evaluación: " + ex.getMessage());
        }
    }
}

