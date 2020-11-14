package data_access;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Customer;
import models.Employee;

import java.sql.*;

public class EmployeeDAO {
    private Connection conn;
    private PreparedStatement statement = null;
    private ResultSet result = null;

    public EmployeeDAO(Connection conn) {
        this.conn = conn;
    }

    public ObservableList<Employee> getAllEmployees() throws SQLException {
        ObservableList<Employee> employees = FXCollections.observableArrayList();

        final String sql = "SELECT e.Id,e.FirstName,e.LastName,e.DOB,e.Address,e.ContactNo,e.Email,g.Name as gender,es.Name as State,er.Name as role FROM \n" +
                "employee as e\n" +
                "inner join employeerole as er\n" +
                "on e.RoleId=er.Id\n" +
                "inner join employeestate as es\n" +
                "on e.StateId=es.Id\n" +
                "inner join gender as g\n" +
                "on e.GenderId=g.Id;";

        try {
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()) {
                Employee emp = new Employee();
                emp.setId(result.getInt("Id"));
                emp.setFirstName(result.getString("FirstName"));
                emp.setLastName(result.getString("LastName"));
                emp.setDob(result.getDate("DOB"));
                emp.setAddress(result.getString("Address"));
                emp.setContactNo(result.getString("ContactNo"));
                emp.setEmail(result.getString("Email"));
                emp.setGender(result.getString("gender"));
                emp.setState(result.getString("State"));
                emp.setRole(result.getString("Role"));
                employees.add(emp);
            }
            return employees;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return employees;
    }

    //Update an existing Employee
    public boolean UpdateEmployee(Employee model) throws SQLException {
        GenderDAO genderDAO = null;
        EmployeeStateDAO employeeStateDAO = null;
        EmployeeRoleDAO employeeRoleDAO = null;

        final String sql = "UPDATE Employee SET FirstName=?,LastName=?,DOB=?,Address=?,ContactNo=?,GenderId=?,Email=?,StateId=?,Image=?,RoleId=? WHERE Id=?;";
        try {
            conn.setAutoCommit(false);

            //Get ID from Gender based on Gender type
            genderDAO = new GenderDAO(conn);
            int genderId = genderDAO.getGenderIdByName(model.getGender());

            //Get ID from Employee state based on name
            employeeStateDAO = new EmployeeStateDAO(conn);
            int employeeStateId = employeeStateDAO.getEmployeeStateIdByName(model.getState());

            //Get ID from Employee role based on eole
            employeeRoleDAO = new EmployeeRoleDAO(conn);
            int employeeroleId = employeeRoleDAO.getEmployeeRoleIdByName(model.getRole());

            statement = conn.prepareStatement(sql);
            statement.setString(1, model.getFirstName());
            statement.setString(2, model.getLastName());
            statement.setDate(3, model.getDob());
            statement.setString(4, model.getAddress());
            statement.setString(5, model.getContactNo());
            statement.setInt(6, genderId);
            statement.setString(7, model.getEmail());
            statement.setInt(8, employeeStateId);
            statement.setBlob(9, model.getImage());
            statement.setInt(10, employeeroleId);
            statement.setInt(11, model.getId());
            statement.executeUpdate();
            conn.commit();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            conn.rollback();
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
                if (genderDAO != null)
                    genderDAO.close();
                if (employeeStateDAO != null)
                    employeeStateDAO.close();
                if (employeeRoleDAO != null)
                    employeeRoleDAO.close();
                if (conn != null)
                    ConnectionResources.close(conn);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    //Create New Employee

    public boolean CreateEmployee(Employee model) throws SQLException {
        GenderDAO genderDAO = null;
        EmployeeStateDAO employeeStateDAO = null;
        EmployeeRoleDAO employeeRoleDAO = null;

        final String sql = "INSERT INTO Employee (FirstName,LastName,DOB,Address,ContactNo,GenderId,Email,StateId,Image,RoleId) Values(?,?,?,?,?,?,?,?,?,?);";
        try {
            conn.setAutoCommit(false);

            //Get ID from Gender based on Gender type
            genderDAO = new GenderDAO(conn);
            int genderId = genderDAO.getGenderIdByName(model.getGender());

            //Get ID from Employee state based on name
            employeeStateDAO = new EmployeeStateDAO(conn);
            int employeeStateId = employeeStateDAO.getEmployeeStateIdByName(model.getState());

            //Get ID from Employee role based on eole
            employeeRoleDAO = new EmployeeRoleDAO(conn);
            int employeeroleId = employeeRoleDAO.getEmployeeRoleIdByName(model.getRole());

            statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, model.getFirstName());
            statement.setString(2, model.getLastName());
            statement.setDate(3, (Date) model.getDob());
            statement.setString(4, model.getAddress());
            statement.setString(5, model.getContactNo());
            statement.setInt(6, genderId);
            statement.setString(7, model.getEmail());
            statement.setInt(8, employeeStateId);
            statement.setBlob(9, model.getImage());
            statement.setInt(10, employeeroleId);
            statement.executeUpdate();
            conn.commit();

            if(model.getRole()=="Manager" ||model.getRole()=="Owner" ||model.getRole()=="Receptionist")
                return true;

            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            int customerId = keys.getInt(1);
            final String sql2 = "INSERT INTO Account (EmployeeId) values (?);";
            statement = conn.prepareStatement(sql2);
            statement.setInt(1, customerId);
            statement.executeUpdate();
            conn.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            conn.rollback();
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
                if (genderDAO != null)
                    genderDAO.close();
                if (employeeStateDAO != null)
                    employeeStateDAO.close();
                if (employeeRoleDAO != null)
                    employeeRoleDAO.close();
                if (conn != null)
                    ConnectionResources.close(conn);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    public Blob getEmployeeImage(int id) {
        final String sql = "SELECT Image FROM employee WHERE Id=?";
        try {
            statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            result = statement.executeQuery();
            // Navigate to first row
            result.absolute(1);
            //Get first row data
            Blob image = result.getBlob("image");
            return image;
        } catch (SQLException throwables) {
            return null;
        }
    }


    public void close() throws SQLException {
        try {
            ConnectionResources.close(result, statement, conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
