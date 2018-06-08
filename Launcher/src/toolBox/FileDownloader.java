package toolBox;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.URL;

public class FileDownloader {

	public static void download(String link, String path) {

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
			System.err.print("Error while downloading from " + link);
		} finally {
			try {
				if (in != null)
					in.close();
				if (fout != null)
					fout.close();
			} catch (Exception e) {
				System.err.print("Error while downloading from " + link);
			}
		}

	}

}
