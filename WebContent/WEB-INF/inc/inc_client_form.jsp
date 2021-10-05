<!DOCTYPE html>
<html lang="fr">
    <head>
		<meta charset="UTF-8" />
        <title>Formulaire d'enregistrement d'un client</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/fichiers/style.css"/>"/>
    </head>
    
    <body>
	    <fieldset>
	        <legend>Informations client</legend>
			
			<c:if test="${ !empty sessionScope.tabClients }">
				<label for="choixNouveauClient">Nouveau client ? <span class="requis">*</span></label>
				<input type="radio" id="choixNouveauClient" name="choixNouveauClient" value="nouveauClient" checked/>
					Oui&nbsp;&nbsp;&nbsp;
				<input type="radio" id="choixNouveauClient" name="choixNouveauClient" value="ancienClient"/>
					Non
				<br /><br />
			</c:if>
			
			<div id="ancienClient"><!-- div avec id pour javascript -->
				<c:if test="${ !empty sessionScope.tabClients }">
					<select name="listeClients" id="listeClients">
						<option value="${ client['nomClient'] }">Choisissez un client ...</option>
						<c:forEach items="${ sessionScope.tabClients }" var="entry">
							<option value="${ entry.value['nomClient'] }">
								<c:out value="${ entry.value['nomClient'] } ${ entry.value['prenomClient'] }" />
							</option>
						</c:forEach>
					</select>
					<br /><br />
				</c:if>
			</div>
			
			<div id="nouveauClient"><!-- div avec id pour javascript -->
		        <label for="nomClient">Nom <span class="requis">*</span></label>
		        <input type="text" id="nomClient" name="nomClient" value="<c:out value="${ empty client.nomClient ? commande.client.nomClient : client.nomClient }"/>" size="20" minlength="2" maxlength="20" required />
		        <span class="erreur"><c:out value="${ !empty errors['nomClient'] ? errors['nomClient'] : '' }"/></span>
		        <br />
		        
		        <label for="prenomClient">Prénom </label>
		        <input type="text" id="prenomClient" name="prenomClient" value="<c:out value="${ empty client.prenomClient ? commande.client.prenomClient : client.prenomClient }"/>" size="20" minlength="2" maxlength="20" />
		        <span class="erreur"><c:out value="${ !empty errors['prenomClient'] ? errors['prenomClient'] : '' }"/></span>
		        <br />
				
		        <label for="adresseClient">Adresse de livraison <span class="requis">*</span></label>
		        <input type="text" id="adresseClient" name="adresseClient" value="<c:out value="${ empty client.adresseClient ? commande.client.adresseClient : client.adresseClient }"/>" size="20" minlength="10" maxlength="20" required />
				<span class="erreur"><c:out value="${ !empty errors['adresseClient'] ? errors['adresseClient'] : '' }"/></span>
		        <br />
				
		        <label for="telephoneClient">Numéro de téléphone <span class="requis">*</span></label>
		        <input type="text" id="telephoneClient" name="telephoneClient" value="<c:out value="${ empty client.telephoneClient ? commande.client.telephoneClient : client.telephoneClient }"/>" size="20" minlength="4" maxlength="20" required />
		        <span class="erreur"><c:out value="${ !empty errors['telephoneClient'] ? errors['telephoneClient'] : '' }"/></span>
		        <br />
		        
		        <label for="emailClient">Adresse email</label>
		        <input type="email" id="emailClient" name="emailClient" value="<c:out value="${ empty client.emailClient ? commande.client.emailClient : client.emailClient }"/>" size="20" maxlength="60" />
		        <span class="erreur"><c:out value="${ !empty errors['emailClient'] ? errors['emailClient'] : '' }"/></span>
		        <br />
		        
		        <label for="imageClient">Image</label>
		        <input type="file" id="imageClient" name="imageClient" value="<c:out value="${ empty client.imageClient ? commande.client.imageClient : client.imageClient }"/>" />
		        <span class="erreur"><c:out value="${ !empty errors['imageClient'] ? errors['imageClient'] : '' }"/></span>
	        </div>
		</fieldset>
		
		<!-- Bibliothèque en ligne fournie directement par Google -->
<!-- 		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js"></script>  -->
		<script src="<c:url value="/fichiers/jquery-3.5.1.js"/>"></script>
		
		<script>
			/* Petite fonction jQuery permettant le remplacement de la première partie du formulaire par la liste déroulante, au clic sur le bouton radio. */
        	jQuery(document).ready(function(){
        		/* 1 - Au lancement de la page, on cache le bloc d'éléments du formulaire correspondant aux clients existants */
        		$("div#ancienClient").hide();
        		/* 2 - Au clic sur un des deux boutons radio "choixNouveauClient", on affiche le bloc d'éléments correspondant (nouveau ou ancien client) */
                jQuery('input[name=choixNouveauClient]:radio').click(function(){
                	$("div#nouveauClient").hide();
                	$("div#ancienClient").hide();
                    var divId = jQuery(this).val();
                    $("div#"+divId).show();
                });
            });
        </script>
    </body>
</html>