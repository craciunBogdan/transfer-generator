package transfer_generator;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that analyses a given list of players and determines the worst
 * position.
 * 
 * @author craciunBogdan
 *
 */
public class TeamAnalyser {

	private ArrayList<Player> playerList;
	private int averageValue;
	private int minValue;
	private int maxValue;
	private String worstPosition;

	/**
	 * The constructor.
	 */
	public TeamAnalyser() {

	}

	/**
	 * This method generates a HashMap which holds every position in the given
	 * player list and calculates the cumulative value of all the players in the
	 * list that play each of these positions. Afterwards, a couple
	 * modifications are made in order to balance the values, then the worst
	 * position is chosen and a range of values is generated. In this range the
	 * algorithm should search for a potential transfer target. The worst
	 * position and the superior and inferior limits of the range are saved in
	 * local variables that should be accessed through the getter methods.
	 */
	private void generatePosition() {
		HashMap<String, Integer> positions = new HashMap<String, Integer>();
		HashMap<String, Integer> positionsCount = new HashMap<String, Integer>();
		// Populate the HashMap.
		while (!playerList.isEmpty()) {
			Player player = playerList.remove(0);
			switch (player.getPosition()) {
			case "Keeper":
				if (!positions.containsKey("Keeper")) {
					positions.put("Keeper", player.getValue());
					positionsCount.put("Keeper", 1);
				} else {
					positions.put("Keeper", positions.get("Keeper") + player.getValue());
					positionsCount.put("Keeper", positionsCount.get("Keeper") + 1);
				}
				break;
			case "Centre Back":
				if (!positions.containsKey("Centre Back")) {
					positions.put("Centre Back", player.getValue());
					positionsCount.put("Centre Back", 1);
				} else {
					positions.put("Centre Back", positions.get("Centre Back") + player.getValue());
					positionsCount.put("Centre Back", positionsCount.get("Centre Back") + 1);
				}
				break;
			case "Left-Back":
				if (!positions.containsKey("Left-Back")) {
					positions.put("Left-Back", player.getValue());
					positionsCount.put("Left-Back", 1);
				} else {
					positions.put("Left-Back", positions.get("Left-Back") + player.getValue());
					positionsCount.put("Left-Back", positionsCount.get("Left-Back") + 1);
				}
				break;
			case "Right-Back":
				if (!positions.containsKey("Right-Back")) {
					positions.put("Right-Back", player.getValue());
					positionsCount.put("Right-Back", 1);
				} else {
					positions.put("Right-Back", positions.get("Right-Back") + player.getValue());
					positionsCount.put("Right-Back", positionsCount.get("Right-Back") + 1);
				}
				break;
			case "Defensive Midfield":
				if (!positions.containsKey("Central Midfield")) {
					positions.put("Central Midfield", player.getValue());
					positionsCount.put("Central Midfield", 1);
				} else {
					positions.put("Central Midfield", positions.get("Central Midfield") + player.getValue());
					positionsCount.put("Central Midfield", positionsCount.get("Central Midfield") + 1);
				}
				break;
			case "Central Midfield":
				if (!positions.containsKey("Central Midfield")) {
					positions.put("Central Midfield", player.getValue());
					positionsCount.put("Central Midfield", 1);
				} else {
					positions.put("Central Midfield", positions.get("Central Midfield") + player.getValue());
					positionsCount.put("Central Midfield", positionsCount.get("Central Midfield") + 1);
				}
				break;
			case "Attacking Midfield":
				if (!positions.containsKey("Attacking Midfield")) {
					positions.put("Attacking Midfield", player.getValue());
					positionsCount.put("Attacking Midfield", 1);
				} else {
					positions.put("Attacking Midfield", positions.get("Attacking Midfield") + player.getValue());
					positionsCount.put("Attacking Midfield", positionsCount.get("Attacking Midfield") + 1);
				}
				break;
			case "Left Wing":
				if (!positions.containsKey("Attacking Midfield")) {
					positions.put("Attacking Midfield", player.getValue());
					positionsCount.put("Attacking Midfield", 1);
				} else {
					positions.put("Attacking Midfield", positions.get("Attacking Midfield") + player.getValue());
					positionsCount.put("Attacking Midfield", positionsCount.get("Attacking Midfield") + 1);
				}
				break;
			case "Right Wing":
				if (!positions.containsKey("Attacking Midfield")) {
					positions.put("Attacking Midfield", player.getValue());
					positionsCount.put("Attacking Midfield", 1);
				} else {
					positions.put("Attacking Midfield", positions.get("Attacking Midfield") + player.getValue());
					positionsCount.put("Attacking Midfield", positionsCount.get("Attacking Midfield") + 1);
				}
				break;
			case "Centre Forward":
				if (!positions.containsKey("Centre Forward")) {
					positions.put("Centre Forward", player.getValue());
					positionsCount.put("Centre Forward", 1);
				} else {
					positions.put("Centre Forward", positions.get("Centre Forward") + player.getValue());
					positionsCount.put("Centre Forward", positionsCount.get("Centre Forward") + 1);
				}
				break;
			case "None":
				break;
			}
		}
		for (HashMap.Entry<String, Integer> entry : positions.entrySet()) {
			positions.put(entry.getKey(), (entry.getValue() / positionsCount.get(entry.getKey())));
		}
		// Because goalkeepers seem to have a lower value than outfield players,
		// increase their value with a third of the average value of the club.
		positions.put("Keeper", positions.get("Keeper") + (averageValue / 3));
		// From what I've seen, left backs and right backs also have values that
		// are quite low compared to other outfiled players.
		// Also, some teams don't have left-backs or right-backs so we have to
		// check if they have any in order to not get a null pointer exception.
		if (positions.keySet().contains("Left-Back")) {
			positions.put("Left-Back", positions.get("Left-Back") + (averageValue / 4));
		}
		if (positions.keySet().contains("Right-Back")) {
			positions.put("Right-Back", positions.get("Right-Back") + (averageValue / 4));
		}

		// Determine the worst position in the team.
		int lowestValue = 0;

		for (HashMap.Entry<String, Integer> entry : positions.entrySet()) {
			if (lowestValue == 0) {
				lowestValue = entry.getValue();
				worstPosition = entry.getKey();
			} else if (entry.getValue() < lowestValue) {
				lowestValue = entry.getValue();
				worstPosition = entry.getKey();
			}
		}

		// Determine the value range of the potential target.
		int indicativeValue = (2 * averageValue) - lowestValue;
		if (worstPosition.equals("Keeper") || worstPosition.equals("Centre Back") || worstPosition.equals("Left-Back")
				|| worstPosition.equals("Right-Back")) {
			minValue = indicativeValue - (averageValue);
			maxValue = indicativeValue;
		} else {
			minValue = indicativeValue;
			maxValue = (int) (indicativeValue + (1.2) * averageValue);
		}

		// Round the values in order to get a more uniform range.
		if (minValue > 1000000) {
			int minValueProxy = Math.round((float) (minValue / 1000000.0));
			minValue = minValueProxy * 1000000;
		}

		if (maxValue > 1000000) {
			int maxValueProxy = Math.round((float) (maxValue / 1000000.0));
			maxValue = maxValueProxy * 1000000;
		}
	}

	/**
	 * Method that returns the inferior limit of the value range in which
	 * potential transfer targets should be found.
	 * 
	 * @return The minimum value of a potential transfer target.
	 */
	public int getMinValue() {
		return minValue;
	}

	/**
	 * Method that returns the superior limit of the value range in which
	 * potential transfer targets should be found.
	 * 
	 * @return The maximum value of a potential transfer target.
	 */
	public int getMaxValue() {
		return maxValue;
	}

	/**
	 * Method that returns the worst position of the given player list.
	 * 
	 * @return The worst position of the given player list.
	 */
	public String getWorstPosition() {
		return worstPosition;
	}

	/**
	 * Method that analyses a team and determines its worst position and the
	 * value range of the potential transfer targets.
	 * 
	 * @param currentPlayerList
	 *            The team given as an ArrayList of players.
	 * @param teamAverageValue
	 *            The average value of the team.
	 */
	public void analyseTeam(ArrayList<Player> currentPlayerList, int teamAverageValue) {
		this.averageValue = teamAverageValue;
		this.playerList = currentPlayerList;
		generatePosition();
	}
}
