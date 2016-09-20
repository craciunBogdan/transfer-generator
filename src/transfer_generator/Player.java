package transfer_generator;

/**
 * Class that represents a player.
 * 
 * @author craciunBogdan
 *
 */
public class Player {
	private int id;
	private String name;
	private int teamId;
	private String position;
	private int age;
	private String nation;
	private int value;
	private String href;

	/**
	 * The constructor.
	 * 
	 * @param id
	 *            The id of the player.
	 * @param name
	 *            The full name of the player.
	 * @param teamId
	 *            The id of the players team.
	 * @param position
	 *            The position of the player.
	 * @param age
	 *            The age of the player.
	 * @param nation
	 *            The nationality of the player.
	 * @param value
	 *            The value of the player.
	 * @param href
	 *            The link to the players transfermarkt.co.uk page.
	 */
	public Player(int id, String name, int teamId, String position, int age, String nation, int value, String href) {
		this.id = id;
		this.name = name;
		this.teamId = teamId;
		this.position = position;
		this.age = age;
		this.nation = nation;
		this.value = value;
		this.href = href;
	}

	/**
	 * Method that returns the id of the player.
	 * 
	 * @return The id of the player.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Method that returns the name of the player.
	 * 
	 * @return The name of the player.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method that returns the id of the players team.
	 * 
	 * @return The id of the player.
	 */
	public int getTeamId() {
		return teamId;
	}

	/**
	 * Method that returns the position of the player.
	 * 
	 * @return The position of the player.
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * Method that returns the age of the player.
	 * 
	 * @return The age of the player.
	 */
	public int getAge() {
		return age;
	}

	/**
	 * Method that returns the nationality of the player.
	 * 
	 * @return The nationality of the player.
	 */
	public String getNation() {
		return nation;
	}

	/**
	 * Method that returns the value of the player.
	 * 
	 * @return The value of the player.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Method that returns the link of the players transfermarkt.co.uk page.
	 * @return The link of the players transfermarkt.co.uk page.
	 */
	public String getHref() {
		return href;
	}

	public String toString() {
		String s = "ID: " + this.id + "\nName: " + this.name + "\nTeam ID: " + this.teamId + "\nPosition: "
				+ this.position + "\nAge: " + this.age + "\nNationality: " + this.nation + "\nValue: " + this.value
				+ "\nHREF: " + this.href + "\n";
		return s;
	}

}
