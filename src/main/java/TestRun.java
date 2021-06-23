import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestRun {

    public static void main(String[] args) throws IOException, InterruptedException {
        TerminalSize ts = new TerminalSize(100, 30);
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        terminalFactory.setInitialTerminalSize(ts);
        Terminal terminal = terminalFactory.createTerminal();
        terminal.setCursorVisible(false);

        int x = terminal.getTerminalSize().getColumns() / 2, y = 10;

        Player player = new Player(48, 28);
        player.draw(terminal);

        Random rand = new Random();
        Ball ball = new Ball(x, y, rand.nextBoolean() ? -1 : 1, -1);
        ball.draw(terminal);

        terminal.flush();

        boolean isRunning = true;
        KeyStroke keyStroke;

        int ballPauseCount = 0;
        List<Integer> playerJustMoved = new ArrayList<>();

        // Create bricks
        List<Brick> bricks = new ArrayList<>();
        bricks.add(new Brick(10, 20, 80, 1));
        /*bricks.add(new Brick(20, 10, 5, 1));
        bricks.add(new Brick(30, 10, 5, 1));
        bricks.add(new Brick(40, 10, 5, 1));
        bricks.add(new Brick(50, 10, 5, 1));
        bricks.add(new Brick(60, 10, 5, 1));
        bricks.add(new Brick(70, 10, 5, 1));
        bricks.add(new Brick(80, 10, 5, 1));
        bricks.add(new Brick(90, 10, 5, 1));*/
        // Draw bricks
        for (Brick b : bricks) {
            b.drawBrick(terminal);
        }

        while (isRunning) {
            Thread.sleep(1);
            keyStroke = terminal.pollInput();
            playerJustMoved.add(0);


            if (keyStroke != null) {
                switch (keyStroke.getKeyType()) {
                    case ArrowRight:
                        player.setNewPositionPlayer(terminal, 2);
                        player.draw(terminal);
                        playerJustMoved.add(1);
                        break;
                    case ArrowLeft:
                        player.setNewPositionPlayer(terminal,-2);
                        player.draw(terminal);
                        playerJustMoved.add(-1);
                        break;
                    case Escape:
                        isRunning = false;
                        break;
                }
            }

            // Move ball
            if (ballPauseCount >= 20) {
                // Check if brick hit
                Brick brickHit = brickHit(bricks, ball);
                if (brickHit != null) {
                    if (brickHit.invertX(ball.x, ball.y)) {
                        ball.xAccel *= -1;
                    }
                    if (brickHit.invertY(ball.x, ball.y)) {
                        ball.yAccel *= -1;
                    }
                }

                ball.setNewPosition(terminal, player, playerJustMoved);
                ball.draw(terminal);
                ballPauseCount = 0;
            }

            // Check for Game Over
            if (ball.y >= terminal.getTerminalSize().getRows()) {
                String gameOver = "Game Over";
                terminal.setForegroundColor(TextColor.ANSI.RED);
                for (int i = 0; i < gameOver.length(); i++) {
                    terminal.setCursorPosition(46 + i, 15);
                    terminal.putCharacter(gameOver.charAt(i));
                }
                terminal.flush();
                Thread.sleep(4000);
                break;
            }

            terminal.flush();

            ballPauseCount++;
            playerJustMoved.remove(0);
        }

        System.out.println("Quit");
        terminal.close();
    }

    public static Brick brickHit(List<Brick> bricks, Ball ball) {
        for (Brick b : bricks) {
            if (ball.y <= b.startY + b.heigth && ball.y >= b.startY - 1) {
                if (ball.x >= b.startX && ball.x <= b.startX + b.width) {
                    return b;
                }
            }
        }

        return null;
    }
}