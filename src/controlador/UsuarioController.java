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
import modelo.Usuario;
import modelo.UsuarioDAO;

public class UsuarioController {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void crearUsuario(Usuario usuario) {
        usuarioDAO.insertarUsuario(usuario);
    }

    public List<Usuario> obtenerUsuarios() {
        return usuarioDAO.listarUsuarios();
    }

    public void actualizarUsuario(Usuario usuario) {
        usuarioDAO.actualizarUsuario(usuario);
    }

    public void eliminarUsuario(int idUsuario) {
        usuarioDAO.eliminarUsuario(idUsuario);
    }

    public void crearUsuario(CrudProyecto.Usuario nuevo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void actualizarUsuario(CrudProyecto.Usuario actualizado) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

