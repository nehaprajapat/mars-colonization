import java.util.ArrayList;

public class Board {
	ArrayList<String> board = new ArrayList<String>();
	Board(){
		for(int i=0;i<9;i++) {board.add(" ");}
	}
	
	boolean isEmpty() {
		boolean ans = true;
		for(int i=0;i<9;i++) {if(!board.get(i).equals(" ")) {ans=false;}}
		return ans;
	}
	boolean isFull() {
		boolean ans = true;
		for(int i=0;i<9;i++) {if(board.get(i).equals(" ")) {ans=false;}}
		return ans;
	}
	boolean insert(String sym,int position) {
		if(position > 8 || !board.get(position).equals(" ")) {return false;}
		board.set(position, sym);
		return true;
	}
	ArrayList<Integer> getAvailableMoves(){
		ArrayList<Integer> moves = new ArrayList<Integer>();
		for(int i=0;i<9;i++) {
			if(board.get(i).equals(" ")) {moves.add(i);}
		}return moves;
	}
	
	Result isEnd() {
		Result ans = new Result("not end",-1,' ');
		if(this.isEmpty()) return ans;
		if(this.isFull()) {ans.res="draw";return ans;}
		
		if(!board.get(0).equals(" ") && board.get(0).equals(board.get(1)) && board.get(0).equals(board.get(2))) {
			ans.dir='H';ans.row=1;ans.res=board.get(0);
            return ans;
        }
		if(!board.get(3).equals(" ") && board.get(3).equals(board.get(4)) && board.get(3).equals(board.get(5))) {
			ans.dir='H';ans.row=2;ans.res=board.get(3);
            return ans;
        }if(!board.get(6).equals(" ") && board.get(6).equals(board.get(7)) && board.get(6).equals(board.get(8))) {
			ans.dir='H';ans.row=3;ans.res=board.get(6);
            return ans;
        }
        
        if(!board.get(0).equals(" ") && board.get(0).equals(board.get(3)) && board.get(0).equals(board.get(6))) {
			ans.dir='V';ans.row=1;ans.res=board.get(0);
            return ans;
        }
		if(!board.get(1).equals(" ") && board.get(1).equals(board.get(4)) && board.get(1).equals(board.get(7))) {
			ans.dir='V';ans.row=2;ans.res=board.get(1);
            return ans;
        }if(!board.get(2).equals(" ") && board.get(2).equals(board.get(5)) && board.get(2).equals(board.get(8))) {
			ans.dir='V';ans.row=3;ans.res=board.get(2);
            return ans;
        }
        
        if(!board.get(0).equals(" ") && board.get(0).equals(board.get(4)) && board.get(0).equals(board.get(8))) {
			ans.dir='D';ans.row=1;ans.res=board.get(0);
            return ans;
        }
		if(!board.get(2).equals(" ") && board.get(2).equals(board.get(4)) && board.get(2).equals(board.get(6))) {
			ans.dir='D';ans.row=2;ans.res=board.get(2);
            return ans;
        }
		return ans;
		
	}
	
	void printBoard() {
		String toprint="";
		for(int i=0;i<9;i++) {
			toprint+=board.get(i)+"|";
		}
	}
	

}
