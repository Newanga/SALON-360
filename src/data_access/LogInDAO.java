package data_access;

import helpers.Encryption.SHA512;
import main.CurrentUserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogInDAO {

    private Connection conn;
    private PreparedStatement statement = null;
    private ResultSet result = null;

    public LogInDAO(Connection conn) {
        this.conn=conn;
    }

    public Boolean ValidateUser(String username,String pass) throws SQLException{
        String passHash= SHA512.encryptThisString(pass);
        final String sql="Select a.id as accId,e.Id as empId,e.Email,er.Name as role ,concat(e.FirstName,' ',e.LastName) as FullName\n" +
                "from account as a\n" +
                "inner join employee as e\n" +
                "on a.EmployeeId=e.id\n" +
                "inner join employeestate as es\n" +
                "on e.StateId=es.id\n" +
                "inner join employeerole er\n" +
                "on e.RoleId=er.Id\n" +
                "where es.Name=\"Employeed\" AND a.Username=? AND a.Password=?;";


        try{
            statement= conn.prepareStatement(sql);
            statement.setString(1,username);
            statement.setString(2,passHash);
            result=statement.executeQuery();
            if (result!=null){
                result.absolute(1);
                CurrentUserData.setAccId(result.getString("accId"));
                CurrentUserData.setEmpId(result.getString("empID"));
                CurrentUserData.setRole(result.getString("role"));
                CurrentUserData.setFullName(result.getString("FullName"));
                CurrentUserData.setEmail(result.getString("Email"));
                return true;
            }
            return false;
        }catch (SQLException ex){
            ex.printStackTrace();
            return false;
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
