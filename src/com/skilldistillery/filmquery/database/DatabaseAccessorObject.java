package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {

	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";

	/*
	 * User Story 1 The user is presented with a menu in which they can choose to:
	 * 
	 * Look up a film by its id. Look up a film by a search keyword. Exit the
	 * application.
	 * 
	 * 
	 * User Story 2 If the user looks up a film by id, they are prompted to enter
	 * the film id. If the film is not found, they see a message saying so. If the
	 * film is found, its title, year, rating, and description are displayed.
	 * 
	 * 
	 * User Story 3 If the user looks up a film by search keyword, they are prompted
	 * to enter it. If no matching films are found, they see a message saying so.
	 * Otherwise, they see a list of films for which the search term was found
	 * anywhere in the title or description, with each film displayed exactly as it
	 * is for User Story 2.
	 * 
	 * 
	 * User Story 4 When a film is displayed, its language (English,Japanese, etc.)
	 * is also displayed.
	 * 
	 * 
	 * User Story 5 When a film is displayed, the list of actors in its cast is
	 * displayed along with the title, year, rating, and description.
	 * 
	 * 
	 */

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// THIS IS THE ONLY PLACE THAT INTERACTS WITH THE DATABASE!!!

	@Override
	public Film findFilmById(int filmId) {
		Film film = null;

		try {
			String sql = "select * from film where film.id = ?";
			String user = "student";
			String pass = "student";
			Connection conn = DriverManager.getConnection(URL, user, pass);
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs2 = stmt.executeQuery();
			while (rs2.next() && !rs2.wasNull()) {
				Integer filmNumber = rs2.getInt("id");
				String title = rs2.getString("title");
				String desc = rs2.getString("description");
				Integer releaseYear = (int) rs2.getShort("release_year");
				int langId = rs2.getInt("language_id");
				int rentDur = rs2.getInt("rental_duration");
				double rate = rs2.getDouble("rental_rate");
				int length = rs2.getInt("length");
				double repCost = rs2.getDouble("replacement_cost");
				String rating = rs2.getString("rating");
				String features = rs2.getString("special_features");
				
				film = new Film(filmNumber, title, desc, releaseYear, langId, rentDur, rate, length, repCost, rating,
						features);

				List<Actor> actors = findActorsByFilmId(filmNumber);
				film.setActors(actors);
				film.setLangauge(findLanguageByLanguageId(langId));

			}
			rs2.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("findFilmById");
			System.out.println(e);

		}

		return film;
	}

	@Override
	public Actor findActorById(int actorId) {
		Actor actor = null;
		try {

			String sql = "select actor.first_name, actor.last_name, actor.id from actor where actor.id = ?";
			String user = "student";
			String pass = "student";
			Connection conn = DriverManager.getConnection(URL, user, pass);
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet rs1 = stmt.executeQuery();
			while (rs1.next() && !(rs1.wasNull())) {
				int actorNumber = rs1.getInt("id");
				String actorFirstName = rs1.getString("first_name");
				String actorLastName = rs1.getString("last_name");
				actor = new Actor(actorNumber, actorFirstName, actorLastName);

			}
			rs1.close();
			stmt.close();
			conn.close();

		} catch (SQLException e) {
			System.out.println("findActorById");
			System.out.println(e);
			return null;

		}
		return actor;

	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> actors = new ArrayList<>();

		try {
			String sql = "select actor.id from actor join film_actor on actor.id = film_actor.actor_id join film on film.id = film_actor.film_id where film.id = ?";
			String user = "student";
			String pass = "student";
			Connection conn = DriverManager.getConnection(URL, user, pass);
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs3 = stmt.executeQuery();
			while (rs3.next() && !(rs3.wasNull())) {
				actors.add(findActorById(rs3.getInt("id")));

			}
			rs3.close();
			stmt.close();
			conn.close();

		} catch (SQLException e) {
			System.out.println("findActorsByFilmId");
			System.out.println(e);
		}
		return actors;
	}

	public List<Film> findFilmsByActorId(int actorId) {
		List<Film> films = new ArrayList<>();
		String sql = "SELECT film.id, film.title, film.description, film.release_year, "
				+ "film.language_id, film.rental_duration, film.rental_rate, film.length, "
				+ "film.replacement_cost, film.rating, film.special_features" + " FROM film JOIN film_actor "
				+ "ON film.id = film_actor.film_id " + "WHERE film_actor.actor_id = ?";

		try {
			String user = "student";
			String pass = "student";
			Connection conn = DriverManager.getConnection(URL, user, pass);
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet rs4 = stmt.executeQuery();
			while (rs4.next()) {
				films.add(findFilmById(rs4.getInt("id")));

			}
			rs4.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("findFilmsByActorId");
			System.out.println(e);

		} catch (NullPointerException e) {
			System.out.println("NullPointer in findFilmsByActorId");

		}
		return films;
	}

	@Override
	public List<Film> findFilmByKeyword(String regexString) {
		List<Film> films = new ArrayList<>();
		try {
			String sql = "select * from film where film.title like ? or film.description like ?";
			String user = "student";
			String pass = "student";
			Connection conn = DriverManager.getConnection(URL, user, pass);
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, "%" + regexString + "%");
			stmt.setString(2, "%" + regexString + "%");
			ResultSet rs5 = stmt.executeQuery();
			while (rs5.next()) {
				films.add(findFilmById(rs5.getInt("id")));
			}
			rs5.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return films;
	}

	private String findLanguageByLanguageId(int langId) {
		String language = "";
		try {
			String sql = "select language.name from language join film on language.id = film.language_id where language.id = ?";
			String user = "student";
			String pass = "student";
			Connection conn = DriverManager.getConnection(URL, user, pass);
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, langId);
			ResultSet rs6 = stmt.executeQuery();
			while (rs6.next()) {
				language = rs6.getString("language.name");
			}
			rs6.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("Language not found.");
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		
		return language;
	}

}
