package com.napier.sem;

import java.sql.*;

public class App
{
    // Connection to MySQL database
    private Connection con = null;

    // Connect to the MySQL database
    public void connect()
    {
        try
        {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;  // Adjust the retries to a smaller number
        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database...");
            try
            {
                // Wait a bit for the database to start
                Thread.sleep(3000); // Adjust to a smaller wait time for faster retries
                // Connect to the database
                con = DriverManager.getConnection("jdbc:mysql://db:3306/employees?useSSL=false", "root", "example");
                System.out.println("Successfully connected");
                break;  // Exit the loop once connected
            }
            catch (SQLException sqle)
            {
                System.out.println("Failed to connect to database attempt " + (i + 1));
                System.out.println(sqle.getMessage());
            }
            catch (InterruptedException ie)
            {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    // Disconnect from the MySQL database
    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                con.close();
                System.out.println("Connection closed");
            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }

    public static void main(String[] args)
    {
        // Create a new instance of the application
        App app = new App();

        // Connect to the database
        app.connect();

        // Disconnect from the database
        app.disconnect();
    }
}
