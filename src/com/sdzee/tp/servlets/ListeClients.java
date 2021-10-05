package com.sdzee.tp.servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
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
 * L'annotation @WebServlet("/ListeClients") d'origine est remplacée par @WebServlet("/liste-clients") 
 * car l'URL définie dans le fichier web.xml a été modifiée. La servlet ListeClients correspond à 
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
	
	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10 ko
	public static final int TAILLE_TAMPON = 10240; // 10 ko
	
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
		 * On récupère le paramètre "imageLink" dans la jsp listerClient.jsp et si imageLink = null, 
		 * alors l'utilisateur n'a pas clické sur le lien. Si imageLink != null, alors l'utilisateur a clické 
		 * sur ce lien et donc on commence le traitement d'affichage de l'image.
		 * */
		String imageLink = getFieldValue(request, "imageLink");
		
		if (imageLink != null) {
			
			/* *
			 * Le paramètre "chemin" est le nom que l'on a enregistré dans le fichier web.xml pour la servlet ListeClients. 
			 * Il correspond au chemin où est localisé le fichier à télécharger. C'est la méthode getInitParameter() qui 
			 * va récupérer le chemin d'accès (en URL absolu) nommé "chemin", 
			 * soit => /Users/julienorrado/Desktop/fichiers/
	 		 * */
			String chemin = this.getServletConfig().getInitParameter("chemin");
			
			/* *
			 * Décode le nom de fichier récupéré, susceptible de contenir des espaces et autres caractères spéciaux, 
			 * et prépare l'objet File 
			 * */
			imageLink = URLDecoder.decode(imageLink, "UTF-8");
			File fichier = new File(chemin, imageLink);
			        
			/* *
			 * Vérifie que le fichier existe bien. Si le fichier n'existe pas il renvoie immédiatement la page 404 et le return 
			 * dans la condition empèche la poursuite de la lecture du code.
			 * */
			if (!fichier.exists()) {
			    /* *
			     * Si non, alors on envoie une erreur 404, qui signifie que la ressource demandée n'existe pas 
			     * */
			    response.sendError(HttpServletResponse.SC_NOT_FOUND);
			    return;
			}
			
			/* *
			 * Récupère le type du fichier à l'aide de la méthode getMimeType(). Pour ce faire, il se base sur les types 
			 * MIME dont il a connaissance, en fonction de l'extension du fichier à retourner. Ces types sont spécifiés 
			 * dans le fichier web.xml global du conteneur, qui est situé dans le répertoire /conf/ du Tomcat Home.
			 * */
			String type = getServletContext().getMimeType(fichier.getName());

			/* *
			 * Si le type de fichier est inconnu, alors on initialise un type par défaut 
			 * */
			if (type == null) {
			    type = "application/octet-stream";
			}
			
			/* *
			 * Initialise la réponse HTTP 
			 * */
			response.reset(); // Efface littéralement l'intégralité du contenu de la réponse initiée par le conteneur
			response.setBufferSize(DEFAULT_BUFFER_SIZE); // Méthode à appeler impérativement après un reset()
			response.setContentType(type); // Spécifie le type des données contenues dans la réponse
			// nous retrouvons ensuite les deux en-têtes HTTP, qu'il faut construire "à la main" via des appels à setHeader()
			response.setHeader("Content-Length", String.valueOf(fichier.length()));
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fichier.getName() + "\"");
			
			/* *
			 * Lecture et envoi du fichier
			 * */
			/* *
			 * Prépare les flux 
			 * */
			BufferedInputStream entree = null;
			BufferedOutputStream sortie = null;
			try {
			    /* *
			     * Ouvre les flux 
			     * */
			    entree = new BufferedInputStream(new FileInputStream(fichier), TAILLE_TAMPON);
			    sortie = new BufferedOutputStream(response.getOutputStream(), TAILLE_TAMPON);
			 
			    /* *
				 * Lit le fichier et écrit son contenu dans la réponse HTTP. À l'aide d'un tableau d'octets jouant le rôle de tampon, 
				 * la boucle mise en place parcourt le fichier et l'écrit, morceau par morceau, dans la réponse.
				 * */
				byte[] tampon = new byte[TAILLE_TAMPON];
				int longueur;
				while ((longueur = entree.read(tampon)) > 0) {
				    sortie.write(tampon, 0, longueur);
				}
				
			} finally {
			    try {
			        sortie.close();
			    } catch (IOException ignore) {
			    }
			    try {
			        entree.close();
			    } catch (IOException ignore) {
			    }
			}
			
		} else {
			
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
			
		}
		
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
