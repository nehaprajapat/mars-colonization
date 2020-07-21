package com.mars.tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mars.tictactoe.Board.Marker;
import com.mars.tictactoe.GameState.FirstPlayer;
import com.mars.tictactoe.GameState.GameLevel;
import com.mars.tictactoe.GameState.GameMode;
import com.mars.tictactoe.GameState.GameStage;
import com.mars.tictactoe.GameState.Symbol;


@Controller
public class GameController {
		
	private static final Logger log = LoggerFactory.getLogger(GameController.class);

	/**
	 * Starts new Tic Tac Toe game.
	 * 
	 * @param session 
	 * @param model Spring framework Model
	 * @return Spring framework View name
	 */
	static int[][] magicSquare = 
		{
				{2 , 7 , 6} ,  // 1  2  3   // 7241
				{9 , 5 , 1} ,  // 4  5  6
				{4 , 3 , 8}    // 7  8  9
		};
	static int x;static int o;
	public static ArrayList<Integer> playerPositions;
	public static ArrayList<Integer> cpuPositions;
	static Stack<Integer> playerStack ;
	static Stack<Integer> cpuStack;
	//static Stack<Integer> p1Stack ;
	//static Stack<Integer> p2Stack;
	
	@RequestMapping(value = "/tictactoe", method = RequestMethod.GET)
	public String game(
			HttpSession session,
			Model model) {
		
		GameState gameState = getStateFromSession(session);
		if(gameState == null) {
			log.info("gameState is null; starting new game");
			gameState = new GameState();
			putStateInSession(session, gameState);
		}
		model.addAttribute(Constants.GAME_STATE, gameState);
	/*	playerPositions= new ArrayList<Integer>();
		cpuPositions= new ArrayList<Integer>();
		playerStack= new Stack<Integer>();
		cpuStack= new Stack<Integer>();*/
		x=0;o=0;
		return Constants.VIEW_GAME;
	}
	
	/**
	 * Resets the game to it's initial state, and allows selection of
	 * either play against the computer, or against another human.
	 * 
	 * @param session 
	 * @param model Spring framework Model
	 * @return Spring framework View name
	 */
	@RequestMapping(value = "/tictactoe/reset", method = RequestMethod.GET)
	public String reset(
			HttpSession session,
			Model model) {
		
		log.info("Resetting new game");
		GameState gameState = new GameState();
		putStateInSession(session, gameState);
		model.addAttribute(Constants.GAME_STATE, gameState);
		x=0;o=0;
		/*playerPositions= new ArrayList<Integer>();
		cpuPositions= new ArrayList<Integer>();
		playerStack= new Stack<Integer>();
		cpuStack= new Stack<Integer>();*/
		
		return Constants.VIEW_GAME;
	}
	
