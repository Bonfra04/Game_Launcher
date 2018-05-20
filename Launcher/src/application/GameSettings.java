package application;

public class GameSettings {

	public static boolean fullScreen = false;
	public static int width = 1080;
	public static int height = 720;
	
	private static boolean preFullScreen = fullScreen;
	private static int preWidth = width;
	private static int preHeight = height;
	
	public static void setFullScreen(boolean fullscreen) {
		preFullScreen = fullscreen;
	}
	
	public static void setWidth(int width) {
		preWidth = width;
	}
	
	public static void setHeight(int height) {
		preHeight = height;
	}
	
	public static void applySettings() {
		fullScreen = preFullScreen;
		width = preWidth;
		height = preHeight;
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
