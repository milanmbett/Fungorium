package com.coderunnerlovagjai.app;
import java.awt.Graphics2D;
import java.awt.Graphics;
import javax.swing.JPanel;


/* 
public class GamePanel extends JPanel 
{
    private final Game game;
    private final Graphics_Game gfx;

    public GamePanel(Game game) {
        this.game = game;
        this.gfx = new Graphics_Game(game);
        // ha Game eseményeket is figyelni akarunk:
        this.game.addListener(gfx::onGameEvent);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g2 = (Graphics2D) g0;
        gfx.render(g2);
    }
}
*/