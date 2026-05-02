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

public class EvidenciaDAO {

    public void insertar(Evidencia e) {
        String sql = "INSERT INTO Evidencia (id_evidencia, descripcion, archivo, fecha, id_practica, id_estudiante) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, e.getIdEvidencia());
            stmt.setString(2, e.getDescripcion());
            stmt.setString(3, e.getArchivo());
            stmt.setDate(4, e.getFecha());
            stmt.setInt(5, e.getIdPractica());
            stmt.setInt(6, e.getIdEstudiante());
            stmt.executeUpdate();
            System.out.println("Evidencia insertada correctamente.");
        } catch (SQLException ex) {
            System.out.println("Error al insertar evidencia: " + ex.getMessage());
        }
    }

    public List<Evidencia> listar() {
        List<Evidencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM Evidencia";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Evidencia e = new Evidencia(
                        rs.getInt("id_evidencia"),
                        rs.getString("descripcion"),
                        rs.getString("archivo"),
                        rs.getDate("fecha"),
                        rs.getInt("id_practica"),
                        rs.getInt("id_estudiante")
                );
                lista.add(e);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar evidencias: " + ex.getMessage());
        }
        return lista;
    }

    public void actualizar(Evidencia e) {
        String sql = "UPDATE Evidencia SET descripcion=?, archivo=?, fecha=?, id_practica=?, id_estudiante=? WHERE id_evidencia=?";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, e.getDescripcion());
            stmt.setString(2, e.getArchivo());
            stmt.setDate(3, e.getFecha());
            stmt.setInt(4, e.getIdPractica());
            stmt.setInt(5, e.getIdEstudiante());
            stmt.setInt(6, e.getIdEvidencia());
            stmt.executeUpdate();
            System.out.println("Evidencia actualizada correctamente.");
        } catch (SQLException ex) {
            System.out.println("Error al actualizar evidencia: " + ex.getMessage());
        }
    }

    public void eliminar(int idEvidencia) {
        String sql = "DELETE FROM Evidencia WHERE id_evidencia=?";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEvidencia);
            stmt.executeUpdate();
            System.out.println("Evidencia eliminada correctamente.");
        } catch (SQLException ex) {
            System.out.println("Error al eliminar evidencia: " + ex.getMessage());
        }
    }
}

