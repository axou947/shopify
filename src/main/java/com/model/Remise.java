package main.java.com.model;

import java.util.Date;

/**
 * Classe représentant une remise dans le système de shopping
 */
public class Remise {
    private int id;
    private String code;
    private double pourcentage;
    private double montantFixe; 
    private Date dateDebut;
    private Date dateFin;
    private int quantiteMin;

    /**
     * Constructeur par défaut
     */
    public Remise() {
        this.pourcentage = 0;
        this.montantFixe = 0;
        this.quantiteMin = 1;
    }

    /**
     * Constructeur avec paramètres
     * @param id Identifiant de la remise
     * @param code Code de la remise
     * @param pourcentage Pourcentage de réduction
     * @param montantFixe Montant fixe de réduction
     * @param dateDebut Date de début de validité
     * @param dateFin Date de fin de validité
     * @param quantiteMin Quantité minimale pour appliquer la remise
     */
    public Remise(int id, String code, double pourcentage, double montantFixe, Date dateDebut, Date dateFin, int quantiteMin) {
        this.id = id;
        this.code = code;
        this.pourcentage = pourcentage;
        this.montantFixe = montantFixe;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.quantiteMin = quantiteMin;
    }

    /**
     * Vérifie si la remise est valide à une date donnée
     * @param date Date à vérifier
     * @return true si la remise est valide, false sinon
     */
    public boolean isValide(Date date) {
        return (date.after(dateDebut) || date.equals(dateDebut)) &&
                (date.before(dateFin) || date.equals(dateFin));
    }

    /**
     * Calcule le montant de remise pour un montant donné
     * @param montant Montant sur lequel appliquer la remise
     * @return Montant de la remise
     */
    public double calculerRemise(double montant) {
        // Remise en pourcentage
        double remisePourcentage = montant * (pourcentage / 100.0);

        // Retourne le maximum entre la remise en pourcentage et le montant fixe
        return Math.max(remisePourcentage, montantFixe);
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(double pourcentage) {
        this.pourcentage = pourcentage;
    }

    public double getMontantFixe() {
        return montantFixe;
    }

    public void setMontantFixe(double montantFixe) {
        this.montantFixe = montantFixe;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public int getQuantiteMin() {
        return quantiteMin;
    }

    public void setQuantiteMin(int quantiteMin) {
        this.quantiteMin = quantiteMin;
    }

    @Override
    public String toString() {
        String description = code + " : ";
        if (pourcentage > 0) {
            description += pourcentage + "% de réduction";
        } else if (montantFixe > 0) {
            description += montantFixe + "€ de réduction";
        }
        return description;
    }
}
