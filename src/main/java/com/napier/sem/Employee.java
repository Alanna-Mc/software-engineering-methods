package com.napier.sem;

/**
 * Represents an employee
 */
public class Employee
{
    /**
     * Employee number
     */
    public int emp_no;

    /**
     * Employee's first name
     */
    public String first_name;

    /**
     * Employee's last name
     */
    public String last_name;

    /**
     * Employee's job title
     */
    public String title;

    /**
     * Employee's salary
     */
    public int salary;

    /**
     * Employee's current department
     */
    public Department dept;

    /**
     * Employee's manager
     */
    public Employee manager;

    /**
     * Employee's birth date
     */
    public String birth_date; // Use String for simplicity, or Date if you handle date parsing

    /**
     * Employee's gender
     */
    public String gender; // Typically 'M' or 'F'

    /**
     * Employee's hire date
     */
    public String hire_date;
}