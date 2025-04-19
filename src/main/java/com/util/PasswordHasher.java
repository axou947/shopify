package main.java.com.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Classe utilitaire pour le hachage sécurisé des mots de passe
 */
public class PasswordHasher {
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;
    private static final String SALT_PASSWORD_SEPARATOR = ":";

    /**
     * Génère un salt aléatoire
     * @return Salt généré
     */
    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Hache un mot de passe avec un salt
     * @param password Mot de passe à hacher
     * @param salt Salt à utiliser
     * @return Mot de passe haché
     */
    private static String hashWithSalt(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithme de hachage non disponible", e);
        }
    }

    /**
     * Hache un mot de passe
     * @param password Mot de passe à hacher
     * @return Chaîne contenant le salt et le mot de passe haché
     */
    public static String hash(String password) {
        byte[] salt = generateSalt();
        String hashedPassword = hashWithSalt(password, salt);
        String saltString = Base64.getEncoder().encodeToString(salt);
        return saltString + SALT_PASSWORD_SEPARATOR + hashedPassword;
    }

    /**
     * Vérifie un mot de passe
     * @param password Mot de passe à vérifier
     * @param storedHash Hachage stocké
     * @return true si le mot de passe correspond, false sinon
     */
    public static boolean verify(String password, String storedHash) {
        String[] parts = storedHash.split(SALT_PASSWORD_SEPARATOR);
        if (parts.length != 2) {
            return false;
        }

        try {
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            String computedHash = hashWithSalt(password, salt);
            return computedHash.equals(parts[1]);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
