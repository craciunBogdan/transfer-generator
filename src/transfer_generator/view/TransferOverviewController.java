package transfer_generator.view;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import transfer_generator.MainApp;
import transfer_generator.Player;

/**
 * Controller class for the GUI.
 * 
 * @author craciunBogdan
 *
 */
public class TransferOverviewController {

	private ObservableList<Player> targetList;
	private int index;
	private MainApp mainApp;
	private String tmLink;

	@FXML
	private ComboBox<String> leagueBox;

	@FXML
	private ComboBox<String> teamBox;

	@FXML
	private Button generateTransferTargetsButton;

	@FXML
	private Label introLabel;

	@FXML
	private Label fullNameLabel;

	@FXML
	private Label currentTeamLabel;

	@FXML
	private Label positionLabel;

	@FXML
	private Label ageLabel;

	@FXML
	private Label nationalityLabel;

	@FXML
	private Label valueLabel;

	@FXML
	private Hyperlink tmPageHyperlink;

	@FXML
	private Label playerFullNameLabel;

	@FXML
	private Label playerCurrentTeamLabel;

	@FXML
	private Label playerPositionLabel;

	@FXML
	private Label playerAgeLabel;

	@FXML
	private Label playerNationalityLabel;

	@FXML
	private Label playerValueLabel;

	@FXML
	private Button previousTargetButton;

	@FXML
	private Button nextTargetButton;

	/**
	 * The constructor.
	 */
	public TransferOverviewController() {

	}

	/**
	 * Initialises the controller class. This method is automatically called
	 * after the FXML file has been loaded.
	 */
	@FXML
	private void initialize() {
		leagueBox.setDisable(true);
		teamBox.setDisable(true);
		generateTransferTargetsButton.setDisable(true);
		setRightPaneVisibility(false);
	}

	/**
	 * Method that loads the leagues into the league box.
	 */
	public void setLeagueBoxItems() {
		ObservableList<String> leaguesList = FXCollections.observableArrayList();
		leaguesList = mainApp.getDatabaseLink().getLeagues();
		leagueBox.setItems(leaguesList);
		leagueBox.setDisable(false);
	}

	/**
	 * Method that is called when the user chooses a league from the league box.
	 */
	@FXML
	private void leagueBoxAction() {
		String league = leagueBox.getValue();
		if (league != null) {
			teamBox.setDisable(false);
			teamBox.setItems(mainApp.getDatabaseLink().getTeams(league));
		} else {
			teamBox.setDisable(true);
		}
	}

	/**
	 * Method that is called when the user chooses a team from the team box.
	 */
	@FXML
	private void teamBoxAction() {
		index = 0;
		if (teamBox.getValue() != null) {
			generateTransferTargetsButton.setDisable(false);
		} else {
			generateTransferTargetsButton.setDisable(true);
		}
	}

	/**
	 * Method that is called when the user clicks the "Generate transfer
	 * targets" button.
	 */
	@FXML
	private void generateTransferTargetsButtonAction() {
		try {
			this.targetList = mainApp.getDatabaseLink().sendTeam(teamBox.getValue());
			this.index = 0;
			if (targetList.size() == 1) {
				this.nextTargetButton.setDisable(true);
			} else {
				this.nextTargetButton.setDisable(false);
			}
			String intro = "The position that needs strengthening the most in this team is "
					+ mainApp.getDatabaseLink().getWorstPosition() + ".";
			this.introLabel.setText(intro);
			showPlayerDetails(targetList.get(index));
			this.previousTargetButton.setDisable(true);
			setRightPaneVisibility(true);
		} catch (SQLException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Unexpected error!");
			alert.setContentText("Unexpected error when generating transfer targets. (SQLException)");

			alert.showAndWait();
		} catch (IndexOutOfBoundsException e) {
			setRightPaneVisibility(false);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("That team is too good already...");
			alert.setHeaderText("The team you have chosen is already too good!");
			alert.setContentText(
					"That's quite the team you have there... Unfortunately, the algorithm hasn't found any players that are good enough to play for this team at their weakest position.");

			alert.showAndWait();
		}
	}

