package at.ac.fhcampuswien.fhmdb.statePattern;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.ObservableList;

public class MovieSorter {

    private MovieSorterState state;

    // Konstruktor
    public MovieSorter(){
        this.state = new UnsortedState(); // Standardzustand ist UnsortedState
    }

    // Setzt den aktuellen Zustand des MovieSorter
    public void setState(MovieSorterState state){
        this.state = state;
    }

    // Gibt den aktuellen Zustand des MovieSorter zurück
    public MovieSorterState getState() {
        return state;
    }

    // Sortiert die Filme basierend auf dem aktuellen Zustand
    public void sortMovies(ObservableList<Movie> movieList){
        state.sort(movieList);
    }
}
