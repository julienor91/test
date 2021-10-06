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

import com.sdzee.tp.beans.Client;
import com.sdzee.tp.forms.CreationClientForm;

/* *
 * L'annotation @WebServlet("/CreationClient") d'origine est remplacée par @WebServlet("/enregistrement-client") 
 * car l'URL définie dans le fichier web.xml a été modifiée. La servlet CreationClient correspond à 
 * <url-pattern>/enregistrement-client</url-pattern> du fichier web.xml 
 * */
@WebServlet("/enregistrement-client")
public class CreationClient extends HttpServlet {
	
	/* *
	 * ********** ATTRIBUTES **************************************************
	 * */
	private static final long serialVersionUID = 1L;
	
	private static final String VIEW_JSP_CREATE = "/WEB-INF/creerClient.jsp";
	private static final String VIEW_JSP_DISPLAY = "/WEB-INF/afficherClient.jsp";
	
	private static final String FORM_CUSTOMER_ATTRIBUTE = "creationClientForm";
	private static final String CUSTOMER_ATTRIBUTE = "client";
	private static final String ERROR_SENTENCES_MAP_ATTRIBUTE = "errors";
	public static final String CHEMIN = "chemin";
	
    private static final String SUCCES_MESSAGE_ATTRIBUTE = "succesMessage";
	private static final String SUCCES_MESSAGE_SENTENCE = "Félicitation, vos informations ont bien été enregistré !";
	
	private static final String HASHMAP_CUSTOMERS_ATTRIBUTE = "tabClients";
	
	/* *
	 * ********** CONSTRUCTOR **************************************************
	 * */
	public CreationClient() {
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
		
		/* *
	     * Lecture du paramètre "chemin" passé à la servlet via la déclaration dans le web.xml
	     * <param-value>/Users/julienorrado/Desktop/</param-value>
	     * Soit => /Users/julienorrado/Desktop/
	     * ATTENTION à bien mettre enctype="multipart/form-data" dans la jsp
	     * */
	    String chemin = this.getServletConfig().getInitParameter(CHEMIN);
	    
		CreationClientForm creationClientForm = new CreationClientForm();
		Client client = new Client();
		
		/* *
		 * Validation des données + création du bean client. 
		 * */
		client = creationClientForm.enregistrementClient(request, chemin);
		
		request.setAttribute(FORM_CUSTOMER_ATTRIBUTE, creationClientForm);
		request.setAttribute(CUSTOMER_ATTRIBUTE, client);
		
		if (creationClientForm.getErrors().isEmpty()) {
			/* *
			 * La valeur est a true car si la session existe on ne la re-créait pas, mais si elle n'existe pas, on la créait.
			 * */
			HttpSession session = request.getSession(true);
			/* *
			 * Pour enregistrer les données des clients dans une collection Map.
			 * */
			Map<String, Client> tabClients = new HashMap<String, Client>();
			
			if (session.getAttribute(HASHMAP_CUSTOMERS_ATTRIBUTE) != null) {
				/* *
				 * On récupère les données du ou des client(s) déjà enregistré(s) en session pour ne pas les effacer.
				 * */
				tabClients = (Map<String, Client>) session.getAttribute(HASHMAP_CUSTOMERS_ATTRIBUTE);
			}
			/* *
			 * On enregistre le nouveau client dans le tableau de clients. Ce tableau peut être vide ou bien posséder les 
			 * données des clients qui étaient enregistrées en session.
			 * */
			tabClients.put(client.getNomClient(), client);
			/* *
			 * On enregistre le tableau de clients contenant le nouveau client dans la session. Soit le tableau de clients 
			 * est créé, soit les anciennes données sont effacées et remplacées.
			 * */
			session.setAttribute(HASHMAP_CUSTOMERS_ATTRIBUTE, tabClients);
			/* *
			 * Pour lire le tableau dans la console.
			 * */
			for (Map.Entry<String, Client> entry : tabClients.entrySet()) {
				System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
			}
			
			request.setAttribute(SUCCES_MESSAGE_ATTRIBUTE, SUCCES_MESSAGE_SENTENCE);
			dispatcher = contexte.getRequestDispatcher(VIEW_JSP_DISPLAY);
        } else {
        	request.setAttribute(ERROR_SENTENCES_MAP_ATTRIBUTE, creationClientForm.getErrors());
        	dispatcher = contexte.getRequestDispatcher(VIEW_JSP_CREATE);
        }
		
		dispatcher.forward(request, response);
	}
	
}
