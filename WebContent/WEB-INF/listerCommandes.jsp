<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Lister les commandes</title>
		<link type="text/css" rel="stylesheet" href="<c:url value="/fichiers/style.css"/>"/>
	</head>
	
	<body>
		<div>
        	<c:import url="/WEB-INF/inc/menu.jsp"/>
        </div>
        
        <div>
        	<c:choose>
        		<c:when test="${ empty sessionScope.tabCommandes }">
        			<p class="phrase">Aucune commande enregistrée.</p>
        		</c:when>
        		<%-- Sinon, affichage du tableau. --%>
            	<c:otherwise>
            		<form method="post" action="<c:url value="/liste-commandes"/>"><!-- <url-pattern>/enregistrement-client</url-pattern> -->
			        	<table>
			        		
			        		<thead>
			        			<tr>
									<th>Client</th>
									<th>Date</th>
									<th>Montant</th>
									<th>Mode de paiement</th>
									<th>Statut de paiement</th>
									<th>Mode de livraison</th>
									<th>Statut de livraison</th>
									<th class="action">Action</th>
								</tr>
			        		</thead>
		        			
			        		<tbody>
			        			<c:forEach items="${ sessionScope.tabCommandes }" var="entry">
									<tr>
										<td><c:out value="${ entry.value.client['prenomClient'] } ${ entry.value.client['nomClient'] }" /></td>
										<td><c:out value="${ entry.value['dateCommande'] }" /></td>
										<td><c:out value="${ entry.value['montantCommande'] }" /></td>
										<td><c:out value="${ entry.value['modePaiementCommande'] }" /></td>
										<td><c:out value="${ entry.value['statutPaiementCommande'] }" /></td>
										<td><c:out value="${ entry.value['modeLivraisonCommande'] }" /></td>
										<td><c:out value="${ entry.value['statutLivraisonCommande'] }" /></td>
										<td><input type="image" class="checkCase" name="commandeSupprime" value="<c:out value="${ entry.key }"/>" alt="Cliquez pour effacer la commande" src="images/red-cross.png"></td>
									</tr>
								</c:forEach>
			        		</tbody>
		        			
						</table>
					</form>
            	</c:otherwise>
        	</c:choose>
        </div>
	</body>
</html>