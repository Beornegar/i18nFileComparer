package components.file.save;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import utils.PropertiesWithPath;

public class PropertiesFileSaver implements FileSaving {

	@Override
	public void saveFiles(Path savePath, List<PropertiesWithPath> propFilesToSave) {

		try {
			Files.createDirectories(savePath);

			for (PropertiesWithPath file : propFilesToSave) {
				
				String folder = savePath.toString() + File.separator;
				
				Path filePath = file.getSavePath().toPath();
				for(int i = 2; i < filePath.getNameCount() - 1; i++) {
					folder += filePath.getName(i) + "_";
				}
				
				Files.createDirectories(Paths.get(folder));
				
				file.getProperties().store(new OutputStreamWriter(
						    new FileOutputStream(folder + File.separator + file.getSavePath().getName()), "UTF-8"), null);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
