package services;
import model.Client;
import dao.Clientdao;
import java.util.List;

public class ClientService {
    private final EmailService emailService = new EmailService();
    private final Clientdao dao = new Clientdao();

    public Client getclient(Client c) throws Exception {
        return dao.findById(c.getId_client());
    }
    public List<Client> showClients() throws Exception {
        return dao.findAll();
    }
    public Client CreateClient(Client c) throws Exception {
        dao.insert(c);
        return c;
    }
    public boolean uodateClient(Client c) throws Exception {
        return dao.update(c);
    }
    public boolean deleteClient(int idClient) throws Exception {
        return dao.delete(idClient);
    }

    public boolean resetPassword(int idClient, String newPassword) throws Exception {
        Client client = dao.findById(idClient);
        if(client == null) return false;

        boolean updated = dao.updatePassword(idClient, newPassword); // updates DB
        if(updated){
            emailService.sendPasswordReset(    // sends email
                    client.getEmail(),
                    client.getNom(),
                    newPassword
            );
        }
        return updated;
    }
}