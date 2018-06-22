package application;

import java.io.File;
import java.io.FileWriter;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import toolBox.FileDownloader;
import toolBox.FileHandler;
import toolBox.graphics.MessageBox;
import toolBox.graphics.OverWindow;

public class UpdateManager extends OverWindow {

	public static final String TITLE = "Installing updates...";

	private static final String DOWNLOAD_UPDATE_URL = "https://raw.githubusercontent.com/Bonfra04/Cloud/master/versions/version.txt";

	private Label updatindText;
	private ProgressBar progressBar;
	private Button finishButton, cancelButton;

	public UpdateManager() {
		super(TITLE);
	}

	@Override
	public BorderPane generateLayout() {

		this.updatindText = new Label("Updating game files...");

		HBox top = new HBox();
		top.setAlignment(Pos.TOP_CENTER);
		top.setPadding(new Insets(15, 15, 15, 15));
		top.getChildren().addAll(this.updatindText);

		this.progressBar = new ProgressBar(0);

		HBox center = new HBox();
		center.setAlignment(Pos.CENTER);
		center.getChildren().add(this.progressBar);

		this.cancelButton = new Button("Cancel");

		this.finishButton = new Button("Finish");
		this.finishButton.setDisable(true);

		HBox bottom = new HBox();
		bottom.setAlignment(Pos.BOTTOM_RIGHT);
		bottom.setPadding(new Insets(15, 15, 15, 15));
		bottom.setSpacing(20);
		bottom.getChildren().addAll(this.finishButton, this.cancelButton);

		BorderPane layout = new BorderPane();

		layout.setTop(top);
		layout.setCenter(center);
		layout.setBottom(bottom);

		return layout;
	}

	@Override
	public void handleEvents() {

		this.progressBar.progressProperty().bind(this.update.progressProperty());

		this.cancelButton.setOnAction(e -> {
			update.cancel();
			System.out.println("Update cancelled");
			System.out.println("Update Manager closed");
			super.stop();
		});

		finishButton.setOnAction(e -> {
			System.out.println("Update Manager closed");
			super.stop();
		});

		update.setOnSucceeded(e -> {
			this.cancelButton.setDisable(true);
			this.finishButton.setDisable(false);

			this.updatindText.setText("Game updated. Restart the launcher to play it!");
		});
	}

	public boolean hasFoundUpdate() {
		if (FileDownloader.download(DOWNLOAD_UPDATE_URL, "data/update.txt"))
			return !FileHandler.readTextFile("data/update.txt").equals(FileHandler.readTextFile("data/versions.txt"));

		MessageBox.newMessageBox("Error", "Error while downloading data. Try to use a different connection", "ok");
		return false;
	}

	public void installUpdates() {
		super.launch();
		update.start();
	}

	private Service<Integer> update = new Service<Integer>() {
		@Override
		protected Task<Integer> createTask() {
			return new Task<Integer>() {

				private final int TOTAL_PROCESS = 100;

				@SuppressWarnings("resource")
				@Override
				protected Integer call() throws Exception {
					int progress = 0;
					super.updateProgress(progress, TOTAL_PROCESS);

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
					super.updateProgress(progress, TOTAL_PROCESS);
					System.out.println("Update progress: " + progress + "%");

					int addOnProgress = (TOTAL_PROCESS - progress) / differencies.length;
					for (String s : differencies) {
						while (true)
							if (!FileDownloader.download(
									"https://raw.githubusercontent.com/Bonfra04/Cloud/master/game/"
											+ s.replaceAll(" ", "_") + ".jar",
									"data/" + s.replaceAll(" ", "_") + ".jar")) {
								System.err.println("Potentially bugs on " + s.replaceAll(" ", "_") + ".jar");
								if (MessageBox.newMessageBox("Download Error", "Error while downloading " + s,
										"Continue", "Retry").equals("Retry"))
									continue;
								break;
							}

						progress += addOnProgress;
						super.updateProgress(progress, TOTAL_PROCESS);
						System.out.println("Update progress: " + progress + "%");
					}

					if (progress < TOTAL_PROCESS) {
						progress = TOTAL_PROCESS;
						super.updateProgress(progress, TOTAL_PROCESS);
						System.out.println("Update progress: " + progress + "%");
					}

					return progress;
				}
			};
		}
	};

}
