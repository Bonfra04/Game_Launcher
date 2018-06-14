package data;

import java.io.FileWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import toolBox.JsonReader;

public class GameData {

	public static boolean fullScreen;
	public static int width;
	public static int height;

	private static boolean preFullScreen;
	private static int preWidth;
	private static int preHeight;

	public static boolean useDefaultFolder;
	public static String defaultFolder;
	public static String folder;

	private static boolean preUseDefaultFolder;
	private static String preDefaultFolder;
	private static String preFolder;

	public static void initializeData() {

		JSONObject object = JsonReader.readJson("data/settings.json");

		JSONArray arrayL = JsonReader.readArrayFromObject(object, "launchingOptions");
		JSONObject launchingOptions = (JSONObject) JsonReader.getArrayContent(arrayL)[0];

		JSONArray arrayG = JsonReader.readArrayFromObject(object, "gameOptions");
		JSONObject gameOptions = (JSONObject) JsonReader.getArrayContent(arrayG)[0];

		GameData.fullScreen = JsonReader.readBooleanFromObject(launchingOptions, "fullScreen");
		GameData.width = (int) JsonReader.readLongFromObject(launchingOptions, "width");
		GameData.height = (int) JsonReader.readLongFromObject(launchingOptions, "height");

		GameData.preFullScreen = GameData.fullScreen;
		GameData.preWidth = GameData.width;
		GameData.preHeight = GameData.height;

		GameData.useDefaultFolder = JsonReader.readBooleanFromObject(gameOptions, "useDefaultFolder");
		GameData.defaultFolder = JsonReader.readStringFromObject(gameOptions, "defaultFolder");
		GameData.folder = JsonReader.readStringFromObject(gameOptions, "folder");

		GameData.preUseDefaultFolder = GameData.useDefaultFolder;
		GameData.preDefaultFolder = GameData.defaultFolder;
		GameData.preFolder = GameData.folder;

	}

	public static void setUseDefaultFolder(boolean useDefaultFolder) {
		preUseDefaultFolder = useDefaultFolder;
	}

	public static void setDefaultFolder(String defaultFolder) {
		preDefaultFolder = defaultFolder;
	}

	public static void setFolder(String folder) {
		preFolder = folder;
	}

	public static void setFullScreen(boolean fullscreen) {
		preFullScreen = fullscreen;
	}

	public static void setWidth(int width) {
		preWidth = width;
	}

	public static void setHeight(int height) {
		preHeight = height;
	}

	@SuppressWarnings("unchecked")
	public static void applySettings() {
		fullScreen = preFullScreen;
		width = preWidth;
		height = preHeight;

		useDefaultFolder = preUseDefaultFolder;
		defaultFolder = preDefaultFolder;
		folder = preFolder;

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

	public static void restoreSettings() {
		preFullScreen = fullScreen;
		preWidth = width;
		preHeight = height;
	}

	public static void resetSettings() {
		fullScreen = false;
		width = 1080;
		height = 720;
	}
}
