package toolBox;

import application.GameLauncher;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class OverWindow {

	public final String TITLE;

	private boolean running;

	private Stage primaryStage = new Stage();

	public OverWindow(String title) {
		this.TITLE = title;
		this.primaryStage.initModality(Modality.APPLICATION_MODAL);
		this.primaryStage.setTitle(TITLE);
		this.primaryStage.setOnCloseRequest(e -> {
			this.running = false;
			System.out.println(TITLE + " closed");
		});
	}

	public void launch() {
		if (!this.running)
			this.start(this.primaryStage);
		this.running = true;
	}

	public void stop() {
		this.running = false;
		this.primaryStage.close();
	}

	private void start(Stage primaryStage) {
		BorderPane layout = this.generateLayout();
		this.handleEvents();
		Scene scene = new Scene(layout, 300, 400);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(GameLauncher.ICON);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	protected abstract BorderPane generateLayout();

	protected abstract void handleEvents();
}
