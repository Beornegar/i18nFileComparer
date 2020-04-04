package api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import components.FileStore;
import components.PropertiesComparer;
import components.PropertiesComparison;
import components.PropertiesFileNameFilter;
import components.PropertiesFileSaver;
import utils.Pair;
import utils.RelatedPropertiesFiles;

public class PropertiesFileHandler {

	public static final FileStore STORE = new FileStore(new PropertiesFileNameFilter());
	public static final PropertiesComparison COMPARER = new PropertiesComparer();
	public static final PropertiesFileSaver SAVER = new PropertiesFileSaver();

	public static final String DEFAULT_FILE_NAME = "i18n.properties";
	public static final String DEFAULT_SAVE_PATH = "C:/";

	public void handle(Path p, Path savePath, String defaultFileName) {

		// ------------- Finding of files ---------------

		System.out.println("Input-Path: " + p);
		System.out.println("Output-Path: " + savePath);

		STORE.visit(p);
		STORE.getStore().forEach(f -> System.out.println(f));

		// ------------- Processing of files ------------

		// load all property-files of same directory and save them together
		List<RelatedPropertiesFiles> allPropertiesFiles = new ArrayList<RelatedPropertiesFiles>();
		for (List<String> files : STORE.getStore()) {

			if(files.size() < 1) {
				System.out.println("No files found in directory!");
				return;
			}
			
			try {
				
				RelatedPropertiesFiles propFile = new RelatedPropertiesFiles();
				propFile.setParentPath(Paths.get(files.get(0)).getParent());
				
				for(String filePath : files) {
					Properties prop = new Properties();
					prop.load(new FileInputStream(filePath));
					propFile.getFiles().add(new Pair<>(Paths.get(filePath).getFileName().toString(), prop));
				}

				allPropertiesFiles.add(propFile);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Loop over those directories and work on each of them
		for(RelatedPropertiesFiles files : allPropertiesFiles) {
			
			List<Pair<String, Properties>> propFilesToSave =  COMPARER.getMissingEntries(files);
			files.getFiles().addAll(propFilesToSave);
		}
		
		
		// ------------- Saving of files -----------------------

		if (savePath == null) {
			try {
				savePath = Paths.get(PropertiesFileHandler.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}

		SAVER.saveFiles(savePath, allPropertiesFiles);

	}

}
