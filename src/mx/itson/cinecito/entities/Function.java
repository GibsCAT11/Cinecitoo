/*
 * Este archivo pertenece al paquete mx.itson.cinecito.entities y representa
 * la clase Function, que extiende la clase Cinecito y proporciona métodos
 * para interactuar con una base de datos de funciones de películas.
 */
package mx.itson.cinecito.entities;

import mx.itson.cinecito.persistence.MySQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Author: GibsCAT11
 * La clase Function representa funciones de películas y proporciona métodos para
 * interactuar con una base de datos de funciones.
 */
public class Function extends Cinecito {

    // Se crea una instancia de la clase Cinecito para utilizar sus métodos y atributos.
    Cinecito cine = new Cinecito();

    /**
     * Recupera una lista de objetos Function desde la base de datos filtrando por nombre.
     *
     * @param filtro Filtro para buscar funciones por nombre.
     * @return Una lista de objetos Function que cumplen con el filtro.
     */
    public static List<Function> getAlls(String filtro) {
        List<Function> functions = new ArrayList<>();

        try {
            // Obtiene una conexión a la base de datos.
            Connection conexion = MySQLConnection.get();
            
            // Prepara la consulta SQL con un parámetro para el filtro de nombre.
            PreparedStatement statement = conexion.prepareStatement("SELECT * FROM functions WHERE name LIKE ?");
            statement.setString(1, "%" + filtro + "%");

            // Ejecuta la consulta y obtiene los resultados.
            ResultSet resultSet = statement.executeQuery();

            // Itera sobre los resultados y crea objetos Function para cada fila.
            while (resultSet.next()) {
                Function e = new Function();
                e.setIdFunction(resultSet.getInt(1));
                e.setName(resultSet.getString(2));
                e.setRoom(resultSet.getString(3));
                e.setTime(resultSet.getString(4));
                functions.add(e);
            }

        } catch (SQLException ex) {
            // Manejo de excepciones en caso de error de SQL.
            ex.printStackTrace();
        }
        
        // Devuelve la lista de objetos Function.
        return functions;
    }

    /**
     * Guarda una nueva función en la base de datos, verificando la disponibilidad de la sala y la hora.
     *
     * @param name Nombre de la función.
     * @param room Sala de la función.
     * @param time Hora de la función.
     * @return true si la operación fue exitosa, false de lo contrario.
     */
    public boolean saveFunction(String name, String room, String time) {
        try {
            // Obtiene una conexión a la base de datos.
            Connection connection = MySQLConnection.get();
            
            // Verifica si la sala y la hora están disponibles antes de insertar la función.
            if (available(room, time)) {
                // Si la sala y la hora están disponibles, inserta la función en la tabla functions.
                String insertQuery = "INSERT INTO functions (name, room, time) VALUES (?, ?, ?)";
                
                try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                    insertStatement.setString(1, name);
                    insertStatement.setString(2, room);
                    insertStatement.setString(3, time);
                    insertStatement.executeUpdate();
                    return true; // Indica que la inserción fue exitosa.
                }
            } else {
                // Informa si la sala y la hora ya están ocupadas y no se puede agregar la función.
                System.out.println("Error: La sala y hora ya están ocupadas. No se puede agregar la función.");
            }
        } catch (Exception ex) {
            // Manejo de excepciones en caso de error.
            System.err.println("ERROR: " + ex.getMessage());
        }
        return false;
    }

    /**
     * Verifica la disponibilidad de una sala y una hora para una función.
     *
     * @param room Sala de la función.
     * @param time Hora de la función.
     * @return true si la sala y la hora están disponibles, false de lo contrario.
     */
    public boolean available(String room, String time) {
        try {
            // Obtiene una conexión a la base de datos.
            Connection conexion = MySQLConnection.get();
            
            // Prepara la consulta SQL para contar las funciones con la misma sala y hora.
            String query = "SELECT COUNT(*) FROM functions WHERE room = ? AND time = ?";
            
            try (PreparedStatement statement = conexion.prepareStatement(query)) {
                statement.setString(1, room);
                statement.setString(2, time);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        // Obtiene el recuento de funciones con la misma sala y hora.
                        int count = resultSet.getInt(1);
                        return count == 0; // La sala y la hora están disponibles si el recuento es 0.
                    }
                }
            }
        } catch (Exception ex) {
            // Manejo de excepciones en caso de error.
            System.err.println("ERROR: " + ex.getMessage());
        }
        
        return false;
    }

    /**
     * Elimina una función de la base de datos por su ID.
     *
     * @param idFunctions ID de la función a eliminar.
     * @return true si la operación fue exitosa, false de lo contrario.
     */
    public boolean delete(int idFunctions) {
        boolean result = false;
        try {
            // Obtiene una conexión a la base de datos.
            Connection conexion = MySQLConnection.get();
            
            // Prepara la consulta SQL para eliminar una función por ID.
            String query = "DELETE FROM functions WHERE idFunctions = ?";
            PreparedStatement statement = conexion.prepareStatement(query);
            
            // Establece el parámetro en la consulta.
            statement.setInt(1, idFunctions);
            
            // Ejecuta la consulta.
            statement.execute();
            
            // Verifica si se realizó la operación con éxito.
            result = statement.getUpdateCount() == 1;
            
            // Cierra la conexión a la base de datos.
            conexion.close();
            
        } catch (Exception ex) {
            // Manejo de excepciones en caso de error.
            System.err.println("Error: " + ex.getMessage());
        }
        
        return result;
    }

    /**
     * Actualiza los datos de una función en la base de datos por su ID,
     * verificando la disponibilidad de la sala y la hora.
     *
     * @param idFunctions ID de la función a actualizar.
     * @param room Nueva sala de la función.
     * @param time Nueva hora de la función.
     * @return true si la operación fue exitosa, false de lo contrario.
     */
    public boolean updates(int idFunctions, String room, String time) {
        boolean result = false;
        try {
            // Obtiene una conexión a la base de datos.
            Connection conexion = MySQLConnection.get();
            
            // Verifica la disponibilidad de la nueva sala y hora antes de actualizar la función.
            if (available(room, time)) {
                // Si la sala y la hora están disponibles, actualiza la función en la tabla functions.
                String query = "UPDATE functions SET room = ?, time = ? WHERE idFunctions = ?";
                PreparedStatement statement = conexion.prepareStatement(query);
                statement.setString(1, room);
                statement.setString(2, time);
                statement.setInt(3, idFunctions);
                statement.execute();
                
                // Verifica si se realizó la operación con éxito.
                result = statement.getUpdateCount() == 1;
                
                // Cierra la conexión a la base de datos.
                conexion.close();
            } else {
                // Informa si la sala y la hora ya están ocupadas y no se puede actualizar la función.
                System.out.println("Error: Las salas ya están ocupadas en el nuevo horario.");
            }
        } catch (Exception ex) {
            // Manejo de excepciones en caso de error.
            System.err.println("Error: " + ex.getMessage());
        }
        
        return result;
    }
}
