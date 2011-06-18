package life;

import java.awt.Color;
import life.Cell.State;

public class Model {

	private int rows;
	private int time;
	public static final int DEFAULT_ROWS = 30;
	public static final Color DEFAULT_COLOR = Color.GRAY;

	private Cell [] [] grid;
	private Cell [] [] tempGrid;

	public int getTime(){
		return time;
	}

	public int getRows(){
		return rows;
	}

	//Contructor when there is a value for a row
	public Model(int _rows) {
		rows = _rows;
		initialise();
	}

	public void initialise(){
		grid = new Cell [rows] [rows];
		tempGrid = new Cell [rows][rows];
		initialiseCells();
		time = 0;

		for (int i = 0; i< rows; i++){
			for (int j = 0; j <rows; j++){
				tempGrid[i][j] = new Cell();
			}
		}
	}

	//Constructor when there is no value for a row
	public Model(){
		rows = DEFAULT_ROWS;
		initialise();
	}


	public void initialiseCells(){
		for (int i =0; i<rows; i++){
			for (int j = 0; j< rows; j++){
				grid[i][j] = new Cell();
			}

		}
	}

	//Making the whole grid grey
	public void clearGrid(){
		for (int i =0; i<rows; i++){
			for (int j = 0; j< rows; j++){
				grid[i][j].setState(State.DEAD);
			}
		}
		time = 0;
	}

	public Color getColor(int x, int y){
		return toColor(grid[x][y].getState());
	}

	public Color toColor(State s){
		if (s == State.RED) 
			return Color.red;
		else if (s == State.GREEN)
			return Color.GREEN;
		else 
			return DEFAULT_COLOR;
	}

	public State toState(Color c){
		if (c == Color.green)
			return State.GREEN;
		else if (c == Color.red)
			return State.RED;
		else
			return State.DEAD;

	}

	public void setColor(int x, int y, Color c){
		grid[x][y].setState(toState(c));
	}

	public void step(){
		time++;


		for (int i = 0; i < rows; i++){
			for (int j = 0; j< rows; j++){
				int neighbours = 0;

				//Check all the neighbours;

				int greenCount = 0;
				int redCount = 0;

				for (int r = -1; r < 2; r++){
					for (int s = - 1; s < 2; s++){
						if (r==0 && s==0){
							//Do nothing for it
						}
						else {
							int x = r+i;
							int y = s+j;

							//Ensures that all successive memory accesses are valid
							if(x == -1)
								x = rows - 1;
							if (y == -1)
								y = rows -1;

							x = x % rows;
							y = y % rows;

							//The real work now happens!
							if (grid[x][y].getState() == State.RED){
								neighbours++;
								redCount++;
							}
							else if (grid[x][y].getState() == State.GREEN){
								neighbours++;
								greenCount++;
							}
							else {
								//Do not count anything
							}
						}
					}

				}

				if ( (neighbours == 2 || neighbours == 3) && grid[i][j].isAlive()){
					tempGrid[i][j].setState(grid[i][j].getState());
				}
				else if (neighbours == 3){
					if (greenCount > redCount){
						tempGrid[i][j].setState(State.GREEN); 
					}
					else {
						tempGrid[i][j].setState(State.RED);
					}
				}
				else {
					tempGrid[i][j].setState(State.DEAD);
				}
			}
		}
		for (int i = 0; i<rows; i++){
			for (int j = 0; j < rows; j++){
				grid[i][j].setState(tempGrid[i][j].getState());
			}
		}

	}


}
