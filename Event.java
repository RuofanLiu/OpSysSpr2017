package project2;

public class Event {
	private String ID;
	private int time;
	private int status;			//status state whether a process is on entrance or exit, true for entrance, and false for exit;
	private int frames;
	private boolean skipped;
	
	public Event(String ID, int time, int status, int frames){
        this.ID = ID;
        this.time = time;
        this.status = status;
        this.frames = frames;
        skipped = false;
	}
	
	public String getID(){
		return ID;
	}

	public int getTime(){
		return time;
	}
	
	public int getStatus(){
		return status;
	}
	
	public int getFrames(){
		return frames;
	}
	
	public boolean getSkipped(){
		return skipped;
	}
	
	public void setSkipped(boolean newSkipped){
		skipped = newSkipped;
	}
}
