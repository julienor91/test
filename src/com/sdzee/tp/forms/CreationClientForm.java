package com.sdzee.tp.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.myProject.regularExpression.Regex;
import com.sdzee.tp.beans.Client;

/* *
 * Ajout de "final" car cette classe ne peut être héritée par une autre classe.
 * Cette classe est donc mise à part, elle ne subira aucune modification. C'est 
 * donc princpalement pour empêcher le développeur d'en faire une utilisation 
 * non appropriée dans une classe dérivée. Ainsi, outre la conception, une classe 
 * est également déclarée "final" dans un soucis d'optimisation du code. 
 * */
public final class CreationClientForm {
	
	/* *
	 * ********** ATTRIBUTES **************************************************
	 * */
	private static final String CUSTOMER_ATTRIBUTE = "client";
	
	private static final String NEW_OR_OLDER_CUSTOMER_ATTRIBUTE = "choixNouveauClient";
	private static final String OLDER_CUSTOMER_ATTRIBUTE = "ancienClient";
	private static final String LIST_CUSTOMER_ATTRIBUTE = "listeClients";
	private static final String HASHMAP_CUSTOMERS_ATTRIBUTE = "tabClients";
	
	private static final String LAST_NAME_FIELD = "nomClient";
    private static final String FIRST_NAME_FIELD = "prenomClient";
    private static final String ADDRESS_FIELD = "adresseClient";
    private static final String PHONE_NUMBER_FIELD = "telephoneClient";
    private static final String EMAIL_FIELD = "emailClient";
    
    private static final Byte LAST_NAME_MAX_LETTER_NUMBER = 20;
    private static final Byte FIRST_NAME_MAX_LETTER_NUMBER = 20;
    private static final Byte ADDRESS_MAX_LETTER_NUMBER = 20;
    private static final Byte PHONE_NUMBER_MAX_LETTER_NUMBER = 10;
    private static final Byte EMAIL_MAX_LETTER_NUMBER = 60;
    
    private static final String LESS_OF_2_CHAR_SENTENCE = "Attention, vous devez inscrire plus de 2 charactères.";
    private static final String LESS_OF_10_CHAR_SENTENCE = "Attention, vous devez inscrire plus de 10 charactères.";
    private static final String LESS_OF_4_NUMBER_SENTENCE = "Attention, vous devez inscrire plus de 4 chiffres.";
    private static final String FIELD_EMPTY_SENTENCE = "Attention ce champ est vide !";
    
	private Map<String, String> errors = new HashMap<String, String>();
	
	/* *
	 * ********** CONSTRUCTOR **************************************************
	 * */
	public CreationClientForm() {
		super();
	}
	
