package components;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import utils.Pair;
import utils.RelatedPropertiesFiles;

public class PropertiesFileSaver implements FileSaving {

	@Override
	public void saveFiles(Path savePath, List<RelatedPropertiesFiles> allPropertiesFiles) {

		try {
			Files.createDirectories(savePath);
			
			for(RelatedPropertiesFiles filesOfOneFolder : allPropertiesFiles) {
				
				String folderName = "";
				
				System.out.println(filesOfOneFolder.getParentPath().toString());
				
				if(null != filesOfOneFolder.getParentPath()) {
					folderName += filesOfOneFolder.getParentPath().getFileName().toString();
					
				}		
				if(null != filesOfOneFolder.getParentPath().getParent()) {
					folderName += "_" + filesOfOneFolder.getParentPath().getParent().getFileName().toString();
				}
				if(null != filesOfOneFolder.getParentPath().getParent().getParent()) {
					folderName += "_" + filesOfOneFolder.getParentPath().getParent().getParent().getFileName().toString();
				}
				
				if(null != filesOfOneFolder.getParentPath().getParent().getParent().getParent()) {
					folderName += "_" + filesOfOneFolder.getParentPath().getParent().getParent().getParent().getFileName().toString();
				}
				
				if(null != filesOfOneFolder.getParentPath().getParent().getParent().getParent().getParent()) {
					folderName += "_" + filesOfOneFolder.getParentPath().getParent().getParent().getParent().getParent().getFileName().toString();
				}
				
				if(null != filesOfOneFolder.getParentPath().getParent().getParent().getParent().getParent().getParent()) {
					folderName += "_" + filesOfOneFolder.getParentPath().getParent().getParent().getParent().getParent().getParent().getFileName().toString();
				}
				
				String folder = savePath.toString() + File.separator + folderName;
				
				Files.createDirectories(Paths.get(folder));
				
				for(Pair<String,Properties> files : filesOfOneFolder.getFiles()) {
					
					//files.getY().store(new FileOutputStream(folder + File.separator + files.getX(), "UTF-8"), null);
					files.getY().store(new OutputStreamWriter(
						    new FileOutputStream(folder + File.separator + files.getX()), "UTF-8"), null);
				}
				
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	  
		
		
	}

}
