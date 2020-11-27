package data_access;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Account;
import models.Employee;
import view_models.AccountVM;

import java.sql.*;

public class AccountDAO {

    private Connection conn;
    private PreparedStatement statement = null;
    private ResultSet result = null;

    public AccountDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean UpdateAccount(Account model){
        try{
            final String sql="UPDATE Account SET Username=?,Password=? where Id=?;";
            statement = conn.prepareStatement(sql);
            statement.setString(1,model.getUsername());
            statement.setString(2,model.getPassword());
            statement.setInt(3,model.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public ObservableList<Account> getAllAccounts() {
        ObservableList<Account> accounts = FXCollections.observableArrayList();


        final String sql = "SELECT a.id,a.Username,a.Password,e.FirstName,e.LastName,es.Name as State ,er.Name as Role\n" +
                "FROM account as a\n" +
                "inner join employee as e\n" +
                "on a.EmployeeId=e.id\n" +
                "inner join employeestate as es\n" +
                "on e.StateId=es.id\n" +
                "inner join employeerole as er\n" +
                "on e.RoleId=er.Id;";

        try {
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();

            while (result.next()) {
                Account acc = new Account();
                acc.setId(result.getInt("Id"));
                acc.setFirstName(result.getString("FirstName"));
                acc.setLastName(result.getString("LastName"));
                acc.setState(result.getString("State"));
                acc.setRole(result.getString("Role"));
                acc.setUsername(result.getString("Username"));
                acc.setPassword(result.getString("Password"));
                accounts.add(acc);
            }
            return accounts;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return accounts;

    }

    public AccountVM getDashBoardData() throws SQLException {
        AccountVM accountVM=new AccountVM();
        final String queryTotalAccounts="SELECT COUNT(account.Id) as Total\n" +
                "FROM account;";

        final String queryActiveAccounts="SELECT COUNT(a.Id) as Active\n" +
                "FROM account as a\n" +
                "inner join employee as e\n" +
                "on a.EmployeeId=e.Id\n" +
                "inner join employeestate as es\n" +
                "on e.StateId=es.id\n" +
                "where es.Name=\"Employeed\";";

        final String queryInactiveAccounts="SELECT COUNT(a.Id) as Inactive\n" +
                "FROM account as a\n" +
                "inner join employee as e\n" +
                "on a.EmployeeId=e.Id\n" +
                "inner join employeestate as es\n" +
                "on e.StateId=es.id\n" +
                "where es.Name=\"LEFT\";";

        try{
            statement = conn.prepareStatement(queryTotalAccounts);
            result=statement.executeQuery();
            result.absolute(1);
            int total = result.getInt("Total");
            accountVM.setTotalAccounts(total);

            statement = conn.prepareStatement(queryActiveAccounts);
            result=statement.executeQuery();
            result.absolute(1);
            int active = result.getInt("Active");
            accountVM.setActiveAccounts(active);


            statement = conn.prepareStatement(queryInactiveAccounts);
            result=statement.executeQuery();
            result.absolute(1);
            int inactive = result.getInt("Inactive");
            accountVM.setInactiveAccounts(inactive);

            return accountVM;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return  accountVM;
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
