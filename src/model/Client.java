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
    public void setPassword(String password){ this.password = hashPassword(password); }

    private String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Hashing failed", e);
        }
    }
}