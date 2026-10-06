package es.iesjuanbosco;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
// La base de datos está en el directorio de trabajo del proyecto.
        String url = "jdbc:sqlite:prueba.db";
        Scanner sc = new Scanner(System.in);

        String sql = "select * from usuarios where localidad='Madridejos'";
        String sql2 = "select * from telefonos inner join usuarios ON telefonos.cod = usuarios.cod where usuarios.cod = ? AND usuarios.localidad = ?";
        String sql3 = "select distinct nombre, localidad from usuarios inner join telefonos on usuarios.cod = telefonos.cod";

        // Conectar a la base de datos
        // Crear un Statement
        // Ejecutar consulta
        try (Connection conexion = DriverManager.getConnection(url);
        PreparedStatement pstmt = conexion.prepareStatement(sql2);
        Statement stmt = conexion.createStatement();
        Statement stmt2 = conexion.createStatement();
        ResultSet rs1 = stmt.executeQuery(sql);
        ResultSet rs3 = stmt2.executeQuery(sql3)){

            System.out.println("Conexión establecida con éxito.");

            // Primera consulta
            System.out.println("Datos de usuarios de Madridejos:");
            while (rs1.next()) {
                int cod = rs1.getInt("cod");
                String nombre = rs1.getString("nombre");
                String apellidos = rs1.getString("apellidos");
                String direccion = rs1.getString("direccion");
                String localidad = rs1.getString("localidad");

                System.out.println(cod + " | " + nombre + " | " + apellidos + " | " + direccion + " | " + localidad);
            }

            System.out.print("Introduce el id del usuario para mostrar sus teléfonos: ");
            int cod_usuario = sc.nextInt();
            System.out.print("Introduce la localidad del usuario: ");
            sc.nextLine();
            String localidad = sc.nextLine();

            boolean comprobar = false;

            pstmt.setInt(1,cod_usuario);
            pstmt.setString(2, localidad);

            // Segunda consulta
            try(ResultSet rs2 = pstmt.executeQuery()){
                System.out.println("Teléfonos del usuario " + cod_usuario + " de " + localidad + ": ");
                while(rs2.next()){
                    String telefono = rs2.getString("telefono");
                    System.out.println(telefono);
                    comprobar = true;
                }
                if(!comprobar){
                    System.out.println("No existen teléfonos para ese usuario. Pasaremos a mostrar los usuarios con al menos 1 teléfono");
                    while(rs3.next()){
                        String nombre = rs3.getString("nombre");
                        String localidadUsuario = rs3.getString("localidad");
                        System.out.println(nombre + " | " + localidadUsuario);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al conectar o consultar la base de datos:");
            e.printStackTrace();
        }
    }
}