	/**
	 * Starts a new game in the current mode.
	 * 
	 * @param session 
	 * @param model Spring framework Model
	 * @return Spring framework View name
	 */
	@RequestMapping(value = "/tictactoe/new", method = RequestMethod.GET)
	public String gameNew(
			HttpSession session,
			Model model) {
		
		log.info("Starting new game");
		GameState gameState = getStateFromSession(session);
		gameState.startNewGame();
		model.addAttribute(Constants.GAME_STATE, gameState);
		playerPositions= new ArrayList<Integer>();
		cpuPositions= new ArrayList<Integer>();
		playerStack= new Stack<Integer>();
		cpuStack= new Stack<Integer>();
//		p1Stack = new Stack<Integer>();
//		p2Stack = new Stack<Integer>();
		if(gameState.getSymbol().equals(Symbol.X)) {gameState.setTurn(Board.Marker.X);
		gameState.setTurnMessage("Turn: X");}else {gameState.setTurn(Board.Marker.O);
		gameState.setTurnMessage("Turn: O");}
		if(gameState.getGameMode().equals(GameMode.AI_VS_HUMAN) && gameState.getFirstPlayer().equals(FirstPlayer.COMPUTER)) {
			if(gameState.getGameLevel().equals(GameLevel.BEGINNER)) {
				determineBeginnerMove(gameState);
				evaluateBoard(gameState);
			}
			else if(gameState.getGameLevel().equals(GameLevel.INTERMEDIATE)) {
				MagicSquareMethod(gameState);
				evaluateBoard(gameState);
			}
			else if(gameState.getGameLevel().equals(GameLevel.HARD)){
				cpuplaying(gameState);
				evaluateBoard(gameState);
			}
			
		}
		return Constants.VIEW_GAME;
	}
	/**
	 * Choose whether to play the game against the computer, or a human oponent.
	 * 
	 * @param session 
	 * @param mode String representing the desired mode: "ai" for play against the computer; "twoplayer" for multiplayer mode.
	 * @param model Spring framework Model
	 * @return Spring framework View name
	 */
	@RequestMapping(value = "/tictactoe/modeselection", method = RequestMethod.GET)
	public String modeSelected(
			HttpSession session,
			@RequestParam(value = "mode", required = true) String mode,
			Model model) {
		
		GameState gameState = getStateFromSession(session);
		if(mode.equals("ai")) {
			gameState.setGameMode(GameMode.AI_VS_HUMAN);
			gameState.setGameStage(GameStage.LEVEL_SELECTION);
			model.addAttribute(Constants.GAME_STATE, gameState);
			return Constants.VIEW_GAME;
		}
		else if(mode.equals("twoplayer")) {
			gameState.setGameMode(GameMode.HUMAN_VS_HUMAN);
		}
		else {
			throw new RuntimeException("Invalid selected game mode:" + mode);
		}
		gameState.setGameStage(GameStage.SYMBOL);
		model.addAttribute(Constants.GAME_STATE, gameState);
		return Constants.VIEW_GAME;
	}
	
	@RequestMapping(value = "/tictactoe/levelselection", method = RequestMethod.GET)
	public String levelSelected(HttpSession session,
	@RequestParam(value = "level", required = true) String level,
	Model model){
		GameState gameState = getStateFromSession(session);
		if(level.equals("easy")) {
			gameState.setGameLevel(GameLevel.BEGINNER);
		}
		else if(level.equals("med")) {
			gameState.setGameLevel(GameLevel.INTERMEDIATE);
		}
		else if(level.equals("hard")) {
			gameState.setGameLevel(GameLevel.HARD);
		}
		else {
			throw new RuntimeException("Invalid selected game level:" + level);
		}
		gameState.setGameStage(GameStage.FIRST_PLAYER);
		model.addAttribute(Constants.GAME_STATE, gameState);
		return Constants.VIEW_GAME;
	}
	
	@RequestMapping(value = "/tictactoe/firstplayer", method = RequestMethod.GET)
	public String startingPlayer(HttpSession session,
	@RequestParam(value = "start", required = true) String start,
	Model model){
		GameState gameState = getStateFromSession(session);
		if(start.equals("ai")) {
			gameState.setFirstPlayer(FirstPlayer.COMPUTER);
		}
		else if(start.equals("human")) {
			gameState.setFirstPlayer(FirstPlayer.HUMAN);
		}
		else {
			throw new RuntimeException("Invalid selected starting player:" + start);
		}
		gameState.setGameStage(GameStage.SYMBOL);
		model.addAttribute(Constants.GAME_STATE, gameState);
		return Constants.VIEW_GAME;
		
	}
	
	@RequestMapping(value = "/tictactoe/symbol", method = RequestMethod.GET)
	public String symbol(HttpSession session,
	@RequestParam(value = "sym", required = true) String s,
	Model model){
		GameState gameState = getStateFromSession(session);
		if(s.equals("x")) {
			gameState.setSymbol(Symbol.X);
			//gameState.setTurnMessage("Turn: X");
			//gameState.setTurn(Board.Marker.X);
		}
		else if(s.equals("o")) {
			gameState.setSymbol(Symbol.O);
			//gameState.setTurnMessage("Turn: O");
			//gameState.setTurn(Board.Marker.O);
		}
		else {
			throw new RuntimeException("Invalid selected symbol:" + s);
		}
		
		model.addAttribute(Constants.GAME_STATE, gameState);
		return "redirect:/tictactoe/new";
	}
	
