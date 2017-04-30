package project2;
import java.util.Comparator;

public class TimeComparator implements Comparator<Process> {
	/**
	 * @param process1: first process to compare
	 * @param process2: second process to compare
	 * @return 1 if process1 has greater time, 0 if both process have the same time and pid, and -1 otherwise
	 */
	@Override
	public int compare(Process process1, Process process2) {
		if(process1.peek() > process2.peek()){
			return 1;
		}
		else if(process1.peek() == process2.peek()){
			if (!process1.getStatus() && process2.getStatus()) {
				return -1;
			}
			
			return process1.getPid().compareTo(process2.getPid());
		}
		return -1;
	}
}
