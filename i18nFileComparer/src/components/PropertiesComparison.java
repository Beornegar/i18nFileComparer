package components;

import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import utils.Pair;
import utils.RelatedPropertiesFiles;

public interface PropertiesComparison {

	public RelatedPropertiesFiles getDifferenceBetweenDefaultFileAndOthers(Pair<Path, Properties> defaultFile,
			List<Pair<Path, Properties>> others, boolean withOriginFiles);

	public List<Pair<String, Properties>> getMissingEntries(RelatedPropertiesFiles propertyFiles);
	
}
