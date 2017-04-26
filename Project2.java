package project2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

public class Project2 {

	public static void main(String[] args) throws IOException{
	
	}

	public Project2(){
		frame = new ArrayList<String>();
		process = new LinkedList<String>();
		comparator = new TimeComparator();
		processQueue = new PriorityQueue<Process>(32, comparator);
		FrequencyMap = new HashMap<String, Integer>();
		processMap = new HashMap<String, Process>();
	}
}
