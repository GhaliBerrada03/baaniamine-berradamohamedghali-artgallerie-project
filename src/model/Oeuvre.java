package model;
public class Oeuvre {
    private int idOeuvre;
    private String titre;
    private String artiste;
    private String categorie;
    private int prix;
private String statut;
    public Oeuvre(){}
    public Oeuvre(String titre,String artiste,String categorie,int prix){
        this.titre = titre;
        this.artiste = artiste;
        this.categorie = categorie;
        this.prix = prix;
    }
    public Oeuvre(String titre,String artiste , String categorie, int prix, int idOeuvre){
        this(titre ,artiste , categorie, prix);
        this.idOeuvre = idOeuvre;
    }
    public Oeuvre(String titre, String artiste, String categorie, int prix, String statut) {
        this(titre, artiste, categorie, prix);
        this.statut = statut;
    }
    public int getIdOeuvre(){
        return idOeuvre;
    }
    public void setIdOeuvre(int idOeuvre) {
        this.idOeuvre = idOeuvre;
    }
    public String getTitre(){
        return titre;
    }
    public void setTitre(String titre) {
        this.titre = titre;    }
    public String getArtiste(){
        return artiste;
    }
    public void setArtiste(String artiste) {
        this.artiste = artiste;    }
    public String getCategorie(){
        return categorie;
    }
    public void setCategorie(String categorie) {
        this.categorie = categorie;    }
    public int getPrix(){
        return prix;
    }
    public void setPrix(int prix) {
        this.prix = prix;    }
    public String getStatut(){
        return statut;
    }
    public void setStatut(String statut) {
        this.statut= statut;    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Oeuvre oeuvre = (Oeuvre) o;
        return idOeuvre == oeuvre.idOeuvre &&
                java.util.Objects.equals(titre, oeuvre.titre);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(idOeuvre, titre);
    }

    @Override
    public String toString() {
        return "Oeuvre{id=" + idOeuvre + ", titre='" + titre + "', artiste='" + artiste + "', prix=" + prix + "}";
    }
}