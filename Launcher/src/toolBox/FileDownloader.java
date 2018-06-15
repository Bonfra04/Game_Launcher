package toolBox;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.URL;

public class FileDownloader {

	public static boolean download(String link, String path) {

		System.out.println("Starting download from " + link + " to " + path);

		BufferedInputStream in = null;
		FileOutputStream fout = null;
		try {
			in = new BufferedInputStream(new URL(link).openStream());
			fout = new FileOutputStream(path);
			byte data[] = new byte[1024];
			int count;
			while ((count = in.read(data, 0, 1024)) != -1) {
				fout.write(data, 0, count);
			}
		} catch (Exception e) {
			System.err.println("Error while downloading from " + link);
			return false;
		} finally {
			try {
				if (in != null)
					in.close();
				if (fout != null)
					fout.close();
			} catch (Exception e) {
				System.err.println("Error while downloading from " + link);
				return false;
			}
		}

		System.out.println("File downloaded");

		return true;

	}

}
