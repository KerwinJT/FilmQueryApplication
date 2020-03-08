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
			PreparedStatement stmt = openAccess(sql);
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
				if (filmNumber != null) {
					List<Actor> actors = findActorsByFilmId(filmNumber);
					film.setActors(actors);
				}
			}
		} catch (SQLException e) {
			System.out.println("Could not find relevent data.");

		}

		return film;
	}

	@Override
	public Actor findActorById(int actorId) {
		Actor actor = null;
		try {

			String sql = "select actor.first_name, actor.last_name, actor.id from actor where actor.id = ?";
			PreparedStatement stmt = openAccess(sql);
			stmt.setInt(1, actorId);
			ResultSet rs1 = stmt.executeQuery();
			while (rs1.next() && !(rs1.wasNull())) {
				int actorNumber = rs1.getInt("id");
				String actorFirstName = rs1.getString("first_name");
				String actorLastName = rs1.getString("last_name");
				actor = new Actor(actorNumber, actorFirstName, actorLastName);

			}

		} catch (SQLException e) {
			System.out.println("Could not find relevent data.");
			return null;

		}
		return actor;

	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> actors = new ArrayList<>();

		try {
			String sql = "select actor.id from actor join film_actor on actor.id = film_actor.actor_id join film on film.id = film_actor.film_id where film.id = ?";
			PreparedStatement stmt = openAccess(sql);
			stmt.setInt(1, filmId);
			ResultSet rs3 = stmt.executeQuery();
			while (rs3.next() && !(rs3.wasNull())) {
				if (findActorById(rs3.getInt("id")) != null) {
					actors.add(findActorById(rs3.getInt("id")));
				}
			}

		} catch (SQLException e) {
			System.out.println("Could not find relevent data.");
		}
		return actors;
	}

	public List<Film> findFilmsByActorId(int actorId) {
		List<Film> films = new ArrayList<>();
		try {
			String sql = "SELECT film.id, film.title, film.description, film.release_year, "
					+ "film.language_id, film.rental_duration, film.rental_rate, film.length, "
					+ "film.replacement_cost, film.rating, film.special_features" + " FROM film JOIN film_actor "
					+ "ON film.id = film_actor.film_id " + "WHERE film_actor.actor_id = ?";

			PreparedStatement stmt = openAccess(sql);
			stmt.setInt(1, actorId);
			ResultSet rs4 = stmt.executeQuery();
			System.out.println("Going into while");
			while (rs4.next()) {
				if (!(rs4.wasNull())) {
					System.out.println("Trying to find film");
					System.out.println(rs4.getInt("id"));
					System.out.println(rs4.wasNull());
					System.out.println(films.add(findFilmById(rs4.getInt("id"))));
					System.out.println("After findFilmById");
					System.out.println();
				}

			}

		} catch (SQLException e) {
			System.out.println("Could not find relevent data.");

		}
		return films;
	}

	private PreparedStatement openAccess(String sql) {
		try {
			String user = "student";
			String pass = "student";
			Connection conn = DriverManager.getConnection(URL, user, pass);
			PreparedStatement stmt = conn.prepareStatement(sql);
			return stmt;
		} catch (SQLException e) {
			System.out.println("Connection to database failed. Please check connection.");
			;
		}
		return null;

	}

}
