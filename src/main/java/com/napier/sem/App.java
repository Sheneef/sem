package com.napier.sem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController

public class App {
    public static void main(String[] args) {
        // Create new Application and connect to database
        App app = new App();

        if (args.length < 1) {
            app.connect("localhost:33060", 0);
        } else {
            app.connect(args[0], Integer.parseInt(args[1]));
        }

        ArrayList<Employee> employees = app.getAllSalaries2();
        app.outputEmployees(employees, "Get All Employees.md");



        // Get Employee
        Employee emp = app.getEmployee(255530);
        // Display results
        app.displayEmployee(emp);
//
        // Extract employee salary information
        ArrayList<Employee> employees = app.getAllSalaries();

        // Test the size of the returned data - should be 240124
        //System.out.println(employees.size());

        //Display Results
        app.printSalaries(employees);

        // Extract employee salary information
        ArrayList<Employee> employees1 = app.getSalariesByRole("Engineer");

        //Display Results
        app.printSalaries(employees1);
//
//
//
//        // Commenting these out to test
//
        //Extract Depart No information
        Department dept = app.getDepartment("Sales");

        //Display Depart No Information
        app.displayDepartmentNo(dept);

        // Extract salary information in Specific Department
        ArrayList<Employee> employees3 = app.getSalariesByDepartment(dept);

        //Display Salary Information in Specific department
        app.printSalariesByDepartment(employees3);

        // Disconnect from database
        app.disconnect();
    }

