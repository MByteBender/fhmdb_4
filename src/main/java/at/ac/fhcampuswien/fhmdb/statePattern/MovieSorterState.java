package at.ac.fhcampuswien.fhmdb.statePattern;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.ObservableList;


// solid s singel responsibility
public interface MovieSorterState {
    void sort(ObservableList<Movie> movieList);
}
