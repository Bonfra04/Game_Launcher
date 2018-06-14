package toolBox;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonReader {

	@SuppressWarnings("unchecked")
	public static <T> T[] getArrayContent(JSONArray array) {

		List<T> content = new ArrayList<T>();
		Iterator<T> iterator = array.iterator();

		while (iterator.hasNext())
			content.add(iterator.next());

		return (T[]) content.toArray();
	}

	public static JSONArray readArrayFromObject(JSONObject object, String name) {
		return (JSONArray) object.get(name);
	}

	public static boolean readBooleanFromObject(JSONObject object, String name) {
		return (boolean) object.get(name);
	}

	public static double readDoubleFromObject(JSONObject object, String name) {
		return (double) object.get(name);
	}

	public static long readLongFromObject(JSONObject object, String name) {
		return (long) object.get(name);
	}

	public static String readStringFromObject(JSONObject object, String name) {
		return (String) object.get(name);
	}

	public static JSONObject readJson(String path) {

		JSONParser parser = new JSONParser();
		JSONObject jsonObject = null;
		try {
			Object obj = parser.parse(new FileReader(path));
			jsonObject = (JSONObject) obj;
		} catch (Exception e) {
			System.err.println("Error while reading json file (" + path + ")");
		}
		return jsonObject;
	}

}
