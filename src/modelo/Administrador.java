/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author parra
 */

/*Identificador único del administrador*/

public class Administrador { 
    private int idAdmin;
    private int idUsuario;

    public Administrador() {}

    public Administrador(int idAdmin, int idUsuario) {
        this.idAdmin = idAdmin;
        this.idUsuario = idUsuario;
    }
/*Permiten acceder y modificar los valores de los atributos de la clase.*/
    public int getIdAdmin() { return idAdmin; }
    public void setIdAdmin(int idAdmin) { this.idAdmin = idAdmin; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    @Override
    public String toString() {
        return "Administrador{" +
                "idAdmin=" + idAdmin +
                ", idUsuario=" + idUsuario +
                '}';
    }
}
