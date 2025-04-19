package controller;

import dao.ClientDAO;
import model.Client;
import java.util.List;

public class ClientController {
    private ClientDAO clientDAO;

    public ClientController() {
        this.clientDAO = new ClientDAO();
    }

    public boolean enregistrerClient(String nom, String prenom, String email, String motDePasse, String typeClient) {
        Client client = new Client(nom, prenom, email, motDePasse, typeClient);
        return clientDAO.ajouterClient(client);
    }

    public Client authentifierClient(String email, String motDePasse) {
        return clientDAO.authentifierClient(email, motDePasse);
    }

    public List<Client> obtenirListeClients() {
        return clientDAO.listerClients();
    }
}