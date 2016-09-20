package transfer_generator;

import java.util.ArrayList;
import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class that analyses a list of players to determine which one is better.
 * 
 * @author craciunBogdan
 *
 */
public class TargetAnalyser {

	private String leagueNation;
	private ArrayList<Player> potentialTargetList;

	/**
	 * The constructor.
	 * 
	 * @param potentialTargetList
	 *            The list of potential transfer targets.
	 * @param leagueNation
	 *            The nation of the team that has been analysed.
	 */
	public TargetAnalyser(ArrayList<Player> potentialTargetList, String leagueNation) {
		this.leagueNation = leagueNation;
		this.potentialTargetList = potentialTargetList;
	}

	/**
	 * Method that analyses the given target list and generates an
	 * ObservableList containing the 3 best players.
	 * 
	 * @return An ObservableList containing the 3 best players.
	 */
	public ObservableList<Player> analyseTargetList() {
		TreeMap<Double, Player> targetRatings = new TreeMap<Double, Player>();
		while (!potentialTargetList.isEmpty()) {
			Player currentPlayer = potentialTargetList.remove(0);
			double currentPlayerRating = targetFormula(currentPlayer);
			targetRatings.put(currentPlayerRating, currentPlayer);
		}
		ObservableList<Player> targetList = FXCollections.observableArrayList();
		for (int i = 0; i < 3; i++) {
			if (targetRatings.isEmpty()) {
				break;
			}
			targetList.add(targetRatings.get(targetRatings.lastKey()));
			targetRatings.remove(targetRatings.lastKey());
		}
		return targetList;
	}

	/**
	 * Method that calculates the rating of a player.
	 * 
	 * @param player
	 *            The player whose rating is required.
	 * @return The rating of the given player.
	 */
	private double targetFormula(Player player) {
		double targetRating = 0;
		if (player.getNation().equals(leagueNation)) {
			targetRating = (player.getValue() / 1000000.0) + (27 - player.getAge()) + 3;
		} else {
			targetRating = (player.getValue() / 1000000.0) + (27 - player.getAge());
		}
		return targetRating;
	}
}
