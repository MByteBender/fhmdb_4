package at.ac.fhcampuswien.fhmdb.ui;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class UserDialog {
    Dialog<String> dialog;

    public UserDialog(String title, String msg){
        // Erstellen des Dialogs mit dem angegebenen Titel und der Nachricht
        dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setContentText(msg);

        // Erstellen eines OK-Buttons und Hinzufügen zum Dialog
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(type);
    }

    // Anzeigen des Dialogs und Warten auf Benutzerinteraktion
    public void show() {
        dialog.showAndWait();
    }
}
