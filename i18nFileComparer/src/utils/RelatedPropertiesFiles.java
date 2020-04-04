package utils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RelatedPropertiesFiles {

	private Path parentPath;
	
	private List<Pair<String, Properties>> files = new ArrayList<Pair<String,Properties>>();

	public Path getParentPath() {
		return parentPath;
	}

	public void setParentPath(Path parentPath) {
		this.parentPath = parentPath;
	}

	public List<Pair<String, Properties>> getFiles() {
		return files;
	}


	public void addPair(Pair<Path,Properties> pair) {
		files.add(new Pair<String,Properties>(pair.getX().getFileName().toString(), pair.getY()));
	}

	public void addAllPairs(List<Pair<Path, Properties>> others) {	
		others.forEach(this::addPair);
	}
}
