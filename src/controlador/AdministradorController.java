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
import modelo.Administrador;
import modelo.AdministradorDAO;

public class AdministradorController {
    private AdministradorDAO administradorDAO = new AdministradorDAO();

    public void crearAdministrador(Administrador administrador) {
        administradorDAO.insertar(administrador);
    }

    public List<Administrador> obtenerAdministradores() {
        return administradorDAO.listar();
    }

    public void actualizarAdministrador(Administrador administrador) {
        administradorDAO.actualizar(administrador);
    }

    public void eliminarAdministrador(int idAdmin) {
        administradorDAO.eliminar(idAdmin);
    }
}
