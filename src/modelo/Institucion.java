/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author parra
 */
public class Institucion {
    private int idInstitucion;
    private String nombre;
    private String direccion;

    public Institucion() {}

    public Institucion(int idInstitucion, String nombre, String direccion) {
        this.idInstitucion = idInstitucion;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public int getIdInstitucion() { return idInstitucion; }
    public void setIdInstitucion(int idInstitucion) { this.idInstitucion = idInstitucion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    @Override
    public String toString() {
        return "Institucion{" +
                "idInstitucion=" + idInstitucion +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }
}
