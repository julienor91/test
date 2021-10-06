package com.sdzee.tp.forms;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.myProject.regularExpression.Regex;
import com.sdzee.tp.beans.Client;
import com.sdzee.tp.beans.Commande;

/* *
 * Ajout de "final" car cette classe ne peut être héritée par une autre classe.
 * Cette classe est donc mise à part, elle ne subira aucune modification. C'est 
 * donc princpalement pour empêcher le développeur d'en faire une utilisation 
 * non appropriée dans une classe dérivée. Ainsi, outre la conception, une classe 
 * est également déclarée "final" dans un soucis d'optimisation du code. 
 * */
public final class CreationCommandeForm {
	
	/* *
	 * ********** ATTRIBUTES **************************************************
	 * */
	private static final String ORDER_ATTRIBUTE = "commande";
	
	private static final String DATE_AND_TIME_FORMAT = "dd/MM/yyyy_HH:mm:ss";
	private static final String ORDER_AMOUNT_FIELD = "montantCommande";
	private static final String ORDER_PAYMENT_METHOD_FIELD = "modePaiementCommande";
	private static final String ORDER_PAYMENT_STATUS_FIELD = "statutPaiementCommande";
	private static final String ORDER_DELIVERY_METHOD_FIELD = "modeLivraisonCommande";
	private static final String ORDER_DELIVERY_STATUS_FIELD = "statutLivraisonCommande";
	
    private static final Byte ORDER_AMOUNT_MAX_CHARACTER_NUMBER = 7;
    private static final Byte ORDER_PAYMENT_METHOD_MAX_LETTER_NUMBER = 25;
    private static final Byte ORDER_PAYMENT_STATUS_MAX_LETTER_NUMBER = 25;
    private static final Byte ORDER_DELIVERY_METHOD_MAX_LETTER_NUMBER = 25;
    private static final Byte ORDER_DELIVERY_STATUS_MAX_LETTER_NUMBER = 25;
    
    private static final String LESS_OF_2_CHAR_SENTENCE = "Attention, vous devez inscrire plus de 2 charactères.";
    private static final String FIELD_EMPTY_SENTENCE = "Attention ce champ est vide !";
    private static final Double ERROR_ORDER_AMOUNT_FIELD = -1D;
    
    public static final String CHEMIN = "chemin";
    
	private Map<String, String> errors = new HashMap<String, String>();
	
	/* *
	 * ********** CONSTRUCTOR **************************************************
	 * */
	public CreationCommandeForm() {
		super();
	}
	
