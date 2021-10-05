<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Lister les clients</title>
		<link type="text/css" rel="stylesheet" href="<c:url value="/fichiers/style.css"/>"/>
	</head>
	
	<body>
		<div>
        	<c:import url="/WEB-INF/inc/menu.jsp"/>
        </div>
        
        <div>
        	<c:choose>
        		<c:when test="${ empty sessionScope.tabClients }">
        			<p class="phrase">Aucun client enregistré.</p>
        		</c:when>
        		<%-- Sinon, affichage du tableau. --%>
        		<c:otherwise>
        			<form method="post" action="<c:url value="/liste-clients"/>"><!-- <url-pattern>/liste-clients</url-pattern> -->
		        		<table>
		        		
			        		<thead>
			        			<tr>
									<th>Nom</th>
									<th>Prénom</th>
									<th>Adresse</th>
									<th>Téléphone</th>
									<th>Email</th>
									<th>Image</th>
									<th class="action">Action</th>
								</tr>
			        		</thead>
		        			
			        		<tbody>
			        			<c:forEach items="${ sessionScope.tabClients }" var="entry">
									<tr>
										<td><c:out value="${ entry.value['nomClient'] }" /></td>
										<td><c:out value="${ entry.value['prenomClient'] }" /></td>
										<td><c:out value="${ entry.value['adresseClient'] }" /></td>
										<td><c:out value="${ entry.value['telephoneClient'] }" /></td>
										<td><c:out value="${ entry.value['emailClient'] }" /></td>
										<td><input type="submit" class="imageLink" name="imageLink" value="<c:out value="${ entry.value['imageClient'] }" />" alt="Voir l'image"></td>
										<td><input type="image" class="checkCase" name="clientSupprime" value="<c:out value="${ entry.key }"/>" alt="Cliquez pour effacer le client" src="images/red-cross.png"></td>
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