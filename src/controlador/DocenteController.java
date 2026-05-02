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
import modelo.Docente;
import modelo.DocenteDAO;

public class DocenteController {
    private DocenteDAO docenteDAO = new DocenteDAO();

    public void crearDocente(Docente docente) {
        docenteDAO.insertar(docente);
    }

    public List<Docente> obtenerDocentes() {
        return docenteDAO.listar();
    }

    public void actualizarDocente(Docente docente) {
        docenteDAO.actualizar(docente);
    }

    public void eliminarDocente(int idDocente) {
        docenteDAO.eliminar(idDocente);
    }
}

