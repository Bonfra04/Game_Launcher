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

public class Updater {

	private static Button finishButton;
	private static Button cancelButton;
	private static Label text;

	public static boolean hasFoundUpdate() {
		return true;
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

		text = new Label("Updating game files...");

		top.getChildren().addAll(text);

		layout.setTop(top);
		layout.setCenter(center);
		layout.setBottom(bottom);

		Scene scene = new Scene(layout, 400, 400);

		scene.getStylesheets().add(Updater.class.getResource("application.css").toExternalForm());

		stage.setScene(scene);

		stage.setResizable(false);

		stage.show();

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

					if (progress == totalProcess) {
						cancelButton.setDisable(true);
						finishButton.setDisable(false);
					}
					return progress;
				}
			};
		}
	};

}
