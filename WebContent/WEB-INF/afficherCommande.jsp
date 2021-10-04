<!DOCTYPE html>
<html lang="fr">
	<head>
		<meta charset="UTF-8">
		<title>Afficher la commande</title>
		<link type="text/css" rel="stylesheet" href="<c:url value="/fichiers/style.css"/>"/>
	</head>
	
	<body>
		<div>
        	<c:import url="/WEB-INF/afficherClient.jsp"/>
        </div>
		<div>	
			<fieldset>
				<legend>Informations commande</legend>
				<p>Date de la commande : <c:out value="${ commande.dateCommande }"/></p>
				<p>Montant de la commande : <c:out value="${ commande.montantCommande }"/></p>
				<p>Mode de paiement : <c:out value="${ commande.modePaiementCommande }"/></p>
				<p>Statut du paiement : <c:out value="${ commande.statutPaiementCommande }"/></p>
				<p>Mode de livraison : <c:out value="${ commande.modeLivraisonCommande }"/></p>
				<p>Statut de la livraison : <c:out value="${ commande.statutLivraisonCommande }"/></p>
				<input type="button" onclick="window.location.href='<c:url value="/enregistrement-commande"/>';" value="Retours"/><br />
			</fieldset>
		</div>
	</body>
</html>