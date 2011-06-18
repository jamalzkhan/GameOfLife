package life;

public class Cell {
	
	public enum State{
		GREEN, RED, DEAD
	}
	
	private State state;
	
	public boolean isAlive(){
		if (state == State.RED || state == State.GREEN)
			return true;
		else
			return false;
	}
	
	public Cell(State s){
		state = s;
	}
	
	public Cell() {
		state = State.DEAD;
	}
	
	public State getState(){
		return state;
	}
	
	public void setState(State s){
		state = s;
	}
	

}
