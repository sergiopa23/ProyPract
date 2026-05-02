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
import modelo.Reporte;
import modelo.ReporteDAO;

public class ReporteController {
    private final ReporteDAO reporteDAO = new ReporteDAO();

    public void crearReporte(Reporte reporte) {
        reporteDAO.insertar(reporte);
    }

    public List<Reporte> obtenerReportes() {
        return reporteDAO.listar();
    }

    public void actualizarReporte(Reporte reporte) {
        reporteDAO.actualizar(reporte);
    }

    public void eliminarReporte(int idReporte) {
        reporteDAO.eliminar(idReporte);
    }
}
