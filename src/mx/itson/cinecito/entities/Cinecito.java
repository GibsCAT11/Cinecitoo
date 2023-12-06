/*
 * Este archivo pertenece al paquete mx.itson.cinecito.entities y proporciona
 * la implementación de la clase Cinecito para interactuar con una base de datos de películas.
 */
package mx.itson.cinecito.entities;

import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import mx.itson.cinecito.persistence.MySQLConnection;

/**
 * Author: GibsCAT11
 * La clase Cinecito representa una película y proporciona métodos para interactuar
 * con una base de datos relacionada con películas.
 */
public class Cinecito {

    // Atributos que representan las propiedades de una película.
    private int idMovie;
    private int idFunctions;
    private String name;
    private String time;
    private String room;
    private String sinopsys;
    private String gender;

    /**
     * Recupera una lista de objetos Cinecito desde la base de datos filtrando por nombre.
     *
     * @param filtro Filtro para buscar películas por nombre.
     * @return Una lista de objetos Cinecito que cumplen con el filtro.
     */
    public static List<Cinecito> getAll(String filtro) {
        List<Cinecito> cinecitos = new ArrayList<>();

        try {
            // Obtiene una conexión a la base de datos.
            Connection conexion = MySQLConnection.get();
            
            // Prepara la consulta SQL con un parámetro para el filtro de nombre.
            PreparedStatement statement = conexion.prepareStatement("SELECT * FROM movie WHERE name LIKE ?");
            statement.setString(1, "%" + filtro + "%");

            // Ejecuta la consulta y obtiene los resultados.
            ResultSet resultSet = statement.executeQuery();

            // Itera sobre los resultados y crea objetos Cinecito para cada fila.
            while (resultSet.next()) {
                Cinecito c = new Cinecito();
                c.setIdMovie(resultSet.getInt(1));
                c.setName(resultSet.getString(2));
                c.setSinopsys(resultSet.getString(3));
                c.setGender(resultSet.getString(4));
                cinecitos.add(c);
            }

        } catch (SQLException ex) {
            // Manejo de excepciones en caso de error de SQL.
            ex.printStackTrace();
        }
        
        // Devuelve la lista de objetos Cinecito.
        return cinecitos;
    }

    /**
     * Agrega una nueva película a la base de datos.
     *
     * @param name Nombre de la película.
     * @param sinopsys Sinopsis de la película.
     * @param gender Género de la película.
     * @return true si la operación fue exitosa, false de lo contrario.
     */
    public boolean save(String name, String sinopsys, String gender) {

        boolean result = false;
        try {
            // Obtiene una conexión a la base de datos.
            Connection conexion = MySQLConnection.get();
            
            // Prepara la consulta SQL para insertar una nueva película.
            String query = "INSERT INTO movie (name, sinopsys, gender) VALUES (?,?,?)";
            PreparedStatement statement = conexion.prepareStatement(query);

            // Establece los parámetros en la consulta.
            statement.setString(1, name);
            statement.setString(2, sinopsys);
            statement.setString(3, gender);
            
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

        // Devuelve el resultado de la operación.
        return result;
    }

    /**
     * Actualiza los datos de una película en la base de datos.
     *
     * @param idMovie ID de la película a actualizar.
     * @param name Nuevo nombre de la película.
     * @param sinopsys Nueva sinopsis de la película.
     * @param gender Nuevo género de la película.
     * @return true si la operación fue exitosa, false de lo contrario.
     */
    public boolean update(int idMovie, String name, String sinopsys, String gender) {
        boolean result = false;
        try {
            // Obtiene una conexión a la base de datos.
            Connection conexion = MySQLConnection.get();

            // Prepara la consulta SQL para actualizar una película por ID.
            String query = "UPDATE movie SET name = ?, sinopsys = ?, gender = ? WHERE idMovie = ?";
            PreparedStatement statement = conexion.prepareStatement(query);
            
            // Establece los parámetros en la consulta.
            statement.setString(1, name);
            statement.setString(2, sinopsys);
            statement.setString(3, gender);
            statement.setInt(4, idMovie);
            
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

        // Devuelve el resultado de la operación.
        return result;
    }

    /**
     * Elimina una película de la base de datos por su ID.
     *
     * @param idMovie ID de la película a eliminar.
     * @return true si la operación fue exitosa, false de lo contrario.
     */
    public boolean delete(int idMovie) {
        boolean result = false;
        try {
            // Obtiene una conexión a la base de datos.
            Connection conexion = MySQLConnection.get();
            
            // Prepara la consulta SQL para eliminar una película por ID.
            String query = "DELETE FROM movie WHERE idMovie = ?";
            PreparedStatement statement = conexion.prepareStatement(query);

            // Establece el parámetro en la consulta.
            statement.setInt(1, idMovie);
            
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

        // Devuelve el resultado de la operación.
        return result;
    }

    // Métodos getter y setter para acceder a los atributos de la clase.

    /**
     * @return La ID de la película.
     */
    public int getIdMovie() {
        return idMovie;
    }

    /**
     * Establece la ID de la película.
     * @param idMovie La nueva ID de la película.
     */
    public void setIdMovie(int idMovie) {
        this.idMovie = idMovie;
    }

    /**
     * @return El nombre de la película.
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre de la película.
     * @param name El nuevo nombre de la película.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return La sinopsis de la película.
     */
    public String getSinopsys() {
        return sinopsys;
    }

    /**
     * Establece la sinopsis de la película.
     * @param sinopsys La nueva sinopsis de la película.
     */
    public void setSinopsys(String sinopsys) {
        this.sinopsys = sinopsys;
    }

    /**
     * @return El género de la película.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Establece el género de la película.
     * @param gender El nuevo género de la película.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    /**
     * @return La ID de la función de la película.
     */
    public int getIdFunction() {
        return idFunctions;
    }

    /**
     * Establece la ID de la función de la película.
     * @param idFunction La nueva ID de la función de la película.
     */
    public void setIdFunction(int idFunction) {
        this.idFunctions = idFunction;
    }

    public int getIdFunctions() {
        return idFunctions;
    }

    public void setIdFunctions(int idFunctions) {
        this.idFunctions = idFunctions;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

}

