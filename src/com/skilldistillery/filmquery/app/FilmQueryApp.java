package com.skilldistillery.filmquery.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) {
		FilmQueryApp app = new FilmQueryApp();
		app.test();
//    app.launch();
	}

	private void test() {
//    Film film = db.findFilmById(1);
		List<Film> films = ((DatabaseAccessorObject)db).findFilmsByActorId(5);
		for (Film film : films) {
			System.out.println(film);
		}
//		List<Actor> actors = ((DatabaseAccessorObject)db).findActorsByFilmId(970);
//		for (Actor actor : actors) {
//			System.out.println(actor);
//		}
//		System.out.println(((DatabaseAccessorObject)db).findActorById(138));
//		System.out.println(((DatabaseAccessorObject)db).findFilmById(1).getActors());
//		System.out.println(films);
	}

	private void launch() {
		Scanner input = new Scanner(System.in);

		startUserInterface(input);

		input.close();
	}

	private void startUserInterface(Scanner input) {
		// Menu driven program. Use this to interact with DatabaseAccessObject
		// Consider a sub-menu per each request. This should help filter selections.

	}

}
