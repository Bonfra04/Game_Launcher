package application;

import java.awt.Desktop;
import java.io.File;

import data.GameData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import toolBox.BatchHandler;
import toolBox.FileHandler;
import toolBox.graphics.ClickableMenue;
import toolBox.graphics.MessageBox;
import toolBox.graphics.Window;

public class GameLauncher extends Application implements Window {

	public static final String TITLE = "Game Launcher";
	public static final Image ICON = new Image("icons/lampPost_top.png");

	public static final GameSettings GAME_SETTINGS = new GameSettings();
	public static final LaunchingOptions LAUNCHING_OPTIONS = new LaunchingOptions();
	public static final UpdateManager UPDATE_MANAGER = new UpdateManager();

	private ClickableMenue launchingOptions, gameSettings;
	private ListView<String> versionList;
	private Button play, update;

	@Override
	public void start(Stage primaryStage) {
		BorderPane layout = this.generateLayout();
		this.handleEvents();
		Scene scene = new Scene(layout, 480, 480);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle(TITLE);
		primaryStage.getIcons().add(ICON);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	@Override
	public BorderPane generateLayout() {
		this.launchingOptions = new ClickableMenue("_Launching Options");

		this.gameSettings = new ClickableMenue("_Game Settings");

		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(this.launchingOptions, this.gameSettings);

		Label gameNameLabel = new Label("Pretty Game");
		gameNameLabel.setScaleY(4);
		gameNameLabel.setScaleX(4);
		gameNameLabel.setPadding(new Insets(5, 0, 0, 40));

		Image screenImage = new Image("screenshots/screen.png");
		ImageView screenView = new ImageView(screenImage);

		VBox centerLayout = new VBox();
		centerLayout.setPadding(new Insets(15, 15, 15, 15));
		centerLayout.setSpacing(50);
		centerLayout.setAlignment(Pos.TOP_LEFT);
		centerLayout.getChildren().addAll(gameNameLabel, screenView);

		this.versionList = new ListView<String>();
		this.versionList.getItems().addAll(FileHandler.readTextFile("data/versions.txt").split("\n"));
		this.versionList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		this.versionList.setMaxWidth(110);
		this.versionList.setPrefHeight(300);
		this.versionList.getSelectionModel().select(0);

		Label versionText = new Label("Versions");
		versionText.setScaleY(2.5);
		versionText.setScaleX(2.5);
		versionText.setPadding(new Insets(0, 2, 5, 0));

		VBox rightLayout = new VBox();
		rightLayout.setPadding(new Insets(15, 3, 0, 15));
		rightLayout.setAlignment(Pos.CENTER);
		rightLayout.getChildren().addAll(versionText, this.versionList);

		this.play = new Button("Play");

		this.update = new Button("Check for update");

		HBox bottomLayout = new HBox();
		bottomLayout.setPadding(new Insets(25, 25, 25, 25));
		bottomLayout.setSpacing(20);
		bottomLayout.getChildren().addAll(this.play, this.update);

		BorderPane layout = new BorderPane();
		layout.setCenter(centerLayout);
		layout.setTop(menuBar);
		layout.setBottom(bottomLayout);
		layout.setRight(rightLayout);

		return layout;
	}

	@Override
	public void handleEvents() {
		this.launchingOptions.setOnClick(e -> {
			LAUNCHING_OPTIONS.launch();
			System.out.println("Launching Options opened");
		});

		this.gameSettings.setOnClick(e -> {
			GAME_SETTINGS.launch();
			System.out.println("Game Settings opened");
		});

		this.play.setOnAction(e -> launchGame());

		this.update.setOnAction(e -> checkForUpdate());
	}

	private void checkForUpdate() {

		if (UPDATE_MANAGER.hasFoundUpdate()) {
			String wants = MessageBox.newMessageBox("Updates found", "Update found! Do you want to install it?", "yes",
					"no");

			if (wants.equals("yes"))
				UPDATE_MANAGER.installUpdates();

		} else
			MessageBox.newMessageBox("Nothing found", "No update found. Try later", "ok");
	}

	private void launchGame() {
		ObservableList<String> versions = versionList.getSelectionModel().getSelectedItems();

		String selectedVersion = null;

		for (String version : versions)
			selectedVersion = version;

		String finalVersion = selectedVersion.replace(" ", "_");

		System.out.println("Launching " + finalVersion + ".jar with: " + GameData.fullScreen + " " + GameData.width
				+ " " + GameData.height + " " + GameData.folder);

		BatchHandler.executeCommand("java -jar data/" + finalVersion + ".jar " + GameData.fullScreen + " "
				+ GameData.width + " " + GameData.height + " " + GameData.folder);

		try {
			Thread.sleep(100);
		} catch (Exception e) {
			System.err.println("Error while closing launcher");
		}

		Platform.exit();
	}

	public static void main(String[] args) {
		GameData.initializeData();
		GameLauncher.launch(args);
	}
}
