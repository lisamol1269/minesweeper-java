import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;
public class GUI extends JFrame
{
    public static boolean menu = false;
    
    public static boolean firstClick = false;
    
    public static final int screenWidth = 778;
    public static final int screenHeight = 785;
    
    public static final int topBar = 70;
   
    public static int spacing = 2;
    
    public int mx = -100;
    public int my = -100;
    
    public GUI()
    {
       // set up window settings
       this.setTitle("Minesweeper");
       this.setSize(screenWidth, screenHeight);// bar at the top is 31 pixels 8 on each border
       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       this.setVisible(true); // visible to the user
       this.setResizable(false);
       
       Canvas canvas = new Canvas();
       canvas.setBackground(Color.DARK_GRAY);
       this.setContentPane(canvas);
       
       Move move = new Move();
       this.addMouseMotionListener(move);
       
       Click click = new Click();
       this.addMouseListener(click);
       
       Keys key = new Keys();
       this.addKeyListener(key);
       
    } // end constructor
    
    //canvas (board)
    public class Canvas extends JPanel
    {
        JButton quitButton = new JButton("Quit Game");
                
        Color[] colors = { null, Color.blue, Color.green, Color.orange, 
                            Color.pink, Color.red, Color.yellow, 
                            Color.black, Color.black };
        // controls the graphics of the window
        public void paintComponent(Graphics g)
        {
            // add gameover panel with reset button and high score shit
            super.paintComponent(g);
            Cell[][] grid = Main.board;
            
            Font font = new Font("Verdana", Font.BOLD, 20);
            g.setFont(font);
            
            //timer
            int minutes = Main.gameSeconds/60;
            int seconds = Main.gameSeconds%60;
            g.setColor(Color.white);
            g.fillRect(600,10,145,50);
            String time = (minutes < 10 ? "0" : "") + Integer.toString(minutes) + ":" + 
                          (seconds < 10 ? "0" : "") + Integer.toString(seconds);
            g.setColor(Color.black);
            g.drawString(time, 670, 40);
            
            //bomb counter
            g.setColor(Color.white);
            g.fillRect(150,10,145,50);
            String count = Integer.toString(Main.numFlagged) + "/" + Integer.toString(Main.bombs);
            g.setColor(Color.black);
            g.drawString(count, 180, 40);

            for (int i = 0; i < grid.length; i++)
            {
                for (int j = 0; j < grid[0].length; j++)
                {
                    Cell cell = grid[i][j];
                    int posx = i*Main.w+spacing+3;
                    int posy = j*Main.w+spacing+topBar;                    
                    if (cell.isBomb && cell.revealed)
                    {
                        g.setColor(Color.red);
                        g.fillRect(posx, posy, Main.w-(spacing<<1), Main.w-(spacing<<1));
                    }
                    else if (!cell.isBomb && cell.revealed)
                    {
                        g.setColor(Color.lightGray);
                        g.fillRect(posx, posy, Main.w-(spacing<<1), Main.w-(spacing<<1));                        
                        g.setColor(colors[cell.neighbors]);
                        if (cell.neighbors > 0) {
                            g.drawString(Integer.toString(cell.neighbors), posx+(Main.w>>1), posy+(Main.w>>1));
                        }
                    }
                    else if (!cell.revealed)
                    {
                        g.setColor(Color.gray);
                        g.fillRect(posx, posy, Main.w-(spacing<<1), Main.w-(spacing<<1));
                    }
                    if (cell.flagged && !cell.revealed)
                    {
                        g.setColor(Color.yellow);
                        g.fillRect(posx, posy, Main.w-(spacing<<1), Main.w-(spacing<<1));
                    }                   
                }
            }
            //pause menu
            if (menu)
            {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(0,0,screenWidth,screenHeight);
                g.setColor(Color.red);
                g.drawString("Paused", (screenWidth >> 1) - 30, screenHeight >> 3);                
            }
        }
        
    }    
    // handle all events
    public class Move implements MouseMotionListener
    {
        @Override
        public void mouseDragged(MouseEvent e)
        {
            
        }
        
        @Override
        public void mouseMoved(MouseEvent e)
        {
            //System.out.println(e);
            mx = e.getX();
            my = e.getY();
        }
    }
    public class Click implements MouseListener
    {
        @Override 
        public void mouseClicked(MouseEvent e)
        {
            
        }
        
        @Override 
        public void mouseEntered(MouseEvent e)
        {
            
        }
        
        @Override
        public void mouseExited(MouseEvent e)
        {
            
        }
        
        @Override 
        public void mousePressed(MouseEvent e)
        {
            if (menu) return;
            int x = (int)Math.floor((mx-9-spacing)/Main.w);
            int y = (int)Math.floor((my-topBar-spacing-29)/Main.w);
            //right click
            if (e.getModifiers() == MouseEvent.BUTTON3_MASK)
            {
                Main.board[x][y].Flag();
            }
            //left click
            if(e.getButton() == MouseEvent.BUTTON1) 
            {
                if (!firstClick)
                {
                    Main.generateBombs(Main.board[x][y]);
                    firstClick = true;
                    Main.CheckNeighbors(Main.gm);
                }
                Main.board[x][y].Reveal();
                //System.out.println("x: " + x + ", y: " + y + " " + Main.rows);
            }
        }
        
        @Override
        public void mouseReleased(MouseEvent e)
        {
            
        }
    }  
    public class Keys implements KeyListener
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            switch(e.getKeyCode())
            {
                case 27:
                    if (!menu) menu = true;
                    else menu = false;
                    break;
            }
        }
        @Override
        public void keyReleased(KeyEvent e)
        {
            
        }
        @Override
        public void keyTyped(KeyEvent e)
        {
            
        }
    }
} // end class GUI