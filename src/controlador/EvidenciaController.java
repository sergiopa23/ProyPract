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
import modelo.Evidencia;
import modelo.EvidenciaDAO;

public class EvidenciaController {
    private final EvidenciaDAO evidenciaDAO = new EvidenciaDAO();

    public void crearEvidencia(Evidencia evidencia) {
        evidenciaDAO.insertar(evidencia);
    }

    public List<Evidencia> obtenerEvidencias() {
        return evidenciaDAO.listar();
    }

    public void actualizarEvidencia(Evidencia evidencia) {
        evidenciaDAO.actualizar(evidencia);
    }

    public void eliminarEvidencia(int idEvidencia) {
        evidenciaDAO.eliminar(idEvidencia);
    }
}

