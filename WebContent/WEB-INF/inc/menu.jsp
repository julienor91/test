<!DOCTYPE html>
<html lang="fr">
	<head>
		<meta charset="UTF-8">
		<title>Menu</title>
		<link type="text/css" rel="stylesheet" href="<c:url value="/fichiers/style.css"/>"/>
	</head>

	<body>
		<fieldset>
			<input type="button" onclick="window.location.href = '<c:url value="/enregistrement-client"/>';" value="Créer un compte client"/>
			<input type="button" onclick="window.location.href = '<c:url value="/enregistrement-commande"/>';" value="Créer une commande"/>
			<br />
			<input type="button" onclick="window.location.href = '<c:url value="/liste-clients"/>';" value="Voir les clients existants"/>
			<input type="button" onclick="window.location.href = '<c:url value="/liste-commandes"/>';" value="Voir les commandes existantes"/>
		</fieldset>
	</body>
</html>