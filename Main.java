// application cpu shit
//add dps desicions per second
//add high score recording with txt file
//start timer with first click
//smart bomb picking
//add different game modes and difficulties
import java.util.*;
//add bomb counter 
// make bombs with fixed amount
enum Gamemode
{
   NORMAL, CORNERS, EDGES, KNIGHTS
}
public class Main implements Runnable
{
    GUI gui = new GUI();
    
    public static int cols = 18;
    public static int w = (int)((800-(cols*GUI.spacing))/cols);    
    public static int rows = (int)((GUI.screenHeight-GUI.topBar)/w)-1;
    
    public static Cell[][] board;
    
    public static int bombs = 100;
    public static int numFlagged = 0;
    public static int numRevealed = 0;
    
    public static int gameSeconds = 0;
    private static Timer timer = new Timer();
    private static TimerTask task = new TimerTask() {
        public void run()
        {
            if (!GUI.menu && GUI.firstClick) gameSeconds++;          
        }
    };
    
    public static Gamemode gm = Gamemode.NORMAL;
    
    public static void Init()
    {                
        gameSeconds = 0;
        numRevealed = 0;
        numFlagged = 0;
        board = Generate();        
    }
    
    public static Cell[][] Generate()
    {
        Cell[][] cells = new Cell[cols][rows];
        for (int i = 0; i < cols; i++)
        {
            for (int j = 0; j < rows; j++)
            {
                cells[i][j] = new Cell(i, j);
            }
        }
        return cells;
    }
    
    public static void generateBombs(Cell avoid)
    {
        ArrayList<Integer> options = new ArrayList<Integer>();       
        for (int i = 0; i < cols*rows; i++)
        {            
            if (avoid.x != (int)(i / cols) && avoid.y != (int)(i % rows))
            {
               options.add(i); 
            } 
        }
        for (int i = 0; i < bombs; i++)
        {
            int index = (int) (Math.random()*options.size());
            int choiceX = options.get(index) / cols;
            int choiceY = options.get(index) % rows;
            board[choiceX][choiceY].isBomb = true;
            options.remove(index);    
        }
    }
    
    public static void CheckNeighbors(Gamemode gameMode)
    {
        for (int i = 0; i < cols; i++)
        {
            for (int j = 0; j < rows; j++)
            {
                Cell cell = board[i][j];
                switch(gameMode)
                {
                    case NORMAL:
                        cell.Neighbors();
                        break;
                    case CORNERS:
                        cell.Corners();
                        break;
                    case EDGES:
                        cell.Edges();
                        break;
                    case KNIGHTS:
                        cell.Knights();
                }
            }
        }
    }
    public static void Gameover()
    {
        System.out.println("You blew up");
        GUI.firstClick = false;
        Init();
    }
    public static boolean CheckWin()
    {
       for (int i = 0; i < board.length; i++)
       {
           for (int j = 0; j < board[0].length; j++)
           {
               Cell cell = board[i][j];
               if (!cell.isBomb && !cell.revealed) return false;
           }
       }
       return true; 
    }
    public static void Win()
    {
        System.out.println("YOU WIN, IT TOOK YOU " + gameSeconds + " SECONDS");
        GUI.firstClick = false;
        Init();
    }
    public static void main(String[] args)
    {
       // start a new game instance
       Init();
       timer.scheduleAtFixedRate(task, 0, 1000);       
       new Thread(new Main()).start();
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            //refresh the screen
            gui.repaint();
        }
    }
}
