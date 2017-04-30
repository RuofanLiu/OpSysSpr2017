package project2;

public class ContiguousProcess extends Process{

	private int startIndexFrame;
	private int endIndexFrame;
	
	/**
	 * @param pid: process id
	 * @param numberOfFrames: number of frames for the given process
	 * @effects initializes fields
	 */
	public ContiguousProcess(String pid, int numberOfFrames){
		super(pid, numberOfFrames);
		startIndexFrame = -1;
		endIndexFrame = -1;
	}
	
	/**
	 * @param p: process object
	 * @effects copies all fields from p
	 */
	public ContiguousProcess(Process p){
		super(p);
		startIndexFrame = -1;
		endIndexFrame = -1;
	}
	
	/**
	 * @param cp: contiguous process object
	 * @effects copies all fields from p
	 */
	public ContiguousProcess(ContiguousProcess cp){
		super(cp);
		startIndexFrame = cp.getStartFrame();
		endIndexFrame = cp.getEndFrame();
	}
	
	/**
	 * @return starting index frame
	 */
	public int getStartFrame(){
		return startIndexFrame;
	}
	
	/**
	 * @return ending index frame
	 */
	public int getEndFrame(){
		return endIndexFrame;
	}
	
	/**
	 * @param startIndexFrame: starting index frame of process
	 * @effects sets new starting index frame
	 * @modifies startIndexFrame field
	 */
	public void setStartFrame(int startIndexFrame){
		this.startIndexFrame = startIndexFrame;
	}
	
	/**
	 * @param endIndexFrame: ending index frame of process
	 * @effects sets new ending index frame
	 * @modifies endIndexFrame field
	 */
	public void setEndFrame(int endIndexFrame){
		this.endIndexFrame = endIndexFrame;
	}

	/**
	 * @return readable string of contiguous process fields
	 */
	@Override
	public String toString() {
		StringBuilder mResult = new StringBuilder();
		mResult.append("PID: ").append(this.getPid()).append('\n');
		mResult.append("# Frames Req: ").append(this.getNumOfFrame())
			   .append(" [").append(startIndexFrame).append("-").append(endIndexFrame).append("]").append('\n');
		mResult.append("Remaining Times: ").append(this.getTimeList().toString()).append('\n');
		mResult.append("Status: ").append(this.getStatus()?"Waiting to Enter\n":"Waiting to Exit\n");
		return mResult.toString();
	}
	
}
