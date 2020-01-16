public class Cell
{    
    public int neighbors = -1;
    public boolean flagged = false;
    public boolean revealed = false;
    public boolean isBomb = false;
    
    public int x;
    public int y;
    
    //handles edge cases.
    private int offX = -1;
    private int limX = 1;
    private int offY = -1;
    private int limY = 1;
    
    public Cell(int col, int row)
    {
        x = col;
        y = row;
        if (col == 0) offX = 0;
        if (col == Main.cols-1) limX = 0;
        if (row == 0) offY = 0;
        if (row == Main.rows-1) limY = 0;
    }
    
    public void Neighbors()
    {
        if (isBomb) return;
        int sum = 0;
        for (int i = offX; i <= limX; i++)
        {
            for (int j = offY; j <= limY; j++)
            {
                if (!(i == 0 && j == 0))
                {
                    if (Main.board[x+i][y+j].isBomb) sum++;
                }
            }
        }

        neighbors = sum;
    }
    
    public void Edges()
    {
        if (isBomb) return;
        int sum = 0;
        for (int i = offX; i <= limX; i++)
        {
            for (int j = offY; j <= limY; j++)
            {
                if (i==0 || j==0)
                {
                    if (Main.board[x+i][y+j].isBomb) sum++;
                }
            }
        }
        neighbors = sum;
    }
    
    public void Corners()
    {
        if (isBomb) return;
        int sum = 0; 
        for (int i = offX; i<= limX; i++)
        {
            for (int j = offY; j <= limY; j++)
            {
                if (!(i==0 || j==0))
                {
                    if (Main.board[x+i][y+j].isBomb) sum++;
                }
            }
        }
        neighbors = sum;
    }
    
    public void Knights()
    {
        if (isBomb) return;
        int sum = 0; 
        for (int i = -1; i <= 1; i+=2)
        {
            for (int j = -2; j <= 2; j+=4)
            {
                if (x+i >= 0 && x+i < Main.cols && y+j >= 0 && y+j < Main.rows)
                {
                    if (Main.board[x+i][y+j].isBomb) sum++;
                }
            }
        }
        for (int j = -1; j <= 1; j+=2)
        {
            for (int i = -2; i <= 2; i+=4)
            {
                if (x+i >= 0 && x+i < Main.cols && y+j >= 0 && y+j < Main.rows)
                {
                    if (Main.board[x+i][y+j].isBomb) sum++;
                }
            }
        }
        neighbors = sum;
    }
    
    
    public void Reveal()
    {        
        if (flagged || revealed) return;
        if (isBomb) 
        {
            Main.Gameover();
            return;
        }
        revealed = true;
        Main.numRevealed++;
        if (Main.numRevealed == (Main.cols*Main.rows)-Main.bombs)
        {
            Main.Win();
        }
        //change for each gamemod knights: reveal all knights moves, corners reveals corners
        if (neighbors == 0)
        {
            switch(Main.gm)
            {
                case NORMAL:
                    for (int i = offX; i <= limX; i++)
                    {
                        for (int j = offY; j <= limY; j++)
                        {          
                           Cell cell = Main.board[x+i][y+j];
                           if (!cell.revealed && !cell.isBomb) cell.Reveal();
                        }
                    }
                    break;
                case CORNERS:
                    for (int i = offX; i<= limX; i++)
                    {
                        for (int j = offY; j <= limY; j++)
                        {
                            if (!(i==0 || j==0))
                            {
                                Cell cell = Main.board[x+i][y+j];
                                if (!cell.revealed && !cell.isBomb) cell.Reveal();
                            }
                        }
                    }
                    break;
                case EDGES:
                    for (int i = offX; i<= limX; i++)
                    {
                        for (int j = offY; j <= limY; j++)
                        {
                            if (i==0 || j==0)
                            {
                                Cell cell = Main.board[x+i][y+j];
                                if (!cell.revealed && !cell.isBomb) cell.Reveal();
                            }
                        }
                    }
                    break;
                    
                case KNIGHTS:
                    for (int i = -1; i <= 1; i+=2)
                        {
                            for (int j = -2; j <= 2; j+=4)
                            {
                                if (x+i >= 0 && x+i < Main.cols && y+j >= 0 && y+j < Main.rows)
                                {
                                    Cell cell = Main.board[x+i][y+j];
                                    if (!cell.revealed && !cell.isBomb) cell.Reveal();
                                }
                            }
                        }
                        for (int j = -1; j <= 1; j+=2)
                        {
                            for (int i = -2; i <= 2; i+=4)
                            {
                                if (x+i >= 0 && x+i < Main.cols && y+j >= 0 && y+j < Main.rows)
                                {
                                    Cell cell = Main.board[x+i][y+j];
                                    if (!cell.revealed && !cell.isBomb) cell.Reveal();
                                }
                        }
                    }
                   
            }
            
        }
    }
    
    public void Flag()
    {
        if (revealed) return;
        if (!flagged)
        {
            flagged = true;
            Main.numFlagged++;
        }
        else 
        {
            flagged = false;
            Main.numFlagged--;
        }
        if (Main.numFlagged == Main.bombs)
        {
            if (Main.CheckWin())
            {
                Main.Win();
            }
        }
    }
}