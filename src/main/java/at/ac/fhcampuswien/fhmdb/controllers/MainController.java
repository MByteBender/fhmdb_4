package at.ac.fhcampuswien.fhmdb.controllers;

import at.ac.fhcampuswien.fhmdb.ControllerFactory;
import at.ac.fhcampuswien.fhmdb.enums.UIComponent;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.observer.ObservableMessages;
import at.ac.fhcampuswien.fhmdb.observer.Observer;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MainController implements Observer {
    @FXML
    public JFXHamburger hamburgerMenu;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private BorderPane mainPane;

    private boolean isMenuCollapsed = true;
    private final ControllerFactory controllerFactory = ControllerFactory.getInstance();

    private HamburgerBasicCloseTransition transition;

    public void initialize() {
        // TODO CONTROLLER FACTORY
        FXMLLoader loader = new FXMLLoader(MainController.class.getResource(UIComponent.HOME.path));
        loader.setControllerFactory(controllerFactory);

        // Initialisierung der Hamburger-Animation
        transition = new HamburgerBasicCloseTransition(hamburgerMenu);
        transition.setRate(-1);
        drawer.toBack();

        // Event-Handler für den Klick auf den Hamburger-Button
        hamburgerMenu.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            toggleMenuDrawer();
        });

        // Start mit der Home-Ansicht
        navigateToMovielist();
    }

    // Ändert den Zustand der Hamburger-Animation
    private void toggleHamburgerTransitionState() {
        transition.setRate(transition.getRate() * -1);
        transition.play();
    }

    // Ändert den Zustand des Menü-Drawers und animiert ihn
    private void toggleMenuDrawer() {
        toggleHamburgerTransitionState();

        if (isMenuCollapsed) {
            // Menü-Drawer einblenden
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), drawer);
            translateTransition.setByX(130);
            translateTransition.play();
            isMenuCollapsed = false;
            drawer.toFront();
        } else {
            // Menü-Drawer ausblenden
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), drawer);
            translateTransition.setByX(-130);
            translateTransition.play();
            isMenuCollapsed = true;
            drawer.toBack();
        }
    }

    // Setzt den Inhalt des Hauptbereichs auf die angegebene FXML-Datei
    public void setContent(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(MainController.class.getResource(fxmlPath));
        try {
            mainPane.setCenter(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Schließt den Menü-Drawer, falls geöffnet
        if (!isMenuCollapsed) {
            toggleMenuDrawer();
        }
    }

    // Zählt, welcher Schauspieler in den meisten Filmen vorkommt
    public String getMostPopularActor(List<Movie> movies) {
        String actor = movies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");

        return actor;
    }

    // Ermittelt die Länge des längsten Filmtitels
    public int getLongestMovieTitle(List<Movie> movies) {
        return movies.stream()
                .mapToInt(movie -> movie.getTitle().length())
                .max()
                .orElse(0);
    }

    // Zählt die Anzahl der Filme eines bestimmten Regisseurs
    public long countMoviesFrom(List<Movie> movies, String director) {
        return movies.stream()
                .filter(movie -> movie.getDirectors().contains(director))
                .count();
    }

    // Filtert Filme nach Jahren
    public List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        return movies.stream()
                .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                .collect(Collectors.toList());
    }

    // Navigiert zur Watchlist-Ansicht
    @FXML
    public void navigateToWatchlist() {
        setContent(UIComponent.WATCHLIST.path);
    }

    // Navigiert zur MovieList-Ansicht
    @FXML
    public void navigateToMovielist() {
        setContent(UIComponent.MOVIELIST.path);
    }



    // Aktualisiert die Ansicht basierend auf Änderungen in der Watchlist
    @Override
    public void update(ObservableMessages messages) {
       /* if (messages == ObservableMessages.ADDED) {
            new Alert(Alert.AlertType.INFORMATION, "Movie was successfully added to the Watchlist", ButtonType.OK).show();
        } else {
            new Alert(Alert.AlertType.WARNING, "Movie is already in Watchlist!", ButtonType.OK).show();
        }*/
    }
}
