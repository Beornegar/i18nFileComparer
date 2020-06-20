package components.file.comparing;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import utils.PropertiesWithPath;

public class PropertiesComparer implements PropertiesComparison {

	@Override
	public List<PropertiesWithPath> getMissingEntries(List<PropertiesWithPath> propertyFiles) {

		List<PropertiesWithPath> erg = new ArrayList<>();
		Set<Object> allKeys = getAllKeysOfAllPropertyFiles(propertyFiles);
		for (PropertiesWithPath file : propertyFiles) {

			Optional<PropertiesWithPath> missingKeysPropertiesFile = getMissingKeysForPropertiesFile(allKeys, file);

			if (missingKeysPropertiesFile.isPresent()) {
				erg.add(missingKeysPropertiesFile.get());
			}
		}

		propertyFiles.stream().forEach(f -> erg.add(f));

		return erg;
	}

	private Set<Object> getAllKeysOfAllPropertyFiles(List<PropertiesWithPath> propertyFiles) {

		Set<Object> erg = new HashSet<>();
		propertyFiles.forEach(file -> erg.addAll(file.getProperties().keySet()));
		return erg;
	}

	private Optional<PropertiesWithPath> getMissingKeysForPropertiesFile(Set<Object> keys,
			PropertiesWithPath fileToCheck) {

		Properties missingKeysPropertiesFile = new Properties();

		Set<Object> firstSet = keys;
		Set<Object> secondSet = fileToCheck.getProperties().keySet();

		Set<Object> one = new HashSet<Object>(firstSet);

		one.removeAll(secondSet);
		one.forEach(o -> missingKeysPropertiesFile.setProperty((String) o, ""));

		if (!missingKeysPropertiesFile.isEmpty()) {
			return Optional.of(new PropertiesWithPath(
					Paths.get(fileToCheck.getSavePath().getParentFile().toString(),
							"Missing_Keys_in_" + fileToCheck.getSavePath().getName()).toFile(),
					missingKeysPropertiesFile));
		}

		return Optional.empty();
	}

}
