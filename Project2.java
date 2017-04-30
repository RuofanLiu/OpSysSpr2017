package project2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Project2 {

	public static void main(String[] args) throws IOException{
		Project2 p = new Project2();
		// Contiguous Memory
		p.parseDataPM(args[0]);
	}
	
	private HashMap<String, Process> processMap;
	private PriorityQueue<Process> processQueue;
	Comparator<Process> comparator;
	
	public Project2(){
		comparator = new TimeComparator();
		processQueue = new PriorityQueue<Process>(32, comparator);
		processMap = new HashMap<String, Process>();
	}
	
	/**
	 * the function parse data from the input text file (only for physical memory)
	 * @param filename: the filename to be read
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
	 * the function parse data from the input text file (only for physical memory)
	 * @param filename: the filename to be read
	 * @throws IOException
	 */
	public void parseDataPM2(String filename) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line = reader.readLine();
		while((line = reader.readLine()) != null){
			String[] token = line.split("/|\\ ");
			Process p = new Process(token[0], Integer.parseInt(token[1]));
			for(int i = 2; i < token.length; i++){	// this for loop adds the time interval to the ArrayList returned by the process class
				if(i % 2 == 0){
					p.getTimeList().add(Integer.parseInt(token[i]));
				}
				else{
					p.getTimeList().add((Integer.parseInt(token[i]) + Integer.parseInt(token[i - 1])));
				}
			}
			p.setStatus(true); //this line sets the initial process of a process to true, since it will enter the memory (please change the status whenever the process exits the memory)
			processMap.put(token[0], p);	//add the element to the map, the key is the process ID, and the value is an object of Process class.
		}
		reader.close();
	}
}
