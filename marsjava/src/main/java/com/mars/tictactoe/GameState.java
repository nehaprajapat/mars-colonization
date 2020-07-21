package com.mars.tictactoe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameState {
	public enum GameMode {
		AI_VS_HUMAN, 
		HUMAN_VS_HUMAN
	};
	public enum GameLevel{
		BEGINNER,
		INTERMEDIATE,
		HARD
	};
	public enum FirstPlayer{
		COMPUTER,
		HUMAN
	};
	public enum Symbol{
		X,O
	};
	public enum GameStage {
		
		MODE_SELECTION,
		LEVEL_SELECTION,
		FIRST_PLAYER,
		SYMBOL,
		PLAYER_IDENTIFICATION,
		IN_GAME,
		POST_GAME
	}
	
	private String xPlayerName;
	private String oPlayerName;
	private String gameMessage;
	private String turnMessage;
	private String xScore;
	private String oScore;
	private String recom;
	private Board.Marker turn;
	private GameMode gameMode;
	private GameLevel gameLevel;
	private GameStage gameStage;
	private FirstPlayer firstPlayer;
	private Symbol sym;
	private Board board;

	private static final Logger log = LoggerFactory.getLogger(GameState.class);

	public GameState()
	{
		board = new Board();

		reset();
	}
	
	public void reset()
	{
		setxPlayerName("X Player");
		setoPlayerName("O Player");
		setGameMessage("");
		setRecom("");
	//	setTurnMessage("Turn: "+sym);
	//	if(sym.equals(Symbol.X)) {setTurn(Board.Marker.X);}else {setTurn(Board.Marker.O);}
		setXScore("X's Score: 0");
		setOScore("O's Score: 0");
		setGameMode(GameMode.AI_VS_HUMAN);
		setGameLevel(GameLevel.BEGINNER);
		setGameStage(GameStage.MODE_SELECTION);
		board.clear();
	}
	
	
	public void startNewGame()
	{
		board.clear();
		setGameMessage("");
	//	setTurnMessage("Turn: "+sym);
	//	if(sym.equals(Symbol.X)) {setTurn(Board.Marker.X);}else {setTurn(Board.Marker.O);}
		setRecom("");
		setGameStage(GameStage.IN_GAME);
	}
	
	
	public String getxPlayerName() {
		return xPlayerName;
	}

	public void setxPlayerName(String xPlayerName) {
		this.xPlayerName = xPlayerName;
	}

	public String getoPlayerName() {
		return oPlayerName;
	}

	public void setoPlayerName(String yPlayerName) {
		this.oPlayerName = yPlayerName;
	}

	public String getGameMessage() {
		return gameMessage;
	}

	public void setGameMessage(String playMessage) {
		this.gameMessage = playMessage;
	}

	public String getTurnMessage() {
		return turnMessage;
	}

	public void setTurnMessage(String turnMessage) {
		this.turnMessage = turnMessage;
	}
    
	public String getXScore() {
		return xScore;
	}

	public void setXScore(String x) {
		this.xScore = x;
	}
	
	public String getOScore() {
		return oScore;
	}

	public void setOScore(String o) {
		this.oScore = o;
	}
	public String getRecom() {
		return recom;
	}

	public void setRecom(String r) {
		this.recom = r;
	}
	public Board.Marker getTurn() {
		return turn;
	}

	public void setTurn(Board.Marker turn) {
		this.turn = turn;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}
	
	public GameLevel getGameLevel() {
		return gameLevel;
	}
    public void setGameLevel(GameLevel gameLevel) {
				this.gameLevel=gameLevel;
	}

	public GameStage getGameStage() {
		return gameStage;
	}
	
	public void setGameStage(GameStage gameStage) {
		this.gameStage = gameStage;
	}
	public FirstPlayer getFirstPlayer() {
		return firstPlayer;
	}
	
	public void setFirstPlayer(FirstPlayer firstPlayer) {
		this.firstPlayer = firstPlayer;
	}
	public Symbol getSymbol() {
		return sym;
	}
	
	public void setSymbol(Symbol s) {
		this.sym = s;
	}

	public Board getBoard() {
		return board;
	}
	
	@Override
	public String toString() {
		return "GameState [xPlayerName=" + xPlayerName + ", oPlayerName=" + oPlayerName + ", gameMessage=" + gameMessage
				+ ", turnMessage=" + turnMessage + ", turn=" + turn + ", gameMode=" + gameMode + ", gameStage="
				+ gameStage + ", board=" + board + "]";
	}
		
}
