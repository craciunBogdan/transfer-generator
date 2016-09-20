package transfer_generator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class that communicates with the database. Used as a link between the MainApp
 * and the database.
 * 
 * @author craciunBogdan
 *
 */
public class DatabaseLink {

	private String worstPosition;
	private Connection c;

	/**
	 * Constructor. Establishes the connection to the database.
	 */
	public DatabaseLink() {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:db/main.db");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * Method that returns an ObservableList containing the names of the leagues
	 * in the database, as well as the nations of the leagues.
	 * 
	 * @return An ObservableList containing the leagues and their nations.
	 */
	public ObservableList<String> getLeagues() {
		ObservableList<String> leaguesList = FXCollections.observableArrayList();
		try {
			Statement stmt;
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT name, nation FROM leagues;");
			while (rs.next()) {
				String league = rs.getString("name");
				String nation = rs.getString("nation");
				String entry = league + " (" + nation + ")";
				leaguesList.add(entry);
			}
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return leaguesList;
	}

	/**
	 * Method that returns an ObservableList containing the teams in the given
	 * league.
	 * 
	 * @param leagueEntry
	 *            The league from which the method should get the teams.
	 * @return An ObservableList containing the teams in the given league.
	 */
	public ObservableList<String> getTeams(String leagueEntry) {
		ObservableList<String> teamsList = FXCollections.observableArrayList();
		String league;
		if (leagueEntry.contains("(")) {
			league = leagueEntry.substring(0, leagueEntry.indexOf('(') - 1);
		} else {
			league = leagueEntry;
		}
		try {
			Statement stmt;
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT id FROM leagues WHERE name = '" + league + "';");
			int id = rs.getInt("id");
			rs = stmt.executeQuery("SELECT name FROM teams WHERE league_id = " + id + ";");
			while (rs.next()) {
				String team = rs.getString("name");
				teamsList.add(team);
			}
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return teamsList;
	}

	/**
	 * Method that takes the name of a team as argument and analyses the team
	 * and returns an ObservableList containing three players that could
	 * potentially be transfer targets for the given team.
	 * 
	 * @param team
	 *            The name of the team to be analysed.
	 * @return An ObservableList containing 3 player that could potentially be
	 *         transfer targets for the team.
	 * @throws SQLException
	 *             if the database was not build properly.
	 */
	public ObservableList<Player> sendTeam(String team) throws SQLException {
		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT id, league_id, avg_value FROM teams WHERE name = '" + team + "';");
		int teamId = rs.getInt("id");
		int leagueId = rs.getInt("league_id");
		int teamAverageValue = rs.getInt("avg_value");
		rs = stmt.executeQuery("SELECT nation FROM leagues WHERE id = " + leagueId + ";");
		String leagueNation = rs.getString("nation");
		rs = stmt.executeQuery("SELECT * FROM players WHERE team_id = " + teamId + ";");
		ArrayList<Player> currentPlayerList = new ArrayList<Player>();
		while (rs.next()) {
			int playerId = rs.getInt("id");
			String playerName = rs.getString("name");
			int playerTeamId = rs.getInt("team_id");
			String playerPosition = rs.getString("position");
			int playerAge = rs.getInt("age");
			String playerNation = rs.getString("nationality");
			int playerValue = rs.getInt("value");
			String playerHref = rs.getString("href");
			currentPlayerList.add(new Player(playerId, playerName, playerTeamId, playerPosition, playerAge,
					playerNation, playerValue, playerHref));
		}
		TeamAnalyser teamAnalyser = new TeamAnalyser();
		teamAnalyser.analyseTeam(currentPlayerList, teamAverageValue);
		rs = stmt.executeQuery("SELECT * FROM players WHERE position = '" + teamAnalyser.getWorstPosition()
				+ "' AND value BETWEEN " + teamAnalyser.getMinValue() + " AND " + teamAnalyser.getMaxValue()
				+ " AND team_id <> " + teamId + " AND age <> 0;");
		this.worstPosition = teamAnalyser.getWorstPosition();
		ArrayList<Player> potentialTargetList = new ArrayList<Player>();
		while (rs.next()) {
			int playerId = rs.getInt("id");
			String playerName = rs.getString("name");
			int playerTeamId = rs.getInt("team_id");
			String playerPosition = rs.getString("position");
			int playerAge = rs.getInt("age");
			String playerNation = rs.getString("nationality");
			int playerValue = rs.getInt("value");
			String playerHref = rs.getString("href");
			potentialTargetList.add(new Player(playerId, playerName, playerTeamId, playerPosition, playerAge,
					playerNation, playerValue, playerHref));
		}
		rs.close();
		stmt.close();

		TargetAnalyser targetAnalyser = new TargetAnalyser(potentialTargetList, leagueNation);
		ObservableList<Player> targetList = targetAnalyser.analyseTargetList();
		return targetList;
	}

	/**
	 * Method that takes the id of a team as argument and returns the name of
	 * the team with the given id.
	 * 
	 * @param teamId
	 *            The id of the team.
	 * @return The name of the team with the given id.
	 */
	public String getTeamName(int teamId) {
		String teamName = null;
		Statement stmt;
		ResultSet rs;
		try {
			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT name FROM teams WHERE id = " + teamId + ";");
			teamName = rs.getString("name");
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return teamName;
	}

	/**
	 * Method that returns the worst position of the team that was analysed.
	 * 
	 * @return The worst position of the analysed team.
	 */
	public String getWorstPosition() {
		return worstPosition;
	}
}
