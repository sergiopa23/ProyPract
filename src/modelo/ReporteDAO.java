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

public class ReporteDAO {

    public void insertar(Reporte r) {
        String sql = "INSERT INTO Reporte (id_reporte, tipo, fecha, id_coordinador) VALUES (?, ?, ?, ?)";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, r.getIdReporte());
            stmt.setString(2, r.getTipo());
            stmt.setDate(3, r.getFecha());
            stmt.setInt(4, r.getIdCoordinador());
            stmt.executeUpdate();
            System.out.println("Reporte insertado correctamente.");
        } catch (SQLException ex) {
            System.out.println("Error al insertar reporte: " + ex.getMessage());
        }
    }

    public List<Reporte> listar() {
        List<Reporte> lista = new ArrayList<>();
        String sql = "SELECT * FROM Reporte";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Reporte r = new Reporte(
                        rs.getInt("id_reporte"),
                        rs.getString("tipo"),
                        rs.getDate("fecha"),
                        rs.getInt("id_coordinador")
                );
                lista.add(r);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar reportes: " + ex.getMessage());
        }
        return lista;
    }

    public void actualizar(Reporte r) {
        String sql = "UPDATE Reporte SET tipo=?, fecha=?, id_coordinador=? WHERE id_reporte=?";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, r.getTipo());
            stmt.setDate(2, r.getFecha());
            stmt.setInt(3, r.getIdCoordinador());
            stmt.setInt(4, r.getIdReporte());
            stmt.executeUpdate();
            System.out.println("Reporte actualizado correctamente.");
        } catch (SQLException ex) {
            System.out.println("Error al actualizar reporte: " + ex.getMessage());
        }
    }

    public void eliminar(int idReporte) {
        String sql = "DELETE FROM Reporte WHERE id_reporte=?";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idReporte);
            stmt.executeUpdate();
            System.out.println("Reporte eliminado correctamente.");
        } catch (SQLException ex) {
            System.out.println("Error al eliminar reporte: " + ex.getMessage());
        }
    }
}

