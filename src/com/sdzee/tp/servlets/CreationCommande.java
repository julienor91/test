package com.sdzee.tp.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sdzee.tp.beans.Commande;
import com.sdzee.tp.forms.CreationCommandeForm;

/* *
 * L'annotation @WebServlet("/CreationCommande") d'origine est remplacé par @WebServlet("/enregistrement-commande") 
 * car l'URL définie dans le web.xml a été modifié. La servlet CreationCommande correspond à 
 * <url-pattern>/enregistrement-commande</url-pattern> du fichier web.xml 
 * */
@WebServlet("/enregistrement-commande")
public class CreationCommande extends HttpServlet {
	
	/* *
	 * ********** ATTRIBUTES **************************************************
	 * */
	private static final long serialVersionUID = 1L;
	
	private static final String VIEW_JSP_CREATE = "/WEB-INF/creerCommande.jsp";
	private static final String VIEW_JSP_DISPLAY = "/WEB-INF/afficherCommande.jsp";
	
	private static final String ERROR_SENTENCES_MAP_ATTRIBUTE = "errors";
	private static final String FORM_ORDER_ATTRIBUTE = "creationCommandeForm";
	private static final String ORDER_ATTRIBUTE = "commande";
    
    private static final String SUCCES_MESSAGE_ATTRIBUTE = "succesMessage";
    private static final String SUCCES_MESSAGE = "Félicitation, vos informations ont bien été enregistré !";
    
    private static final String HASHMAP_ORDERS_ATTRIBUTE = "tabCommandes";
	
    /* *
	 * ********** CONSTRUCTOR **************************************************
	 * */
    public CreationCommande() {
        super();
    }
    
    /* *
	 * ********** HTTP METHODS **************************************************
	 * */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		this.getServletContext().getRequestDispatcher(VIEW_JSP_CREATE).forward(request, response);
	}
    
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
			
		ServletContext contexte;
		RequestDispatcher dispatcher;
		contexte = getServletContext();
		
		CreationCommandeForm creationCommandeForm = new CreationCommandeForm();
		Commande commande = new Commande();
		
		/* *
		 * Validation des données + création du bean commande. 
		 * */
		commande = creationCommandeForm.enregistrementCommande(request);
		
		request.setAttribute(FORM_ORDER_ATTRIBUTE, creationCommandeForm);
		request.setAttribute(ORDER_ATTRIBUTE, commande);
		
		if (creationCommandeForm.getErrors().isEmpty()) {
			/* *
			 * La valeur est a true car si la session existe on ne la re-créait pas, mais si elle n'existe pas, on la créait.
			 * */
			HttpSession session = request.getSession(true);
			/* *
			 * Pour enregistrer les données des commandes dans une collection Map.
			 * */
			Map<String, Commande> tabCommandes = new HashMap<String, Commande>();
			
			if (session.getAttribute(HASHMAP_ORDERS_ATTRIBUTE) != null) {
				/* *
				 * On récupère les données du ou des commande(s) déjà enregistrée(s) en session pour ne pas les effacer.
				 * */
				tabCommandes = (Map<String, Commande>) session.getAttribute(HASHMAP_ORDERS_ATTRIBUTE);
			}
			/* *
			 * On enregistre la nouveau commande dans le tableau de commandes. Ce tableau peut être vide ou bien posséder les 
			 * données des commandes qui étaient enregistrées en session.
			 * */
			tabCommandes.put(commande.getDateCommande(), commande);
			/* *
			 * On enregistre le tableau de commandes contenant la nouveau commande dans la session. Soit le tableau de commandes 
			 * est créé, soit les anciennes données sont effacées et remplacées.
			 * */
			session.setAttribute(HASHMAP_ORDERS_ATTRIBUTE, tabCommandes);
			/* *
			 * Pour lire le tableau dans la console.
			 * */
			for (Map.Entry<String, Commande> entry : tabCommandes.entrySet()) {
				System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
			}
				
			request.setAttribute(SUCCES_MESSAGE_ATTRIBUTE, SUCCES_MESSAGE);
			dispatcher = contexte.getRequestDispatcher(VIEW_JSP_DISPLAY);
        } else {
        	request.setAttribute(ERROR_SENTENCES_MAP_ATTRIBUTE, creationCommandeForm.getErrors());
        	dispatcher = contexte.getRequestDispatcher(VIEW_JSP_CREATE);
        }
		
		dispatcher.forward(request, response);
	}
	
}
