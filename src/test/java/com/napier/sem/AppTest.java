//package com.napier.sem;
//
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//
//public class AppTest
//{
//    static App app;
//
//    @BeforeAll
//    static void init()
//    {
//        app = new App();
//    }
//
//    @Test
//    void printSalariesTestNull()
//    {
//        app.printSalaries(null);
//    }
//
//    @Test
//    void printSalariesTestContainsNull()
//    {
//        ArrayList<Employee> employess = new ArrayList<Employee>();
//        employess.add(null);
//        app.printSalaries(employess);
//    }
//
//
//
//
//    @Test
//    void printSalaries()
//    {
//        ArrayList<Employee> employees = new ArrayList<Employee>();
//        Employee emp = new Employee();
//        emp.emp_no = 1;
//        emp.first_name = "Kevin";
//        emp.last_name = "Chalmers";
//        emp.title = "Engineer";
//        emp.salary = 55000;
//        employees.add(emp);
//        app.printSalaries(employees);
//    }
//
//    @Test
//    void displayEployee()
//    {
//        app.displayEmployee(null);
//    }
//
//    @Test
//    void displayEmployeeTestEmpty() {
//        Employee emp = new Employee();
//        app.displayEmployee(emp);
//    }
//
//
////
////    @Test
////    void printSalariesbyRoleTestNull()
////    {
////        app.printSalariesByRole(null);
////    }
////    @Test
////    void printSalariesByRoleTestNull(){
////        ArrayList<Employee> employees1 = new ArrayList<Employee>();
////        employees1.add(null);
////        app.printSalariesByRole(employees1);
////
////    }
//
////    @Test
////    void printSalariesByRole()
////    {
////        ArrayList<Employee> employees1 = new ArrayList<Employee>();
////        Employee emp = new Employee();
////        emp.emp_no = 1;
////        emp.first_name = "Kevin";
////        emp.last_name = "Chalmers";
////        emp.title = "Engineer";
////        emp.salary = 55000;
////        employees1.add(emp);
////        app.printSalariesByRole(employees1);
////    }
//
//
////Print Salaries by Department
//
//    @Test
//    void printSalariesByDepartmentTestnull()
//    {
//        app.printSalariesByDepartment(null);
//    }
//
//    @Test
//    void printSalariesByDepartmentTEST(){
//        ArrayList<Employee> employees3 = new ArrayList<Employee>();
//        employees3.add(null);
//        app.printSalariesByDepartment(employees3);
//
//    }
//
//    @Test
//    void printSalariesByDepartment()
//    {
//        ArrayList<Employee> employees3 = new ArrayList<Employee>();
//        Employee emp = new Employee();
//        emp.emp_no = 1;
//        emp.first_name = "Kevin";
//        emp.last_name = "Chalmers";
//        emp.title = "Engineer";
//        emp.salary = 55000;
//        employees3.add(emp);
//        app.printSalariesByDepartment(employees3);
//    }
//
//
//}
//
