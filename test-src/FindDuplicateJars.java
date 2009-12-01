import java.io.*;
import java.util.*;
/**
 * This utility class locates the duplicate jars between kettle and the embedded platform
 * and generates ant includes for the build.xml.
 * This should be run at the root of a kettle distribution with the agile bi plugin installed.
 **/
public class FindDuplicateJars {
	public static void main(String args[]) throws Exception {
		List<File> jars = new ArrayList<File>();
		findJars(jars, new File("."));
		Map<String, List<File>> map = new HashMap<String, List<File>>();
		for (File f : jars) {
			String fn = f.getName();
			if (fn.equals("swt.jar") || fn.startsWith("launcher")) {
				continue;
			}
			fn = fn.replaceAll("-TRUNK.*|-[0-9].*|[.]jar|-jdk.*", "");
			List<File> list = map.get(fn);
			if (list != null) {
				System.out.println("FOUND A MATCH: " + fn);
				System.out.println("   " + f);
				for (File mf : list) {
				  System.out.println("   " + mf);
				}
			} else {
				list = new ArrayList<File>();
				map.put(fn, list);
			}
			list.add(f);
		}
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys);
		File tmpdir = new File("plugins/spoon/agile-bi/tmp");
		boolean move = false;
		for (String key : keys) {
			List<File> files = map.get(key);
			if (files.size() == 1) {
				// skip, not a dupe
				continue;
			}
			if (files.size() > 2) {
				System.out.println("More than 2, skipping " + key);
				continue;
			}
			File libext = files.get(0);
			File plugin = files.get(1);
			if (libext.toString().indexOf("libext") < 0) {
				// swap
				plugin = files.get(0);
				libext = files.get(1);
			}
			if (libext.toString().indexOf("libext") < 0) {
				System.out.println("No libext version of " + key + " found");
				continue;
			}
			if (plugin.toString().indexOf("agile-bi/lib") < 0) {
				System.out.println("No agile-bi/lib version of " + key + " found");
				continue;
			}
			System.out.println("			<include name=\"" + key + "-*.jar\"/>");
			if (move) {
				plugin.renameTo(new File(tmpdir, plugin.getName()));
			}		
		}
	}

	public static void findJars(List<File> jars, File currDir) {
		File files[] = currDir.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				findJars(jars, f);
			} else {
				if (f.getName().endsWith(".jar")) {
					jars.add(f);
				}
			}
		}
	}
}
