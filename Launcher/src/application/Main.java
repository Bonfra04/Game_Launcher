package application;

import java.io.IOException;

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
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import toolBox.FileHandler;
import toolBox.MessageBox;

public class Main extends Application {

	public static final String TITLE = "Game Launcher";
	public static final Image ICON = new Image("icons/rayquaza.png");

	private static Stage stage;

	private static ListView<String> versionList;

	public static void main(String[] args) {
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

		Menu file = new Menu("_File");
		MenuItem settings = new MenuItem("Settings...");
		settings.setOnAction(e -> {
			new SettingsTab();
			System.out.println("Settings opened");
		});
		file.getItems().add(settings);
		file.getItems().add(new SeparatorMenuItem());

		menuBar.getMenus().addAll(file);

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

		Image screen1Image = new Image("textures/screen1.png");
		ImageView screen1View = new ImageView(screen1Image);

		centerLayout.getChildren().addAll(gameNameLabel, screen1View);

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

		String[] versionArray = selectedVersion.split(" ");
		String finalVersion = "";
		for (int i = 0; i < versionArray.length; i++)
			finalVersion += i == versionArray.length - 1 ? "_" + versionArray[i] : versionArray[i];

		System.out.println("Launching " + finalVersion + " with: " + GameSettings.fullScreen + " " + GameSettings.width
				+ " " + GameSettings.height);

		try {
			Runtime.getRuntime().exec("java -jar " + finalVersion + ".jar " + GameSettings.fullScreen + " "
					+ GameSettings.width + " " + GameSettings.height);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}

		stage.close();
	}
}
