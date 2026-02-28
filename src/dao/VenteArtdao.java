package dao;

import model.*;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VenteArtdao implements daoInterface<VenteArt> {
    @Override
    public VenteArt findById(int idVente) throws Exception {
        String sql = " SELECT * FROM vente_art WHERE idVente = ?";
        try (PreparedStatement ps = Db_connection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, idVente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Client client = new Clientdao().findById(rs.getInt("idClient"));
                    Oeuvre Oeuvre = new Oeuvredao().findById(rs.getInt("idOeuvre"));
                    return new VenteArt(
                            rs.getInt("idVente"),
                            client,
                            Oeuvre,
                            rs.getTimestamp("date_vente").toLocalDateTime());
                }
            }
            return null;

        }
    }

    @Override
    public List<VenteArt> findAll() throws Exception {
        List<VenteArt> venteArts = new ArrayList<>();
        String sql = "SELECT * FROM vente_art";
        try (Statement stt = Db_connection.getInstance().getConnection().createStatement();
                ResultSet rs = stt.executeQuery(sql)) {
            while (rs.next()) {
                Client client = new Clientdao().findById(rs.getInt("idClient"));
                Oeuvre Oeuvre = new Oeuvredao().findById(rs.getInt("idOeuvre"));
                VenteArt venteArt = new VenteArt(
                        rs.getInt("idVente"),
                        client,
                        Oeuvre,
                        rs.getTimestamp("date_vente").toLocalDateTime());
                venteArts.add(venteArt);
            }
            return venteArts;
        }

    }

    @Override
    public int insert(VenteArt venteart) throws Exception {
        String sql = "INSERT INTO vente_art(idClient,idOeuvre,date_vente)VALUES (?,?,?)";
        try (PreparedStatement ps = Db_connection.getInstance().getConnection().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, venteart.getClient().getId_client());
            ps.setInt(2, venteart.getOuvre().getIdOeuvre());
            ps.setTimestamp(3, Timestamp.valueOf(venteart.getDateVente()));
            int rows = ps.executeUpdate();
            if (rows == 1) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    new Oeuvredao().updatedstatut(venteart.getOuvre().getIdOeuvre());
                    if (keys.next()) {
                        int key = keys.getInt(1);
                        venteart.setIdVente(key);
                        return key;
                    }
                }
            }
        }
        return -1;
    }

    @Override
    public boolean update(VenteArt venteart) throws Exception {
        String sql = "UPDATE  vente_art SET idClient=? , idOeuvre=? , date_vente=? WHERE idVente = ?";
        try (PreparedStatement ps = Db_connection.getInstance().getConnection().prepareStatement(sql)) {

            ps.setInt(1, venteart.getClient().getId_client());
            ps.setInt(2, venteart.getOuvre().getIdOeuvre());
            ps.setTimestamp(3, Timestamp.valueOf(venteart.getDateVente()));
            // ❌ LocalDateTime ≠ sql.Date
            ps.setInt(4, venteart.getIdVente());

            return ps.executeUpdate() == 1;
        }

    }

    @Override
    public boolean delete(int idVente) throws Exception {
        String sql = "DELETE FROM vente_art WHERE idVente = ?";
        try (PreparedStatement ps = Db_connection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, idVente);
            return ps.executeUpdate() == 1;
        }
    }

    public List<String> afficherTopArtiste() throws Exception {
        String sql = "SELECT o.artiste, COUNT(*) AS nbventes FROM vente_art v " +
                "JOIN oeuvre o ON v.idOeuvre = o.idOeuvre " +
                "GROUP BY o.artiste " +
                "ORDER BY nbVentes DESC";
        try (Statement stt = Db_connection.getInstance().getConnection().createStatement();
                ResultSet rs = stt.executeQuery(sql)) {
            List<String> result = new ArrayList<>();
            while (rs.next()) {
                result.add(rs.getString("artiste") + "|" + rs.getInt("nbventes") + "ventes");
            }
            return result;
        }

    }

}