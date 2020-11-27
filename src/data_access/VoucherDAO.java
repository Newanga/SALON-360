package data_access;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Voucher;
import view_models_dashboard.VoucherVM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VoucherDAO {

    private Connection conn;
    private PreparedStatement statement = null;
    private ResultSet result = null;

    public VoucherDAO(Connection conn) {
        this.conn = conn;
    }

    public ObservableList<Voucher> getAllVouchers() throws SQLException {
        ObservableList<Voucher> voucherslist = FXCollections.observableArrayList();
        final String sql ="SELECT v.Id,v.Amount,v.DateAdded,v.SpecialNotes,vs.Name as State\n" +
                "FROM voucher as v\n" +
                "inner join voucherstate as vs\n" +
                "on v.StateId=vs.Id;";

        try {
            statement = conn.prepareStatement(sql);
            result = statement.executeQuery();
            while (result.next()) {
                Voucher vouc = new Voucher();
                vouc.setId(result.getInt("id"));
                vouc.setAmount(result.getInt("amount"));
                vouc.setSpecialNotes(result.getString("specialnotes"));
                vouc.setDateAdded(result.getDate("dateadded"));
                vouc.setState(result.getString("state"));
                voucherslist.add(vouc);
            }
            return voucherslist;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return voucherslist;
    }


    public Boolean UpdateVoucher(Voucher model) throws SQLException {
        VoucherStateDAO vsdao = null;

        final String sql = "Update voucher SET Amount=?,DateAdded=?,SpecialNotes=?,StateId=? where Id=?;";

        try{
            conn.setAutoCommit(false);

            //Get ID from servicestate based on state name
            vsdao = new VoucherStateDAO(conn);
            int voucherStateId = vsdao.getVoucherStateIdByName(model.getState());

            statement=conn.prepareStatement(sql);
            statement.setInt(1,model.getAmount());
            statement.setDate(2,model.getDateAdded());
            statement.setString(3,model.getSpecialNotes());
            statement.setInt(4,voucherStateId);
            statement.setInt(5,model.getId());
            statement.execute();
            conn.commit();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            conn.rollback();
            return false;
        }finally {
            conn.setAutoCommit(true);
            try{
                if(vsdao!=null)
                    vsdao.close();
                ConnectionResources.close(conn);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public Boolean CreateNewVoucher(Voucher model) throws SQLException {
        VoucherStateDAO vsdao = null;

        final String sql = "INSERT INTO voucher (Amount,DateAdded,SpecialNotes,StateId) values (?,?,?,?);";

        try{
            conn.setAutoCommit(false);

            //Get ID from voucherstate based on state name
            vsdao = new VoucherStateDAO(conn);
            int voucherStateId = vsdao.getVoucherStateIdByName(model.getState());

            statement=conn.prepareStatement(sql);
            statement.setInt(1,model.getAmount());
            statement.setDate(2,model.getDateAdded());
            statement.setString(3,model.getSpecialNotes());
            statement.setInt(4,voucherStateId);
            statement.execute();
            conn.commit();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            conn.rollback();
            return false;
        }finally {
            conn.setAutoCommit(true);
            try{
                if(vsdao!=null)
                    vsdao.close();
                ConnectionResources.close(conn);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public void close() throws SQLException {
        try {
            ConnectionResources.close(result, statement, conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public VoucherVM getDashBoardData() {
        VoucherVM voucherVM=new VoucherVM();

        final String activeVoucher="SELECT COUNT(v.Id) as Valid\n" +
                                    "FROM voucher as v\n" +
                                    "inner join voucherstate as vs\n" +
                                    "on v.StateId=vs.Id\n" +
                                    "where vs.Name=\"Valid\";";

        try{
            statement= conn.prepareStatement(activeVoucher);
            result =statement.executeQuery();
            result.absolute(1);
            int totalValid = result.getInt("Valid");
            voucherVM.setActiveVouchers(totalValid);
            return voucherVM;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return voucherVM;
        }
    }
}
