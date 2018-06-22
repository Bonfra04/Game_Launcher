package application;

import data.GameData;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import toolBox.graphics.OverWindow;

public class GameSettings extends OverWindow {
	
	public static final String TITLE = "Game Settings";

	private CheckBox useDefaultFolder;
	private TextField path;
	private Button apply, close, applyAndClose;

	private boolean saved = true;

	public GameSettings() {
		super(TITLE);
	}
	
	@Override
	public BorderPane generateLayout() {
		this.useDefaultFolder = new CheckBox("Use default folder");
		this.useDefaultFolder.setSelected(GameData.useDefaultFolder);

		this.path = new TextField(GameData.folder);
		if (this.useDefaultFolder.isSelected())
			this.path.setDisable(true);

		this.useDefaultFolder.setPadding(new Insets(0, 0, 15, 0));

		VBox vBoxLayout = new VBox();
		vBoxLayout.setPadding(new Insets(10, 10, 10, 10));

		vBoxLayout.getChildren().addAll(this.useDefaultFolder, this.path);

		this.apply = new Button("Apply");
		this.close = new Button("Close");
		this.applyAndClose = new Button("Apply and close");

		HBox hBoxLayout = new HBox();
		hBoxLayout.setPadding(new Insets(10, 10, 10, 10));
		hBoxLayout.setSpacing(20);
		hBoxLayout.getChildren().addAll(this.apply, this.close, this.applyAndClose);

		BorderPane layout = new BorderPane();
		layout.setTop(vBoxLayout);
		layout.setBottom(hBoxLayout);

		return layout;
	}

	@Override
	public void handleEvents() {
		this.useDefaultFolder.setOnAction(e -> {
			if (this.useDefaultFolder.isSelected()) {
				this.path.setText(GameData.defaultFolder);
				this.path.setDisable(true);
			} else
				this.path.setDisable(false);

			this.saved = false;
		});

		this.path.setOnKeyPressed(e -> {
			this.saved = false;
		});

		this.apply.setOnAction(e -> {
			GameData.useDefaultFolder = this.useDefaultFolder.isSelected();
			GameData.folder = this.path.getText();
			GameData.applySettings();

			if (!this.saved)
				System.out.println("Launching Options applyed");
			this.saved = true;
		});

		this.close.setOnAction(e -> {
			if (!saved)
				System.out.println("Change of Game Settings canceled");
			System.out.println("Game Settings closed");
			super.stop();
		});

		applyAndClose.setOnAction(e -> {
			GameData.useDefaultFolder = this.useDefaultFolder.isSelected();
			GameData.folder = path.getText();
			GameData.applySettings();

			if (!this.saved)
				System.out.println("Launching Options applyed");
			this.saved = true;
			super.stop();
		});
	}

}
