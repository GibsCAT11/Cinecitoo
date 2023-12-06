/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.cinecito.persistence;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author GibsCAT11
 * Clase de conexión a la base de datos de mysql 
 */
public class MySQLConnection {

     public static Connection get (){
        Connection connection = null;
        
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3305/Cinecito", "root", "elAvenoBailador11.4");
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
        return connection;
}

}