	/* *
	 * ********** METHODS **************************************************
	 * */
	@SuppressWarnings("unchecked")
	public Client enregistrementClient(HttpServletRequest request) {
		
		/* *
		 * Création de la session pour la récupérer dans la création commande. La valeur est a true car si la 
		 * session existe on ne la créait pas mais si elle n'existe pas on la créait.
		 * */
		HttpSession session = request.getSession(true);
		
		/* *
		 * Création de l'objet client 
		 * */
		Client client = new Client();
		
		String choixNouveauClient = getFieldValue(request, NEW_OR_OLDER_CUSTOMER_ATTRIBUTE);
		String listeClients = getFieldValue(request, LIST_CUSTOMER_ATTRIBUTE);
		/* *
		 * Pour savoir si l'utilisateur a choisi un ancien client avec la liste déroulante.
		 * */
		if (choixNouveauClient != null && 
				choixNouveauClient.equals(OLDER_CUSTOMER_ATTRIBUTE) &&
				listeClients != null) {
			String ancienClient = listeClients;
			/* *
			 * Si le client est déjà supprimé dans le tableau de session mais que la requête est renvoyée avec le nom de ce client, 
			 * alors la valeur renvoyée sera nulle et posera un problème.
			 * */
			int clientDejaSupprime = 0;
			/* *
			 * Pour récupérer les données des clients dans une collection Map.
			 * */
			Map<String, Client> tabClients = new HashMap<String, Client>();
			tabClients = (Map<String, Client>) session.getAttribute(HASHMAP_CUSTOMERS_ATTRIBUTE);
			for (Map.Entry<String, Client> entry : tabClients.entrySet()) {
				if ((entry.getKey()).equals(ancienClient)) {
					client = entry.getValue();
					/* *
					 * On a trouvé la valeur correspondante.
					 * */
					clientDejaSupprime = 1;
				}
			}
			/* *
			 * Pour récupérer les données des clients dans une collection Map.
			 * */
			/* *
			 * On n'a pas trouvé la valeur correspondante. On lui met alors la dernière valeur du tableau de session afin d'éviter 
			 * de renvoyer une valeur nulle pour l'objet client.
			 * */
			if (clientDejaSupprime == 0) {
				for (Map.Entry<String, Client> entry : tabClients.entrySet()) {
						client = entry.getValue();
					}
			} 
			/* *
			 * Ou bien on peut utiliser cela :
			 * */
			// client = ((Map<String, Client>) session.getAttribute(HASHMAP_CUSTOMERS_ATTRIBUTE)).get(ancienClient);
			session.setAttribute(CUSTOMER_ATTRIBUTE, client);
		} else {
			/* *
			 * Utilisation d'un try catch dans le cas où un utilisateur modifierait le fichier HTML en enlevant l'attribut 
			 * required de l'input nomClient avec un paramètre null ou vide dans son champ. Cela provoquerait une erreur 
			 * avec la méthode toUpperCase() qui n'accepte pas de paramètre null. Avec le fichier d'erreur affiché on 
			 * pourrait voir que Java est le langage de programmation utilisé ainsi que le noms des classes.
			 * */
			try { // N'accepte pas de valeur nulle avec la méthode toUpperCase()
				client.setNomClient(getFieldValue(request, LAST_NAME_FIELD).toUpperCase());
			} catch (Exception e) { // Accepte une valeur nulle
				client.setNomClient(getFieldValue(request, LAST_NAME_FIELD));
			}
			client.setPrenomClient(getFieldValue(request, FIRST_NAME_FIELD));
			client.setAdresseClient(getFieldValue(request, ADDRESS_FIELD));
			client.setTelephoneClient(getFieldValue(request, PHONE_NUMBER_FIELD));
			client.setEmailClient(getFieldValue(request, EMAIL_FIELD));
			
			/* *
			 * Pour enregistrer les données du dernier client et les réafficher dans les champs des jsp.
			 * */
			session.setAttribute(CUSTOMER_ATTRIBUTE, client);
			
			Regex regex = new Regex();
			
			/* *
		     * Validation du champ nomClient.
			 * Dans le cas d'une valeur nulle avec la méthode length() cela provoquerait un fichier d'erreur utile à un mauvais 
		     * utilisateur. Condition "if" pour contrer une attaque en supprimant l'attribut required dans le fichier HTML. 
		     * La méthode length() ne pourrait lire une valeur nulle et ce paramètre ne pourrait être que nul si le champ n'est 
		     * pas rempli car la méthode getFieldValue() exclut tout autre possibilité.
			 * */
			if (client.getNomClient() != null) {
				if (client.getNomClient().length() < 2) {
					errors.put(LAST_NAME_FIELD, LESS_OF_2_CHAR_SENTENCE);
				} else {
					try {
						regex.lastNameValidation(client.getNomClient(), LAST_NAME_MAX_LETTER_NUMBER);
					} catch (Exception e) {
						errors.put(LAST_NAME_FIELD, e.getMessage());
					}
				}
			} else {
				errors.put(LAST_NAME_FIELD, FIELD_EMPTY_SENTENCE);
			}
			
			/* *
		     * Validation du champ prenomClient. 
			 * Condition "if" car le champ du prénom n'est pas obligatoire. De plus la méthode length() ne peut lire une valeur 
			 * nulle et le paramètre ne peut être que nul si le champ n'est pas rempli car la méthode getFieldValue() exclut tout 
			 * autre possibilité.
			 * */
			if (client.getPrenomClient() != null) {
				if (client.getPrenomClient().length() < 2) {
					errors.put(FIRST_NAME_FIELD, LESS_OF_2_CHAR_SENTENCE);
				} else {
					try {
						regex.firstNameValidation(client.getPrenomClient(), FIRST_NAME_MAX_LETTER_NUMBER);
					} catch (Exception e) {
						errors.put(FIRST_NAME_FIELD, e.getMessage());
					}
				}
			} 
			
			/* *
		     * Validation du champ adresseClient. 
		     * Dans le cas d'une valeur nulle avec la méthode length() cela provoquerait un fichier d'erreur utile à un mauvais 
		     * utilisateur. Condition "if" pour contrer une attaque en supprimant l'attribut required dans le fichier HTML. 
		     * La méthode length() ne pourrait lire une valeur nulle et ce paramètre ne pourrait être que nul si le champ n'est 
		     * pas rempli car la méthode getFieldValue() exclut tout autre possibilité.
			 * */
			if (client.getAdresseClient() != null) {
				if (client.getAdresseClient().length() < 10) {
					errors.put(ADDRESS_FIELD, LESS_OF_10_CHAR_SENTENCE);
				} else {
					try {
						regex.addressValidation(client.getAdresseClient(), ADDRESS_MAX_LETTER_NUMBER);
					} catch (Exception e) {
						errors.put(ADDRESS_FIELD, e.getMessage());
					}
				}
			} else {
				errors.put(ADDRESS_FIELD, FIELD_EMPTY_SENTENCE);
			}
			
			/* *
		     * Validation du champ telephoneClient. 
		     * Dans le cas d'une valeur nulle avec la méthode length() cela provoquerait un fichier d'erreur utile à un mauvais 
		     * utilisateur. Condition "if" pour contrer une attaque en supprimant l'attribut required dans le fichier HTML. 
		     * La méthode length() ne pourrait lire une valeur nulle et ce paramètre ne pourrait être que nul si le champ n'est 
		     * pas rempli car la méthode getFieldValue() exclut tout autre possibilité.
			 * */
			if (client.getTelephoneClient() != null) {
				if (client.getTelephoneClient().length() < 4) {
					errors.put(PHONE_NUMBER_FIELD, LESS_OF_4_NUMBER_SENTENCE);
				} else {
					try {
						regex.phoneNumberValidation(client.getTelephoneClient(), PHONE_NUMBER_MAX_LETTER_NUMBER);
					} catch (Exception e) {
						errors.put(PHONE_NUMBER_FIELD, e.getMessage());
					}
				}
			} else {
				errors.put(PHONE_NUMBER_FIELD, FIELD_EMPTY_SENTENCE);
			}
			
			/* *
		     * Validation du champ emailClient. 
			 * Condition "if" car le champ de l'adresses email n'est pas obligatoire. 
			 * Le paramètre ne peut être que nul si le champ n'est pas rempli car la méthode getFieldValue() exclut tout 
			 * autre possibilité.
			 * */
			if (client.getEmailClient() != null) {
				try {
					regex.mailingAddressValidation(client.getEmailClient(), EMAIL_MAX_LETTER_NUMBER);
				} catch (Exception e) {
					errors.put(EMAIL_FIELD, e.getMessage());
				}
			}
		}
		return client;
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
	
	/* *
	 * ********** GETTER METHODS **************************************************
	* Méthode get pour permettre à la jsp de venir récupérer les données result et le tableau errors.
	* */
	public Map<String, String> getErrors() {
		return errors;
	}
	
}
