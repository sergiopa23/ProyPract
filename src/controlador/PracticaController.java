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
import modelo.Practica;
import modelo.PracticaDAO;

public class PracticaController {
    private PracticaDAO practicaDAO = new PracticaDAO();

    public void crearPractica(Practica practica) {
        practicaDAO.insertar(practica);
    }

    public List<Practica> obtenerPracticas() {
        return practicaDAO.listar();
    }

    public void actualizarPractica(Practica practica) {
        practicaDAO.actualizar(practica);
    }

    public void eliminarPractica(int idPractica) {
        practicaDAO.eliminar(idPractica);
    }
}
