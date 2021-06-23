import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        //TerminalSize ts = new TerminalSize(400, 420);
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        //terminalFactory.setInitialTerminalSize(ts);
        Terminal terminal = terminalFactory.createTerminal();
        terminal.setCursorVisible(false);
        TextGraphics tGraphics = terminal.newTextGraphics();

        List<Brick> brickList = makeBricks((terminal.getTerminalSize().getColumns() / 2), 6, 15);;

        boolean invertX;
        boolean invertY;

        int x = terminal.getTerminalSize().getColumns() / 2, y = 20;

        Ball ball = new Ball(x, y, 1, -1);
        ball.draw(terminal);

        boolean isRunning = true;
        KeyStroke keyStroke;

        while (isRunning) {
            Thread.sleep(100);
            drawBrick(terminal, brickList);
            keyStroke = terminal.pollInput();

            if (keyStroke != null) {
                if (keyStroke.getKeyType() == KeyType.Escape) {
                    isRunning = false;
                }
            }

            // Move ball

            ball.setNewPosition(terminal, invertX, invertY);
            ball.draw(terminal);

            terminal.flush();
        }

        System.out.println("Quit");
        terminal.close();

    }

    private static List<Brick> makeBricks(int midPointX, int blocksY, int blocksX) {
        List<Brick> bricks = new ArrayList<>();
        int brickWidth = 4;
        int brickHeight = 1;
        for (int i = 2; i <= blocksY*brickHeight + 2; i+=brickHeight + 1) {
            for (int j = (midPointX - (brickWidth * (blocksX/2)));
                 j <= (midPointX + (brickWidth * (blocksX/2))); j+=brickWidth + 1) {
                bricks.add(new Brick(j, i, brickWidth, brickHeight));
            }
        }
        return bricks;
    }

    private static void drawBrick (Terminal terminal, List<Brick> brickPos) throws IOException {

        for (Brick b : brickPos) {
            b.
        }
    }


}