package application;

import data.GameData;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import toolBox.BatchHandler;
import toolBox.FileHandler;
import toolBox.MessageBox;

public class Main extends Application {

	public static final String TITLE = "Game Launcher";
	public static final Image ICON = new Image("icons/lampPost.png");

	private static Stage stage;

	private static ListView<String> versionList;

	public static void main(String[] args) {
		GameData.initializeData();
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			Scene scene = loadScene();
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle(TITLE);
			primaryStage.getIcons().add(ICON);
			primaryStage.setResizable(false);
			primaryStage.show();
			stage = primaryStage;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Scene loadScene() {
		BorderPane layout = new BorderPane();
		MenuBar menuBar = new MenuBar();
		HBox bottomLayout = new HBox();
		VBox rightLayout = new VBox();
		VBox centerLayout = new VBox();

		bottomLayout.setPadding(new Insets(25, 25, 25, 25));
		bottomLayout.setSpacing(20);

		centerLayout.setPadding(new Insets(15, 15, 15, 15));
		centerLayout.setSpacing(50);
		centerLayout.setAlignment(Pos.TOP_LEFT);

		rightLayout.setPadding(new Insets(15, 3, 0, 15));
		rightLayout.setAlignment(Pos.CENTER);

		Menu launchOption = new Menu("_Launching Options");

		MenuItem dummyItem = new MenuItem();
		launchOption.getItems().add(dummyItem);
		launchOption.setOnShown(e -> {
			launchOption.getItems().remove(0);
			launchOption.getItems().add(dummyItem);

			new LaunchingOptions();
			System.out.println("Launching Options opened");
		});

		Menu gameSettings = new Menu("_Game Settings");

		MenuItem dummyItem2 = new MenuItem();
		gameSettings.getItems().add(dummyItem2);
		gameSettings.setOnShown(e -> {
			gameSettings.getItems().remove(0);
			gameSettings.getItems().add(dummyItem2);

			new GameSetting();
			System.out.println("Game Settings opened");
		});

		menuBar.getMenus().addAll(launchOption, gameSettings);

		Button buttonPlay = new Button();
		buttonPlay.setText("Play");
		buttonPlay.setOnAction(e -> launchGame());

		Button updateButton = new Button();
		updateButton.setText("Check for update");
		updateButton.setOnAction(e -> checkForUpdate());

		bottomLayout.getChildren().addAll(buttonPlay, updateButton);

		versionList = new ListView<String>();
		versionList.getItems().addAll(FileHandler.readTextFile("data/versions.txt").split("\n"));
		versionList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		versionList.setMaxWidth(110);
		versionList.setPrefHeight(300);
		versionList.getSelectionModel().select(0);

		Label versionText = new Label("Versions");
		versionText.setScaleY(2.5);
		versionText.setScaleX(2.5);
		versionText.setPadding(new Insets(0, 2, 5, 0));

		rightLayout.getChildren().addAll(versionText, versionList);

		Label gameNameLabel = new Label("Pretty Game");
		gameNameLabel.setScaleY(4);
		gameNameLabel.setScaleX(4);
		gameNameLabel.setPadding(new Insets(5, 0, 0, 40));

		Image screenImage = new Image("textures/screen.png");
		ImageView screenView = new ImageView(screenImage);

		centerLayout.getChildren().addAll(gameNameLabel, screenView);

		layout.setCenter(centerLayout);
		layout.setTop(menuBar);
		layout.setBottom(bottomLayout);
		layout.setRight(rightLayout);
		Scene scene = new Scene(layout, 480, 480);
		return scene;
	}

	private static void checkForUpdate() {

		if (Updater.hasFoundUpdate()) {
			String wants = MessageBox.newMessageBox("Updates found", "Update found! Do you want to install it?", "yes",
					"no");

			if (wants.equals("yes"))
				Updater.installUpdates();

		} else
			MessageBox.newMessageBox("Nothing found", "No update found. Try later", "ok");
	}

	private static void launchGame() {
		ObservableList<String> versions = versionList.getSelectionModel().getSelectedItems();

		String selectedVersion = null;

		for (String version : versions)
			selectedVersion = version;

		String finalVersion = selectedVersion.replace(" ", "_");

		System.out.println("Launching " + finalVersion + ".jar with: " + GameData.fullScreen + " " + GameData.width
				+ " " + GameData.height + " " + GameData.folder);

		BatchHandler.executeCommand("java -jar " + finalVersion + ".jar " + GameData.fullScreen + " " + GameData.width
				+ " " + GameData.height + " " + GameData.folder);

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}

		stage.close();
	}
}
