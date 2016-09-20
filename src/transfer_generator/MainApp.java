package transfer_generator;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import transfer_generator.view.TransferOverviewController;

/**
 * The main class which launches the DatabaseLink and the GUI.
 * 
 * @author craciunBogdan
 *
 */
public class MainApp extends Application {

	private DatabaseLink db;
	private Stage primaryStage;
	private AnchorPane mainWindow;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Transfer Target Generator");
		this.primaryStage.getIcons().add(new Image("file:resources/icon/icon.png"));

		initTransferOverview();
	}

	/**
	 * Method that initialises the GUI.
	 */
	private void initTransferOverview() {
		try {
			db = new DatabaseLink();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/TransferOverview.fxml"));
			mainWindow = (AnchorPane) loader.load();

			TransferOverviewController controller = loader.getController();
			controller.setMainApp(this);
			controller.setLeagueBoxItems();

			Scene scene = new Scene(mainWindow);
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method that returns the DatabaseLink object used by this class.
	 * 
	 * @return The DatabaseLink used by this class.
	 */
	public DatabaseLink getDatabaseLink() {
		return db;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
