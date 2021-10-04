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

import com.sdzee.tp.beans.Commande;
import com.sdzee.tp.forms.CreationCommandeForm;

/* *
 * L'annotation @WebServlet("/ListeCommandes") d'origine est remplacé par @WebServlet("/liste-commandes") 
 * car l'URL définie dans le fichier web.xml a été modifié. La servlet ListeCommandes correspond à 
 * <url-pattern>/liste-commandes</url-pattern> du fichier web.xml 
 * */
@WebServlet("/liste-commandes")
public class ListeCommandes extends HttpServlet {
	
	/* *
	 * ********** ATTRIBUTES **************************************************
	 * */
	private static final long serialVersionUID = 1L;
	
	private static final String VIEW_JSP_LIST = "/WEB-INF/listerCommandes.jsp";
	
	private static final String FORM_ORDER_ATTRIBUTE = "creationCommandeForm";
	private static final String ORDER_ATTRIBUTE = "commande";
	private static final String DELETE_ORDER_ATTRIBUTE = "commandeSupprime";
	
	private static final String HASHMAP_ORDERS_ATTRIBUTE = "tabCommandes";
    
	/* *
	 * ********** CONSTRUCTOR **************************************************
	 * */
    public ListeCommandes() {
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
		
		CreationCommandeForm creationCommandeForm = new CreationCommandeForm();
		Commande commande = new Commande();
		String commandeSupprime;
		
		creationCommandeForm = (CreationCommandeForm) request.getAttribute(FORM_ORDER_ATTRIBUTE);
		commande = (Commande) request.getAttribute(ORDER_ATTRIBUTE);
		commandeSupprime = getFieldValue(request, DELETE_ORDER_ATTRIBUTE);
		
		request.setAttribute(FORM_ORDER_ATTRIBUTE, creationCommandeForm);
		request.setAttribute(ORDER_ATTRIBUTE, commande);
		
		/* *
		 * La valeur est a true car si la session existe on ne la re-créait pas, mais si elle n'existe pas, on la créait.
		 * */
		HttpSession session = request.getSession(true);
		/* *
		 * Pour enregistrer les données des commandes dans une collection Map.
		 * */
		Map<String, Commande> tabCommandes = new HashMap<String, Commande>();
		
		/* *
		 * On récupère les données du ou des commande(s) déjà enregistrée(s) en session pour ne pas les effacer.
		 * */
		tabCommandes = (Map<String, Commande>) session.getAttribute(HASHMAP_ORDERS_ATTRIBUTE);
		
		if (tabCommandes != null) { // Si l'utilisateur recharge la page avec des valeurs qui ont été supprimées
			try {
				for (Map.Entry<String, Commande> entry : tabCommandes.entrySet()) {
					if (entry.getKey().equals(commandeSupprime)) {
						tabCommandes.remove(entry.getKey()); // Pour supprimer la valeur de la collection avec sa clé
					}
				}
			} catch (ConcurrentModificationException e) {}
		}
		
		session.setAttribute(HASHMAP_ORDERS_ATTRIBUTE, tabCommandes);
		
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
