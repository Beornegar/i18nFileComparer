package api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import components.FileStore;
import components.PropertiesComparer;
import components.PropertiesComparison;
import components.PropertiesFileNameFilter;
import components.PropertiesFileSaver;
import utils.PropertiesWithPath;

public class PropertiesFileHandler {

	public static final FileStore STORE = new FileStore(new PropertiesFileNameFilter());
	public static final PropertiesComparison COMPARER = new PropertiesComparer();
	public static final PropertiesFileSaver SAVER = new PropertiesFileSaver();

	public static final String DEFAULT_FILE_NAME = "i18n.properties";
	public static final String DEFAULT_SAVE_PATH = "C:/";

	public void handle(Path p, Path savePath) {

		if(savePath == null || p == null) {
			return;
		}

		findAllFiles(p, savePath);

		Map<File, List<PropertiesWithPath>> mappedProperties = loadFileContent(STORE.getStorage());
		
		List<PropertiesWithPath> propFilesToSave = new ArrayList<>();
		for(Entry<File, List<PropertiesWithPath>> entry : mappedProperties.entrySet()) {
			propFilesToSave.addAll(COMPARER.getMissingEntries(entry.getValue()));
		}

		// ------------- Saving of files -----------------------

		SAVER.saveFiles(savePath, propFilesToSave);
	}

	private void findAllFiles(Path p, Path savePath) {
		System.out.println("Input-Path: " + p);
		System.out.println("Output-Path: " + savePath);

		try {
			Files.walk(p).filter(Files::isDirectory).forEach(STORE::visit);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		STORE.getStorage().forEach((path, list) -> System.out
				.println("Path [" + path.toString() + "]" + ", Elemente [" + list.toString() + "]"));
	}

	private Map<File, List<PropertiesWithPath>> loadFileContent(Map<File,List<File>> propertyFiles) {

		Map<File,List<PropertiesWithPath>> erg = new HashMap<>();
				
		try {
			for (File directory : propertyFiles.keySet()) {
				List<PropertiesWithPath> properties = new ArrayList<>();
				for(File file : propertyFiles.get(directory)) {
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
