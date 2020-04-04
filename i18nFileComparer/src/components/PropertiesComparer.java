package components;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import utils.Pair;
import utils.RelatedPropertiesFiles;

public class PropertiesComparer implements PropertiesComparison {

	public List<Pair<String, Properties>> getDifferencePropertiesFiles(Pair<Path, Properties> defaultFile,
			Pair<Path, Properties> otherPair) {

		List<Pair<String, Properties>> erg = new ArrayList<Pair<String, Properties>>();

		Properties keysInDefaultFileButNotInOtherFile = new Properties();
		Properties keysInOtherFileButNotInDefaultFile = new Properties();

		String defaultFileName = defaultFile.getX().getFileName().toString();
		String otherFileName = otherPair.getX().getFileName().toString();

		Pair<String, Properties> newFile1 = new Pair<>(
				"In_" + defaultFileName + "_But_Not_In_" + otherFileName + ".properties",
				keysInDefaultFileButNotInOtherFile);
		Pair<String, Properties> newFile2 = new Pair<>(
				"In_" + otherFileName + "_But_Not_In_" + defaultFileName + ".properties",
				keysInOtherFileButNotInDefaultFile);

		Set<Object> firstSet = defaultFile.getY().keySet();
		Set<Object> secondSet = otherPair.getY().keySet();

		Set<Object> one = new HashSet<Object>(firstSet);
		Set<Object> two = new HashSet<Object>(secondSet);
		one.removeAll(secondSet);
		two.removeAll(firstSet);

		one.forEach(o -> newFile1.getY().setProperty((String) o, ""));
		two.forEach(o -> newFile2.getY().setProperty((String) o, ""));

		if (!newFile1.getY().isEmpty()) {
			erg.add(newFile1);
		}

		if (!newFile2.getY().isEmpty()) {
			erg.add(newFile2);
		}

		return erg;
	}

	public RelatedPropertiesFiles getDifferenceBetweenDefaultFileAndOthers(Pair<Path, Properties> defaultFile,
			List<Pair<Path, Properties>> others, boolean withOriginFiles) {

		RelatedPropertiesFiles saver = new RelatedPropertiesFiles();
		saver.setParentPath(defaultFile.getX().getParent());

		if (withOriginFiles) {
			saver.addPair(defaultFile);
			saver.addAllPairs(others);
		}

		for (Pair<Path, Properties> otherPair : others) {
			saver.getFiles().addAll(getDifferencePropertiesFiles(defaultFile, otherPair));
		}

		return saver;
	}

	@Override
	public List<Pair<String, Properties>> getMissingEntries(RelatedPropertiesFiles propertyFiles) {

		Set<Object> allKeys = getAllKeysOfAllPropertyFiles(propertyFiles);

		List<Pair<String, Properties>> erg = new ArrayList<>();

		for (Pair<String, Properties> files : propertyFiles.getFiles()) {

			Properties missingKeysPropertiesFile = getMissingKeysForPropertiesFile(allKeys, files.getY());

			if (missingKeysPropertiesFile != null) {
				Pair<String, Properties> missingKeysFile = new Pair<>("Missing_Keys_in" + files.getX(),
						missingKeysPropertiesFile);
				erg.add(missingKeysFile);
			}

		}

		return erg;
	}

	private Set<Object> getAllKeysOfAllPropertyFiles(RelatedPropertiesFiles propertyFiles) {

		Set<Object> erg = new HashSet<>();

		propertyFiles.getFiles().forEach(pair -> erg.addAll(pair.getY().keySet()));

		return erg;
	}

	private Properties getMissingKeysForPropertiesFile(Set<Object> keys, Properties fileToCheck) {

		Properties missingKeysPropertiesFile = new Properties();

		Set<Object> firstSet = keys;
		Set<Object> secondSet = fileToCheck.keySet();

		Set<Object> one = new HashSet<Object>(firstSet);

		one.removeAll(secondSet);
		one.forEach(o -> missingKeysPropertiesFile.setProperty((String) o, ""));

		if (!missingKeysPropertiesFile.isEmpty()) {
			return missingKeysPropertiesFile;
		}

		return null;
	}

}
