package toolBox.graphics;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class ClickableMenue extends Menu {

	public ClickableMenue() {
		MenuItem dummyItem = new MenuItem();
		super.getItems().add(dummyItem);

		super.setOnShowing(e -> {
			super.getItems().remove(0);
			super.getItems().add(dummyItem);
		});
	}

	public ClickableMenue(String title) {
		super(title);
		MenuItem dummyItem = new MenuItem();
		super.getItems().add(dummyItem);

		super.setOnShowing(e -> {
			super.getItems().remove(0);
			super.getItems().add(dummyItem);
		});
	}

	public void setOnClick(EventHandler<Event> onClick) {
		super.setOnShown(onClick);
	}

}
