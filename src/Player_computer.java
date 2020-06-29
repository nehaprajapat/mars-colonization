import java.util.HashMap;
import java.util.Map;

public class Player_computer {
	
	int max_depth;
	Map nodes_map;
	Player_computer(){
	this.max_depth=-1;
	this.nodes_map = new HashMap();
	}
   
   static int minimax(Board b, int depth, Boolean isMax) { 
		Result res = b.isEnd();
		if (res.res.equals("X")) 
			return 10; 
		if (res.res.equals("O")) 
			return -10; 
		if (b.isFull() == false) 
			return 0; 
		if (isMax) { 
			int best = -1000; 
			for (int i = 0; i < 9; i++) {  
				if (b.board.get(i).equals(" ")) {
                    b.board.set(i, "X"); 
                    best = Math.max(best, minimax(b,depth + 1, !isMax)); 
                    b.board.set(i, " ");
                } 
            } 
        return best; } 
   else { 
        int best = 1000; 
        for (int i = 0; i < 9; i++) { 
          if (b.board.get(i).equals(" ")) { 
            b.board.set(i, "O"); 
            best = Math.min(best, minimax(b, depth + 1, !isMax)); 
            b.board.set(i, " ");
            } 
        } 
   return best;} 
  } 
   
   static int findBestMove(Board b) 
   { 
       int bestVal = -1000; 
       int bestMove = -1; 
        
     
       // Traverse all cells, evaluate minimax function  
       // for all empty cells. And return the cell  
       // with optimal value. 
       for (int i = 0; i < 9; i++) 
       { 
               // Check if cell is empty 
               if (b.board.get(i).equals(" ")) 
               { 
                   // Make the move 
            	   b.board.set(i, "X"); 
     
                   // compute evaluation function for this 
                   // move. 
                   int moveVal = minimax(b, 0, false); 
     
                   // Undo the move 
                   b.board.set(i, " ");
     
                   // If the value of the current move is 
                   // more than the best value, then update 
                   // best/ 
                   if (moveVal > bestVal) 
                   {   bestMove=i; 
                       bestVal = moveVal; 
                   } 
               } 
            
       } return bestMove; 
   } 
}
