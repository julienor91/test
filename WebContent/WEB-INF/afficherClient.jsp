<!DOCTYPE html>
<html lang="fr">
	<head>
		<meta charset="UTF-8">
		<title>Afficher le client</title>
		<link type="text/css" rel="stylesheet" href="<c:url value="/fichiers/style.css"/>"/>
	</head>

	<body>
        <div>
        	<c:import url="/WEB-INF/inc/menu.jsp"/>
        </div>
        <div>
        	<p class="succes">&nbsp;&nbsp;&nbsp;<c:if test="${ !empty succesMessage }">${ succesMessage }</c:if></p>
        </div>
        <div>
			<fieldset>
				<p>Nom : <c:out value="${ empty client.nomClient ? commande.client.nomClient : client.nomClient }"/></p>
				<p>Prénom : <c:out value="${ empty client.prenomClient ? commande.client.prenomClient : client.prenomClient }"/></p>
				<p>Adresse de livraison : <c:out value="${ empty client.adresseClient ? commande.client.adresseClient : client.adresseClient }"/></p>
				<p>Numéro de téléphone : <c:out value="${ empty client.telephoneClient ? commande.client.telephoneClient : client.telephoneClient }"/></p>
				<p>Adresse email : <c:out value="${ empty client.emailClient ? commande.client.emailClient : client.emailClient }"/></p>
				<p>Image : <c:out value="${ empty client.imageClient ? commande.client.imageClient : client.imageClient }"/></p>
				<input type="button" onclick="window.location.href='<c:url value="/enregistrement-client"/>';" value="Retours"/><br />
	        </fieldset>
		</div>
	</body>
</html>