package toolBox.graphics;

import application.GameLauncher;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MessageBox {

	private static String result;

	public static String newMessageBox(String title, String message, String... options) {

		Stage stage = new Stage();

		BorderPane grid = new BorderPane();
		HBox layout = new HBox();
		stage.setTitle(title);

		for (String s : options) {
			Button button = new Button(s);
			button.setOnAction(e -> {
				result = s;
				stage.close();
			});
			layout.getChildren().add(button);
		}

		StackPane top = new StackPane();
		top.getChildren().add(new Label(message));

		top.setPadding(new Insets(15, 15, 5, 15));

		grid.setTop(top);

		layout.setPadding(new Insets(5, 15, 15, 15));

		layout.setAlignment(Pos.CENTER);
		layout.setSpacing(15);

		grid.setCenter(layout);

		Scene scene = new Scene(grid);
		scene.getStylesheets().add(GameLauncher.class.getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setResizable(false);

		stage.showAndWait();

		return result;
	}

}
