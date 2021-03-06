import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class SimpleTicTacTocUsingRandomization {
	static ArrayList<Integer> playerPositions = new ArrayList<Integer>();
	static ArrayList<Integer> cpuPositions = new ArrayList<Integer>();
	public static void main(String args[])
	{
		char[][] gameBoard = {
		{' ', '|', ' ', '|', ' '},
		{'-', '+', '-', '+', '-'},
		{' ', '|', ' ', '|', ' '},
		{'-', '+', '-', '+', '-'},
		{' ', '|', ' ', '|', ' '}
		};
		while(true)
		{
			Scanner scan = new Scanner(System.in);
			System.out.println("Enter your postion from 1:9 - ");
			int playerPos = scan.nextInt();
			while (playerPositions.contains(playerPos) || cpuPositions.contains(playerPos))// better way is create a list and store all // the remaining positions in it and then choose random value from this list. 
			{
				System.out.println("Position is laready filled .Please enter again");
				playerPos = scan.nextInt();
			}
			placePiece(gameBoard, playerPos, "player");
			printgameBoard(gameBoard);
			System.out.println();
			if (playerPositions.containsAll(Arrays.asList(1,2,3)) ||  playerPositions.containsAll(Arrays.asList(4,5,6))  || 
				playerPositions.containsAll(Arrays.asList(7,8,9)) ||  playerPositions.containsAll(Arrays.asList(1,4,7))  ||
				playerPositions.containsAll(Arrays.asList(2,5,8)) ||  playerPositions.containsAll(Arrays.asList(3,6,9))  ||
				playerPositions.containsAll(Arrays.asList(1,5,9)) ||  playerPositions.containsAll(Arrays.asList(3,5,7)))
			{
				System.out.println("Congratulations!! You won the game !");
				break;
			}
			else if (playerPositions.size() + cpuPositions.size() == 9)
			{
				System.out.println("Its a tie");
				break;
			}
			
			
			Random rand = new Random();
			int cpuPos = rand.nextInt(9)+1;
			int i = 1 ;
			while (playerPositions.contains(cpuPos) || cpuPositions.contains(cpuPos))
			{
				System.out.println("Trying new value" + i++);
				cpuPos = rand.nextInt(9) + 1;
			}
			placePiece(gameBoard, cpuPos, "cpu");
			printgameBoard(gameBoard);
			if (cpuPositions.containsAll(Arrays.asList(1,2,3)) ||  cpuPositions.containsAll(Arrays.asList(4,5,6))  || 
					cpuPositions.containsAll(Arrays.asList(7,8,9)) ||  cpuPositions.containsAll(Arrays.asList(1,4,7))  ||
					cpuPositions.containsAll(Arrays.asList(2,5,8)) ||  cpuPositions.containsAll(Arrays.asList(3,6,9))  ||
					cpuPositions.containsAll(Arrays.asList(1,5,9)) ||  cpuPositions.containsAll(Arrays.asList(3,5,7)))
				{
					System.out.println("Cpu won !sorry :(");
					break;
				}
			else if (playerPositions.size() + cpuPositions.size() == 9)
			{
				System.out.println("Its a tie");
				break;
			}
			
		}
		
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
		switch(pos)
		{
			case 1 : gameBoard[0][0] = symbol ;break;
			case 2 : gameBoard[0][2] = symbol ;break;
			case 3 : gameBoard[0][4] = symbol ;break;
			case 4 : gameBoard[2][0] = symbol ;break;
			case 5 : gameBoard[2][2] = symbol ;break;
			case 6 : gameBoard[2][4] = symbol ;break;
			case 7 : gameBoard[4][0] = symbol ;break;
			case 8 : gameBoard[4][2] = symbol ;break;
			case 9 : gameBoard[4][4] = symbol ;break;
			default : break;
		}
				
	}
	
	

}
