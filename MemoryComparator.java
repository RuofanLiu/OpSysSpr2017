package project2;

import java.util.Comparator;

public class MemoryComparator implements Comparator<ContiguousProcess> {
	/**
	 * @param contiguousProcess1: first contiguousProcess to compare
	 * @param contiguousProcess2: second contiguousProcess to compare
	 * @return 1 if contiguousProcess1 has later end index frame, -1 if contiguousProcess1 has earlier start index frame, and 0 otherwise 
	 */
	@Override
	public int compare(ContiguousProcess contiguousProcess1, ContiguousProcess contiguousProcess2) {
		if(contiguousProcess1.getEndFrame() > contiguousProcess2.getEndFrame()) {
			return 1;
		} else if (contiguousProcess1.getStartFrame() < contiguousProcess2.getStartFrame()) {
			return -1;
		}
		
		return 0;
	}
}
