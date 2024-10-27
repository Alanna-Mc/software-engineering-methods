package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
    }

    // Employees is null
    @Test
    void printSalariesTestNull()
    {
        app.printSalaries(null);
    }

    // Employees is Empty
    @Test
    void printSalariesTestEmpty()
    {
        ArrayList<Employee> employees = new ArrayList<Employee>();
        app.printSalaries(employees);
    }

    // Employees Contains null
    @Test
    void printSalariesTestContainsNull()
    {
        ArrayList<Employee> employees = new ArrayList<Employee>();
        employees.add(null);
        app.printSalaries(employees);
    }

    // Employee Contains All Non-null
    @Test
    void printSalaries()
    {
        ArrayList<Employee> employees = new ArrayList<Employee>();
        Employee emp = new Employee();
        emp.emp_no = 1;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";
        emp.title = "Engineer";
        emp.salary = 55000;
        employees.add(emp);
        app.printSalaries(employees);
    }
}