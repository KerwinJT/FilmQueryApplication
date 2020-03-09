package com.skilldistillery.filmquery.app;

import java.util.ArrayList;
import java.util.InputMismatchException;
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
//		app.test();
		app.launch();
	}

//	private void test() {
////    
//	}

	private void launch() {
		Scanner input = new Scanner(System.in);

		startUserInterface(input);

		input.close();
	}

	private void startUserInterface(Scanner input) {
		// Menu driven program. Use this to interact with DatabaseAccessObject
		// Consider a sub-menu per each request. This should help filter selections.
		int choice = 0;
		System.out.println("\tWelcome to the Film Query Application.".toUpperCase());
		while (choice != 3) {
			System.out.println("\tPlease choose from the following menu:".toUpperCase());
			System.out.println("1. Search film by ID:");
			System.out.println("2. Search film by Keyword:");
			System.out.println("3. Exit application:");

			try {
				System.out.print("Choice: ");
				choice = input.nextInt();
				input.nextLine();
			} catch (InputMismatchException e) {
				System.out.println("Please enter a valid selection.\n");
				input.nextLine();
			}

			switch (choice) {
			case 1:
				menuChoiceFilmId(input);
				break;
			case 2:
				menuChoiceKeywordSearch(input);
				break;
			case 3:
				System.out.println("\tThank you for using the automated film application".toUpperCase());
				break;
			default:
				System.out.println("Invalid selection.");
			}
		}

	}

	private void menuChoiceFilmId(Scanner keyboard) {
		Film film = null;
		System.out.println("\t**Search by Film ID**".toUpperCase());
		System.out.print("Enter film ID: ");
		try {
			film = (db.findFilmById(keyboard.nextInt()));
		} catch (InputMismatchException e) {
			keyboard.nextLine();
			System.out.println("Please provide and integer value.");
		}
		if (film != null) {
			displayFilm(film);
			int choice = 0;
			System.out.println("Would you lke to see additional information?");
			System.out.print("1) For additional information: ");
			try {
				choice = keyboard.nextInt();
				
			} catch (InputMismatchException e) {
				System.out.println("Invalid entry.");
				// TODO: handle exception
			}
			keyboard.nextLine();
			switch (choice) {
			case 1:
				System.out.println(film.toString());
				break;
			default:
				System.out.println("Return to main menu.");
			}
		} else {
			System.out.println("\nFilm not found.\n");
		}
		
		
		
	}

	private void menuChoiceKeywordSearch(Scanner keyboard) {
		List<Film> films = new ArrayList<>();
		System.out.println("\t**Search by keyword**".toUpperCase());
		System.out.print("Enter a keyword: ");
		try {
			films.addAll(db.findFilmByKeyword(keyboard.nextLine()));
		} catch (Exception e) {
			System.out.println("Error in retrieving films.");// TODO: handle exception
		}
		if (!films.isEmpty()) {
			for (Film film : films) {
				if (film != null) {
					displayFilm(film);
				}
			}
		} else {
			System.out.println("No matches were found\n");
		}

	}

	private void displayFilm(Film film) {
		System.out.println("\nFilm title: " + film.getTitle());
		System.out.println("Year filmed: " + film.getYear());
		System.out.println("Film rating: " + film.getRating());
		System.out.println("Language of film: " + film.getLangauge());
		System.out.println("Film description:\n" + film.getDescription());
		System.out.println("Actors:");
		List<Actor> actors = film.getActors();
		for (Actor actor : actors) {
			System.out.println("\t" + actor);
		}
		System.out.println();
	}

}
