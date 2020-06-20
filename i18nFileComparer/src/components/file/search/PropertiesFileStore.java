package components.file.search;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import utils.PropertiesWithPath;

public class PropertiesFileStore implements FileFinder {

	private final Map<File, List<File>> storage = new HashMap<>();

	private final FilenameFilter filter;

	public PropertiesFileStore(FilenameFilter filter) {
		this.filter = filter;
	}

	@Override
	public void visit(Path inputPath) {

		try {
			Files.walk(inputPath).filter(Files::isDirectory).forEach(this::getAllFiles);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		print();
	}
	
	private void getAllFiles(Path path) {

		File f = path.toAbsolutePath().toFile();
		List<String> filePaths = Arrays.asList(f.list(filter));
		List<File> paths = new ArrayList<>();

		filePaths.stream().forEach(s -> System.out.println(s));

		paths = filePaths.stream().map(s -> Paths.get(path.toString(), s).toFile()).collect(Collectors.toList());

		if (filePaths.size() > 0) {
			storage.put(path.toFile(), paths);
		}
	}

	public Map<File, List<PropertiesWithPath>> loadFileContent() {

		Map<File, List<PropertiesWithPath>> erg = new HashMap<>();

		try {
			for (File directory : storage.keySet()) {
				List<PropertiesWithPath> properties = new ArrayList<>();
				for (File file : storage.get(directory)) {
					InputStream inputStream = new FileInputStream(file);
					Reader reader = new InputStreamReader(inputStream, "UTF-8");
					Properties prop = new Properties();
					prop.load(reader);
					properties.add(new PropertiesWithPath(file, prop));
				}
				erg.put(directory, properties);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return erg;
	}
	
	public void print() {
		storage.forEach((path, list) -> System.out
				.println("Path [" + path.toString() + "]" + ", Elemente [" + list.toString() + "]"));
	}

	public FilenameFilter getFilter() {
		return filter;
	}

	public Map<File, List<File>> getStorage() {
		return storage;
	}

}
