package application;

import java.io.File;
import java.io.FileWriter;

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
import toolBox.MessageBox;

public class Updater {

	private static final String DOWNLOAD_UPDATE_URL = "https://raw.githubusercontent.com/Bonfra04/Cloud/master/versions/version.txt";

	private static Button finishButton;
	private static Button cancelButton;
	private static Label updatindText;

	public static boolean hasFoundUpdate() {
		if (FileDownloader.download(DOWNLOAD_UPDATE_URL, "data/update.txt")) {

			String gettedVersions = FileHandler.readTextFile("data/update.txt");

			String havedVersions = FileHandler.readTextFile("data/versions.txt");

			return !gettedVersions.equals(havedVersions);
		} else {

			MessageBox.newMessageBox("Error", "Error while downloading data. Try to use a different connection", "ok");

			return false;
		}
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

			updatindText.setText("Game updated. Restart the launcher to play it!");
		});

		update.reset();
		update.start();
	}

	private static Service<Integer> update = new Service<Integer>() {
		@Override
		protected Task<Integer> createTask() {
			return new Task<Integer>() {
				@SuppressWarnings("resource")
				@Override
				protected Integer call() throws Exception {
					int totalProcess = 100;
					int progress = 0;
					super.updateProgress(progress, totalProcess);

					System.out.println("Update progress: " + progress + "%");

					String[] gettedVersion = FileHandler.readTextFile("data/update.txt").split("\n");
					String[] havedVersion = FileHandler.readTextFile("data/versions.txt").split("\n");

					String[] differencies = new String[gettedVersion.length - havedVersion.length];
					int i = 0;
					for (String g : gettedVersion) {
						boolean found = false;
						for (String h : havedVersion)
							if (g.equals(h)) {
								found = true;
								break;
							}
						if (!found) {
							differencies[i] = g;
							i++;
						}
					}

					String getted = "";
					for (String s : differencies)
						getted += s + "\n";

					String haved = "";
					for (String s : havedVersion)
						haved += s + "\n";

					String missing = getted + haved;

					FileWriter writer = new FileWriter(new File("data/versions.txt"));
					writer.write(missing);
					writer.flush();

					progress += 10;
					super.updateProgress(progress, totalProcess);
					System.out.println("Update progress: " + progress + "%");

					int addOnProgress = (totalProcess - progress) / differencies.length;
					for (String s : differencies) {
						if (!FileDownloader.download("https://raw.githubusercontent.com/Bonfra04/Cloud/master/game/"
								+ s.replaceAll(" ", "_") + ".jar", "data/" + s.replaceAll(" ", "_") + ".jar")) {
							System.err.println("Potentially bugs on " + s.replaceAll(" ", "_") + ".jar");
						}

						progress += addOnProgress;
						super.updateProgress(progress, totalProcess);
						System.out.println("Update progress: " + progress + "%");
					}

					if (progress < totalProcess) {
						progress = totalProcess;
						super.updateProgress(progress, totalProcess);
						System.out.println("Update progress: " + progress + "%");
					}

					return progress;
				}
			};
		}
	};

}
