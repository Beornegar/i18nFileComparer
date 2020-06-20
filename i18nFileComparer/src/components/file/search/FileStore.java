package components.file.search;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FileStore implements FileFinder {

	private final Set<List<String>> store = new HashSet<>();

	// private final Map<Path, List<String>> storage = new HashMap<>();
	private final Map<File, List<File>> storage = new HashMap<>();

	private final FilenameFilter filter;

	public FileStore(FilenameFilter filter) {
		this.filter = filter;
	}

	@Override
	public void visit(Path path) {

		File f = path.toAbsolutePath().toFile();
		List<String> filePaths = Arrays.asList(f.list(filter));
		List<File> paths = new ArrayList<>();

		System.out.println("Visited Path [" + path + "]");

		// filePaths = filePaths.stream().map(s -> path.toAbsolutePath() +
		// File.separator + s).collect(Collectors.toList());

		filePaths.stream().forEach(s -> System.out.println(s));

		paths = filePaths.stream().map(s -> Paths.get(path.toString(), s).toFile()).collect(Collectors.toList());

		if (filePaths.size() > 0) {
			store.add(filePaths);
			storage.put(path.toFile(), paths);
		}
	}

	@Override
	public void visitRecursively(Path path) {
		try {

			Files.walk(path).filter(p -> p.toString().endsWith(".ext")).map(p -> p.getParent().getParent()).distinct()
					.forEach(System.out::println);

			File f = path.toAbsolutePath().toFile();
			List<String> filePaths = Arrays.asList(f.list(filter));
			if (filePaths.size() > 0) {
				store.add(filePaths);
			} else {
				Files.walk(path).filter(Files::isDirectory).forEach(this::visitRecursively);
			}

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

	public Map<File, List<File>> getStorage() {
		return storage;
	}

}
