package components;

import java.io.File;
import java.io.FilenameFilter;

public class PropertiesFileNameFilter implements FilenameFilter {

	private final String ENDING = ".properties";
	private final String I18N = "i18n";
	private final String I18N_COMMOMBUNDLE = "commonbundle";

	@Override
	public boolean accept(File file, String name) {

		String lowercaseName = name.toLowerCase();
		if (lowercaseName.startsWith(I18N) && lowercaseName.endsWith(ENDING)) {
			return true;
		} else if(lowercaseName.startsWith(I18N_COMMOMBUNDLE) && lowercaseName.endsWith(ENDING)) {
			return true;
		}
		else {
			return false;
		}
	}

}