	@RequestMapping(value = "/tictactoe/recom", method = RequestMethod.GET)
	public String Recommendation(HttpSession session,
	@RequestParam(value = "hint", required = true) String s,
	Model model){
		GameState gameState = getStateFromSession(session);
		if(s.equals("yes")) {
			int r = recommendationsToPlayer(gameState);
			String rec = "Cell: "+r;
			gameState.setRecom(rec);
		}

		model.addAttribute(Constants.GAME_STATE, gameState);
		return Constants.VIEW_GAME;
	}
	/**
	 * Places a marker for the current player in the requested position.
	 * 
	 * @param session 
	 * @param row Number of row to place marker
	 * @param col Number of column to place marker
	 * @param model Spring framework Model
	 * @return Spring framework View name
	 */
	@RequestMapping(value = "/tictactoe/move", method = RequestMethod.GET)
	public String playerMove(
			HttpSession session,
			@RequestParam(value = "row", required = true) Integer row, 
			@RequestParam(value = "col", required = true) Integer col, 
			Model model) {
		
		GameState gameState = getStateFromSession(session);
		model.addAttribute(Constants.GAME_STATE, gameState);
		log.info("move=(" + row + ", " + col + ")");
		gameState.setRecom("");
		model.addAttribute(Constants.GAME_STATE, gameState);
		// If not in the midst of a game, don't allow move.
		if(!gameState.getGameStage().equals(GameStage.IN_GAME)) {
			log.info("Game not in progress); ignoring move request.");
			return Constants.VIEW_GAME;
		}
		
		Board board = gameState.getBoard();
		
	/*	try {
			board.move(row, col, gameState.getTurn());
			evaluateBoard(gameState);
			
			// If game has not ended one way or another, and the game is 
			// against the computer, determine where it will move.
			if(gameState.getGameStage().equals(GameStage.IN_GAME) &&
					gameState.getGameMode().equals(GameMode.AI_VS_HUMAN) && 
					gameState.getGameLevel().equals(GameLevel.BEGINNER)) {
				determineBeginnerMove(gameState);
				evaluateBoard(gameState);
			}
			else if(gameState.getGameStage().equals(GameStage.IN_GAME) &&
					gameState.getGameMode().equals(GameMode.AI_VS_HUMAN) && 
					gameState.getGameLevel().equals(GameLevel.INTERMEDIATE)) {
				MagicSquareMethod(gameState);
				evaluateBoard(gameState);
			}
			else if(gameState.getGameStage().equals(GameStage.IN_GAME) &&
					gameState.getGameMode().equals(GameMode.AI_VS_HUMAN) && 
					gameState.getGameLevel().equals(GameLevel.HARD)) {
				cpuplaying(gameState);
				evaluateBoard(gameState);
			}
		}
		catch( Exception e )
		{
			// TODO: Add message to user.  As it is now, move request is
			// ignored, but letting them know would probably be better
			log.error("Cannot complete move", e);
		}*/
		try {
			board.move(row, col, gameState.getTurn());
			int pos = (3*row) + col+ 1;
			playerPositions.add(magicSquare[(pos-1)/3][(pos-1) % 3]);
			playerStack.add((3*row) + col + 1);
			evaluateBoard(gameState);
		    if(gameState.getGameMode().equals(GameMode.AI_VS_HUMAN)) {
			
			if(gameState.getGameStage().equals(GameStage.IN_GAME) &&
					gameState.getGameLevel().equals(GameLevel.BEGINNER)) {
				determineBeginnerMove(gameState);
				evaluateBoard(gameState);
			}
			else if(gameState.getGameStage().equals(GameStage.IN_GAME) &&
					gameState.getGameLevel().equals(GameLevel.INTERMEDIATE)) {
				MagicSquareMethod(gameState);
				evaluateBoard(gameState);
			}
			else if(gameState.getGameStage().equals(GameStage.IN_GAME) &&
					gameState.getGameLevel().equals(GameLevel.HARD)) {
				cpuplaying(gameState);
				evaluateBoard(gameState);
			}
			
		}
		}
		catch(Exception e){
			// TODO Auto-generated catch block
			log.error("Cannot complete move", e);
		}
		
		return Constants.VIEW_GAME;
	}
	
