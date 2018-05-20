package toolBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileHandler {

	@SuppressWarnings("resource")
	public static String readTextFile(String path) {

		String file = "";

		try {
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);

			String line = br.readLine();
			while (line != null) {
				file += line;
				line = br.readLine();
				if (line != null)
					file += "\n";
			}
		} catch (IOException e) {
			System.err.println("Error while loading font settings (" + path + ")");
		}

		return file;

	}

}
