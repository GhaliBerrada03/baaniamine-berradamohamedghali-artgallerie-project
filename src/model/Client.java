package model;
public class Client {
    private int idClient;
    private String nom;
    private String email;
    private String password; //  added

    public Client(){}
    public Client(String nom, String email){
        this.nom = nom;
        this.email = email;
    }
    public Client(int idClient, String nom, String email){
        this(nom, email);
        this.idClient = idClient;
    }
    public Client(int idClient, String nom, String email, String password){
        this(idClient, nom, email);
        this.password = password;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return idClient == client.idClient &&
                java.util.Objects.equals(email, client.email);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(idClient, email);
    }

    @Override
    public String toString() {
        return "Client{id=" + idClient + ", nom='" + nom + "', email='" + email + "'}";
    }
    public int getId_client(){ return idClient; }
    public void setId_client(int idClient){ this.idClient = idClient; }

    public String getNom(){ return nom; }
    public void setNom(String nom){ this.nom = nom; }

    public String getEmail(){ return email; }
    public void setEmail(String email){ this.email = email; }

    public String getPassword(){ return password; }
    public void setPassword(String password){ this.password = password; }
}