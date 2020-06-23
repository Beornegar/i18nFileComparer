package components;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import components.file.comparing.PropertiesComparer;
import components.file.comparing.PropertiesComparison;
import components.file.comparing.PropertiesFileNameFilter;
import components.file.save.PropertiesFileSaver;
import components.file.search.PropertiesFileStore;
import utils.PropertiesWithPath;

public class PropertiesFileHandler implements FileHandler {

	public static final PropertiesFileStore STORE = new PropertiesFileStore(new PropertiesFileNameFilter());
	public static final PropertiesComparison COMPARER = new PropertiesComparer();
	public static final PropertiesFileSaver SAVER = new PropertiesFileSaver();

	public void handle(Path inputPath, Path outputPath) {

		if (outputPath == null || inputPath == null) {
			return;
		}

		STORE.visit(inputPath);
		Map<File, List<PropertiesWithPath>> mappedProperties = STORE.loadFileContent();

		List<PropertiesWithPath> propFilesToSave = new ArrayList<>();
		for (Entry<File, List<PropertiesWithPath>> entry : mappedProperties.entrySet()) {
			propFilesToSave.addAll(COMPARER.getMissingEntries(entry.getValue()));
		}

		SAVER.saveFiles(outputPath, propFilesToSave);
	}

}
