package gameengine;

import java.util.Date;

public class GameMode {
	
	private final int MATCHPOINTS = 11; 

	// Spielstaende.
	private int p1_score;
	private int p2_score;

	private int p1_returns;
	private int p2_returns;

	// Spielzeit.
	private Date gameTimeStart;
	private Date gameTimeCurrent;
	
	// Spielmodus.
	private String gameMode;
	
	public GameMode(String mode) {
		gameTimeStart = new Date();

		p1_score = 0;
		p2_score = 0;
		p1_returns = 0;
		p2_returns = 0;
		
		gameMode = mode;
		
	}
	
	public void update() {
		gameTimeCurrent = new Date();
	}
	
	public boolean checkVictory() {
		if (gameMode.equals("normal")) {
			return p2_score == 11 ? false : true;
		}
		if (gameMode.equals("expert")) {
			return p2_score > 0 ? false : true;
		}
		if (gameMode.equals("training")) {
			// Milliseconds als Differenz.
			return gameTimeCurrent.getTime() - gameTimeStart.getTime() >= 60000 ? false : true;
		}
		return true;
	}

	public void p1_death() {
		p2_score++;
	}
	
	public void p2_death() {
		p1_score++;
	}
	
	public void p1_return() {
		p1_returns++;
	}
	
	public void p2_return() {
		p2_returns++;
	}
	
	public String getGameMode() {
		return gameMode;
	}
	
	public long getGameTime() {
		return gameTimeCurrent.getTime() - gameTimeStart.getTime();
	}
	
	public int p1_getScore() {
		return p1_score;
	}
	
	public int p2_getScore() {
		return p2_score;
	}
}
