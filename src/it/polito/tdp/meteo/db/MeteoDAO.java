package it.polito.tdp.meteo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.bean.SimpleCity;

public class MeteoDAO {

	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {

		return null;
	}

	public Double getAvgRilevamentiLocalitaMese(int mese, String localita) {

		final String sql = "SELECT AVG(Umidita) AS media_umidita FROM situazione WHERE Localita=? AND month(Data)=?;";
		Double avgUmidita = -1.0;

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, localita);
			st.setInt(2, mese);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				avgUmidita = rs.getDouble("media_umidita");
			}

			conn.close();
			return avgUmidita;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
	public List<SimpleCity> listaCitta() {
		final String sql = "SELECT DISTINCT Localita FROM situazione";

		List<SimpleCity> tutteCitta = new ArrayList<>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				SimpleCity citta = new SimpleCity(rs.getString("Localita"));
				tutteCitta.add(citta);
			}

			conn.close();
			return tutteCitta;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

}
