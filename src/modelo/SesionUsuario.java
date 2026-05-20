/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

public class SesionUsuario {

    public static int idUsuario;
    public static String nombreUsuario;
    public static String rolUsuario;

    public static void limpiarSesion() {
        idUsuario = 0;
        nombreUsuario = null;
        rolUsuario = null;
    }
}
