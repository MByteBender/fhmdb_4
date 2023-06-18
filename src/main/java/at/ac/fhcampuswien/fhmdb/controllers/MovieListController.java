package at.ac.fhcampuswien.fhmdb.controllers;

import at.ac.fhcampuswien.fhmdb.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.api.MovieApiException;
import at.ac.fhcampuswien.fhmdb.database.DataBaseException;
import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import at.ac.fhcampuswien.fhmdb.observer.ObservableMessages;
import at.ac.fhcampuswien.fhmdb.observer.Observer;
import at.ac.fhcampuswien.fhmdb.statePattern.UnsortedState;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import at.ac.fhcampuswien.fhmdb.ui.UserDialog;
import at.ac.fhcampuswien.fhmdb.statePattern.AscendingState;
import at.ac.fhcampuswien.fhmdb.statePattern.DescendingState;
import at.ac.fhcampuswien.fhmdb.statePattern.MovieSorter;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class MovieListController implements Initializable, Observer {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXComboBox releaseYearComboBox;

    @FXML
    public JFXComboBox ratingFromComboBox;

    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies;
    private MovieSorter movieSorter = new MovieSorter();

    protected ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    protected SortedState sortedState;

    //TODO List controller zur ansicht der movies Homeansicht

    // Event-Handler für den Klick auf "Add to Watchlist"
    private final ClickEventHandler onAddToWatchlistClicked = (clickedItem) -> {
        if (clickedItem instanceof Movie movie) {
            WatchlistMovieEntity watchlistMovieEntity = new WatchlistMovieEntity(
                    movie.getId(),
                    movie.getTitle(),
                    movie.getDescription(),
                    movie.getReleaseYear(),
                    movie.getGenres(),
                    movie.getImgUrl(),
                    movie.getLengthInMinutes(),
                    movie.getRating());
            try {
                WatchlistRepository repository = WatchlistRepository.getInstance();
                repository.addToWatchlist(watchlistMovieEntity);
            } catch (DataBaseException e) {
                UserDialog dialog = new UserDialog("Database Error", "Could not add movie to watchlist");
                dialog.show();
                e.printStackTrace();
            }
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeState();
        initializeLayout();
        try {
            WatchlistRepository.getInstance().addObserver(this);
        } catch (DataBaseException e) {
            e.printStackTrace();
        }
    }

    // Initialisiert den Anfangszustand der Filmliste
    public void initializeState() {
        List<Movie> result = new ArrayList<>();
        try {
            result = MovieAPI.getAllMovies();
        } catch (MovieApiException e) {
            UserDialog dialog = new UserDialog("MovieAPI Error", "Could not load movies from api");
            dialog.show();
        }

        setMovies(result);
        setMovieList(result);
        sortedState = SortedState.NONE;
    }

    // Initialisiert das Layout der Benutzeroberfläche
    public void initializeLayout() {
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(movieListView -> new MovieCell(onAddToWatchlistClicked));

        Object[] genres = Genre.values();
        genreComboBox.getItems().add("No filter");
        genreComboBox.getItems().addAll(genres);
        genreComboBox.setPromptText("Filter by Genre");

        releaseYearComboBox.getItems().add("No filter");
        Integer[] years = new Integer[124];
        for (int i = 0; i < years.length; i++) {
            years[i] = 1900 + i;
        }
        releaseYearComboBox.getItems().addAll(years);
        releaseYearComboBox.setPromptText("Filter by Release Year");

        ratingFromComboBox.getItems().add("No filter");
        Integer[] ratings = new Integer[11];
        for (int i = 0; i < ratings.length; i++) {
            ratings[i] = i;
        }
        ratingFromComboBox.getItems().addAll(ratings);
        ratingFromComboBox.setPromptText("Filter by Rating");
    }

    // Setzt die gesamte Filmliste
    public void setMovies(List<Movie> movies) {
        allMovies = movies;
    }

    // Setzt die angezeigte Filmliste
    public void setMovieList(List<Movie> movies) {
        observableMovies.clear();
        observableMovies.addAll(movies);
    }

    // Sortiert die Filme
    public void sortMovies() {
        if (movieSorter.getState() instanceof UnsortedState || movieSorter.getState() instanceof DescendingState) {
            movieSorter.setState(new AscendingState());
            movieSorter.sortMovies(observableMovies);
        } else if (movieSorter.getState() instanceof AscendingState) {
            movieSorter.setState(new DescendingState());
            movieSorter.sortMovies(observableMovies);
        }
    }

    // sort movies based on sortedState
    // by default sorted state is NONE
    // afterwards it switches between ascending and descending
//    public void sortMovies(SortedState sortDirection) {
//        if (sortDirection == SortedState.ASCENDING) {
//            observableMovies.sort(Comparator.comparing(Movie::getTitle));
//            sortedState = SortedState.ASCENDING;
//        } else {
//            observableMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
//            sortedState = SortedState.DESCENDING;
//        }
//    }

    // Filtert die Filme anhand einer Suchanfrage
    public List<Movie> filterByQuery(List<Movie> movies, String query) {
        if (query == null || query.isEmpty()) return movies;

        if (movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream().filter(movie ->
                        movie.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                                movie.getDescription().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }

    // Filtert die Filme anhand des Genres
    public List<Movie> filterByGenre(List<Movie> movies, Genre genre) {
        if (genre == null) return movies;

        if (movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream().filter(movie -> movie.getGenres().contains(genre)).toList();
    }

    // Anwendet alle Filter auf die Filme
    public void applyAllFilters(String searchQuery, Object genre) {
        List<Movie> filteredMovies = allMovies;

        if (!searchQuery.isEmpty()) {
            filteredMovies = filterByQuery(filteredMovies, searchQuery);
        }

        if (genre != null && !genre.toString().equals("No filter")) {
            filteredMovies = filterByGenre(filteredMovies, Genre.valueOf(genre.toString()));
        }

        observableMovies.clear();
        observableMovies.addAll(filteredMovies);
    }

    // Event-Handler für den Klick auf den Suchen-Button
    public void searchBtnClicked(ActionEvent actionEvent) {
        String searchQuery = searchField.getText().trim().toLowerCase();
        String releaseYear = validateComboboxValue(releaseYearComboBox.getSelectionModel().getSelectedItem());
        String ratingFrom = validateComboboxValue(ratingFromComboBox.getSelectionModel().getSelectedItem());
        String genreValue = validateComboboxValue(genreComboBox.getSelectionModel().getSelectedItem());

        Genre genre = null;
        if (genreValue != null) {
            genre = Genre.valueOf(genreValue);
        }

        List<Movie> movies = getMovies(searchQuery, genre, releaseYear, ratingFrom);

        setMovies(movies);
        setMovieList(movies);

        if (sortedState != SortedState.NONE) {
            sortMovies();
        }
    }

    // Validiert den Wert einer ComboBox und gibt ihn zurück
    public String validateComboboxValue(Object value) {
        if (value != null && !value.toString().equals("No filter")) {
            return value.toString();
        }
        return null;
    }

    // Ruft die Filmdaten aus der API ab
    public List<Movie> getMovies(String searchQuery, Genre genre, String releaseYear, String ratingFrom) {
        try {
            return MovieAPI.getAllMovies(searchQuery, genre, releaseYear, ratingFrom);
        } catch (MovieApiException e) {
            System.out.println(e.getMessage());
            UserDialog dialog = new UserDialog("MovieApi Error", "Could not load movies from api.");
            dialog.show();
            return new ArrayList<>();
        }
    }

    // Event-Handler für den Klick auf den Sortieren-Button
    public void sortBtnClicked(ActionEvent actionEvent) {
        sortMovies();
    }

//TODO update observer logic with alert
    // Aktualisiert die Filmliste basierend auf Änderungen in der Watchlist
    @Override
    public void update(ObservableMessages messages) {
        if (messages == ObservableMessages.ADDED) {
            new Alert(Alert.AlertType.INFORMATION, "Movie was successfully added to the Watchlist", ButtonType.OK).show();
        } else {
            new Alert(Alert.AlertType.WARNING, "Movie is already in Watchlist!", ButtonType.OK).show();
        }
    }
}
