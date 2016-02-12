package Sim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Statistics {
	

	//Used to keep track of important times between any two nodes
	static ArrayList<Times> times = new ArrayList<Times>();
	
	public static void Initialize(NetworkAddr source, NetworkAddr destination, int numMessages, int startSeq) {
		times.add(new Times(source, destination, numMessages, startSeq));
	}

	//Redirects the time to be added to the right connection
	public static void addTime(NetworkAddr source, NetworkAddr destination, int seq, int timestamp){
		for(Times t : times){
			if((t.source.compareTo(source)) && (t.destination.compareTo(destination))){
				t.addTime(seq, timestamp);
				return;
			}
		}
	}
	
	
	//prints statistics over what all connections timedata
	public static void printTimes(){
		for(Times t : times){
			
			//need to calculate stats for connection t
			t.calculateStats();
			
			System.out.println("Statistics for connection between source node " + t.source.networkId()+"."+t.source.nodeId() + ""
					+ " and destination node "+ t.destination.networkId() + "." + t.destination.nodeId());
			System.out.println("Messages received: " + t.timeNodesSuccess.size() + " of " + t.numMessages);
			t.printTimes();
			
		}
	}	

	//prints statistics over what all connections timedata
	public static void printTimesLaTexFormat(){
		for(Times t : times){
			
			//need to calculate stats for connection t
			t.calculateStats();
			
			System.out.println("Statistics for connection between source node " + t.source.networkId()+"."+t.source.nodeId() + ""
					+ " and destination node "+ t.destination.networkId() + "." + t.destination.nodeId());
			System.out.println("Messages received: " + t.timeNodesSuccess.size() + " of " + t.numMessages);
			t.printTimesLaTeXFormat();
			
		}
	}
	
	//prints statistics over what all connections timedata
	public static void printTimesJitterPlot(){
		for(Times t : times){
			
			//need to calculate stats for connection t
			t.calculateStats();
			
			System.out.println("Statistics for connection between source node " + t.source.networkId()+"."+t.source.nodeId() + ""
					+ " and destination node "+ t.destination.networkId() + "." + t.destination.nodeId());
			System.out.println("Messages received: " + t.timeNodesSuccess.size() + " of " + t.numMessages);
			t.printTimesJitterPlot();
			
		}
	}
	
	//Print all the receive times
	public static void printTimesReceive(){
		for(Times t : times){
			
			//need to calculate stats for connection t
			t.calculateStats();
			
			System.out.println("ReceiveTimes for connection between source node " + t.source.networkId()+"."+t.source.nodeId() + ""
					+ " and destination node "+ t.destination.networkId() + "." + t.destination.nodeId());
			
			t.printTimesReceive();
			
		}
	}
	
	public static void printRandomValues(){
	
		System.out.println("Randoms without ");
		ArrayList<Integer> rands = new ArrayList<Integer>();
		Random r = new Random();
		for(int i = 0; i < 100; i++){
			rands.add(r.nextInt(100));
//			System.out.println(i + ","+ r.nextInt(100));
		}
		
		Collections.sort(rands);
		
		for(int i = 0; i < 100;i++){
			System.out.println(i+"," + rands.get(i));
		}
		
	}
	
	
	
}