	/* *
	 * ********** METHODS **************************************************
	 * */
	public Commande enregistrementCommande(HttpServletRequest request, String chemin) {
		
		CreationClientForm creationClientForm = new CreationClientForm();
		Client client = new Client();
		
		/* *
		 * Validation des données + création du bean client. 
		 * */
		client = creationClientForm.enregistrementClient(request, chemin);
		/* *
		 * Récupération du contenu de la map errors. 
		 * */
		errors = creationClientForm.getErrors();
		
		/* *
		 * La valeur est a true car si la session existe on ne la créait pas mais si elle n'existe pas on la créait. 
		 * Comme elle est déjà créé lors de la création du client alors elle ne fait rien mais on peut lui rajouter 
		 * des attributs.
		 * */
		HttpSession session = request.getSession(true);
		
		/* *
		 * Création de l'objet commande
		 * */
		Commande commande = new Commande();
		
		/* *
		 * Display of current date 
		 * */
		String timeStamp = new SimpleDateFormat(DATE_AND_TIME_FORMAT).format(Calendar.getInstance().getTime());
		
		commande.setClient(client);
		commande.setDateCommande(timeStamp);
		try { 
			commande.setMontantCommande(Double.valueOf(getFieldValue(request, ORDER_AMOUNT_FIELD))); 
		} catch (Exception e) {
			commande.setMontantCommande(ERROR_ORDER_AMOUNT_FIELD);
		}
		commande.setModePaiementCommande(getFieldValue(request, ORDER_PAYMENT_METHOD_FIELD));
		commande.setStatutPaiementCommande(getFieldValue(request, ORDER_PAYMENT_STATUS_FIELD));
		commande.setModeLivraisonCommande(getFieldValue(request, ORDER_DELIVERY_METHOD_FIELD));
		commande.setStatutLivraisonCommande(getFieldValue(request, ORDER_DELIVERY_STATUS_FIELD));
		
		session.setAttribute(ORDER_ATTRIBUTE, commande);
		Regex regex = new Regex();
		
		/* *
		 * Validation du champ montantCommande. 
		 * */
		try {
			regex.priceValidation(String.valueOf(commande.getMontantCommande()), ORDER_AMOUNT_MAX_CHARACTER_NUMBER);
		} catch (Exception e) {
			errors.put(ORDER_AMOUNT_FIELD, e.getMessage());
		}
		
		/* *
	     * Validation du champ modePaiementCommande.
		 * Dans le cas d'une valeur nulle avec la méthode length() cela provoquerait un fichier d'erreur utile à un mauvais 
	     * utilisateur. Condition "if" pour contrer une attaque en supprimant l'attribut required dans le fichier HTML. 
	     * La méthode length() ne pourrait lire une valeur nulle et ce paramètre ne pourrait être que nul si le champ n'est 
	     * pas rempli car la méthode getFieldValue() exclut tout autre possibilité.
		 * */
		if (commande.getModePaiementCommande() != null) {
			if (commande.getModePaiementCommande().length() < 2) {
				errors.put(ORDER_PAYMENT_METHOD_FIELD, LESS_OF_2_CHAR_SENTENCE);
			} else {
				try {
					regex.simpleTextValidation(commande.getModePaiementCommande(), ORDER_PAYMENT_METHOD_MAX_LETTER_NUMBER);
				} catch (Exception e) {
					errors.put(ORDER_PAYMENT_METHOD_FIELD, e.getMessage());
				}
			}
		} else {
			errors.put(ORDER_PAYMENT_METHOD_FIELD, FIELD_EMPTY_SENTENCE);
		}
		
		/* *
		 * Condition "if" car le champ du statut de paiement n'est pas obligatoire. De plus la méthode length() ne peut lire une valeur 
		 * nulle et le paramètre ne peut être que nul si le champ n'est pas rempli car la méthode getFieldValue() exclut tout 
		 * autre possibilité.
		 * */
		if (commande.getStatutPaiementCommande() != null) {
			if (commande.getStatutPaiementCommande().length() < 2) {
				errors.put(ORDER_PAYMENT_STATUS_FIELD, LESS_OF_2_CHAR_SENTENCE);
			} else {
				try {
					regex.simpleTextValidation(commande.getStatutPaiementCommande(), ORDER_PAYMENT_STATUS_MAX_LETTER_NUMBER);
				} catch (Exception e) {
					errors.put(ORDER_PAYMENT_STATUS_FIELD, e.getMessage());
				}
			}
		}
		
		/* *
	     * Validation du champ modeLivraisonCommande.
		 * Dans le cas d'une valeur nulle avec la méthode length() cela provoquerait un fichier d'erreur utile à un mauvais 
	     * utilisateur. Condition "if" pour contrer une attaque en supprimant l'attribut required dans le fichier HTML. 
	     * La méthode length() ne pourrait lire une valeur nulle et ce paramètre ne pourrait être que nul si le champ n'est 
	     * pas rempli car la méthode getFieldValue() exclut tout autre possibilité.
		 * */
		if (commande.getModeLivraisonCommande() != null) {
			if (commande.getModeLivraisonCommande().length() < 2) {
				errors.put(ORDER_DELIVERY_METHOD_FIELD, LESS_OF_2_CHAR_SENTENCE);
			} else {
				try {
					regex.simpleTextValidation(commande.getModeLivraisonCommande(), ORDER_DELIVERY_METHOD_MAX_LETTER_NUMBER);
				} catch (Exception e) {
					errors.put(ORDER_DELIVERY_METHOD_FIELD, e.getMessage());
				}
			}
		} else {
			errors.put(ORDER_DELIVERY_METHOD_FIELD, FIELD_EMPTY_SENTENCE);
		}
		
		/* *
		 * Condition "if" car le champ du statut de la livraison n'est pas obligatoire. De plus la méthode length() ne peut lire une valeur 
		 * nulle et le paramètre ne peut être que nul si le champ n'est pas rempli car la méthode getFieldValue() exclut tout 
		 * autre possibilité.
		 * */
		if (commande.getStatutLivraisonCommande() != null) {
			if (commande.getStatutLivraisonCommande().length() < 2) {
				errors.put(ORDER_DELIVERY_STATUS_FIELD, LESS_OF_2_CHAR_SENTENCE);
			} else {
				try {
					regex.simpleTextValidation(commande.getStatutLivraisonCommande(), ORDER_DELIVERY_STATUS_MAX_LETTER_NUMBER);
				} catch (Exception e) {
					errors.put(ORDER_DELIVERY_STATUS_FIELD, e.getMessage());
				}
			}
		}
		
		return commande;
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
