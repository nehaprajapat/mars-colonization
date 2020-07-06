import java.util.ArrayList;
import java.util.Scanner;

public class TicTacToeUsingMagicSquare 
{
	static ArrayList<Integer> playerPositions = new ArrayList<Integer>();
	static ArrayList<Integer> cpuPositions = new ArrayList<Integer>();
	static int[][] magicSquare = 
		{
				{2 , 7 , 6} ,  // 1  2  3   // 7241
				{9 , 5 , 1} ,  // 4  5  6
				{4 , 3 , 8}    // 7  8  9
		};
	
	public static void main(String args[])
	{
		char[][] gameBoard = 
		{
				{' ' , '|' , ' ', '|', ' '},
				{'-' , '+' , '-', '+', '-'},
				{' ' , '|' , ' ', '|', ' '},
				{'-' , '+' , '-', '+', '-'},
				{' ' , '|' , ' ', '|', ' '}
				
		};
		Scanner sc = new Scanner(System.in);
		System.out.println("If you want to play first enter 0 otherwise enter 1");
		int input = sc.nextInt();
		while (true) {
		if (input ==0 )
		{
			game1(gameBoard);
			break;
		}
		else if (input == 1)
		{
			game2(gameBoard);
			break;
		}
		else
		{
			System.out.println("Enter again");
			input = sc.nextInt();
		}
		}
	}
	public static void game1(char[][] gameBoard)
	{
		while(true)
		{
			userplaying(gameBoard);
			if (userwinning())
			{
				System.out.println("Congratulations ! You won the game");
				break;
			}
			else if (playerPositions.size() + cpuPositions.size() == 9)
			{
				System.out.println("Its a tie");
				break;
			}
			cpuplaying(gameBoard);
			printgameBoard(gameBoard);
			if (cpuwinning())
			{
				System.out.println("Cpu won. Sorry :(");
				break;
			}
			else if (playerPositions.size() + cpuPositions.size() == 9)
			{
				System.out.println("Its a tie");
				break;
			}
		}
	}
	
	public static void game2(char[][] gameBoard)
	{
		while(true)
		{
			cpuplaying(gameBoard);
			printgameBoard(gameBoard);
			if (cpuwinning())
			{
				System.out.println("Cpu won. Sorry :(");
				break;
			}
			else if (playerPositions.size() + cpuPositions.size() == 9)
			{
				System.out.println("Its a tie");
				break;
			}
			userplaying(gameBoard);
			if (userwinning())
			{
				System.out.println("Congratulations ! You won the game");
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
		for (char[] chararray : gameBoard)
		{
			for (char c : chararray)
			{
				System.out.print(c);
			}
			System.out.println();
		}
	}
	public static void userplaying(char[][] gameBoard)
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter your position from 1 to 9 : ");
		int pos = sc.nextInt();
		placepositions(gameBoard , pos, "player");
		printgameBoard(gameBoard);
		System.out.println();
		
		
	}
	
	public static void cpuplaying(char[][] gameBoard)
	{
		if (cpuPositions.size() >= 2) 
		{
			for (int i = 0 ; i <= cpuPositions.size()-2 ; i++)
			{
				int number = 15 - (cpuPositions.get(i) + cpuPositions.get(cpuPositions.size()-1));
				if (number >=1 && number<=9 && !(playerPositions.contains(number) || cpuPositions.contains(number)))
				{
					System.out.println("First if");
					placepositions(gameBoard , getPositionFromMagicSquare(number) , "cpu"); // cpuPositions = 5  6  9     7,2,4,1
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
					placepositions(gameBoard , getPositionFromMagicSquare(number) , "cpu");
					return ;
				}
			}
		}
		
		if (gameBoard[2][2] == ' ')
		{
			System.out.println("Third if");
			placepositions(gameBoard , 5 , "cpu");
			return;
		}
		if (playerPositions.size() == 2 && gameBoard[2][2] == 'X' && (gameBoard[0][0] == 'X' || gameBoard[0][2] == 'X' || gameBoard[2][0] == 'X' 
				|| gameBoard[2][2] == 'X'))
		{
			if(fillCorner(gameBoard))
			{
				System.out.println("Fourth if");
				return;
			}
		}
		int[] corners = {2,4,6,8};
		if (playerPositions.size() == 2 && (counter(corners) == 2))
		{
			if (fillemptysides(gameBoard))
			{
				System.out.println("Fifth if");
				return;
			}
		}
		int[] sides = {1 , 3 , 5 , 7};
		if (playerPositions.size() == 2 && counter(sides) == 2)
		{
			int q = cornersforsides();
			if (q != -1)
			{
				if(!(cpuPositions.contains(q)))
				{
					System.out.println("Sixth if");
					placepositions(gameBoard , getPositionFromMagicSquare(q) , "cpu");
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
				placepositions(gameBoard , getPositionFromMagicSquare(result) , "cpu");
				return;
			}
		}
		if (fillCorner(gameBoard)) {
			System.out.println("Eighth if");
			return;}
		if(fillemptysides(gameBoard))
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
	public static int cornersforsides()
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
	public static boolean fillemptysides(char[][] gameBoard)
	{
		int[] arr1 = {0 , 2 , 2 , 4};
		int[] arr2 = {2 , 0 , 4 , 2};
		for (int i = 0, j = 0 ; i < arr1.length && j < arr2.length ; i++,j++ )
		{
			if (gameBoard[arr1[i]][arr2[j]] == ' ')
			{
				placepositions(gameBoard , (arr1[i] + arr2[j])/2 + arr1[i] + 1 , "cpu");
				return true;
			}
		}
		return false;
	}
	
	public static int counter(int[] arr)
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
	
	public static boolean fillCorner(char[][] gameBoard)
	{
		int[] arr1 = {0 , 0 , 4 ,4};
		int[] arr2 = {0 ,4 , 0 , 4};
		for (int i = 0, j = 0 ; i < arr1.length && j < arr2.length ; i++,j++ )
		{
			if (gameBoard[arr1[i]][arr2[j]] == ' ')
			{
				placepositions(gameBoard , (arr1[i] + arr2[j])/2 + arr1[i] + 1 , "cpu");
				return true;
			}
		}
		return false;
	}
	
	public static void placepositions(char[][] gameBoard , int pos , String user)
	{
		char symbol = 'X';
		if (user.equals("player"))
		{
			symbol = 'X';
			playerPositions.add(magicSquare[(pos-1)/3][(pos-1) % 3]);
		}
		else if (user.equals("cpu"))
		{
			symbol = '0';
			cpuPositions.add(magicSquare[(pos-1)/3][(pos-1) % 3]);
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
	public static boolean userwinning()
	{
		if (playerPositions.size() <= 2)
		{
			return false;
		}
		else
		{
			for (int i = 0 ; i <=  playerPositions.size()-3 ; i++)
			{
				for (int j = i+1 ; j <= playerPositions.size()-2 ; j++)
				{
					if (playerPositions.get(i) + playerPositions.get(j) + playerPositions.get(playerPositions.size()-1) == 15)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	public static boolean cpuwinning()
	{
		if (cpuPositions.size() <= 2)
		{
			return false;
		}
		else
		{
			for (int i = 0 ; i <=  cpuPositions.size()-3 ; i++)
			{
				for (int j = i+1 ; j <= cpuPositions.size()-2 ; j++)
				{
					if (cpuPositions.get(i) + cpuPositions.get(j) + cpuPositions.get(cpuPositions.size()-1) == 15)
					{
						return true;
					}
				}
			}
		}
		return false;
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
	

}
