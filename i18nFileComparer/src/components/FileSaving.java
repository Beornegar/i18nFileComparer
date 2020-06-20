package components;

import java.nio.file.Path;
import java.util.List;

import utils.PropertiesWithPath;
import utils.RelatedPropertiesFiles;

public interface FileSaving {

	public void saveFiles(Path savePath, List<PropertiesWithPath> propFilesToSave);
	
}
