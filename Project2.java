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
	
	private PriorityQueue<Process> processQueue;
	Comparator<Process> comparator;
	
	public Project2(){
		comparator = new TimeComparator();
		processQueue = new PriorityQueue<Process>(32, comparator);
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
			String[] token = line.split(" |/");
			
			Process p = new Process(token[0], Integer.parseInt(token[1]));	//create a new process object, argument 1 is the PID, and argument 2 is the number of frames
			for(int i = 2; i < token.length; i++){		// this for loop adds the time interval to the ArrayList returned by the process class
				p.getTimeList().add(Integer.parseInt(token[i]));
			}
			p.setStatus(true); 		//this line sets the initial process of a process to true, since it will enter the memory (please change the status whenever the process exits the memory)
			processQueue.add(p);	//add the element to the queue, each element is an object of process.
		}
		reader.close();
	}
}
