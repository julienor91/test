package com.sdzee.tp.servlets;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sdzee.tp.beans.Client;
import com.sdzee.tp.forms.CreationClientForm;

/* *
 * L'annotation @WebServlet("/ListeClients") d'origine est remplacé par @WebServlet("/liste-clients") 
 * car l'URL définie dans le fichier web.xml a été modifié. La servlet ListeClients correspond à 
 * <url-pattern>/liste-clients</url-pattern> du fichier web.xml 
 * */
@WebServlet("/liste-clients")
public class ListeClients extends HttpServlet {
	
	/* *
	 * ********** ATTRIBUTES **************************************************
	 * */
	private static final long serialVersionUID = 1L;
	
	private static final String VIEW_JSP_LIST = "/WEB-INF/listerClients.jsp";
	
	private static final String FORM_CUSTOMER_ATTRIBUTE = "creationClientForm";
	private static final String CUSTOMER_ATTRIBUTE = "client";
	private static final String DELETE_CUSTOMER_ATTRIBUTE = "clientSupprime";
	
	private static final String HASHMAP_CUSTOMERS_ATTRIBUTE = "tabClients";
	
	/* *
	 * ********** CONSTRUCTOR **************************************************
	 * */
    public ListeClients() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /* *
	 * ********** HTTP METHODS **************************************************
	 * */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		this.getServletContext().getRequestDispatcher(VIEW_JSP_LIST).forward(request, response);
	}
	
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		CreationClientForm creationClientForm = new CreationClientForm();
		Client client = new Client();
		String clientSupprime;
		
		/* * 
		 * creationClientForm et client arrivent directement de la requête. Ces informations ne dépendent pas 
		 * de cases remplies par l'utilisateur.
		 * */
		creationClientForm = (CreationClientForm) request.getAttribute(FORM_CUSTOMER_ATTRIBUTE);
		client = (Client) request.getAttribute(CUSTOMER_ATTRIBUTE);
		/* * 
		 * clientSupprime dépende d'une case remplie par l'utilisateur, il faut donc créer une méthode de vérification.
		 * */
		clientSupprime = getFieldValue(request, DELETE_CUSTOMER_ATTRIBUTE);
		
		request.setAttribute(FORM_CUSTOMER_ATTRIBUTE, creationClientForm);
		request.setAttribute(CUSTOMER_ATTRIBUTE, client);
		
		/* *
		 * La valeur est a true car si la session existe on ne la re-créait pas, mais si elle n'existe pas, on la créait.
		 * */
		HttpSession session = request.getSession(true);
		/* *
		 * Pour enregistrer les données des clients dans une collection Map.
		 * */
		Map<String, Client> tabClients = new HashMap<String, Client>();
		
		/* *
		 * On récupère les données du ou des client(s) déjà enregistré(s) en session pour ne pas les effacer.
		 * */
		tabClients = (Map<String, Client>) session.getAttribute(HASHMAP_CUSTOMERS_ATTRIBUTE);
		
		if (tabClients != null) { // Si l'utilisateur recharge la page avec des valeurs qui ont été supprimées
			try {
				for (Map.Entry<String, Client> entry : tabClients.entrySet()) {
					if (entry.getKey().equals(clientSupprime)) {
						tabClients.remove(entry.getKey()); // Pour supprimer la valeur de la collection avec sa clé
					}
				}
			} catch (ConcurrentModificationException e) {}
		}
		
		session.setAttribute(HASHMAP_CUSTOMERS_ATTRIBUTE, tabClients);
		
		this.getServletContext().getRequestDispatcher(VIEW_JSP_LIST).forward(request, response);
	}
	
	/* *
	* Méthode utilitaire qui retourne null si un champ est vide, et son contenu sinon.
	* */
	private static String getFieldValue(HttpServletRequest request, String nomChamp) {
	    String valeur = request.getParameter(nomChamp);
	    if (valeur == null || valeur.trim().length() == 0) {
	        return null;
	    } else {
	        return valeur.trim();
	    }
	}
	
}