	/**
	 * Evaluate the game board to see if a winner can be declared, or if there is a draw.
	 * If neither of these conditions is detected, switch active player.
	 * 
	 * @param gameState
	 */
	public void evaluateBoard(GameState gameState) {
		Board board = gameState.getBoard();
		// First, check for a draw
		if(board.isDraw()) {
			x+=1;o+=1;
			gameState.setOScore("O's Score: "+o);
			gameState.setXScore("X's Score: "+x);
			gameState.setGameMessage("It's a draw!");
			
			gameState.setGameStage(GameStage.POST_GAME);
		}
		else if(board.isWinner(gameState.getTurn())) {
			if(gameState.getTurn().equals(Board.Marker.O)) {
				o+=1;
				gameState.setOScore("O's Score: "+o);
				gameState.setGameMessage("O wins!");
		/*		if(gameState.getSymbol().equals(Symbol.X)) {gameState.setTurn(Board.Marker.X);
				gameState.setTurnMessage("Turn: X");}else {gameState.setTurn(Board.Marker.O);
				gameState.setTurnMessage("Turn: O");}*/
			}
			else {x+=1;
			    gameState.setXScore("X's Score: "+x);
				gameState.setGameMessage("X wins!");
		/*		if(gameState.getSymbol().equals(Symbol.X)) {gameState.setTurn(Board.Marker.X);
				gameState.setTurnMessage("Turn: X");}else {gameState.setTurn(Board.Marker.O);
				gameState.setTurnMessage("Turn: O");}*/
			}
			gameState.setGameStage(GameStage.POST_GAME);
		}
		else
		{
			if(gameState.getTurn() == Board.Marker.X) {
				gameState.setTurn(Board.Marker.O);
				gameState.setTurnMessage("Turn: O");
			}
			else {
				gameState.setTurn(Board.Marker.X);
				gameState.setTurnMessage("Turn: X");
			}
		}
	}
	
	/**
	 * This method is called during play against the computer, and 
	 * attempts to find the best possible move.
	 * 
	 * @param gameState
	 */
	public void determineBeginnerMove(GameState gameState)
	{
		Board.Marker board[][] = gameState.getBoard().board;
		Board.Marker playerMarker = gameState.getTurn() ;
		Board.Marker opponentMarker = playerMarker.equals(Board.Marker.X) ? Board.Marker.O : Board.Marker.X ;
		int r,c;
		boolean found = false;
		Random random = new Random();
		while(!found) {
			r = random.nextInt(3);
			c = random.nextInt(3);
			if(board[r][c].equals(Board.Marker.BLANK)) {
				try {
					gameState.getBoard().move(r, c, playerMarker );
					found = true;
				}
				catch(Exception e) {
					log.error("Problem making random move!", e);
				}			
			}
		}
	}
	
