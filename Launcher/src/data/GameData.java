package data;

import java.io.FileWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import toolBox.JsonReader;

public class GameData {

	public static boolean fullScreen;
	public static int width;
	public static int height;

	public static boolean useDefaultFolder;
	public static String defaultFolder;
	public static String folder;

	public static void initializeData() {

		JSONObject object = JsonReader.readJson("data/settings.json");

		JSONArray arrayL = JsonReader.readArrayFromObject(object, "launchingOptions");
		JSONObject launchingOptions = (JSONObject) JsonReader.getArrayContent(arrayL)[0];

		JSONArray arrayG = JsonReader.readArrayFromObject(object, "gameOptions");
		JSONObject gameOptions = (JSONObject) JsonReader.getArrayContent(arrayG)[0];

		GameData.fullScreen = JsonReader.readBooleanFromObject(launchingOptions, "fullScreen");
		GameData.width = (int) JsonReader.readLongFromObject(launchingOptions, "width");
		GameData.height = (int) JsonReader.readLongFromObject(launchingOptions, "height");

		GameData.useDefaultFolder = JsonReader.readBooleanFromObject(gameOptions, "useDefaultFolder");
		GameData.defaultFolder = JsonReader.readStringFromObject(gameOptions, "defaultFolder");
		GameData.folder = JsonReader.readStringFromObject(gameOptions, "folder");
	}

	@SuppressWarnings("unchecked")
	public static void applySettings() {
		JSONObject object = new JSONObject();

		JSONArray arrayL = new JSONArray();
		JSONObject launchingOptions = new JSONObject();

		JSONArray arrayG = new JSONArray();
		JSONObject gameOptions = new JSONObject();

		launchingOptions.put("fullScreen", fullScreen);
		launchingOptions.put("width", width);
		launchingOptions.put("height", height);

		arrayL.add(launchingOptions);

		object.put("launchingOptions", arrayL);

		gameOptions.put("useDefaultFolder", useDefaultFolder);
		gameOptions.put("defaultFolder", defaultFolder);
		gameOptions.put("folder", folder);

		arrayG.add(gameOptions);

		object.put("gameOptions", arrayG);

		try (FileWriter file = new FileWriter("data/settings.json")) {
			file.write(object.toString());
			file.flush();
		} catch (Exception e) {
			System.err.println("Error while saving settings to disk");
		}
	}

	public static void resetSettings() {
		fullScreen = false;
		width = 1080;
		height = 720;
	}
}
