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
import modelo.Coordinador;
import modelo.CoordinadorDAO;

public class CoordinadorController {
    private CoordinadorDAO coordinadorDAO = new CoordinadorDAO();

    public void crearCoordinador(Coordinador coordinador) {
        coordinadorDAO.insertar(coordinador);
    }

    public List<Coordinador> obtenerCoordinadores() {
        return coordinadorDAO.listar();
    }

    public void actualizarCoordinador(Coordinador coordinador) {
        coordinadorDAO.actualizar(coordinador);
    }

    public void eliminarCoordinador(int idCoordinador) {
        coordinadorDAO.eliminar(idCoordinador);
    }
}

