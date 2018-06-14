package application;

import data.GameData;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import toolBox.MessageBox;

public class LaunchingOptions {

	private boolean saved = true;

	private Stage stage;
	private BorderPane layout;
	private Scene scene;

	public LaunchingOptions() {
		stage = new Stage();
		layout = new BorderPane();

		generateLayout();

		scene = new Scene(layout, 300, 400);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Launching Options");
		stage.getIcons().add(Main.ICON);
		stage.setResizable(false);
		stage.show();

		stage.setOnCloseRequest(e -> {
			if (!saved)
				System.out.println("Launching Options closed without saving");
			else
				System.out.println("Launching Options closed");
		});
	}

	private void generateLayout() {
		GridPane gridLayout = new GridPane();
		gridLayout.setPadding(new Insets(10, 10, 10, 10));
		gridLayout.setHgap(-20);
		gridLayout.setVgap(10);
		HBox hBoxLayout = new HBox();
		hBoxLayout.setPadding(new Insets(10, 10, 10, 10));
		hBoxLayout.setSpacing(20);

		Label widthLabel = new Label("Width");
		TextField widthField = new TextField("" + GameData.width);
		widthField.setMaxWidth(50);
		if (GameData.fullScreen)
			widthField.setDisable(true);
		widthField.setOnKeyPressed(e -> {
			saved = false;
		});

		Label heightLabel = new Label("Height");
		TextField heightField = new TextField("" + GameData.height);
		heightField.setMaxWidth(50);
		if (GameData.fullScreen)
			heightField.setDisable(true);
		heightField.setOnKeyPressed(e -> {
			saved = false;
		});

		CheckBox fullScreen = new CheckBox("FullScreen");
		fullScreen.setSelected(GameData.fullScreen);
		fullScreen.setOnAction(e -> {
			if (fullScreen.isSelected()) {
				widthField.setDisable(true);
				heightField.setDisable(true);
				GameData.setFullScreen(true);
			} else {
				widthField.setDisable(false);
				heightField.setDisable(false);
				GameData.setFullScreen(false);
			}
			saved = false;
		});

		Button apply = new Button("Apply");
		apply.setOnAction(e -> {
			ErrorType error = ErrorType.NULL;
			try {
				GameData.setWidth(Integer.parseInt(widthField.getText()));
			} catch (Exception ex) {
				System.err.println("\"" + widthField.getText() + "\" is not a number");
				error = ErrorType.WIDTH;
				MessageBox.newMessageBox("Error", "\"" + widthField.getText() + "\" is not a number", "ok");
			}

			try {
				GameData.setHeight(Integer.parseInt(heightField.getText()));
			} catch (Exception ex) {
				System.err.println("\"" + heightField.getText() + "\" is not a number");
				error = ErrorType.HEIGHT;
				MessageBox.newMessageBox("Error", "\"" + heightField.getText() + "\" is not a number", "ok");
			}

			if (error == ErrorType.NULL) {
				GameData.applySettings();
				if (!saved)
					System.out.println("Launching Options applyed");
				saved = true;
			}
		});

		Button applyAndClose = new Button("Apply and close");
		applyAndClose.setOnAction(e -> {
			ErrorType error = ErrorType.NULL;
			try {
				GameData.setWidth(Integer.parseInt(widthField.getText()));
			} catch (Exception ex) {
				System.err.println("\"" + widthField.getText() + "\" is not a number");
				error = ErrorType.WIDTH;
				MessageBox.newMessageBox("Error", "\"" + widthField.getText() + "\" is not a number", "ok");
			}

			try {
				GameData.setHeight(Integer.parseInt(heightField.getText()));
			} catch (Exception ex) {
				System.err.println("\"" + heightField.getText() + "\" is not a number");
				error = ErrorType.HEIGHT;
				MessageBox.newMessageBox("Error", "\"" + heightField.getText() + "\" is not a number", "ok");
			}

			if (error == ErrorType.NULL) {
				GameData.applySettings();
				if (!saved)
					System.out.println("Launching Options applyed");
				saved = true;
				System.out.println("Launching Options closed");
				stage.close();
			}
		});

		Button close = new Button("Close");
		close.setOnAction(e -> {
			if (!saved)
				System.out.println("Change of Launching Options canceled");
			System.out.println("Launching Options closed");
			stage.close();
		});

		GridPane.setConstraints(fullScreen, 0, 0);
		GridPane.setConstraints(widthField, 0, 1);
		GridPane.setConstraints(widthLabel, 1, 1);
		GridPane.setConstraints(heightField, 0, 2);
		GridPane.setConstraints(heightLabel, 1, 2);
		gridLayout.getChildren().addAll(fullScreen, widthField, widthLabel, heightField, heightLabel);

		hBoxLayout.getChildren().addAll(apply, close, applyAndClose);

		layout.setCenter(gridLayout);
		layout.setBottom(hBoxLayout);
	}

	private enum ErrorType {
		NULL, WIDTH, HEIGHT;
	}

}
