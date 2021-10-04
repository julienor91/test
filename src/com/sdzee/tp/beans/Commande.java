package com.sdzee.tp.beans;

public class Commande {
	
	/* *
	 * ********** ATTRIBUTES **************************************************
	 * */
	private Client client;
	private String dateCommande;
	private Double montantCommande;
	private String modePaiementCommande;
	private String statutPaiementCommande;
	private String modeLivraisonCommande;
	private String statutLivraisonCommande;
	
	/* *
	 * ********** CONSTRUCTORS **************************************************
	 * */
	public Commande() {
		super();
	}
	
	public Commande(Client client, String dateCommande, double montantCommande,
			String modePaiementCommande, String statutPaiementCommande,
			String modeLivraisonCommande, String statutLivraisonCommande) {
		super();
		this.client = client;
		this.dateCommande = dateCommande;
		this.montantCommande = montantCommande;
		this.modePaiementCommande = modePaiementCommande;
		this.statutPaiementCommande = statutPaiementCommande;
		this.modeLivraisonCommande = modeLivraisonCommande;
		this.statutLivraisonCommande = statutLivraisonCommande;
	}

	/* *
	 * ********** GETTER METHODS **************************************************
	 * */
	public Client getClient() {
		return client;
	}

	public String getDateCommande() {
		return dateCommande;
	}

	public double getMontantCommande() {
		return montantCommande;
	}

	public String getModePaiementCommande() {
		return modePaiementCommande;
	}

	public String getStatutPaiementCommande() {
		return statutPaiementCommande;
	}

	public String getModeLivraisonCommande() {
		return modeLivraisonCommande;
	}

	public String getStatutLivraisonCommande() {
		return statutLivraisonCommande;
	}

	/* *
	 * ********** SETTER METHODS **************************************************
	 * */
	public void setClient(Client client) {
		this.client = client;
	}

	public void setDateCommande(String dateCommande) {
		this.dateCommande = dateCommande;
	}

	public void setMontantCommande(double montantCommande) {
		this.montantCommande = montantCommande;
	}

	public void setModePaiementCommande(String modePaiementCommande) {
		this.modePaiementCommande = modePaiementCommande;
	}

	public void setStatutPaiementCommande(String statutPaiementCommande) {
		this.statutPaiementCommande = statutPaiementCommande;
	}

	public void setModeLivraisonCommande(String modeLivraisonCommande) {
		this.modeLivraisonCommande = modeLivraisonCommande;
	}

	public void setStatutLivraisonCommande(String statutLivraisonCommande) {
		this.statutLivraisonCommande = statutLivraisonCommande;
	}

	/* *
	 * ********** TOSTRING METHOD **************************************************
	 * */
	@Override
	public String toString() {
		return "Commande [client=" + client + ", dateCommande=" + dateCommande
				+ ", montantCommande=" + montantCommande
				+ ", modePaiementCommande=" + modePaiementCommande
				+ ", statutPaiementCommande=" + statutPaiementCommande
				+ ", modeLivraisonCommande=" + modeLivraisonCommande
				+ ", statutLivraisonCommande=" + statutLivraisonCommande + "]";
	}
	
}
