package com.sdzee.tp.forms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

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
    private static final String IMAGE_FIELD = "imageClient";
    
    private static final Byte LAST_NAME_MAX_LETTER_NUMBER = 20;
    private static final Byte FIRST_NAME_MAX_LETTER_NUMBER = 20;
    private static final Byte ADDRESS_MAX_LETTER_NUMBER = 20;
    private static final Byte PHONE_NUMBER_MAX_LETTER_NUMBER = 10;
    private static final Byte EMAIL_MAX_LETTER_NUMBER = 60;
    private static final int TAILLE_TAMPON = 10240; // 10 ko
    
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
	public Client enregistrementClient(HttpServletRequest request, String chemin) {
		
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
			 * La méthode enregistrerImage() enregistre l'image à l'emplacement décidé et renvoie son nom 
			 * via une chaine de caractère String
			 * */
			String nomImage = enregistrerImage(request, chemin);
			client.setImageClient(nomImage);
			
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
	
	public String enregistrerImage(HttpServletRequest request, String chemin) {
		
        String nomImage = null;
        InputStream contenuImage = null;
        try {
        	/* *
    	     * Les données reçues sont multipart, on doit donc utiliser la méthode
    	     * getPart() pour traiter le champ d'envoi de fichiers. La méthode 
    	     * getParts() avec un "s" est pour les collections. Pour rendre disponible 
    	     * cette méthode dans une servlet, il faut compléter sa déclaration dans 
    	     * le fichier web.xml avec une section <multipart-config>
    	     * Objet part donne une adresse :
    	     * ex => org.apache.catalina.core.ApplicationPart@4dac004
    	     * */
            Part part = request.getPart(IMAGE_FIELD);
            /* *
             * Il faut déterminer s'il s'agit bien d'un champ de type fichier :
             * on délègue cette opération à la méthode utilitaire getNomFichier().
             * ex => index.html
             * */
            nomImage = getNomFichier(part);

            /* *
             * Si la méthode a renvoyé quelque chose, il s'agit donc d'un
             * champ de type fichier (input type="file").
             * */
            if (nomImage != null && !nomImage.isEmpty()) {
            	/* ***** DEBUT DE LA LIGNE DE CODE NON OBLIGATOIRE 
            	 * POUR PALIER AU BUG DU NAVIGATEUR 
    	         * INTERNET-EXPLORER *****
                 * Antibug pour Internet Explorer, qui transmet pour une
                 * raison mystique le chemin du fichier local à la machine
                 * du client...
                 * 
                 * Ex : C:/dossier/sous-dossier/fichier.ext
                 * 
                 * On doit donc faire en sorte de ne sélectionner que le nom
                 * et l'extension du fichier, et de se débarrasser du
                 * superflu.
                 * */
            	nomImage = nomImage.substring(nomImage.lastIndexOf('/') + 1)
                        .substring(nomImage.lastIndexOf('\\') + 1);
                /***** FIN DE LA LIGNE DE CODE NON OBLIGATOIRE POUR 
                 * PALIER AU BUG DU NAVIGATEUR 
    	         * INTERNET-EXPLORER ***** */

                /* *
                 * Récupération du contenu du fichier 
                 * */
                contenuImage = part.getInputStream();
                
                if (!testImage(nomImage)) {
                	errors.put(IMAGE_FIELD, "Le fichier envoyé n'est pas une image.");
                }
                
            }
        } catch (IllegalStateException e) {
            /* *
             * Exception retournée si la taille des données dépasse les limites
             * définies dans la section <multipart-config> de la déclaration de
             * notre servlet d'upload dans le fichier web.xml
             * */
            e.printStackTrace();
            errors.put(IMAGE_FIELD, "Les données envoyées sont trop volumineuses (1 Mo max).");
        } catch (IOException e) {
            /* *
             * Exception retournée si une erreur au niveau des répertoires de
             * stockage survient (répertoire inexistant, droits d'accès
             * insuffisants, etc.)
             * */
            e.printStackTrace();
            errors.put(IMAGE_FIELD, "Erreur de configuration du serveur.");
        } catch (ServletException e) {
            /* *
             * Exception retournée si la requête n'est pas de type
             * multipart/form-data. Cela ne peut arriver que si l'utilisateur
             * essaie de contacter la servlet d'upload par un formulaire
             * différent de celui qu'on lui propose... attaque pirate ! :|
             * */
            e.printStackTrace();
            errors.put(IMAGE_FIELD, 
                    "Ce type de requête n'est pas supporté, merci d'utiliser le formulaire prévu pour envoyer votre fichier.");
        }
        
        /* Si aucune erreur n'est survenue jusqu'à présent */
        if (errors.isEmpty()) {
            /* Validation du champ fichier. */
            try {
                validationFichier(nomImage, contenuImage);
            } catch (Exception e) {
            	errors.put(IMAGE_FIELD, e.getMessage());
            }
        }
        
        /* Si aucune erreur n'est survenue jusqu'à présent */
        if (errors.isEmpty()) {
            /* Écriture du fichier sur le disque */
            try {
                ecrireFichier(contenuImage, nomImage, chemin);
            } catch (Exception e) {
            	errors.put(IMAGE_FIELD, "Erreur lors de l'écriture du fichier sur le disque.");
            }
        }
        
        return nomImage;
    }
	
    /* *
     * Valide le fichier envoyé.
     * */
    private void validationFichier(String nomFichier, InputStream contenuFichier) throws Exception {
        if (nomFichier == null || contenuFichier == null) {
            throw new Exception("Merci de sélectionner un fichier à envoyer.");
        }
    }
    
    /* *
     * Méthode pour tester si le fichier est une image ou non via son extension. Cependant la bibliothèque 
     * MimeUtil permet d'arriver au même resultat mais de manière plus sécurisé.
     * exemple de l'utalisation de cette bibliothèque => 
     * Extraction du type MIME du fichier depuis l'InputStream nommé "contenu" 
	 *	MimeUtil.registerMimeDetector( "eu.medsea.mimeutil.detector.MagicMimeMimeDetector" );
	 *	Collection<?> mimeTypes = MimeUtil.getMimeTypes( contenu );
	 * Si le fichier est bien une image, alors son en-tête MIME
	 * commence par la chaîne "image"
	 *	if ( mimeTypes.toString().startsWith( "image" ) ) {
	 * Appeler ici la méthode d'écriture du fichier sur le disque... 
    *	} else {
    * Envoyer ici une exception précisant que le fichier doit être une image... 
    *	}
    * */
    private boolean testImage(String nomImage) {
    	
    	boolean imageExtension;
    	
    	if (nomImage.substring(nomImage.indexOf(".") + 1).equals("jpg") || 
    			nomImage.substring(nomImage.indexOf(".") + 1).equals("jpeg") ||
    			nomImage.substring(nomImage.indexOf(".") + 1).equals("png") ||
    			nomImage.substring(nomImage.indexOf(".") + 1).equals("html") || // pour faire le test
    			nomImage.substring(nomImage.indexOf(".") + 1).equals("gif") || 
    			nomImage.substring(nomImage.indexOf(".") + 1).equals("svg")) {
    		imageExtension = true;
    	} else {
    		imageExtension = false;
    	}
    	
    	return imageExtension;
    }
    
    /* *
     * Méthode utilitaire qui a pour unique but d'analyser l'en-tête
     * "content-disposition", et de vérifier si le paramètre "filename" y est
     * présent. Si oui, alors le champ traité est de type File et la méthode
     * retourne son nom, sinon il s'agit d'un champ de formulaire classique et
     * la méthode retourne null.
     * */
    private static String getNomFichier(Part part) {
    	/* * 
	     * Boucle sur chacun des paramètres de l'en-tête "content-disposition". 
	     * Cela exclut tous les autres noms de paramètre du Header
	     * Exemple => 		// Pour un champ <input type="text"> nommé 'description'
		 *							Content-Disposition: form-data; name="description"
		 *
		 *							// Pour un champ <input type="file"> nommé 'fichier'
		 *							Content-Disposition: form-data; name="fichier"; filename="nom_du_fichier.ext"
	     * <input type="file"> permet donc d'avoir un paramètre "filename" et sa valeur.
	     * Donc soit il y a un paramètre "filename" et c'est bien un fichier, soit il n'y a pas de 
	     * paramètre "filename" et ce n'est pas un fichier.
	     * */
        for (String contentDisposition : part.getHeader("content-disposition").split(";")) {
            /* *
             * Recherche de l'éventuelle présence du paramètre "filename". 
             * */
            if (contentDisposition.trim().startsWith("filename")) {
                /* *
                 * Si "filename" est présent, alors renvoi de sa valeur qui est inscite après le "=" (paramètre="valeur"),
                 * c'est-à-dire du nom de fichier sans guillemets. Puis méthode pour remplacer les apostrophes.
                 * */
                return contentDisposition.substring(contentDisposition.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        /* Et pour terminer, si rien n'a été trouvé... */
        return null;
    }
    
    /* *
     * Méthode utilitaire qui a pour but d'écrire le fichier passé en paramètre
     * sur le disque, dans le répertoire donné et avec le nom donné.
     * */
    private void ecrireFichier(InputStream contenu, String nomFichier, String chemin) throws Exception {
        /* Prépare les flux. */
        BufferedInputStream entree = null;
        BufferedOutputStream sortie = null;
        try {
            /* Ouvre les flux. */
        	/* *
	    	 * Dans le flux entree, il nous suffit de récupérer le flux directement depuis la méthode getInputStream() 
	    	 * de l'objet Part. Nous décorons ensuite ce flux avec un BufferedInputStream, avec ici dans l'exemple un 
	    	 * tampon de 10 ko => ATTENTION A LA TAILLE DU FICHIER DE NE PAS DEPASSER !
	    	 * */
            entree = new BufferedInputStream(contenu, TAILLE_TAMPON);
            /* *
	    	 * Dans le flux sortie, nous devons mettre en place un fichier sur le disque, en vue d'y écrire ensuite le contenu 
	    	 * de l'entrée. Nous décorons ensuite ce flux avec un BufferedOutputStream, avec ici dans l'exemple un tampon 
	    	 * de 10 ko.
	    	 * */
            sortie = new BufferedOutputStream(new FileOutputStream(
            		new File(chemin + nomFichier)), TAILLE_TAMPON);

            /*
             * Lit le fichier reçu et écrit son contenu dans un fichier sur le
             * disque.
             */
            byte[] tampon = new byte[TAILLE_TAMPON];
            int longueur = 0;
            while ((longueur = entree.read(tampon)) > 0) {
                sortie.write(tampon, 0, longueur);
            }
            /* *
	         * Toujours ouvrir les flux dans un bloc try, et les fermer dans le bloc finally associé. Ainsi, nous nous assurons, 
	         * quoi qu'il arrive, que la fermeture de nos flux sera bien effectuée !
	         * */
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
