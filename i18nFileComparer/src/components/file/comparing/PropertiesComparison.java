package components.file.comparing;

import java.util.List;

import utils.PropertiesWithPath;

public interface PropertiesComparison {

	public List<PropertiesWithPath> getMissingEntries(List<PropertiesWithPath> list);
	
}
