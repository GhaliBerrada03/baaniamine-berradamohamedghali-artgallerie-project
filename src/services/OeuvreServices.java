package services;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dao.Db_connection;
import dao.Oeuvredao;
import model.Oeuvre;
public class OeuvreServices {
    private final Oeuvredao dao = new Oeuvredao();
    public Oeuvre findOeuvre(int idOeuvre) throws Exception{
        return dao.findById(idOeuvre);
    }
    public List<Oeuvre> findAllOeuvre() throws Exception{
        return dao.findAll();
    }
    public Oeuvre insert(Oeuvre oeuvre) throws Exception{
        dao.insert(oeuvre);
        return oeuvre;
    }
    public boolean updateOeuvre(Oeuvre oeuvre) throws Exception{
        return dao.update(oeuvre);
    }
    public boolean deleteOeuvre(int idOeuvre) throws Exception{
        return dao.delete(idOeuvre);
    }
    public List<Oeuvre> filtreByArtist(String artiste) throws Exception{
        return dao.filtreByArtist(artiste);
    }
    public List<Oeuvre> filtreByCategory(String categorie) throws Exception{
        return dao.filtreByCategory(categorie);
    }
    public Boolean updatedstatut(int idOeuvre) throws Exception{
        return dao.updatedstatut(idOeuvre);
    }
}
