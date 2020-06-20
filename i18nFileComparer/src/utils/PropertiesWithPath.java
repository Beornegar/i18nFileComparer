package utils;

import java.io.File;
import java.util.Properties;

public class PropertiesWithPath {

	private File savePath;
	private Properties properties;
	
	public PropertiesWithPath() {}
	
	public PropertiesWithPath(File savePath, Properties properties) {
		this.savePath = savePath;
		this.properties = properties;
	}
	
	
	public File getSavePath() {
		return savePath;
	}
	public void setSavePath(File savePath) {
		this.savePath = savePath;
	}
	public Properties getProperties() {
		return properties;
	}
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
}
