package services;
import java.util.List;
import dao.VenteArtdao;
import model.VenteArt;
public class VenteArtService {
    private final EmailService emailService = new EmailService(); //email
    private final VenteArtdao dao = new VenteArtdao();
    public VenteArt findVente(int idVente) throws Exception{
        return dao.findById(idVente);
    }
    public List<VenteArt> showAllventes() throws Exception{
        return dao.findAll();
    }
    public VenteArt newVente(VenteArt venteart) throws Exception{
        dao.insert(venteart);
        emailService.sendPurchaseConfirmation(
                venteart.getClient().getEmail(),
                venteart.getClient().getNom(),
                venteart.getOuvre().getTitre(),
                venteart.getOuvre().getPrix()
        );

        return venteart;
    }
    public boolean updateVente(VenteArt venteart) throws Exception{
        return dao.update(venteart);
    }
    public boolean deleteUpdate(int idVente) throws Exception{
        return dao.delete(idVente);
    }
    public List<String> afficherTopArtiste() throws Exception{
return dao.afficherTopArtiste();
    }

}
