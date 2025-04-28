package main.java.com.dao;

import java.util.List;

/**
 * Interface générique pour les DAO
 * @param <T> Le type d'objet géré par le DAO
 */
public interface GenericDAO<T> {
    /**
     * Trouve un objet par son ID
     * @param id L'identifiant de l'objet
     * @return L'objet trouvé ou null si non trouvé
     */
    T findById(int id);

    
    /**
     * Récupère tous les objets
     * @return Liste de tous les objets
     */
    List<T> findAll();

    /**
     * Crée un nouvel objet
     * @param t L'objet à créer
     * @return true si l'opération a réussi, false sinon
     */
    boolean create(T t);

    /**
     * Met à jour un objet existant
     * @param t L'objet à mettre à jour
     * @return true si l'opération a réussi, false sinon
     */
    boolean update(T t);

    /**
     * Supprime un objet par son ID
     * @param id L'identifiant de l'objet à supprimer
     * @return true si l'opération a réussi, false sinon
     */
    boolean delete(int id);
}