	public void MagicSquareMethod(GameState gameState) {
		Board b = gameState.getBoard();
		Board.Marker board[][] = gameState.getBoard().board;
		Board.Marker playerMarker = gameState.getTurn() ;
		Board.Marker opponentMarker = playerMarker.equals(Board.Marker.X) ? Board.Marker.O : Board.Marker.X ;
		
		if (cpuPositions.size() >= 2) 
		{
			for (int i = 0 ; i <= cpuPositions.size()-2 ; i++)
			{
				int number = 15 - (cpuPositions.get(i) + cpuPositions.get(cpuPositions.size()-1));
				if (number >=1 && number<=9 && !(playerPositions.contains(number) || cpuPositions.contains(number)))
				{
					System.out.println("First if");
					placepositions(gameState, b, board , getPositionFromMagicSquare(number) , "cpu",opponentMarker,playerMarker); // cpuPositions = 5  6  9     7,2,4,1
					return ;
				}
			}
			
		}
		
		if (playerPositions.size() >= 2)
		{
			for (int i = 0 ; i <= playerPositions.size()-2 ; i++)
			{
				int number = 15 - (playerPositions.get(i) + playerPositions.get(playerPositions.size()-1));
				if (number >=1 && number<=9 && !(playerPositions.contains(number) || cpuPositions.contains(number)))
				{
					System.out.println("Second if");
					placepositions(gameState, b, board , getPositionFromMagicSquare(number) , "cpu",opponentMarker,playerMarker);
					return ;
				}
			}
		}
		
		if (board[1][1] == Marker.BLANK)
		{
			System.out.println("Third if");
			placepositions(gameState, b, board , 5 , "cpu",opponentMarker,playerMarker);
			return;
		}
		if (playerPositions.size() == 2 && board[1][1] != opponentMarker && (board[0][0] != opponentMarker|| board[0][1] != opponentMarker || board[1][0] != opponentMarker
				|| board[1][1] != opponentMarker))
		{
			if(fillCorner(gameState, b, board,opponentMarker,playerMarker))
			{
				System.out.println("Fourth if");
				return;
			}
		}
		int[] corners = {2, 4, 6, 8};
		if (playerPositions.size() == 2 && (counter(corners, b) == 2))
		{
			if (fillemptysides(gameState, b, board,opponentMarker,playerMarker))
			{
				System.out.println("Fifth if");
				return;
			}
		}
		int[] sides = {1, 3, 7, 9};
		if (playerPositions.size() == 2 && counter(sides,b) == 2)
		{
			int q = cornersforsides(b);
			if (q != -1)
			{
				if(!(cpuPositions.contains(q)))
				{
					System.out.println("Sixth if");
					placepositions(gameState, b, board , getPositionFromMagicSquare(q) , "cpu",opponentMarker,playerMarker);
					return;
				}
			}
		}
		for (int i = playerPositions.size()-1 ; i >=0 ; i--)
		{
			int result = oppositeCorners(playerPositions.get(i));
			if (result != -1 && !(cpuPositions.contains(result) || playerPositions.contains(result)))
			{
				System.out.println("Seventh if");
				placepositions(gameState, b, board , getPositionFromMagicSquare(result) , "cpu",opponentMarker,playerMarker);
				return;
			}
		}
		if (fillCorner(gameState, b,board,opponentMarker,playerMarker)) {
			System.out.println("Eighth if");
			return;}
		if(fillemptysides(gameState, b,board,opponentMarker,playerMarker))
		{
			System.out.println("Ninth if");
			return;
		}
		System.out.println("Not entering in any position");
	}
	public static int oppositeCorners(int num)
	{
		if (num == 2)
		{
			return 8 ;
		}
		else if (num == 8)
		{
			return 2;
		}
		else if (num == 4)
		{
			return 6 ;
		}
		else if(num == 6)
		{
			return 4;
		}
		return -1;
	}
	public static int cornersforsides( Board board)
	{
		if(playerPositions.contains(1))
		{
			if(playerPositions.contains(3))
			{
				return 8 ;
			}
			if (playerPositions.contains(7))
			{
				return 6;
			}
			
		}
		if(playerPositions.contains(9))
		{
			if(playerPositions.contains(3))
			{
				return 4 ;
			}
			if (playerPositions.contains(7))
			{
				return 2;
			}
			
		}
		return -1;
	}
	public static boolean fillemptysides(GameState gameState, Board board,Board.Marker[][] gameBoard,Board.Marker opponentMarker, Board.Marker playerMarker)
	{    
		int[] arr1 = {0 , 1 , 1 , 2};
		int[] arr2 = {1 , 0 , 2 , 1};
		for (int i = 0, j = 0 ; i < arr1.length && j < arr2.length ; i++,j++ )
		{
			if (gameBoard[arr1[i]][arr2[j]] == Marker.BLANK)
			{
				placepositions(gameState, board ,gameBoard, (3*arr1[i]) + arr2[j]+ 1 , "cpu", opponentMarker, playerMarker);
				return true;
			}
		}
		return false;
	}
	
