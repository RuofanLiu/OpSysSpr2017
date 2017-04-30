package project2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class Project2 {

	public static void main(String[] args) throws IOException{
		Project2 p = new Project2();
		// Contiguous Memory
		p.parseDataPM(args[0]);
		
		p.nextFit();
		System.out.println();
		
		p.bestFit();
	}
	
	private PriorityQueue<Process> processQueue;
	private TreeSet<ContiguousProcess> mMemorySet;
	private static final int FRAMES_PER_LINE = 32;
	private static final int FRAMES_TOTAL = 256;
	private static final int t_menmove = 1;
	Comparator<Process> timeComparator;
	Comparator<ContiguousProcess> memoryComparator;
	
	public Project2(){
		timeComparator = new TimeComparator();
		memoryComparator = new MemoryComparator();
		processQueue = new PriorityQueue<Process>(32, timeComparator);
	}
	
	/**
	 * @param filename: the filename to be read
	 * @effects parses data from text file and creates Process objects
	 * @throws IOException
	 */
	public void parseDataPM(String filename) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line = reader.readLine();
		processQueue = new PriorityQueue<Process>(Integer.parseInt(line), new TimeComparator());
		while((line = reader.readLine()) != null){
			String[] token = line.split("/|\\ ");
			Process p = new Process(token[0], Integer.parseInt(token[1]));	//create a new process object, argument 1 is the PID, and argument 2 is the number of frames
			for(int i = 2; i < token.length; i++){		// this for loop adds the time interval to the ArrayList returned by the process class
				if(i % 2 == 0){
					p.getTimeList().add(Integer.parseInt(token[i]));
				}
				else{
					p.getTimeList().add((Integer.parseInt(token[i]) + Integer.parseInt(token[i - 1])));
				}
			}
			p.setStatus(true); 		//this line sets the initial process of a process to true, since it will enter the memory (please change the status whenever the process exits the memory)
			processQueue.add(p);	//add the element to the queue, each element is an object of process.
		}
		reader.close();
	}
	
	/**
	 * @param set: sorted list of contiguous processes based on time
	 * @effects prints set in readable format
	 */
	private void printPhysicalMemory(TreeSet<ContiguousProcess> set) {
		StringBuilder sb = new StringBuilder();
		sb.append(new String(new char[FRAMES_PER_LINE]).replace("\0", "=")).append("\n");
		
		StringBuilder sbTemp = new StringBuilder();
		sbTemp.append(new String(new char[FRAMES_TOTAL]).replace("\0", "."));
		
		Iterator<ContiguousProcess> itrMem = set.iterator();
		while (itrMem.hasNext()) {
			ContiguousProcess cp = itrMem.next();
			sbTemp.replace(cp.getStartFrame(), cp.getEndFrame() + 1,
					new String(new char[(cp.getEndFrame() + 1) - cp.getStartFrame()]).replace("\0", cp.getPid()));
		}
		
		while (sbTemp.length() != 0) {
			sb.append(sbTemp.substring(0, FRAMES_PER_LINE)).append("\n");
			sbTemp.delete(0, FRAMES_PER_LINE);
		}
		
		sb.append(new String(new char[FRAMES_PER_LINE]).replace("\0", "="));
		System.out.println(sb.toString());
	}
	
	private List<Process> defragPhysicalMemory(TreeSet<ContiguousProcess> mMemorySet, List<ContiguousProcess> contiguousList, int time, Process p) {
		int lastEmptyIndex = 0;
		List<Process> movedProcesses = new LinkedList<>();
		Iterator<ContiguousProcess> itrMem = mMemorySet.iterator();
		
		ContiguousProcess cp = null;
		int difference = 0;
		while (itrMem.hasNext()) {
			cp = itrMem.next();
			difference = cp.getStartFrame() - lastEmptyIndex;
			if (difference > 0) {
				movedProcesses.add(cp);
			}
			
			cp.setStartFrame(cp.getStartFrame() - difference);
			cp.setEndFrame(cp.getEndFrame() - difference);
			lastEmptyIndex = cp.getEndFrame() + 1;
		}
		
		return movedProcesses;
	}
	
	/**
	 * @param amtMemUsed: total amount of memory used in mMemorySet
	 * @param time: current time
	 * @param cp: contiguous process to place
	 * @param lastEmptyIndex: last known empty index that can fit cp
	 * @effects places process in mMemorySet and prints readable information
	 * @modifies mMemorySet field
	 * @return amtMemUsed
	 */
	private int placeProcess(int amtMemUsed, int time, ContiguousProcess cp, int lastEmptyIndex) {
		cp.pop();
		cp.setStartFrame(lastEmptyIndex);
		cp.setEndFrame(lastEmptyIndex + cp.getNumOfFrame() - 1);
		
		mMemorySet.add(cp);
		
		System.out.println("time " + time  + "ms: Placed process " + cp.getPid() + ":");
		printPhysicalMemory(mMemorySet);
		amtMemUsed += cp.getNumOfFrame();
		
		return amtMemUsed;
	}
	
	public void nextFit() {
		int time = 0;
		int amtMemUsed = 0;
		PriorityQueue<Process> processQueue = new PriorityQueue<Process>(this.processQueue.size(), this.timeComparator);
		int lastUsedIndex = 0;
		
		mMemorySet = new TreeSet<>(memoryComparator);
		
		List<ContiguousProcess> contiguousList = new LinkedList<>();
		for (Process process : this.processQueue) {
			contiguousList.add(new ContiguousProcess(process));
		}
		
		processQueue.addAll(contiguousList);
		
		System.out.println("time " + time + "ms: Simulator started (Contiguous -- Next-Fit)");
		ContiguousProcess cp;
		
		while (processQueue.peek() != null) {			
			cp = (ContiguousProcess) processQueue.poll();
			
			boolean isEntering = cp.getStatus();
			while (cp.peek() < time) {
				/* Process cannot be placed into memory yet because arrival time is not current time */
				System.out.println("time " + time + "ms: Cannot place process " + cp.getPid() + " -- skiped!");
				
				if (!cp.isDone()) {
					cp.pop(); cp.pop();
				}
			}
			
			time = cp.peek();
			/* Placement Algorithm */
			if (isEntering) {
				System.out.println("time " + time + "ms: Process " + cp.getPid() + " arrived (requires " + cp.getNumOfFrame() + " frames)");
				
				/* Start searching from last placed process end index */
				int lastEmptyIndex = lastUsedIndex;
				Iterator<ContiguousProcess> itrMem = mMemorySet.iterator();
				boolean isPlaced = false;
				ContiguousProcess cpTemp = null;
				
				while (itrMem.hasNext()) {
					cpTemp = itrMem.next();
					
					/* Check processes whose starting frame is equal or later than lastUsedIndex */
					if (cpTemp.getStartFrame() >= lastUsedIndex) {
						int amtFreeSpace = cpTemp.getStartFrame() - lastEmptyIndex;
						
						/* Place process */
						if(amtFreeSpace >= cp.getNumOfFrame()) {
							amtMemUsed = placeProcess(amtMemUsed, time, cp, lastEmptyIndex);
							lastUsedIndex =  cp.getEndFrame() + 1 % FRAMES_TOTAL;
							isPlaced = true;
							break;
						}				
						/* Not enough space in physical memory to place contiguous process */
						else {
							lastEmptyIndex = cpTemp.getEndFrame() + 1;
						}
					}
				}
				
				if (!isPlaced) {
					int amtFreeSpace = FRAMES_TOTAL - lastEmptyIndex;
					if (amtFreeSpace >= cp.getNumOfFrame()) {
						amtMemUsed = placeProcess(amtMemUsed, time, cp, lastEmptyIndex);						
						lastUsedIndex = cp.getEndFrame() + 1 % FRAMES_TOTAL;
						isPlaced = true;
					} 
				}
				
				/* Check from beginning of physical memory */
				if (!isPlaced) {
					itrMem = mMemorySet.iterator();
					lastEmptyIndex = 0;
									
					while (itrMem.hasNext()) {
						cpTemp = itrMem.next();
						int amtFreeSpace = cpTemp.getStartFrame() - lastEmptyIndex;
						
						/* Place process */
						if (amtFreeSpace >= cp.getNumOfFrame()) {
							amtMemUsed = placeProcess(amtMemUsed, time, cp, lastEmptyIndex);
							lastUsedIndex = cp.getEndFrame() + 1 % FRAMES_TOTAL;
							isPlaced = true;
							break;
						}
						
						/* Not enough space in physical memory to place contiguous process */
						else {
							lastEmptyIndex = cpTemp.getEndFrame() + 1;
						}
					}
					
					if (!isPlaced) {
						int amtFreeSpace = FRAMES_TOTAL - lastEmptyIndex;
						if (amtFreeSpace >= cp.getNumOfFrame()) {
							amtMemUsed = placeProcess(amtMemUsed, time, cp, lastEmptyIndex);
							lastUsedIndex = cp.getEndFrame() + 1 % FRAMES_TOTAL;
						}
						
						/* Defrag */
						else if ((FRAMES_TOTAL - amtMemUsed) >= cp.getNumOfFrame()){
							System.out.println("time " + time + "ms: Cannot place process " + cp.getPid() + " -- starting defragmentation");
							List<Process> movedProcesses = defragPhysicalMemory(mMemorySet, contiguousList, time, cp);
							
							/* Build results string */
							int amtFramesMoved = 0;
							StringBuilder result = new StringBuilder();
							Iterator<Process> itrP = movedProcesses.iterator();
							Process pTemp = null;
							
							while (itrP.hasNext()) {
								pTemp = itrP.next();
								amtFramesMoved += pTemp.getNumOfFrame();
								result.append(pTemp.getPid());
								if (itrP.hasNext()) result.append(", ");
							}
							
							result.append(")");
							
							/* Add defrag delays to time */
							int delayTime = amtFramesMoved * t_menmove;
							time += delayTime;
							
							/* Print completed defrag statement */
							result.insert(0, " frames: ").insert(0, amtFramesMoved).insert(0, "time " + time + "ms: Defragmentation complete (moved ");
							System.out.println(result);
							
							Iterator<ContiguousProcess> itrCPList = contiguousList.iterator();
							while (itrCPList.hasNext()) {
								itrCPList.next().addDefragTime(0, delayTime);
							}
							
							printPhysicalMemory(mMemorySet);
							
							lastEmptyIndex = mMemorySet.last().getEndFrame() + 1;
							amtFreeSpace = FRAMES_TOTAL - lastEmptyIndex;
							if (amtFreeSpace >= cp.getNumOfFrame()) {
								amtMemUsed = placeProcess(amtMemUsed, time, cp, lastEmptyIndex);
							}
							
							lastUsedIndex = cp.getEndFrame() + 1 % FRAMES_TOTAL;
						}
						/* Skip process */
						else {
							System.out.println("time " + time + "ms: Cannot place process " + cp.getPid() + " -- skipped!");
							cp.pop(); cp.pop();
						}
					}
				}
			}
			/* Remove process from memory */
			else {
				cp.pop();
				mMemorySet.remove(cp);
				System.out.println("time " + time + "ms: Process " + cp.getPid() + " removed:");
				printPhysicalMemory(mMemorySet);
				cp.setStartFrame(-1);
				cp.setEndFrame(-1);;
				amtMemUsed -= cp.getNumOfFrame();
			}
			/* Add if times are not equal */
			if (!cp.isDone()) {
				processQueue.add(cp);
			}
		}
		
		System.out.println("time " + time + "ms: Simulator ended (Contiguous -- Next-Fit)");
	}
	
	public void bestFit() {
		int time = 0;
		int amtMemUsed = 0;
		PriorityQueue<Process> processQueue = new PriorityQueue<Process>(this.processQueue.size(), this.timeComparator);
		mMemorySet = new TreeSet<>(memoryComparator);
		
		List<ContiguousProcess> contiguousList = new LinkedList<>();
		for (Process process : this.processQueue) {
			contiguousList.add(new ContiguousProcess(process));
		}		
		
		processQueue.addAll(contiguousList);
		
		System.out.println("time " + time + "ms: Simulator started (Contiguous -- Best-Fit)");
		ContiguousProcess cp;
		
		while (processQueue.peek() != null) {
			cp = (ContiguousProcess) processQueue.poll();
			
			boolean isEntering = cp.getStatus();
			while (cp.peek() < time) {
				System.out.println("time " + time + "ms: Cannos place process " + cp.getPid() + " -- skippped!");
				cp.pop(); cp.pop();
				
				printPhysicalMemory(mMemorySet);
			}
			
			time = cp.peek();
			
			/* Placement Algorithm */
			if (isEntering) {
				System.out.println("time " + time + "ms: Process " + cp.getPid() + " arrived (requires " + cp.getNumOfFrame() + " frames)");
				
				int lastEmptyIndex = 0;
				int smallestSize = -1;
				int smallestIndex = -1;
				Iterator<ContiguousProcess> itrMem = mMemorySet.iterator();
				boolean isPlaced = false;
				ContiguousProcess cpSmallest = null;
				ContiguousProcess cpTemp = null;
				
				while (itrMem.hasNext()) {
					cpTemp = itrMem.next();
					int amtFreeSpace = cpTemp.getStartFrame() - lastEmptyIndex;
					if (amtFreeSpace >= cp.getNumOfFrame()) {
						if ((smallestSize == -1) || (smallestSize > amtFreeSpace)) {
							smallestSize = amtFreeSpace;
							smallestIndex = lastEmptyIndex;
							cpSmallest = cp;
						}
						isPlaced = true;
					}
					
					lastEmptyIndex = cpTemp.getEndFrame() + 1;
				}
				
				int amtFreeSpace = FRAMES_TOTAL - lastEmptyIndex;
				if (amtFreeSpace >= cp.getNumOfFrame()) {
					if ((smallestSize == -1) || (smallestSize > amtFreeSpace)) {
						smallestSize = amtFreeSpace;
						smallestIndex = lastEmptyIndex;
						cpSmallest = cp;
					}
					isPlaced = true;
				}
				
				/* Place Process */
				if (isPlaced) {
					amtMemUsed = placeProcess(amtMemUsed, time, cpSmallest, smallestIndex);
				}
				else {
					if ((FRAMES_TOTAL - amtMemUsed) >= cp.getNumOfFrame()) {
						/* Defrag */
						System.out.println("time " + time + "ms: Cannot place process " + cp.getPid() + " -- starting defragmentation");
						List<Process> movedProcesses = defragPhysicalMemory(mMemorySet, contiguousList, time, cp);
						
						/* Build results string */
						int amtFramesMoved = 0;
						StringBuilder result = new StringBuilder();
						Iterator<Process> itrP = movedProcesses.iterator();
						Process pTemp = null;
						
						while (itrP.hasNext()) {
							pTemp = itrP.next();
							amtFramesMoved += pTemp.getNumOfFrame();
							result.append(pTemp.getPid());
							if (itrP.hasNext()) result.append(", ");
						}
						
						result.append(")");
						
						/* Add defrag delays to time*/
						int delayTime = amtFramesMoved * t_menmove;
						time += delayTime;
						
						/* Print completed defrag statement */
						result.insert(0,  " frames: ").insert(0,  amtFramesMoved).insert(0,  "time " + time + "ms: Defragmentation complete (moved ");
						System.out.println(result);
						
						Iterator<ContiguousProcess> itrCPList = contiguousList.iterator();
						while (itrCPList.hasNext()) {
							itrCPList.next().addDefragTime(0, delayTime);
						}				
						
						printPhysicalMemory(mMemorySet);
						
						lastEmptyIndex = mMemorySet.last().getEndFrame() + 1;
						amtFreeSpace = FRAMES_TOTAL - lastEmptyIndex;
						if (amtFreeSpace >= cp.getNumOfFrame()) {
							amtMemUsed = placeProcess(amtMemUsed, time, cp, lastEmptyIndex);
						}
					}
					/* Skip Process */
					else {
						System.out.println("time " + time + "ms: Cannot place process " + cp.getPid() + " -- skipped!");
						cp.pop(); cp.pop();
					}
				}
			}
			/* Remove Process from memory */
			else {
				cp.pop();
				mMemorySet.remove(cp);
				System.out.println("time " + time + "ms: Process " + cp.getPid() + " removed:");
				printPhysicalMemory(mMemorySet);
				cp.setStartFrame(-1);
				cp.setEndFrame(-1);
				amtMemUsed -= cp.getNumOfFrame();
			}
			
			/* Add if times are not equal */
			if (!cp.isDone()) {
				processQueue.add(cp);
			}
		}
		
		System.out.println("time " + time + "ms: Simulator ended (Contiguous -- Best-Fit)");
	}
}
