package com.skilldistillery.filmquery.database;

import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
	//THIS IS THE ONLY PLACE THAT INTERACTS WITH THE DATABASE!!!
  @Override
  public Film findFilmById(int filmId) {
    return null;
  }

}
