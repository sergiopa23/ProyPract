/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

/**
 *
 * @author parra
 */
import java.util.List;
import modelo.Estudiante;
import modelo.EstudianteDAO;

public class EstudianteController {
    private EstudianteDAO estudianteDAO = new EstudianteDAO();

    public void crearEstudiante(Estudiante estudiante) {
        estudianteDAO.insertar(estudiante);
    }

    public List<Estudiante> obtenerEstudiantes() {
        return estudianteDAO.listar();
    }

    public void actualizarEstudiante(Estudiante estudiante) {
        estudianteDAO.actualizar(estudiante);
    }

    public void eliminarEstudiante(int idEstudiante) {
        estudianteDAO.eliminar(idEstudiante);
    }
}

