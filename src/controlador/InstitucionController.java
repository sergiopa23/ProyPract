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
import modelo.Institucion;
import modelo.InstitucionDAO;

public class InstitucionController {
    private InstitucionDAO institucionDAO = new InstitucionDAO();

    public void crearInstitucion(Institucion institucion) {
        institucionDAO.insertar(institucion);
    }

    public List<Institucion> obtenerInstituciones() {
        return institucionDAO.listar();
    }

    public void actualizarInstitucion(Institucion institucion) {
        institucionDAO.actualizar(institucion);
    }

    public void eliminarInstitucion(int idInstitucion) {
        institucionDAO.eliminar(idInstitucion);
    }
}

