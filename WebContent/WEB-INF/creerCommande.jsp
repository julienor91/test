<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="UTF-8" />
        <title>Création d'une commande qui suit l'inscription client</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/fichiers/style.css"/>"/>
    </head>
    
    <body>
        <div>
        	<c:import url="/WEB-INF/inc/menu.jsp"/>
        </div>
        <div>
            <form method="post" action="<c:url value="/enregistrement-commande"/>" enctype="multipart/form-data"><!-- <url-pattern>/enregistrement-commande</url-pattern> -->
				<c:import url="/WEB-INF/inc/inc_client_form.jsp"/>
                <fieldset>
					<legend>Informations commande</legend>
                    
                    <label for="dateCommande">Date <span class="requis">*</span></label>
                    <input type="text" id="dateCommande" name="dateCommande" value="<c:out value="${ commande.dateCommande }"/>" size="20" maxlength="20" disabled required />
                    <br />
                   
                    <label for="montantCommande">Montant <span class="requis">*</span></label>
                    <input type="text" id="montantCommande" name="montantCommande" value="<c:out value="${ commande.montantCommande }"/>" size="20" maxlength="20" required />
                    <span class="erreur"><c:out value="${ !empty errors['montantCommande'] ? errors['montantCommande'] : '' }"/></span>
                    <br />
                    
                    <label for="modePaiementCommande">Mode de paiement <span class="requis">*</span></label>
                    <input type="text" id="modePaiementCommande" name="modePaiementCommande" value="<c:out value="${ commande.modePaiementCommande }"/>" size="20" minlength="2" maxlength="20" required />
                    <span class="erreur"><c:out value="${ !empty errors['modePaiementCommande'] ? errors['modePaiementCommande'] : '' }"/></span>
                    <br />
                    
                    <label for="statutPaiementCommande">Statut du paiement</label>
                    <input type="text" id="statutPaiementCommande" name="statutPaiementCommande" value="<c:out value="${ commande.statutPaiementCommande }"/>" size="20" minlength="2" maxlength="20" />
                    <span class="erreur"><c:out value="${ !empty errors['statutPaiementCommande'] ? errors['statutPaiementCommande'] : '' }"/></span>
                    <br />
                    
                    <label for="modeLivraisonCommande">Mode de livraison <span class="requis">*</span></label>
                    <input type="text" id="modeLivraisonCommande" name="modeLivraisonCommande" value="<c:out value="${ commande.modeLivraisonCommande }"/>" size="20" minlength="2" maxlength="20" required />
                    <span class="erreur"><c:out value="${ !empty errors['modeLivraisonCommande'] ? errors['modeLivraisonCommande'] : '' }"/></span>
                    <br />
                    
                    <label for="statutLivraisonCommande">Statut de la livraison</label>
                    <input type="text" id="statutLivraisonCommande" name="statutLivraisonCommande" value="<c:out value="${ commande.statutLivraisonCommande }"/>" size="20" minlength="2" maxlength="20" />
                    <span class="erreur"><c:out value="${ !empty errors['statutLivraisonCommande'] ? errors['statutLivraisonCommande'] : '' }"/></span>
                    <br />
                </fieldset>
                &nbsp;&nbsp;&nbsp;<input type="submit" value="Valider" />
	    		<input type="reset" value="re-initialiser" />
            </form>
        </div>
    </body>
</html>