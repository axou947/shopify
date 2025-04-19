package main.java.com.model;

import java.util.Date;

/**
 * Classe représentant un utilisateur du système (client ou administrateur)
 */
public class Utilisateur {
    private int id;
    private String email;
    private String motDePasse;
    private String type; // "client" ou "admin"
    private String nom;
    private String prenom;
    private String adresse;
    private String telephone;
    private Date dateInscription;

    /**
     * Constructeur par défaut
     */
    public Utilisateur() {
        this.dateInscription = new Date(); // Par défaut, date d'inscription = aujourd'hui
    }

    /**
     * Constructeur avec paramètres
     * @param id Identifiant de l'utilisateur
     * @param email Email de l'utilisateur
     * @param motDePasse Mot de passe de l'utilisateur
     * @param type Type d'utilisateur (client ou admin)
     * @param nom Nom de l'utilisateur
     * @param prenom Prénom de l'utilisateur
     * @param adresse Adresse de l'utilisateur
     * @param telephone Téléphone de l'utilisateur
     * @param dateInscription Date d'inscription de l'utilisateur
     */
    public Utilisateur(int id, String email, String motDePasse, String type, String nom, String prenom, String adresse, String telephone, Date dateInscription) {
        this.id = id;
        this.email = email;
        this.motDePasse = motDePasse;
        this.type = type;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.dateInscription = dateInscription;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Date dateInscription) {
        this.dateInscription = dateInscription;
    }

    @Override
    public String toString() {
        return prenom + " " + nom + " (" + email + ")";
    }
}