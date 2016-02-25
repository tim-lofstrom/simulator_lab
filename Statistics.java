package Sim;

import java.util.ArrayList;

public class Statistics {

	// Used to keep track of important times between any two nodes
	static ArrayList<Times> times = new ArrayList<Times>();

	public static void Initialize(Node host1, Node host2) {
		times.add(new Times(host1, host2));
	}

	// Add a sendtime if correct connection
	public static void addSendTime(Node node, int seq, int timestamp) {
		for (Times t : times) {
			if (t._source.compareTo(node)) {
				t.addSendTime(seq, timestamp);
				return;
			}
		}
	}

	
	// Add a recvtime if correct connection
	public static void addRecvTime(Node node, int seq, int timestamp) {
		for (Times t : times) {
			if (t._destination.compareTo(node)) {
				t.addRecvTime(seq, timestamp);
				return;
			}
		}
	}

	// prints statistics over what all connections timedata
	public static void printTimes() {
		for (Times t : times) {

			// need to calculate stats for connection t
			t.calculateStats();

			NetworkAddr source = t._source.getAddr();
			NetworkAddr destination = t._destination.getAddr();

			System.out.println(
					"Statistics for connection between source node " + source.networkId() + "." + source.nodeId() + ""
							+ " and destination node " + destination.networkId() + "." + destination.nodeId());
			System.out.println("Messages received: " + t.timeNodesSuccess.size() + " of " + t.timeNodes.size());
			t.printTimes();

		}
	}

	// prints statistics over what all connections timedata
	public static void printTimesLaTexFormat() {
		for (Times t : times) {

			// need to calculate stats for connection t
			t.calculateStats();

			NetworkAddr source = t._source.getAddr();
			NetworkAddr destination = t._destination.getAddr();
			
			System.out.println(
					"Statistics for connection between source node " + source.networkId() + "." + source.nodeId()
							+ "" + " and destination node " + destination.networkId() + "." + destination.nodeId());
			System.out.println("Messages received: " + t.timeNodesSuccess.size() + " of " + t.timeNodes.size());
			t.printTimesLaTeXFormat();

		}
	}

	// prints statistics over what all connections timedata
	public static void printTimesJitterPlot() {
		for (Times t : times) {

			// need to calculate stats for connection t
			t.calculateStats();

			
			NetworkAddr source = t._source.getAddr();
			NetworkAddr destination = t._destination.getAddr();
			
			System.out.println(
					"Statistics for connection between source node " + source.networkId() + "." + source.nodeId()
							+ "" + " and destination node " + destination.networkId() + "." + destination.nodeId());
			System.out.println("Messages received: " + t.timeNodesSuccess.size() + " of " + t.timeNodes.size());
			t.printTimesJitterPlot();

		}
	}

	// Print all the receive times
	public static void printTimesReceive() {
		for (Times t : times) {

			// need to calculate stats for connection t
			t.calculateStats();

			
			NetworkAddr source = t._source.getAddr();
			NetworkAddr destination = t._destination.getAddr();
			
			System.out.println(
					"ReceiveTimes for connection between source node " + source.networkId() + "." + source.nodeId()
							+ "" + " and destination node " + destination.networkId() + "." + destination.nodeId());

			t.printTimesReceive();
		}
	}
	
	public static void printTimesReceivePlot(){
		for (Times t : times) {

			// need to calculate stats for connection t
			t.calculateStats();

			
			NetworkAddr source = t._source.getAddr();
			NetworkAddr destination = t._destination.getAddr();
			
			System.out.println(
					"ReceiveTimes for connection between source node " + source.networkId() + "." + source.nodeId()
							+ "" + " and destination node " + destination.networkId() + "." + destination.nodeId());

//			t.test();
			t.printTimesReceivePlot();
		}
	}
}
