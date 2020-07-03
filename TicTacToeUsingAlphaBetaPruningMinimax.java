import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class TicTacToeUsingAlphaBetaPruningMinimax {
	static Stack<Integer> playerPositions = new Stack<Integer>();
	static Stack<Integer> cpuPositions = new Stack<Integer>();
	public static void main(String args[])
	{
		char[][] gameBoard = {
		{' ', '|', ' ', '|', ' '},
		{'-', '+', '-', '+', '-'},
		{' ', '|', ' ', '|', ' '},
		{'-', '+', '-', '+', '-'},
		{' ', '|', ' ', '|', ' '}
		};
		
		playgame(gameBoard);
		
	}
	public static void playgame(char[][] gameBoard)
	{
		while(true)
		{
			
			userplaying(gameBoard);
			if (winningGameValue() == -1)
			{
				System.out.println("Congratulations!! You won the game !");

				break;
			}
			else if (winningGameValue() == 0)
			{
				System.out.println("Its a tie");
				break;
			}
			cpuplaying(gameBoard);
			
			printgameBoard(gameBoard);
			if (winningGameValue() == 1)
			{
				System.out.println("Cpu won !sorry :(");
				break;
			}
			else if (winningGameValue() == 0)
			{
				System.out.println("Its a tie");
				break;
			}
			
		}
	}
	public static void userplaying(char[][] gameBoard)
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter your postion from 1:9 - ");
		int playerPos = scan.nextInt();
		while (playerPositions.contains(playerPos) || cpuPositions.contains(playerPos))// better way is create a list and store all // the remaining positions in it and then choose random value from this list. 
		{
			System.out.println("Position is already filled .Please enter again");
			playerPos = scan.nextInt();
		}
		placePiece(gameBoard, playerPos, "player");
		printgameBoard(gameBoard);
		System.out.println();
	}
	public static int minimax(char[][] gameBoard, int depth, boolean isMaximum , int alpha , int beta)
	{
		if (winningGameValue() != 5)
		{
			return winningGameValue();
		}
		int[] arr = {0,2,4};
		if (isMaximum)
		{
			int bestScore = Integer.MIN_VALUE;
			
			//int score = 0;
			for (int i : arr)
			{
				for (int j : arr)
					{
						if (gameBoard[i][j] == ' ')
						{
							gameBoard[i][j] = '0';
							cpuPositions.add( (i+j)/2  + i + 1 );
							int score = minimax(gameBoard , depth + 1 , false, alpha , beta);
							//System.out.println("max" + score);
							gameBoard[i][j] = ' ';
							cpuPositions.pop();
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
						if (gameBoard[i][j] == ' ')
						{
							gameBoard[i][j] = 'X';
							playerPositions.add(  (i+j)/2  + i + 1 );
							int score = minimax(gameBoard , depth + 1 , true, alpha , beta);
							//System.out.println("mini  " + score);
							gameBoard[i][j] = ' ';
							playerPositions.pop();
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
	public static void cpuplaying(char[][] gameBoard)
	{
		int[] arr1 = {0,2,4};
		int bestScore = Integer.MIN_VALUE;
		//int best_i = 0 , best_j =0;
		int[] arr2 = new int[2];
		for (int i : arr1)
		{
			for (int j : arr1)
			{
				if (gameBoard[i][j] == ' ')
				{
					// System.out.println(i + "  " + j);
					gameBoard[i][j] = '0';
					cpuPositions.add( (i+j)/2  + i + 1 );
					int score = minimax(gameBoard,0,false, Integer.MIN_VALUE , Integer.MAX_VALUE);
					//System.out.println("score : " + score);
					if (score > bestScore)
					{
						bestScore = score;
						arr2[0] = i;
						//System.out.println(arr2[0] + "arr2[0]");
						arr2[1] = j;
						//System.out.println(arr2[0] + "arr2[1]");
					}
					gameBoard[i][j] = ' ';
					cpuPositions.pop();
				}
			}
		}
		
		//System.out.println(arr2[0] + ": arr2[0]   " + arr2[1] + ": arr2[1]");
		
		gameBoard[arr2[0]][arr2[1]] = '0';
		cpuPositions.add(   (arr2[0] + arr2[1])/2       + arr2[0] + 1);
		//System.out.println(gameBoard[arr2[0]][arr2[1]] + "  gameboard placing 0 ");
		
	}
	public static void printgameBoard(char[][] gameBoard)
	{
		for (char[] row : gameBoard)
		{
			for (char c : row)
			{
				System.out.print(c);
			}
			System.out.println();
		}
	}
	public static void placePiece(char[][] gameBoard, int pos, String user)
	{
		char symbol = 'X';
		if (user.equals("player"))
		{
			symbol = 'X';
			playerPositions.add(pos);
		}
		else if(user.equals("cpu"))
		{
			symbol = '0';
			cpuPositions.add(pos);
		}
		if (pos == 1 || pos == 2 || pos ==3)
		{
			gameBoard[0][2 * (pos-1)] = symbol;
		}
		else if (pos == 4 || pos ==5 || pos ==6)
		{
			gameBoard[2][2 * pos - 8] = symbol;
		}
		else if (pos == 7 || pos ==8 || pos ==9)
		{
			gameBoard[4][2 * pos -14] = symbol;
		}
				
	}
	public static int winningGameValue()
	{
		
		
			if (playerPositions.containsAll(Arrays.asList(1,2,3)) ||  playerPositions.containsAll(Arrays.asList(4,5,6))  || 
					playerPositions.containsAll(Arrays.asList(7,8,9)) ||  playerPositions.containsAll(Arrays.asList(1,4,7))  ||
					playerPositions.containsAll(Arrays.asList(2,5,8)) ||  playerPositions.containsAll(Arrays.asList(3,6,9))  ||
					playerPositions.containsAll(Arrays.asList(1,5,9)) ||  playerPositions.containsAll(Arrays.asList(3,5,7)))
				{
										return -1;
				}
		
		
		
			else if (cpuPositions.containsAll(Arrays.asList(1,2,3)) ||  cpuPositions.containsAll(Arrays.asList(4,5,6))  || 
					cpuPositions.containsAll(Arrays.asList(7,8,9)) ||  cpuPositions.containsAll(Arrays.asList(1,4,7))  ||
					cpuPositions.containsAll(Arrays.asList(2,5,8)) ||  cpuPositions.containsAll(Arrays.asList(3,6,9))  ||
					cpuPositions.containsAll(Arrays.asList(1,5,9)) ||  cpuPositions.containsAll(Arrays.asList(3,5,7)))
				{
					
					return 1;
				}
		
			else if (playerPositions.size() + cpuPositions.size() == 9)
		
		         return 0;
			
		
		return 5;
	}
	
	
	

}