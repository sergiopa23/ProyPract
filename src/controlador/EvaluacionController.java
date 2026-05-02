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
import modelo.Evaluacion;
import modelo.EvaluacionDAO;

public class EvaluacionController {
    private EvaluacionDAO evaluacionDAO = new EvaluacionDAO();

    public void crearEvaluacion(Evaluacion evaluacion) {
        evaluacionDAO.insertar(evaluacion);
    }

    public List<Evaluacion> obtenerEvaluaciones() {
        return evaluacionDAO.listar();
    }

    public void actualizarEvaluacion(Evaluacion evaluacion) {
        evaluacionDAO.actualizar(evaluacion);
    }

    public void eliminarEvaluacion(int idEvaluacion) {
        evaluacionDAO.eliminar(idEvaluacion);
    }
}

