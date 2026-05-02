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

public class InstitucionDAO {

    public void insertar(Institucion i) {
        String sql = "INSERT INTO Institucion (id_institucion, nombre, direccion) VALUES (?, ?, ?)";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, i.getIdInstitucion());
            stmt.setString(2, i.getNombre());
            stmt.setString(3, i.getDireccion());
            stmt.executeUpdate();
            System.out.println("Institución insertada correctamente.");
        } catch (SQLException ex) {
            System.out.println("Error al insertar institución: " + ex.getMessage());
        }
    }

    public List<Institucion> listar() {
        List<Institucion> lista = new ArrayList<>();
        String sql = "SELECT * FROM Institucion";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Institucion i = new Institucion(
                        rs.getInt("id_institucion"),
                        rs.getString("nombre"),
                        rs.getString("direccion")
                );
                lista.add(i);
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar instituciones: " + ex.getMessage());
        }
        return lista;
    }

    public void actualizar(Institucion i) {
        String sql = "UPDATE Institucion SET nombre=?, direccion=? WHERE id_institucion=?";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, i.getNombre());
            stmt.setString(2, i.getDireccion());
            stmt.setInt(3, i.getIdInstitucion());
            stmt.executeUpdate();
            System.out.println("Institución actualizada correctamente.");
        } catch (SQLException ex) {
            System.out.println("Error al actualizar institución: " + ex.getMessage());
        }
    }

    public void eliminar(int idInstitucion) {
        String sql = "DELETE FROM Institucion WHERE id_institucion=?";
        try (Connection conn = (Connection) ConexionBD.getInstancia();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idInstitucion);
            stmt.executeUpdate();
            System.out.println("Institución eliminada correctamente.");
        } catch (SQLException ex) {
            System.out.println("Error al eliminar institución: " + ex.getMessage());
        }
    }
}

