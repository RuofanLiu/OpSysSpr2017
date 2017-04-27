package project2;

import java.util.ArrayList;

public class Process {
	/* Process ID */
	private final String pid;
	/* The size of each frame;*/
	private final int numberOfFrame;
	/* Time interval [arr_time0, run_time0, arr_time1, run_time1, ... , arr_timeN, run_timeN] */
	private ArrayList<Integer> timeList;
	/* Represents whether a process is entering (true)/exiting (false)*/
	private boolean status;
	
	/**
	 * @param pid: process id
	 * @param numberOfFrame: number of frames for the given process
	 * @effects initializes fields
	 */
	public Process(String pid, int numberOfFrame){
		this.pid = pid;
		this.numberOfFrame = numberOfFrame;
		timeList = new ArrayList<Integer>();
		status = true;
	}
	
	/**
	 * @param p: process object
	 * @effects copies all fields from p
	 */
	public Process(Process p){
		pid = p.pid;
		numberOfFrame = p.numberOfFrame;
		timeList = new ArrayList<Integer>();
		for(int i = 0; i < p.timeList.size(); i++){
			timeList.add(p.timeList.get(i).intValue());
		}
		status = p.status;
	}
	
	/**
	 * @return process id
	 */
	public String getPid(){
		return pid;
	}

	/**
	 * @return number of frames for the given process
	 */
	public int getNumOfFrame(){
		return numberOfFrame;
	}
	
	/**
	 * @return time intervals of process
	 */
	public ArrayList<Integer> getTimeList(){
		return timeList;
	}
	
	/**
	 * @return true for entrance and false for exit
	 */
	public boolean getStatus(){
		return status;
	}
	
	/**
	 * @param s: status of process
	 * @effects sets status of process to s
	 */
	public void setStatus(boolean s){
		status = s;
	}
	
	/**
	 *  @effects sets status of process to the opposite value and removes next time from list  
	 */ 
	public int pop(){
		status = !status;
		return timeList.remove(0);
	}
	
	/**
	 *  @effects checks the next time from list
	 */ 
	public int peek(){
		return timeList.get(0);
	}
	
	/**
	 * @return true if process has no times left in list
	 */
	public boolean isDone(){
		return timeList.isEmpty();
	}
	
	/**
	 * @param currentTime: time after fragmentation
	 * @param defragTime: total defragmentation time
	 * @effects adds duration time to all times in list
	 */
	public void addDefragTime(int currentTime, int defragTime){
		int index = 0;
		
		// Determine if this Process is waiting queue
		boolean isArriving = getStatus();
		if(timeList.isEmpty() == false && isArriving == false){
			// Add total defragmentation time, shift 1
			timeList.set(index, timeList.get(index) + defragTime);
			index += 1;
		}
		
		//For every interval
		for(; index < timeList.size(); index += 2){
			//Check if time is during defragmentation
			int shiftTime = -1;
			if(timeList.get(index) < currentTime){
				shiftTime = currentTime - timeList.get(index);
			} 
			// After defragmentation
			else {
				// Shift by total defragmentation time
				shiftTime = defragTime;
			}
			timeList.set(index, timeList.get(index) + shiftTime);
			timeList.set(index + 1, timeList.get(index + 1) + shiftTime);
		}
	}
	
	/**
	 * @return true if process has same PID and number of required frames
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Process){
			Process p = (Process) obj;
			return this.pid.equals(p.pid) && this.numberOfFrame == p.numberOfFrame;
		}
		return false;
	}
}
