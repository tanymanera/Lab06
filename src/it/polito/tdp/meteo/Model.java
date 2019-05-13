package it.polito.tdp.meteo;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
//	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 1;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
//	private final static int NUMERO_GIORNI_CITTA_MAX = 1;
	private final static int NUMERO_GIORNI_TOTALI = 15;
//	private final static int NUMERO_GIORNI_TOTALI = 3;
	
	private MeteoDAO dao;
	private List<SimpleCity> allCities;
	
	public Model() {
		dao = new MeteoDAO();
		allCities = dao.listaCitta();
	}

	public String getUmiditaMedia(int mese) {

		StringBuilder result = new StringBuilder();
		for (SimpleCity city : allCities) {
			result.append(city.toString());
			result.append(": ");
			result.append(dao.getAvgRilevamentiLocalitaMese(mese, city.toString()));
			result.append("\n");
		}
		return result.toString();
	}

	public String trovaSequenza(int mese) {

		List<SimpleCity> parziale = new ArrayList<>();
		generaSequenze(mese, parziale,0, 0);
		return "TODO!";
	}

	private void generaSequenze(int mese, List<SimpleCity> parziale, int numeroGiornoVisita, int levelDay) {
		// al giorno 15 si esce dalla ricorsione
		// ulteriore controllo se parziale visita tutte le citta
		if (levelDay == NUMERO_GIORNI_TOTALI) {
			if (areAllCityVisited(parziale) && numeroGiornoVisita >= NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN) {
				System.out.println(parziale.toString());
			}
			return;
		}

		// da levelDay si genera levelDay + 1
		for (SimpleCity city : allCities) {
			// genera nuova soluzione parziele
			parziale.add(new SimpleCity(city.getNome(), 0));
			// se la lista parziale soddisfa le condizioni locali fa partire il
			// livello successivo di ricorsione
			int newNumeroGiornoVisita = controllaParziale(parziale, numeroGiornoVisita, levelDay);
			if (newNumeroGiornoVisita > -1) {
				generaSequenze(mese, parziale, newNumeroGiornoVisita, levelDay + 1);
			}
			// backtracking
			parziale.remove(parziale.size() - 1);
		}
	}

	private boolean areAllCityVisited(List<SimpleCity> parziale) {
		// TODO Auto-generated method stub
		return true;
	}

	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {

		double score = 0.0;
		return score;
	}

	/**
	 * Controlla se ultima citta immessa è compatibile con i vincoli: almeno
	 * {@code NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN} in stessa città non più di
	 * {@code NUMERO_GIORNI_CITTA_MAX} nella stessa città
	 * 
	 * Il controllo non è definitivo perché a fine costruzione lista devo
	 * controllare che tutte le citta siano presenti.
	 * 
	 * @param parziale la lista attuale delle città presenti in lista
	 * @param numeroGiorniVisita persenze consecutive nella stessa citta prima inserimento ultima
	 * @param leveDay livello di ricorsione. ATTENZIONE non sono giorni perché partono da zero
	 * 
	 * @return numero giorni consecutivi nella città controllata, -1 se ultimo inserimento non va bene
	 */
	private int controllaParziale(List<SimpleCity> parziale, int numeroGiornoVisita, int levelDay) {

		// Vincolo 1: la stessa città non può essere visitata per più di 6 giorni
		int counter = 0;
		for (SimpleCity city : parziale) {
			if (city.equals(parziale.get(levelDay))) {
				counter++;
				if (counter > NUMERO_GIORNI_CITTA_MAX)
					return -1;
			}
		}

		// Vincolo 2: la visita deve durare almeno tre giorni consecutivi

		// Se è la prima città inserita tutto OK
		if (levelDay == 0) {
			//nella prima città inserita 1 giorno di visita
			return 1; 
		}
		// controllo 2+ città
		if (numeroGiornoVisita >= NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN) {
			if (parziale.get(levelDay).equals(parziale.get(levelDay - 1))) {
				numeroGiornoVisita++;
			} else {
				numeroGiornoVisita = 1;
			}
			return numeroGiornoVisita;
		} else { //non è stato raggiunto il numero minimo di giornate consecutive
			if (parziale.get(levelDay).equals(parziale.get(levelDay-1))) {
				numeroGiornoVisita++;
				return numeroGiornoVisita;
			} else { //si è inserita una città diversa da quella per raggiungere il numero minimo
				// il numero di giorni della visita non va modificato, perché l'ultimo inserito
				// viene subito cancellato e le visite consecutive valide sono le precedenti.
				return -1;
			}
		}
	}

}
