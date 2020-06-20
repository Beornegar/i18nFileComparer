package application;

import java.io.File;
import java.nio.file.Path;

import api.PropertiesFileHandler;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class Main extends Application {
	
	private Path inputPath;
	private Path outputPath;
	private PropertiesFileHandler handler = new PropertiesFileHandler();
	
	
	@Override
	public void start(Stage primaryStage) {
		try {

			primaryStage.setTitle("JavaFX App");

			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, 200, 200);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			DirectoryChooser directoryChooser = new DirectoryChooser();
			
			String userDirectoryString = System.getProperty("user.home");
			File userDirectory = new File(userDirectoryString);
			if(!userDirectory.canRead()) {
			    userDirectory = new File("c:/");
			}
			directoryChooser.setInitialDirectory(userDirectory);
			
			Label label = new Label("Finished");
			label.setVisible(false);
			
			Button button1 = new Button("Select Input-Directory");
			button1.setPrefSize(150, 10);
			

			Button button2 = new Button("Select Output-Directory");
			button2.setPrefSize(150, 10);
			

			Button button3 = new Button("Start comparing!");
			button3.setPrefSize(150, 10);
			button3.setDisable(true);
			
			button1.setOnAction(e -> {
				File selectedDirectory = directoryChooser.showDialog(primaryStage);

				inputPath = selectedDirectory.toPath();
				
				if(outputPath != null && inputPath != null) {
					button3.setDisable(false);
				} else {
					button3.setDisable(true);
				}
				
				label.setVisible(false);
				System.out.println(selectedDirectory.getAbsolutePath());
			});
			
			button2.setOnAction(e -> {
				File selectedDirectory = directoryChooser.showDialog(primaryStage);

				outputPath = selectedDirectory.toPath();
				
				if(outputPath != null && inputPath != null) {
					button3.setDisable(false);
				} else {
					button3.setDisable(true);
				}
				
				label.setVisible(false);
				System.out.println(selectedDirectory.getAbsolutePath());
			});
			
			
			
			button3.setOnAction(e -> {
				handler.handle(inputPath, outputPath);
				label.setVisible(true);
			});

			VBox vBox = new VBox(button1, button2, button3, label);
			vBox.setPadding(new Insets(10,10,10,10));
			vBox.setSpacing(10);
			vBox.setAlignment(Pos.CENTER);
			root.setCenter(vBox);
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
