package dao;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import model.Client;

public class Clientdao implements daoInterface<Client> {

    @Override
    public Client findById(int idClient) throws Exception {
        String sql = "SELECT * FROM client WHERE idClient = ?";
        try(PreparedStatement ps = Db_connection.getInstance().getConnection().prepareStatement(sql)){
            ps.setInt(1, idClient);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new Client(
                            rs.getInt("idClient"),
                            rs.getString("nom"),
                            rs.getString("email"),
                            rs.getString("password") //  added
                    );
                }
                return null;
            }
        }
    }

    @Override
    public List<Client> findAll() throws Exception {
        List<Client> clientslist = new ArrayList<>();
        String sql = "SELECT * FROM client";
        try(Statement stt = Db_connection.getInstance().getConnection().createStatement();
            ResultSet rs = stt.executeQuery(sql)){
            while(rs.next()){
                clientslist.add(new Client(
                        rs.getInt("idClient"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("password") //  added
                ));
            }
            return clientslist;
        }
    }

    @Override
    public int insert(Client client) throws Exception {
        String sql = "INSERT INTO client(nom, email, password) VALUES (?,?,?)"; // added password
        try(PreparedStatement ps = Db_connection.getInstance().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, client.getNom());
            ps.setString(2, client.getEmail());
            ps.setString(3, hashPassword(client.getPassword())); // added
            int rows = ps.executeUpdate();
            if(rows == 1){
                try(ResultSet key = ps.getGeneratedKeys()){
                    if(key.next()){
                        int id = key.getInt(1);
                        client.setId_client(id);
                        return id;
                    }
                }
            }
        }
        return -1;
    }

    @Override
    public boolean update(Client client) throws Exception {
        String sql = "UPDATE client SET nom=?, email=?, password=? WHERE idClient=?"; // added password
        try(PreparedStatement ps = Db_connection.getInstance().getConnection().prepareStatement(sql)){
            ps.setString(1, client.getNom());
            ps.setString(2, client.getEmail());
            ps.setString(3, client.getPassword());
            ps.setInt(4, client.getId_client());
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean delete(int idClient) throws Exception {
        String sql = "DELETE FROM client WHERE idClient = ?";
        try(PreparedStatement ps = Db_connection.getInstance().getConnection().prepareStatement(sql)){
            ps.setInt(1, idClient);
            return ps.executeUpdate() == 1;
        }
    }


    public boolean updatePassword(int idClient, String newPassword) throws Exception {
        String sql = "UPDATE client SET password=? WHERE idClient=?";
        try(PreparedStatement ps = Db_connection.getInstance().getConnection().prepareStatement(sql)){
            Client temp = new Client();
            temp.setPassword(newPassword);
            ps.setString(1, temp.getPassword());
            ps.setInt(2, idClient);
            return ps.executeUpdate() == 1;
        }
    }


}



