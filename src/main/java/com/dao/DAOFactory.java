package main.java.com.dao;
/**
 * Factory pour créer les instances de DAO
 * Utilise le pattern Factory pour centraliser la création des objets DAO
 */
public class DAOFactory {

    /**
     * Crée et retourne une instance de ArticleDAO
     * @return Une instance de ArticleDAO
     */
    public static ArticleDAO getArticleDAO() {
        return new ArticleDAO();
    }

    /**
     * Crée et retourne une instance de ClientDAO
     * @return Une instance de ClientDAO
     */
    public static ClientDAO getClientDAO() {
        return new ClientDAO();
    }

    /**
     * Crée et retourne une instance de CommandeDAO
     * @return Une instance de CommandeDAO
     */
    public static CommandeDAO getCommandeDAO() {
        return new CommandeDAO();
    }

    /**
     * Crée et retourne une instance de MarqueDAO
     * @return Une instance de MarqueDAO
     */
    public static MarqueDAO getMarqueDAO() {
        return new MarqueDAO();
    }

    /**
     * Crée et retourne une instance de UtilisateurDAO
     * @return Une instance de UtilisateurDAO
     */
    public static UtilisateurDAO getUtilisateurDAO() {
        return new UtilisateurDAO();
    }
}