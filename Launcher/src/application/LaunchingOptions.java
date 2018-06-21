package application;

import data.GameData;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import toolBox.MessageBox;
import toolBox.OverWindow;

public class LaunchingOptions extends OverWindow {

	public static final String TITLE = "Launching Options";

	private CheckBox fullScreen;
	private TextField widthField, heightField;
	private Button apply, close, applyAndClose;

	private boolean saved = true;

	public LaunchingOptions() {
		super(TITLE);
	}

	@Override
	protected BorderPane generateLayout() {
		this.fullScreen = new CheckBox("FullScreen");
		this.fullScreen.setSelected(GameData.fullScreen);

		this.widthField = new TextField("" + GameData.width);
		this.widthField.setMaxWidth(50);
		if (GameData.fullScreen)
			this.widthField.setDisable(true);
		Label widthLabel = new Label("Width");

		this.heightField = new TextField("" + GameData.height);
		this.heightField.setMaxWidth(50);
		if (GameData.fullScreen)
			this.heightField.setDisable(true);
		Label heightLabel = new Label("Height");

		GridPane gridLayout = new GridPane();
		gridLayout.setPadding(new Insets(10, 10, 10, 10));
		gridLayout.setHgap(-20);
		gridLayout.setVgap(10);
		GridPane.setConstraints(this.fullScreen, 0, 0);
		GridPane.setConstraints(this.widthField, 0, 1);
		GridPane.setConstraints(widthLabel, 1, 1);
		GridPane.setConstraints(this.heightField, 0, 2);
		GridPane.setConstraints(heightLabel, 1, 2);
		gridLayout.getChildren().addAll(this.fullScreen, this.widthField, widthLabel, this.heightField, heightLabel);

		this.apply = new Button("Apply");
		this.close = new Button("Close");
		this.applyAndClose = new Button("Apply and close");

		HBox hBoxLayout = new HBox();
		hBoxLayout.setPadding(new Insets(10, 10, 10, 10));
		hBoxLayout.setSpacing(20);
		hBoxLayout.getChildren().addAll(this.apply, this.close, this.applyAndClose);

		BorderPane layout = new BorderPane();
		layout.setCenter(gridLayout);
		layout.setBottom(hBoxLayout);

		return layout;
	}

	@Override
	protected void handleEvents() {
		this.widthField.setOnKeyPressed(e -> {
			this.saved = false;
		});

		this.heightField.setOnKeyPressed(e -> {
			this.saved = false;
		});

		this.fullScreen.setOnAction(e -> {
			if (fullScreen.isSelected()) {
				widthField.setDisable(true);
				heightField.setDisable(true);
			} else {
				widthField.setDisable(false);
				heightField.setDisable(false);
			}
			this.saved = false;
		});

		this.apply.setOnAction(e -> {
			try {
				GameData.width = Integer.parseInt(this.widthField.getText());
			} catch (Exception ex) {
				System.err.println("\"" + this.widthField.getText() + "\" is not a number");
				MessageBox.newMessageBox("Error", "\"" + this.widthField.getText() + "\" is not a number", "ok");
				return;
			}

			try {
				GameData.height = Integer.parseInt(this.heightField.getText());
			} catch (Exception ex) {
				System.err.println("\"" + this.heightField.getText() + "\" is not a number");
				MessageBox.newMessageBox("Error", "\"" + this.heightField.getText() + "\" is not a number", "ok");
				return;
			}

			GameData.fullScreen = this.fullScreen.isSelected();

			if (!this.saved) {
				GameData.applySettings();
				System.out.println("Launching Options applyed");
			}

			this.saved = true;
		});

		this.close.setOnAction(e -> {
			if (!this.saved)
				System.out.println("Change of Launching Options canceled");
			System.out.println("Launching Options closed");
			super.stop();
		});

		this.applyAndClose.setOnAction(e -> {
			try {
				GameData.width = Integer.parseInt(this.widthField.getText());
			} catch (Exception ex) {
				System.err.println("\"" + this.widthField.getText() + "\" is not a number");
				MessageBox.newMessageBox("Error", "\"" + this.widthField.getText() + "\" is not a number", "ok");
				return;
			}

			try {
				GameData.height = Integer.parseInt(this.heightField.getText());
			} catch (Exception ex) {
				System.err.println("\"" + this.heightField.getText() + "\" is not a number");
				MessageBox.newMessageBox("Error", "\"" + this.heightField.getText() + "\" is not a number", "ok");
				return;
			}

			if (!this.saved) {
				GameData.applySettings();
				System.out.println("Launching Options applyed");
			}

			GameData.fullScreen = this.fullScreen.isSelected();

			this.saved = true;
			System.out.println("Launching Options closed");
			super.stop();
		});
	}
}