	public static int counter(int[] arr, Board board)
	{   
		int counter = 0 ;
		for (int i = 0 ; i < arr.length ; i++)
		{
			if (playerPositions.contains(arr[i]))
			{
				counter++;
			}
		}
		return counter;
	}
	
	public static boolean fillCorner(GameState gameState, Board board,Board.Marker[][] gameBoard,Board.Marker opponentMarker, Board.Marker playerMarker)
	{   
		int[] arr1 = {0 , 0 , 2 ,2};
		int[] arr2 = {0 ,2 , 0 , 2};
		for (int i = 0, j = 0 ; i < arr1.length && j < arr2.length ; i++,j++ )
		{
			if (gameBoard[arr1[i]][arr2[j]] == Marker.BLANK)
			{
				placepositions(gameState, board ,gameBoard, (3*arr1[i]) + arr2[j]+ 1 , "cpu", opponentMarker, playerMarker);
				return true;
			}
		}
		return false;
	}
	
	public static void placepositions(GameState gameState, Board board,Board.Marker[][] gameBoard, int pos , String user, Board.Marker opponentMarker, Board.Marker playerMarker)
	{   	
		Board.Marker symbol = Marker.BLANK;
		if (user.equals("player"))
		{
			symbol = opponentMarker;
			playerPositions.add(magicSquare[(pos-1)/3][(pos-1) % 3]);
		}
		else if (user.equals("cpu"))
		{
			symbol = playerMarker;
			cpuPositions.add(magicSquare[(pos-1)/3][(pos-1) % 3]);
		}
		if (pos == 1 || pos == 2 || pos ==3)
		{
			try {
				gameState.getBoard().move(0, pos-1, symbol);
			} catch (Exception e) {
				log.error("Cannot complete move", e);
			}//gameBoard[0][2 * (pos-1)]=symbol;
			
		}
		else if (pos == 4 || pos ==5 || pos ==6)
		{
			try {
				gameState.getBoard().move(1,pos-4, symbol);
			} catch (Exception e) {
				log.error("Cannot complete move", e);
			}//gameBoard[2][2 * pos - 8] = symbol;
		}
		else if (pos == 7 || pos ==8 || pos ==9)
		{
			try {
				gameState.getBoard().move(2,pos-7, symbol);
			} catch (Exception e) {
				log.error("Cannot complete move", e);
			}//gameBoard[4][2 * pos -14] = symbol;
		}
	}

	public static int getPositionFromMagicSquare(int number)
	{
		if (number == 1) {return 6;}
		else if (number == 2) { return 1;}
		else if (number == 3) { return 8;}
		else if (number == 4) { return 7;}
		else if (number == 5) { return 5;}
		else if (number == 6) { return 3;}
		else if (number == 7) { return 2;}
		else if (number == 8) { return 9;}
		else if (number == 9) { return 4;}
		else {return -1;}
	}
	