    /**
     * Connection to MySQL database.
     */
    private static Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public static void connect(String location, int delay) {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        boolean shouldWait = false;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                if (shouldWait) {
                    // Wait a bit for db to start
                    Thread.sleep(delay);
                }

                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://" + location
                                + "/employees?allowPublicKeyRetrieval=true&useSSL=false",
                        "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + i);
                System.out.println(sqle.getMessage());

                // Let's wait before attempting to reconnect
                shouldWait = true;
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public static  void disconnect() {
        if (con != null) {
            try {
                // Close connection
                con.close();
            } catch (Exception e) {
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
                    "SELECT emp_no, first_name, last_name "
                            + "FROM employees "
                            + "WHERE emp_no = " + ID;
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

//       public Employee getEmployee(int ID) {
//        try {
//            // Create an SQL statement
//            Statement stmt = con.createStatement();
//            // Create string for SQL statement
//            String strSelect =
//
//                    "select e.emp_no, e.first_name, e.last_name, t.title, dt.dept_no, "
//                            + "(select emp_no from dept_manager where dept_no = dt.dept_no and to_date='9999-01-01') as ManagerID,"
//                            + "(select dept_name from departments where dept_no = dt.dept_no) as dept_name, "
//                            + "(select salary from salaries where emp_no = "+ ID +" and to_date='9999-01-01') as salary,"
//                            + "(Select concat(first_name, ' ' ,last_name) from employees where emp_no = ManagerID) as manager "
//                            + "From employees as e "
//                            + "inner join titles as t on e.emp_no = t.emp_no "
//                            + "inner join dept_emp as dm on e.emp_no = dm.emp_no "
//                            + "inner join departments as dt on dm.dept_no = dt.dept_no "
//                            + "WHERE e.emp_no = " + ID;
//            // Execute SQL statement
//            ResultSet rset = stmt.executeQuery(strSelect);
//            // Return new employee if valid.
//            // Check one is returned
//            if (rset.next()) {
//                Employee emp = new Employee();
//                emp.emp_no = rset.getInt("emp_no");
//                emp.first_name = rset.getString("first_name");
//                emp.last_name = rset.getString("last_name");
//                //emp.manager = rset.getString("manager");
//                emp.title = rset.getString("title");
//                //emp.dept = rset.getString("dept");
//                emp.salary = rset.getInt("salary");
//                return emp;
//            } else
//                return null;
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            System.out.println("Failed to get employee details");
//            return null;
//        }
//    }

    public void displayEmployee(Employee emp) {
        if (emp != null) {
            System.out.println(
                    emp.emp_no + " "
                            + emp.first_name + " "
                            + emp.last_name + "\n"
                            + emp.title + "\n"
                            + "Salary:" + emp.salary + "\n"
                            + emp.dept + "\n"
                            + "Manager: " + emp.manager + "\n");
        }
    }

    /**
     * Gets all the current employees and salaries.
     *
     * @return A list of all employees and salaries, or null if there is an error.
     */
    public ArrayList<Employee> getAllSalaries() {
        try {
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
            System.out.println("Failed to get salary details");
            return null;
        }
    }

    /**
     * Prints a list of employees.
     *
     * @param employees The list of employees to print.
     */
    public void printSalaries(ArrayList<Employee> employees)
    {
        // Check employees is not null
        if (employees == null)
        {
            System.out.println("No employees");
            return;
        }
        // Print header
        System.out.println(String.format("%-10s %-15s %-20s %-8s", "Emp No", "First Name", "Last Name", "Salary"));
        // Loop over all employees in the list
        for (Employee emp : employees)
        {
            if (emp == null)
                continue;
            String emp_string =
                    String.format("%-10s %-15s %-20s %-8s",
                            emp.emp_no, emp.first_name, emp.last_name, emp.salary);
            System.out.println(emp_string);
        }
    }

    /**
     * Gets all the current employees and salaries.
     *
     * @return A list of all employees and salaries, or null if there is an error.
     */
    public ArrayList<Employee> getSalariesByRole(String title) {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
                            + "FROM employees, salaries, titles "
                            + "WHERE employees.emp_no = salaries.emp_no "
                            + "AND employees.emp_no = titles.emp_no "
                            + "AND salaries.to_date = '9999-01-01'"
                            + "AND titles.to_date = '9999-01-01' "
                            + "AND titles.title = '" + title + "' "
                            + " ORDER BY employees.emp_no ASC Limit 10";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Employee> employees1 = new ArrayList<Employee>();
            while (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary = rset.getInt("salaries.salary");
                employees1.add(emp);
            }
            return employees1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
            return null;
        }
    }

    /**
     * Prints a list of employees.
     *
     * @param employees1 The list of employees to print.
     */
    public void printSalariesByRole(ArrayList<Employee> employees1) {

        // Check employees is not null
        if (employees1 == null)
        {
            System.out.println("No employees");
            return;
        }
        // Print header
        System.out.println(String.format("%-10s %-15s %-20s %-8s", "Emp No", "First Name", "Last Name", "Salary"));
        // Loop over all employees in the list
        for (Employee emp : employees1) {
            if (emp == null)
                continue;
            String emp_string =
                    String.format("%-10s %-15s %-20s %-8s",
                            emp.emp_no, emp.first_name, emp.last_name, emp.salary);
            System.out.println(emp_string);
        }
    }

    public Department getDepartment(String dept_name) {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "select d.dept_no, dm.emp_no, concat(e.first_name,' ', e.last_name) as manager "
                            + "From departments as d "
                            + "inner join dept_manager as dm on d.dept_no = dm.dept_no "
                            + "inner join employees as e on dm.emp_no = e.emp_no "
                            + "WHERE dm.to_date = '9999-01-01' AND d.dept_name = '"+ dept_name +"'" ;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new Department Number if valid.
            // Check one is returned
            if (rset.next()) {
                Department dep = new Department();
                dep.dept_no = rset.getString("dept_no");
                //dep.manager = rset.getString("manager");
                return dep;
            } else
                return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get Department Name details");
            return null;
        }
    }

    public void displayDepartmentNo(Department dep) {
        if (dep != null) {
            System.out.println(dep.dept_no + " " + dep.manager);

        }
    }

// Get Salaries By Department




    public ArrayList<Employee> getSalariesByDepartment(Department dept) {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
                            + "FROM employees, salaries, dept_emp, departments "
                            + "WHERE employees.emp_no = salaries.emp_no "
                            + "AND employees.emp_no = dept_emp.emp_no "
                            + "AND dept_emp.dept_no = departments.dept_no "
                            + "AND salaries.to_date = '9999-01-01' "
                            + "AND departments.dept_no = '" + dept.dept_no+"' "
                            + " ORDER BY employees.emp_no ASC Limit 10";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Employee> employees3 = new ArrayList<Employee>();
            while (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary = rset.getInt("salaries.salary");
                employees3.add(emp);
            }
            return employees3;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
            return null;
        }
    }

    /**
     * Prints a list of employees.
     *
     * @param employees3 The list of employees to print.
     */
    public void printSalariesByDepartment(ArrayList<Employee> employees3) {

        // Check employees is not null
        if (employees3 == null)
        {
            System.out.println("No employees");
            return;
        }

        // Print header
        System.out.println(String.format("%-10s %-15s %-20s %8s", "Emp No", "First Name", "Last Name", "Salary"));
        // Loop over all employees in the list
        for (Employee emp : employees3) {

            if (emp == null)
                continue;

            String emp_string =
                    String.format("%-10s %-15s %-20s %8s",
                            emp.emp_no, emp.first_name, emp.last_name, emp.salary);
            System.out.println(emp_string);
        }
    }

    public void addEmployee(Employee emp)
    {
        try
        {
            Statement stmt = con.createStatement();
            String strUpdate =
                    "INSERT INTO employees (emp_no, first_name, last_name, birth_date, gender, hire_date) " +
                            "VALUES (" + emp.emp_no + ", '" + emp.first_name + "', '" + emp.last_name + "', " +
                            "'9999-01-01', 'M', '9999-01-01')";
            stmt.execute(strUpdate);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to add employee");
        }
    }

    // Get All Salaries Code # 2 to mark down

    public ArrayList<Employee> getAllSalaries2() {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
                            + "FROM employees, salaries "
                            + "WHERE employees.emp_no = salaries.emp_no AND salaries.to_date = '9999-01-01' "
                            + "ORDER BY employees.emp_no ASC "
                            + "LIMIT 20 ";
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
            System.out.println("Failed to get salary details");
            return null;
        }
    }

    /**
     * Outputs to Markdown
     *
     * @param employees
     */
    public void outputEmployees(ArrayList<Employee> employees, String filename) {
        // Check employees is not null
        if (employees == null) {
            System.out.println("No employees");
            return;
        }

        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| Emp No | First Name | Last Name |  Salary |\r\n");
        sb.append("| --- | --- | --- | --- | \r\n");
        // Loop over all employees in the list
        for (Employee emp : employees) {
            if (emp == null) continue;
            sb.append("| " + emp.emp_no + " | " +
                    emp.first_name + " | " + emp.last_name + " | " +
                   + emp.salary + " | \r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new                                 File("./reports/" + filename)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}


