package com.sdzee.tp.beans;

public class Client {
	
	/* *
	 * ********** ATTRIBUTES **************************************************
	 * */
	private String nomClient;
	private String prenomClient;
	private String adresseClient;
	private String telephoneClient;
	private String emailClient;
	private String imageClient;
	
	/* *
	 * ********** CONSTRUCTORS **************************************************
	 * */
	public Client() {
		super();
	}
	
	public Client(String nomClient, String prenomClient, String adresseClient,
			String telephoneClient, String emailClient, String imageClient) {
		super();
		this.nomClient = nomClient;
		this.prenomClient = prenomClient;
		this.adresseClient = adresseClient;
		this.telephoneClient = telephoneClient;
		this.emailClient = emailClient;
		this.imageClient = imageClient;
	}
	
	/* *
	 * ********** GETTER METHODS **************************************************
	 * */
	public String getNomClient() {
		return nomClient;
	}

	public String getPrenomClient() {
		return prenomClient;
	}

	public String getAdresseClient() {
		return adresseClient;
	}

	public String getTelephoneClient() {
		return telephoneClient;
	}

	public String getEmailClient() {
		return emailClient;
	}
	
	public String getImageClient() {
		return imageClient;
	}

	/* *
	 * ********** SETTER METHODS **************************************************
	 * */
	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

	public void setPrenomClient(String prenomClient) {
		this.prenomClient = prenomClient;
	}

	public void setAdresseClient(String adresseClient) {
		this.adresseClient = adresseClient;
	}

	public void setTelephoneClient(String telephoneClient) {
		this.telephoneClient = telephoneClient;
	}

	public void setEmailClient(String emailClient) {
		this.emailClient = emailClient;
	}
	
	public void setImageClient(String imageClient) {
		this.imageClient = imageClient;
	}
	
	/* *
	 * ********** TOSTRING METHOD **************************************************
	 * */
	@Override
	public String toString() {
		return "Client [nomClient=" + nomClient + ", prenomClient="
				+ prenomClient + ", adresseClient=" + adresseClient
				+ ", telephoneClient=" + telephoneClient + ", emailClient="
				+ emailClient + ", imageClient=" + imageClient + "]";
	}
	
}
