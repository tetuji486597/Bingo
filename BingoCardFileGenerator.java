package bingo;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class BingoCardFileGenerator implements Runnable{
	private final String filePath;
	private final BingoCard[] cards;
	private BufferedImage cardImage;
	private final int cardWidth;
	private final int cardHeight;
	private int currentCard;
	public BingoCardFileGenerator(String filePath, BingoCard[] cards) {
		this.filePath = filePath + "\\Cards";
		this.cards = cards;
		cardWidth = 1265;
		cardHeight = 1640;
		currentCard = 0;
		try {
			cardImage = ImageIO.read(BingoCardFileGenerator.class.getResource("/images/card.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		createFolder();
		for(int i = 0; i < cards.length/4; i++) {
			printCard(currentCard+1);
		}
		if(cards.length%4 > 0) {
			printCard(currentCard+1, cards.length%4);
		}
	}
	private void printCard(int startingID) {
		printCard(startingID, 4);
	}
	private void printCard(int startingID, int amount) {
		BufferedImage image = new BufferedImage(2550, 3330, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g.setColor(Color.BLACK);
		g.drawImage(cardImage, 0, 0, cardWidth, cardHeight, null);
		if(amount >=2) {
			g.drawImage(cardImage, cardWidth+20, 0, cardWidth, cardHeight, null);
		}
		if(amount >= 3) {
			g.drawImage(cardImage, 0, cardHeight+20, cardWidth, cardHeight, null);
		}
		if(amount == 4) {
			g.drawImage(cardImage, cardWidth+20, cardHeight+20, cardWidth, cardHeight, null);
		}
		String startID = Integer.toString(startingID);
		String endID = Integer.toString((startingID+amount)-1);
		fillCards(g, amount);
		String fileName = "card"+startID+"-to-"+endID;
		setCardsFilePath(Integer.parseInt(startID), Integer.parseInt(endID), fileName);
		createFile(fileName, image);
	}
	private void fillCards(Graphics g, int amount) {
		int width = 234;
		int height = 256;
		Font font = new Font("TimesRoman", Font.BOLD, 150);
		for(int quadrant = 0; quadrant < amount; quadrant++) {
			int x = 0;
			int y = 0;
			int baseHeight;
			BingoCard bingoCard = cards[currentCard];
			int[][] card = bingoCard.getCard();
			int id = bingoCard.getID();
			drawID(g, id, quadrant);
			switch (quadrant) {
            case 0: // top left
                x = 30;
                y = 368;
                break;
            case 1: // top right
                x = cardWidth + 50;
                y = 368;
                break;
            case 2: // bottom left
                x = 30;
                y = cardHeight + 388;
                break;
            case 3: // bottom right
                x = cardWidth + 50;
                y = cardHeight + 388;
                break;
        }
        baseHeight = y;
        for (int col = 0; col < card[0].length; col++) {
            // accounts for each bingo box having a different width
            switch (col) {
                case 0:
                    width = 234;
                    break;
                case 1: case 3:
                    width = 238;
                    break;
                case 2:
                    width = 233;
                    break;
                case 4:
                    width = 235;
                    break;
            }
            for (int row = 0; row < card.length; row++) {
                // accounts for each bingo box having a different height
                switch (row) {
                    case 0:
                        height = 210; //236;
                        break;
                    case 1:
                        height = 210; //244;
                        break;
                    case 2:
                        height = 210; //237;
                        break;
                    case 3:
                        height = 210; //228;
                        break;
                    case 4:
                        height = 210; //227;
                        break;
                }
                Rectangle rect = new Rectangle(x, y, width, height);
                if (card[row][col] != 0) {
                    drawCenteredString(g, Integer.toString(card[row][col]), rect, font);
                }
                y += rect.height + 18; // accounts for border and goes to next row
            }
            y = baseHeight;
            x += width + 14; // accounts for border and goes to next column
        }
        currentCard++;
		}
	}
	private void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
		FontMetrics metrics = g.getFontMetrics(font);
		int x = rect.x + (rect.width-metrics.stringWidth(text)) / 2;
		int y = rect.y + ((rect.height-metrics.getHeight()) / 2) + metrics.getAscent();
		g.setFont(font);
		g.drawString(text, x, y);
	}
	private void drawID(Graphics g, int id, int quadrant) {
		g.setFont(new Font("TimesRoman", Font.BOLD, 60));
		String formattedID = "#" + String.format("%06d", id);
		final int x = 75;
		final int y = 50;
		final int xWithBorder = cardWidth + x + 20;
		final int yWithBorder = cardHeight + y + 20;
		switch(quadrant) {
			case 0:
				g.drawString(formattedID, x, y);
				break;
			case 1:
				g.drawString(formattedID, xWithBorder, y);
				break;
			case 2:
				g.drawString(formattedID, x, yWithBorder);
				break;
			case 3:
				g.drawString(formattedID, xWithBorder, yWithBorder);
		}
	}
	private void createFolder() {
		File folder = new File(filePath);
		if(!folder.exists()) {
			if(folder.mkdir()) {
				System.out.println("Bingo Cards directory created in " + folder.getAbsolutePath());
			} else {
				System.out.println("Failed to create Bingo Cards directory");
			}
		} else {
			System.out.println("Bingo Cards directory already exists");
		}
	}
	private void createFile(String fileName, BufferedImage image) {
		try {
			String path = filePath + "/" + fileName + ".png";
			ImageIO.write(image, "png", new File(path));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	private void setCardsFilePath(int start, int end, String fileName) {
		for(int i = start-1; i <= end-1; i++) {
			cards[i].setFilePath(filePath + "\\" + fileName + ".png");
		}
	}
}