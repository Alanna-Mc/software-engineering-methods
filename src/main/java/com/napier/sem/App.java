package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

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

        int retries = 20;  // Adjust the retries to a smaller number
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

    public Employee getEmployee(int ID)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, " +
                            "departments.dept_name, " +
                            "managers.emp_no AS manager_emp_no, managers.first_name AS manager_first_name, " +
                            "managers.last_name AS manager_last_name " +
                            "FROM employees " +
                            "JOIN dept_emp ON employees.emp_no = dept_emp.emp_no " +
                            "JOIN departments ON dept_emp.dept_no = departments.dept_no " +
                            "JOIN dept_manager ON departments.dept_no = dept_manager.dept_no " +
                            "JOIN employees AS managers ON dept_manager.emp_no = managers.emp_no " +
                            "WHERE employees.emp_no = " + ID;

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            if (rset.next())
            {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                emp.title = rset.getString("title");
                emp.salary = rset.getInt("salary");

                // Create and set the department
                Department dept = new Department();
                dept.dept_name = rset.getString("dept_name");

                // Create and assign the department manager
                Employee manager = new Employee();
                manager.emp_no = rset.getInt("manager_emp_no");
                manager.first_name = rset.getString("manager_first_name");
                manager.last_name = rset.getString("manager_last_name");

                // Assign the department and manager to the employee
                dept.manager = manager;
                emp.dept = dept;
                emp.manager = manager;

                return emp;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    public void displayEmployee(Employee emp)
    {
        if (emp != null)
        {
            System.out.println(
                    emp.emp_no + " "
                            + emp.first_name + " "
                            + emp.last_name + "\n"
                            + emp.title + "\n"
                            + "Salary:" + emp.salary + "\n"
                            + emp.dept.dept_name + "\n"
                            + "Manager: " + emp.manager + "\n");
        }
    }
    public Department getDepartment(String dept_name) {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create SQL query to fetch department and its manager
            String strSelect =
                    "SELECT departments.dept_no, departments.dept_name, " +
                            "managers.emp_no AS manager_emp_no, managers.first_name AS manager_first_name, " +
                            "managers.last_name AS manager_last_name " +
                            "FROM departments " +
                            "JOIN dept_manager ON departments.dept_no = dept_manager.dept_no " +
                            "JOIN employees AS managers ON dept_manager.emp_no = managers.emp_no " +
                            "WHERE departments.dept_name = '" + dept_name + "'";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);

            // Check if a department is returned
            if (rset.next()) {
                // Create a new Department object and populate it with data from the ResultSet
                Department dept = new Department();
                dept.dept_no = rset.getString("dept_no");
                dept.dept_name = rset.getString("dept_name");

                // Create the manager as an Employee object
                Employee manager = new Employee();
                manager.emp_no = rset.getInt("manager_emp_no");
                manager.first_name = rset.getString("manager_first_name");
                manager.last_name = rset.getString("manager_last_name");

                // Assign the manager to the department
                dept.manager = manager;

                // Return the populated Department object
                return dept;
            } else {
                // Return null if no department is found with the given name
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get department details");
            return null;
        }
    }


    public ArrayList<Employee> getSalariesByDepartment(Department dept){
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // SQL query to get salaries of employees in a specific department
            String strSelect = "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary " +
                    "FROM employees, salaries, dept_emp, departments " +
                    "WHERE employees.emp_no = salaries.emp_no " +
                    "AND employees.emp_no = dept_emp.emp_no " +
                    "AND dept_emp.dept_no = departments.dept_no " +
                    "AND salaries.to_date = '9999-01-01' " +
                    "AND departments.dept_no = '" + dept.dept_no + "' " +
                    "ORDER BY employees.emp_no ASC";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Employee> employees = new ArrayList<Employee>();
            while (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary = rset.getInt("salaries.salary");
                employees.add(emp);
            }
            return employees;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details by department");
            return null;
        }
    }

    /**
     * Gets all the current employees and salaries.
     * @return A list of all employees and salaries, or null if there is an error.
     */
    public ArrayList<Employee> getAllSalaries()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
                            + "FROM employees, salaries "
                            + "WHERE employees.emp_no = salaries.emp_no AND salaries.to_date = '9999-01-01' "
                            + "ORDER BY employees.emp_no ASC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Employee> employees = new ArrayList<Employee>();
            while (rset.next())
            {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary = rset.getInt("salaries.salary");
                employees.add(emp);
            }
            return employees;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
            return null;
        }
    }

    /**
     * Prints a list of employees.
     * @param employees The list of employees to print.
     */
    public void printSalaries(ArrayList<Employee> employees)
    {
        // Print header
        System.out.println(String.format("%-10s %-15s %-20s %-8s", "Emp No", "First Name", "Last Name", "Salary"));
        // Loop over all employees in the list
        for (Employee emp : employees)
        {
            String emp_string =
                    String.format("%-10s %-15s %-20s %-8s",
                            emp.emp_no, emp.first_name, emp.last_name, emp.salary);
            System.out.println(emp_string);
        }
    }

    /**
     * Gets all the current employees and salaries by selected role.
     * @param role The role (title) to filter salaries by.
     * @return A list of all employees and salaries by role, or null if there is an error.
     */
    public ArrayList<Employee> getAllSalariesByRole(String role)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary " +
                            "FROM employees, salaries, titles " +
                            "WHERE employees.emp_no = salaries.emp_no " +
                            "AND employees.emp_no = titles.emp_no " +
                            "AND salaries.to_date = '9999-01-01' " +
                            "AND titles.to_date = '9999-01-01' " +
                            "AND titles.title = '" + role + "' " +
                            "ORDER BY employees.emp_no ASC";

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Employee> employees = new ArrayList<Employee>();
            while (rset.next())
            {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary = rset.getInt("salaries.salary");
                employees.add(emp);
            }
            return employees;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
            return null;
        }
    }

    public static void main(String[] args)
    {
        // Create new Application
        App a = new App();

        // Connect to database
        a.connect();

        // Extract employee salary information
        //ArrayList<Employee> employees = a.getAllSalaries();

        // Print the salaries of all employees
        // a.printSalaries(employees);

        // Extract employee salary information by role
        //ArrayList<Employee> engineers = a.getAllSalariesByRole("Engineer");

        // Print the salaries of all engineers
        //a.printSalaries(engineers);

        // Get the department information (example: "Sales")
        Department salesDept = a.getDepartment("Sales");

        // Get the salaries for the "Sales" department
        ArrayList<Employee> salesEmployees = a.getSalariesByDepartment(salesDept);

        // Print the salaries of employees in the "Sales" department
        a.printSalaries(salesEmployees);

        // Disconnect from database
        a.disconnect();
    }
}
