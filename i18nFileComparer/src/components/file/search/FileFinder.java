package components.file.search;

import java.nio.file.Path;

public interface FileFinder {

	public void visit(Path path);

	public void visitRecursively(Path path);

}
