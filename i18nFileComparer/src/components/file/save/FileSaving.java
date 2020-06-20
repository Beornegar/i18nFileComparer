package components.file.save;

import java.nio.file.Path;
import java.util.List;

import utils.PropertiesWithPath;

public interface FileSaving {

	public void saveFiles(Path savePath, List<PropertiesWithPath> propFilesToSave);
	
}
