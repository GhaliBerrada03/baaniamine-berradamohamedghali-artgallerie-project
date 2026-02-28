package model;
import java.sql.Timestamp;
import java.time.*;
public class VenteArt {
    private int idVente;
private Client client ;
private Oeuvre ouvre;
private LocalDateTime dateVente;
//constructor
public VenteArt(Client client, Oeuvre ouvre, LocalDateTime dateVente){
    this.client = client;
    this.ouvre = ouvre;
    this.dateVente = dateVente;
}
    public VenteArt(int idVente, Client client, Oeuvre ouvre, LocalDateTime dateVente){
        this(client,ouvre,dateVente);
        this.idVente = idVente;
    }

//getters ND SETTERS
    public Client getClient(){
return client;    }
    public void setClient(Client client){
    this.client = client;
    }
    public Oeuvre getOuvre(){
        return ouvre;    }
    public void setOuvre(Oeuvre ouvre){
        this.ouvre = ouvre;
    }
    public LocalDateTime getDateVente(){
        return dateVente;    }
    public void setDateVente(LocalDateTime dateVente){
        this.dateVente = dateVente;
    }
    public int getIdVente(){
        return idVente;    }
    public void setIdVente(int idVente){
        this.idVente = idVente;
    }
}