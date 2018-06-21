package toolBox;

import javafx.stage.Stage;

public abstract class OverWindow {

	protected String[] args;

	private boolean running;

	private Stage primaryStage = new Stage();

	public void launch(String[] args) {
		this.args = args;
		if (!this.running)
			this.start(primaryStage);
		this.running = true;
	}

	public abstract void start(Stage primaryStage);

	public void stop() {
		this.running = false;
		primaryStage.close();
	}

}