	/**
	 * Method that is called when the user clicks the "Next" button.
	 */
	@FXML
	private void nextButtonAction() {
		index++;
		this.previousTargetButton.setDisable(false);
		if (index == targetList.size() - 1) {
			this.nextTargetButton.setDisable(true);
		}
		showPlayerDetails(targetList.get(index));
	}

	/**
	 * Method that is called when the user clicks the "Previous" button.
	 */
	@FXML
	private void previousButtonAction() {
		index--;
		this.nextTargetButton.setDisable(false);
		if (index == 0) {
			this.previousTargetButton.setDisable(true);
		}
		showPlayerDetails(targetList.get(index));
	}

	@FXML
	private void tmPageHyperlinkAction() {
		if (Desktop.isDesktopSupported()) {
			new Thread(() -> {
				try {
					Desktop.getDesktop().browse(new URL(this.tmLink).toURI());
				} catch (IOException e){
					e.printStackTrace();
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Unexpected error!");
					alert.setContentText("Unexpected error when generating transfer targets. (IOExcpetion)");

					alert.showAndWait();
				} catch (URISyntaxException e) {
					e.printStackTrace();
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Unexpected error!");
					alert.setContentText("Unexpected error when generating transfer targets. (URISyntaxException)");

					alert.showAndWait();
				} 
			}).start();
		} else {
			System.out.println("well fuck");
		}
	}

	/**
	 * Method that converts the value integer into something more readable (i.e.
	 * 12000000 to £12.0m)
	 * 
	 * @param value
	 *            The value of a player.
	 * @return A more readable version of the value.
	 */
	private String valueConvertor(int value) {
		boolean million = false;
		if (value > 1000000) {
			million = true;
		}
		if (million) {
			return "£" + (((double) value) / 1000000.0) + "m";
		} else {
			return "£" + (((double) value) / 1000.0) + "k";
		}
	}

	/**
	 * Method that sets the visibility of the elements in the right pane.
	 * 
	 * @param visible
	 *            The visibility of the elements in the right pane.
	 */
	private void setRightPaneVisibility(boolean visible) {
		this.introLabel.setVisible(visible);
		this.fullNameLabel.setVisible(visible);
		this.currentTeamLabel.setVisible(visible);
		this.positionLabel.setVisible(visible);
		this.ageLabel.setVisible(visible);
		this.nationalityLabel.setVisible(visible);
		this.valueLabel.setVisible(visible);
		this.tmPageHyperlink.setVisible(visible);
		this.playerFullNameLabel.setVisible(visible);
		this.playerCurrentTeamLabel.setVisible(visible);
		this.playerPositionLabel.setVisible(visible);
		this.playerAgeLabel.setVisible(visible);
		this.playerNationalityLabel.setVisible(visible);
		this.playerValueLabel.setVisible(visible);
		this.previousTargetButton.setVisible(visible);
		this.nextTargetButton.setVisible(visible);
	}

	/**
	 * Method that shows the player details in the appropriate section.
	 * 
	 * @param player
	 *            The player whose details are to be displayed.
	 */
	private void showPlayerDetails(Player player) {
		this.playerFullNameLabel.setText(player.getName());
		this.playerCurrentTeamLabel.setText(mainApp.getDatabaseLink().getTeamName(player.getTeamId()));
		this.playerPositionLabel.setText(player.getPosition());
		this.playerAgeLabel.setText(Integer.toString(player.getAge()));
		this.playerNationalityLabel.setText(player.getNation());
		this.playerValueLabel.setText(valueConvertor(player.getValue()));
		this.tmLink = "http://www.transfermarkt.co.uk" + player.getHref();
	}

	/**
	 * Method that sets the MainApp. Should only be called from MainApp to get a
	 * reference to itself.
	 * 
	 * @param mainApp
	 *            The MainApp.
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
}
