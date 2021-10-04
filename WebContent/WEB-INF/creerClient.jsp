<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="UTF-8" />
        <title>Création d'un client</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/fichiers/style.css"/>"/>
    </head>
    
    <body>
        <div>
        	<c:import url="/WEB-INF/inc/menu.jsp"/>
        </div>
        <div>
        	<form method="post" action="<c:url value="/enregistrement-client"/>"><!-- <url-pattern>/enregistrement-client</url-pattern> -->
        		<c:import url="/WEB-INF/inc/inc_client_form.jsp"/>
        		&nbsp;&nbsp;&nbsp;<input type="submit" value="Valider" />
	    		<input type="reset" value="Remettre à zéro" />
        	</form>
        </div>
    </body>
</html>