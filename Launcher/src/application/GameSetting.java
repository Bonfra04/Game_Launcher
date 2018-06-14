package application;

import data.GameData;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameSetting {

	private boolean saved = true;

	private Stage stage;
	private BorderPane layout;
	private Scene scene;

	public GameSetting() {
		stage = new Stage();
		layout = new BorderPane();

		generateLayout();

		scene = new Scene(layout, 300, 400);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Game Settings");
		stage.getIcons().add(Main.ICON);
		stage.setResizable(false);
		stage.show();

		stage.setOnCloseRequest(e -> {
			if (!saved)
				System.out.println("Game Settings closed without saving");
			else
				System.out.println("Game Settings closed");
		});
	}

	private void generateLayout() {

		CheckBox checkBox = new CheckBox("Use default folder");
		checkBox.setSelected(GameData.useDefaultFolder);
				
		TextField path = new TextField(GameData.folder);
		if (checkBox.isSelected())
			path.setDisable(true);

		checkBox.setOnAction(e -> {
			if (checkBox.isSelected()) {
				path.setText(GameData.defaultFolder);
				path.setDisable(true);
			} else
				path.setDisable(false);

			this.saved = false;
		});

		checkBox.setPadding(new Insets(0, 0, 15, 0));

		path.setOnKeyPressed(e -> {
			this.saved = false;
		});

		VBox vBoxLayout = new VBox();
		vBoxLayout.setPadding(new Insets(10, 10, 10, 10));

		vBoxLayout.getChildren().addAll(checkBox, path);

		Button apply = new Button("Apply");
		Button close = new Button("Close");
		Button applyAndClose = new Button("Apply and close");

		apply.setOnAction(e -> {
			GameData.setUseDefaultFolder(checkBox.isSelected());
			GameData.setFolder(path.getText());

			GameData.applySettings();

			if (!saved)
				System.out.println("Launching Options applyed");
			saved = true;
		});

		close.setOnAction(e -> {
			if (!saved)
				System.out.println("Change of Game Settings canceled");
			System.out.println("Game Settings closed");
			stage.close();
		});

		applyAndClose.setOnAction(e -> {
			GameData.setUseDefaultFolder(checkBox.isSelected());
			GameData.setFolder(path.getText());

			GameData.applySettings();

			if (!saved)
				System.out.println("Launching Options applyed");
			saved = true;
			stage.close();
		});

		HBox hBoxLayout = new HBox();
		hBoxLayout.setPadding(new Insets(10, 10, 10, 10));
		hBoxLayout.setSpacing(20);

		hBoxLayout.getChildren().addAll(apply, close, applyAndClose);
		
		layout.setTop(vBoxLayout);
		layout.setBottom(hBoxLayout);;
	}

}
