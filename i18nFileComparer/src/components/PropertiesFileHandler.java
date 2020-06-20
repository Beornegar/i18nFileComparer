package components;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import components.file.comparing.PropertiesComparer;
import components.file.comparing.PropertiesComparison;
import components.file.comparing.PropertiesFileNameFilter;
import components.file.save.PropertiesFileSaver;
import components.file.search.FileStore;
import utils.PropertiesWithPath;

public class PropertiesFileHandler implements FileHandler {

	public static final FileStore STORE = new FileStore(new PropertiesFileNameFilter());
	public static final PropertiesComparison COMPARER = new PropertiesComparer();
	public static final PropertiesFileSaver SAVER = new PropertiesFileSaver();

	public void handle(Path inputPath, Path outputPath) {

		System.out.println("Input-Path: " + inputPath);
		System.out.println("Output-Path: " + outputPath);

		if (outputPath == null || inputPath == null) {
			return;
		}

		findAllFiles(inputPath);

		Map<File, List<PropertiesWithPath>> mappedProperties = loadFileContent(STORE.getStorage());

		List<PropertiesWithPath> propFilesToSave = new ArrayList<>();
		for (Entry<File, List<PropertiesWithPath>> entry : mappedProperties.entrySet()) {
			propFilesToSave.addAll(COMPARER.getMissingEntries(entry.getValue()));
		}

		// ------------- Saving of files -----------------------

		SAVER.saveFiles(outputPath, propFilesToSave);
	}

	private void findAllFiles(Path inputPath) {

		try {
			Files.walk(inputPath).filter(Files::isDirectory).forEach(STORE::visit);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		STORE.getStorage().forEach((path, list) -> System.out
				.println("Path [" + path.toString() + "]" + ", Elemente [" + list.toString() + "]"));
	}

	private Map<File, List<PropertiesWithPath>> loadFileContent(Map<File, List<File>> propertyFiles) {

		Map<File, List<PropertiesWithPath>> erg = new HashMap<>();

		try {
			for (File directory : propertyFiles.keySet()) {
				List<PropertiesWithPath> properties = new ArrayList<>();
				for (File file : propertyFiles.get(directory)) {
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

}
