package application;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import toolBox.FileDownloader;
import toolBox.FileHandler;

public class Updater {

	private static final String DOWNLOAD_UPDATE_URL = "https://raw.githubusercontent.com/Bonfra04/Game_Launcher/master/versions/versions.txt";

	private static Button finishButton;
	private static Button cancelButton;
	private static Label updatindText;

	public static boolean hasFoundUpdate() {
		FileDownloader.download(DOWNLOAD_UPDATE_URL, "data/update.txt");

		String gettedVersions = FileHandler.readTextFile("data/update.txt");

		String havedVersions = FileHandler.readTextFile("data/versions.txt");

		return !gettedVersions.equals(havedVersions);
	}

	public static void installUpdates() {

		Stage stage = new Stage();
		stage.setTitle("Installing updates...");
		BorderPane layout = new BorderPane();

		ProgressBar bar = new ProgressBar(0);
		bar.progressProperty().bind(update.progressProperty());

		HBox center = new HBox();
		center.setAlignment(Pos.CENTER);

		center.getChildren().add(bar);

		HBox bottom = new HBox();
		bottom.setAlignment(Pos.BOTTOM_RIGHT);
		bottom.setPadding(new Insets(15, 15, 15, 15));
		bottom.setSpacing(20);

		cancelButton = new Button("Cancel");
		cancelButton.setOnAction(e -> {
			update.cancel();
			stage.close();
		});

		finishButton = new Button("Finish");
		finishButton.setOnAction(e -> stage.close());
		finishButton.setDisable(true);

		bottom.getChildren().addAll(finishButton, cancelButton);

		HBox top = new HBox();
		top.setAlignment(Pos.TOP_CENTER);
		top.setPadding(new Insets(15, 15, 15, 15));

		updatindText = new Label("Updating game files...");

		top.getChildren().addAll(updatindText);

		layout.setTop(top);
		layout.setCenter(center);
		layout.setBottom(bottom);

		Scene scene = new Scene(layout, 400, 400);

		scene.getStylesheets().add(Updater.class.getResource("application.css").toExternalForm());

		stage.setScene(scene);

		stage.setResizable(false);

		stage.show();

		update.setOnSucceeded(e -> {
			cancelButton.setDisable(true);
			finishButton.setDisable(false);

			updatindText.setText("Game updated");
		});

		update.reset();
		update.start();
	}

	private static Service<Integer> update = new Service<Integer>() {
		@Override
		protected Task<Integer> createTask() {
			return new Task<Integer>() {
				@Override
				protected Integer call() throws Exception {
					int totalProcess = 100;
					int progress = 0;
					super.updateProgress(progress, totalProcess);

					// func1();
					// progress += 1;
					// super.updateProgress(progress, totalProcess);

					for (progress = 0; progress < totalProcess; progress++) {
						super.updateProgress(progress, totalProcess);
						Thread.sleep(10);
						System.out.println("Update progress: " + progress);
					}
					System.out.println("Update progress: " + progress);

					return progress;
				}
			};
		}
	};

}
