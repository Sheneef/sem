package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
        app.connect("localhost:33060", 30000);

    }

    @Test
    void testAddEmployee()
    {
        Employee emp = new Employee();
        emp.emp_no = 500000;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";
        app.addEmployee(emp);
        emp = app.getEmployee(500000);
        assertEquals(emp.emp_no, 500000);
        assertEquals(emp.first_name, "Kevin");
        assertEquals(emp.last_name, "Chalmers");
    }

    @Test
    void testGetEmployee()
    {
        Employee emp = app.getEmployee(255530);
        assertEquals(emp.emp_no, 255530);
        assertEquals(emp.first_name, "Ronghao");
        assertEquals(emp.last_name, "Garigliano");

    }
//
    @Test
    void testdisplayEmployee()
    {
        Employee emp = app.getEmployee(255530);
        assertEquals(emp.emp_no, 255530);
        assertEquals(emp.first_name, "Ronghao");
        assertEquals(emp.last_name, "Garigliano");
    }
//
//    @Test
//    void getAllSalaries()
//    {
//        ArrayList<Employee>employees = new ArrayList<Employee>()
//        Employee emp = new Employee();
//        employees.emp(emp);
//        app.getEmployee(1001);
//        assertEquals(emp.emp_no, 5000);
//        assertEquals(emp.first_name, "Kevin");
//        assertEquals(emp.last_name, "Chalmers");
//        assertEquals(emp.salary, 55000);
//    }


    @Test
    void testgetAllSalaries()
    {
        ArrayList<Employee>employees = new ArrayList<Employee>();
        Employee emp = app.getEmployee(10001);
        assertEquals(emp.emp_no, 10001);
        assertEquals(emp.first_name, "Georgi");
        assertEquals(emp.last_name, "Facello");
        app.getEmployee(1001);
    }

//    @Test
//    void testdisplayEmployee()
//    {
//        Employee emp = app.getEmployee(255530);
//        assertEquals(emp.emp_no, 255530);
//        assertEquals(emp.first_name, "Ronghao");
//        assertEquals(emp.last_name, "Garigliano");
//    }

}