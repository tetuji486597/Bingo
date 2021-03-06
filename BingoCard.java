package bingo;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class BingoCard {
	private final int id;
	private String filePath;
	private final int[][] card;
	private int[][] displayCard;
	private final HashMap<Integer, Coordinate> squares;
	private final HashSet<Coordinate> chosenSquares;
	private boolean isWinner;
	private int roundWin;
	private int amountToWin;
	private String winDay;
	private String winType;
	
	public BingoCard(int id) {
		this.id = id;
		this.filePath = null;
		card = new int[5][5];
		squares = new HashMap<>();
		chosenSquares = new HashSet<>();
		isWinner = false;
		roundWin = -1;
		amountToWin = -1;
		winDay = null;
		winType = null;
		
		fillCard();
	}
	
	public void fillCard() {
		int min = 1;
		int max = 16;
		
		HashSet<Integer> selectedNums = new HashSet<>();
		
		for(int col = 0; col < card[0].length; col++) {
			for(int row = 0; row < card.length; row++) {
				int num = RandomNumber.nextNumber(max-min)+min;
				
				while(selectedNums.contains(num)) {
					num = RandomNumber.nextNumber(max-min)+min;
				}
				if(row == 2 && col == 2) {
					num = 0;
				}
				
				card[row][col] = num;
				squares.put(num, new Coordinate(row, col));
				selectedNums.add(num);
			}
			min+=15;
			max+=15;
		}
		Coordinate freeSpace = new Coordinate(2,2);
		chosenSquares.add(freeSpace);
		displayCard = Arrays.stream(card).map(int[]::clone).toArray(int[][]::new);
	}
	
	public boolean chooseNumber(int number) {
		if(squares.containsKey(number)) {
			Coordinate coordinate = squares.get(number);
			card[coordinate.getRow()][coordinate.getColumn()] = 0;
			chosenSquares.add(coordinate);
			return true;
		}
		return false;
	}
	public boolean checkWin() {
		boolean win;
		for(int[] row: card) {
			win = true;
			for(int num : row) {
				if(num != 0) {
					win = false;
					break;
				}
			}
			if(win) {
				winType = "Horizontal";
				isWinner = true;
				return true;
			}
		}
		for(int col = 0; col < card[0].length; col++) {
			win = true;
			for(int[] row: card) {
				if(row[col] != 0) {
					win = false;
					break;
				}
			}
			if(win) {
				winType = "Vertical";
				isWinner = true;
				return true;
			}
		}
		win = true;
		for(int i = 0; i < 5; i++) {
			if(card[i][i] != 0) {
				win = false;
				break;
			}
		}
		if(win) {
			winType = "Diagonal";
			isWinner = true;
			return true;
		}
		win = true;
		int y = 4;
		for(int x= 0; x<5; x++) {
			if(card[x][y] != 0) {
				win = false;
				break;
			}
			y--;
		}
		if(win) {
			winType = "Diagonal";
			isWinner = true;
		}
		return win;
	}
	public String getWinType() {
		return winType;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public void setAmountToWin(int amount) {
		amountToWin = amount;
	}
	public void setWinDay(String winDay) {
		this.winDay = winDay;
	}
	public void setRoundWin(int round) {
		roundWin = round;
	}
	public int getRoundWin() {
		return roundWin;
	}
	public String getFilePath() {
        return this.filePath;
    }

    public int getID() {
        return id;
    }

    public int[][] getCard() {
        return displayCard;
    }

    public int getAmountToWin() {
        return amountToWin;
    }

    public String getWinDay() {
        return winDay;
    }

    public HashSet<Coordinate> getChosenSquares() {
        return chosenSquares;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public String toString() {
        return "\n==========================\n" + "Card ID: " + id + "\n\n" + Arrays.deepToString(card).replace("], ", "]\n") + "\n==========================\n";
    }
}
