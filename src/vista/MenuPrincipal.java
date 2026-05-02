/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author parra
 */
import java.util.Scanner;
import modelo.Usuario;

public class MenuPrincipal {

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            int opcion;
            
            do {
                System.out.println("===== MENÚ PRINCIPAL =====");
                System.out.println("1. Gestionar Usuarios");
                System.out.println("2. Gestionar Estudiantes");
                System.out.println("3. Gestionar Docentes");
                System.out.println("4. Gestionar Coordinadores");
                System.out.println("5. Gestionar Administradores");
                System.out.println("6. Gestionar Instituciones");
                System.out.println("7. Gestionar Prácticas");
                System.out.println("8. Gestionar Evidencias");
                System.out.println("9. Gestionar Evaluaciones");
                System.out.println("10. Gestionar Reportes");
                System.out.println("0. Salir");
                System.out.print("Seleccione una opción: ");
                opcion = sc.nextInt();
                
                switch (opcion) {
                    case 1 -> menuUsuarios();
                    case 2 -> menuEstudiantes();
                    case 3 -> menuDocentes();
                    case 4 -> menuCoordinadores();
                    case 5 -> menuAdministradores();
                    case 6 -> menuInstituciones();
                    case 7 -> menuPracticas();
                    case 8 -> menuEvidencias();
                    case 9 -> menuEvaluaciones();
                    case 10 -> menuReportes();
                    case 0 -> System.out.println("Saliendo del sistema...");
                    default -> System.out.println("Opción inválida.");
                }
            } while (opcion != 0);
        }
    }

    // Ejemplo de submenú para Usuarios
    private static void menuUsuarios() {
        Scanner sc = new Scanner(System.in);
        UsuarioDAO dao = new UsuarioDAO();
        int opcion;

        do {
            System.out.println("=== MENÚ USUARIOS ===");
            System.out.println("1. Insertar Usuario");
            System.out.println("2. Listar Usuarios");
            System.out.println("3. Actualizar Usuario");
            System.out.println("4. Eliminar Usuario");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();

            switch (opcion) {
                case 1 -> {
                    Usuario u = new Usuario(1, "Juan Pérez", "juan@correo.com", "12345", "Estudiante");
                    dao.insertarUsuario(u);
                }
                case 2 -> {
                    for (Usuario usuario : dao.listarUsuarios()) {
                        System.out.println(usuario);
                    }
                }
                case 3 -> {
                    Usuario u2 = new Usuario(1, "Juan P.", "juan@correo.com", "54321", "Docente");
                    dao.actualizarUsuario(u2);
                }
                case 4 -> dao.eliminarUsuario(1);
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    // Aquí puedes crear métodos similares para Estudiantes, Docentes, etc.
    private static void menuEstudiantes() {
        System.out.println("Submenú de Estudiantes (pendiente de implementar).");
    }

    private static void menuDocentes() {
        System.out.println("Submenú de Docentes (pendiente de implementar).");
    }

    private static void menuCoordinadores() {
        System.out.println("Submenú de Coordinadores (pendiente de implementar).");
    }

    private static void menuAdministradores() {
        System.out.println("Submenú de Administradores (pendiente de implementar).");
    }

    private static void menuInstituciones() {
        System.out.println("Submenú de Instituciones (pendiente de implementar).");
    }

    private static void menuPracticas() {
        System.out.println("Submenú de Prácticas (pendiente de implementar).");
    }

    private static void menuEvidencias() {
        System.out.println("Submenú de Evidencias (pendiente de implementar).");
    }

    private static void menuEvaluaciones() {
        System.out.println("Submenú de Evaluaciones (pendiente de implementar).");
    }

    private static void menuReportes() {
        System.out.println("Submenú de Reportes (pendiente de implementar).");
    }
}

