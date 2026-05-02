/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author parra
 */
public class Coordinador {
    private int idCoordinador;
    private int idUsuario;

    public Coordinador() {}

    public Coordinador(int idCoordinador, int idUsuario) {
        this.idCoordinador = idCoordinador;
        this.idUsuario = idUsuario;
    }

    public int getIdCoordinador() { return idCoordinador; }
    public void setIdCoordinador(int idCoordinador) { this.idCoordinador = idCoordinador; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    @Override
    public String toString() {
        return "Coordinador{" +
                "idCoordinador=" + idCoordinador +
                ", idUsuario=" + idUsuario +
                '}';
    }
}
