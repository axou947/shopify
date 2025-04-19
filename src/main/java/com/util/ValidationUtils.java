package main.java.com.util;

import java.util.regex.Pattern;

/**
 * Classe utilitaire pour la validation des données
 */
public class ValidationUtils {
    // Regex pour valider un email
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    // Regex pour valider un numéro de téléphone
    private static final String PHONE_REGEX = "^[0-9]{10}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    /**
     * Valide un email
     * @param email Email à valider
     * @return true si l'email est valide, false sinon
     */
    public static boolean isEmailValid(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Valide un numéro de téléphone
     * @param phone Numéro à valider
     * @return true si le numéro est valide, false sinon
     */
    public static boolean isPhoneValid(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Valide un mot de passe (au moins 6 caractères)
     * @param password Mot de passe à valider
     * @return true si le mot de passe est valide, false sinon
     */
    public static boolean isPasswordValid(String password) {
        return password != null && password.length() >= 6;
    }

    /**
     * Valide un prix (doit être positif)
     * @param price Prix à valider
     * @return true si le prix est valide, false sinon
     */
    public static boolean isPriceValid(double price) {
        return price > 0;
    }

    /**
     * Valide une quantité (doit être positive ou nulle)
     * @param quantity Quantité à valider
     * @return true si la quantité est valide, false sinon
     */
    public static boolean isQuantityValid(int quantity) {
        return quantity >= 0;
    }
}