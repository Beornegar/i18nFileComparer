package components;

import java.nio.file.Path;
import java.util.List;

import utils.RelatedPropertiesFiles;

public interface FileSaving {

	public void saveFiles(Path savePath, List<RelatedPropertiesFiles> saver);
	
}
