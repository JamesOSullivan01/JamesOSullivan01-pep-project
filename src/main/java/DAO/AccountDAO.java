package DAO;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    Connection connection = ConnectionUtil.getConnection();

        public Account registerNewUser(Account account){
                try{
                    String SQL = "INSERT INTO account (username, password) VALUES (?,?)";
                    PreparedStatement ps = connection.prepareStatement(SQL,  java.sql.Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, account.getUsername());
                    ps.setString(2, account.getPassword());
                    ps.executeUpdate();

                    ResultSet generatedKeys = ps.getGeneratedKeys();
                    if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    account.setAccount_id(generatedId);
                    return new Account(generatedId, account.getUsername(), account.getPassword());
                }

                } catch(Exception e){
                    e.printStackTrace();
                }
                    return null;
        }

        public Account loginToAccount(Account account){
            try {
                connection = ConnectionUtil.getConnection();
                String SQL = "SELECT * FROM Account WHERE username = ? AND password = ?";
                PreparedStatement ps = connection.prepareStatement(SQL);
                ps.setString(1, account.getUsername());
                ps.setString(2, account.getPassword());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                     int accountId = rs.getInt("account_id"); 
                     String username = rs.getString("username"); 
                     String password = rs.getString("password");
                    return new Account(accountId, username, password);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }
}
