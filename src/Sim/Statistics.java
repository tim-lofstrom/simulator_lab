package Sim;

import java.util.ArrayList;

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
}
