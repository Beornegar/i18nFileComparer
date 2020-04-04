package components;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FileStore implements FileFinder {

	private final Set<List<String>> store = new HashSet<>();
	private final FilenameFilter filter;

	public FileStore(FilenameFilter filter) {
		this.filter = filter;
	}

	@Override
	public void visit(Path path) {

		File f = path.toAbsolutePath().toFile();
		List<String> filePaths = Arrays.asList(f.list(filter));

		System.out.println("FilePaths");
		
		filePaths = filePaths.stream().map(s -> path.toAbsolutePath() + File.separator + s).collect(Collectors.toList());

		if (filePaths.size() > 0) {
			store.add(filePaths);
		}
	}

	@Override
	public void visitRecursively(Path path) {
		try {
			Files.walk(path).filter(Files::isDirectory).forEach(this::visit);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void print() {
		store.forEach(System.out::println);
	}

	public Set<List<String>> getStore() {
		return store;
	}

	public FilenameFilter getFilter() {
		return filter;
	}

}
