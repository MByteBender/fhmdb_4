package at.ac.fhcampuswien.fhmdb.statePattern;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.ObservableList;

import java.util.Comparator;

public class AscendingState implements MovieSorterState {

    public void sort(ObservableList<Movie> movieList) {
        movieList.sort(Comparator.comparing(Movie::getTitle));
    }
}