	public static int minimax(GameState gameState,Board b,Board.Marker[][]board, int depth, boolean isMaximum , int alpha , int beta,  Board.Marker opponentMarker, Board.Marker playerMarker)
	{   
	   
		if (winningGameValue() != 5)
		{
			return winningGameValue();
		}
		int[] arr = {0,1,2};
		if (isMaximum)
		{
			int bestScore = Integer.MIN_VALUE;
			
	        for (int i : arr)
			{
				for (int j : arr)
					{
						if (board[i][j] == Marker.BLANK)
						{
							board[i][j] = playerMarker;
							cpuStack.add( (3*i)  + j + 1 );
							int score = minimax(gameState,b,board, depth + 1 , false, alpha , beta, opponentMarker,playerMarker);
							//System.out.println("max" + score);
							board[i][j] = Marker.BLANK;
							cpuStack.pop();
							bestScore = Math.max(bestScore, score);
							alpha = Math.max(alpha , bestScore);
							if (alpha >= beta)
							{
								break;
							}
						}
					}
			}
			return bestScore;
		}
		else
		{
			int bestScore = Integer.MAX_VALUE;
			//System.out.println("bestScore: " + bestScore);
			for (int i : arr)
			{
				for (int j : arr)
					{
						if (board[i][j] == Marker.BLANK)
						{
							board[i][j] = opponentMarker;
							playerStack.add(  (3*i)  + j + 1 );
							int score = minimax(gameState,b,board , depth + 1 , true, alpha , beta, opponentMarker, playerMarker);
							//System.out.println("mini  " + score);
							board[i][j] = Marker.BLANK;
							playerStack.pop();
							bestScore = Math.min(score, bestScore);
							beta = Math.min(beta, bestScore);
							if (alpha >= beta)
							{
								break;
							}
						}
					}
			}
			return bestScore;
		}
	}
	
	
	public static void cpuplaying(GameState gameState)
	{   Board board = gameState.getBoard();
        Board.Marker gameBoard[][] = new Board.Marker[3][3];
        gameBoard = gameState.getBoard().board;
		Board.Marker playerMarker = gameState.getTurn() ;
		Board.Marker opponentMarker = playerMarker.equals(Board.Marker.X) ? Board.Marker.O : Board.Marker.X ;
		int[] arr1 = {0,1,2};
		int bestScore = Integer.MIN_VALUE;
		int[] arr2 = new int[2];
		for (int i : arr1)
		{
			for (int j : arr1)
			{
				if (gameBoard[i][j] == Marker.BLANK)
				{
					
					gameBoard[i][j] = playerMarker;
					cpuStack.add( 3*i + j + 1 );
					int score = minimax(gameState,board, gameBoard,0,false, Integer.MIN_VALUE , Integer.MAX_VALUE, opponentMarker, playerMarker);
					if (score > bestScore)
					{
						bestScore = score;
						arr2[0] = i;
						
						arr2[1] = j;
						
					}
					gameBoard[i][j] = Marker.BLANK;
					cpuStack.pop();
				}
			}
		}
		try {
			gameState.getBoard().move(arr2[0],arr2[1], playerMarker);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Cannot complete move", e);
		}
		//gameBoard[arr2[0]][arr2[1]] = playerMarker;
		cpuStack.add( (3*arr2[0]) + arr2[1] + 1);
		
	}
	
	
	public static int minimaxForPlayer(Board.Marker[][] gameBoard, int depth, boolean isMaximum, Board.Marker opponentMarker, Board.Marker playerMarker)
	{
		if (winningGameValue() != 5)
		{
			return -1 * winningGameValue();
		}
		int[] arr = {0,1,2};
		if (isMaximum)
		{
			int bestScore = Integer.MIN_VALUE;
			
			//int score = 0;
			for (int i : arr)
			{
				for (int j : arr)
					{
						if (gameBoard[i][j] == Marker.BLANK)
						{
							gameBoard[i][j] = playerMarker;
							playerStack.add( (3*i)  + j + 1 );
							int score = minimaxForPlayer(gameBoard , depth + 1 , false, opponentMarker, playerMarker);
							//System.out.println("max" + score);
							gameBoard[i][j] = Marker.BLANK;
							playerStack.pop();
							bestScore = Math.max(bestScore, score);
						}
					}
			}
			return bestScore;
		}
		else
		{
			int bestScore = Integer.MAX_VALUE;
			//System.out.println("bestScore: " + bestScore);
			for (int i : arr)
			{
				for (int j : arr)
					{
						if (gameBoard[i][j] == Marker.BLANK)
						{
							gameBoard[i][j] = opponentMarker;
							cpuStack.add(  (3*i)  + j + 1 );
							int score = minimaxForPlayer(gameBoard , depth + 1 , true, opponentMarker, playerMarker);
							//System.out.println("mini  " + score);
							gameBoard[i][j] = Marker.BLANK;
							cpuStack.pop();
							bestScore = Math.min(score, bestScore);
						}
					}
			}
		
			return bestScore;}
	}
	public static int recommendationsToPlayer(GameState gameState)
	{   Board board = gameState.getBoard();
        Board.Marker gameBoard[][] = new Board.Marker[3][3];
        gameBoard = gameState.getBoard().board;
	    Board.Marker playerMarker = gameState.getTurn() ;
	    Board.Marker opponentMarker = playerMarker.equals(Board.Marker.X) ? Board.Marker.O : Board.Marker.X ;
		int[] arr1 = {0,1,2};
		int bestScore = Integer.MIN_VALUE;
		//int best_i = 0 , best_j =0;
		int[] arr2 = new int[2];
		for (int i : arr1)
		{
			for (int j : arr1)
			{
				if (gameBoard[i][j] == Marker.BLANK)
				{
					// System.out.println(i + "  " + j);
					gameBoard[i][j] = playerMarker;
					playerStack.add( 3*i + j + 1 );
					int score = minimaxForPlayer(gameBoard,0,false, opponentMarker, playerMarker);
					//System.out.println("score : " + score);
					if (score > bestScore)
					{
						bestScore = score;
						arr2[0] = i;
						//System.out.println(arr2[0] + "arr2[0]");
						arr2[1] = j;
						//System.out.println(arr2[0] + "arr2[1]");
					}
					gameBoard[i][j] = Marker.BLANK;
					playerStack.pop();
				}
			}
		}
		
		//System.out.println(arr2[0] + ": arr2[0]   " + arr2[1] + ": arr2[1]");
		
		
		return   ((3*arr2[0])  + arr2[1] + 1);
	}
	public static int winningGameValue()
	{
		
		
			if (playerStack.containsAll(Arrays.asList(1,2,3)) ||  playerStack.containsAll(Arrays.asList(4,5,6))  || 
					playerStack.containsAll(Arrays.asList(7,8,9)) ||  playerStack.containsAll(Arrays.asList(1,4,7))  ||
					playerStack.containsAll(Arrays.asList(2,5,8)) ||  playerStack.containsAll(Arrays.asList(3,6,9))  ||
					playerStack.containsAll(Arrays.asList(1,5,9)) ||  playerStack.containsAll(Arrays.asList(3,5,7)))
				{
										return -1;
				}
		
		
		
			else if (cpuStack.containsAll(Arrays.asList(1,2,3)) ||  cpuStack.containsAll(Arrays.asList(4,5,6))  || 
					cpuStack.containsAll(Arrays.asList(7,8,9)) ||  cpuStack.containsAll(Arrays.asList(1,4,7))  ||
					cpuStack.containsAll(Arrays.asList(2,5,8)) ||  cpuStack.containsAll(Arrays.asList(3,6,9))  ||
					cpuStack.containsAll(Arrays.asList(1,5,9)) ||  cpuStack.containsAll(Arrays.asList(3,5,7)))
				{
					
					return 1;
				}
		
			else if (playerStack.size() + cpuStack.size() == 9)
		
		         return 0;
			
		
		return 5;
	}
	

	
	
	/**
	 * Convenience method to retrieve game state from session.
	 * 
	 * @param session
	 * @return Current game state.
	 */
	private GameState getStateFromSession(HttpSession session)
	{
		GameState gameState = (GameState)session.getAttribute(Constants.GAME_STATE);
		if(gameState == null) {
			log.info("New GameState created and put in session");
			gameState = new GameState();
			putStateInSession(session, gameState);
		}
		return gameState;
	}
	
	/**
	 * Convenience method to save game state in session.
	 * 
	 * @param session
	 */
	private void putStateInSession(HttpSession session, GameState gameState) {
		session.setAttribute(Constants.GAME_STATE, gameState);
	}
}