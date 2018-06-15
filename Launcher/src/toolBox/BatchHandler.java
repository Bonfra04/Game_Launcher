package toolBox;

import java.io.IOException;

public class BatchHandler {
	
	public static final void executeCommand(String command) {
		try {
			Runtime.getRuntime().exec("cmd /c" + command);
		} catch (IOException e) {
			System.err.println("Error while running a windows command (" + command + ")");
		}	
	}

}
