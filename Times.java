package Sim;

import java.util.ArrayList;

public class Times {
	
	private class TimeNode{
		Integer recv;
		Integer send;
		Integer seq;
	}
	
	NetworkAddr source;
	NetworkAddr destination;

	int numMessages;
	int startSeq;
	
	ArrayList<TimeNode> timeNodes = new ArrayList<TimeNode>();
	ArrayList<TimeNode> timeNodesSuccess = new ArrayList<TimeNode>();
	ArrayList<Integer> jitterBuffer = new ArrayList<Integer>();
	
	
	//Needed to add timeevents for statistics. The source and destination is used to determine the connection and the direction it has.
	public Times(NetworkAddr source, NetworkAddr destination, int numMessages, int startSeq){
		this.source = source;
		this.destination=destination;
		this.numMessages = numMessages;
		this.startSeq = startSeq;
	}
	
	
	//Adds a timeevent, this function decides if it is a send or receive message, and what Seq it belongs to.
	public void addTime(int seq, int timestamp){
		
		int index = seq - startSeq;
		
		if(timeNodes.size() <= index){
			timeNodes.add(new TimeNode());
		}
		
		if(timeNodes.get(index).send == null){
			timeNodes.get(index).send = timestamp;
			timeNodes.get(index).seq = seq;
		}else if(timeNodes.get(index).recv == null){
			timeNodes.get(index).recv = timestamp;
		}
	}

	//D(i,j) = (Rj - Ri) - (Sj - Si) = (Rj - Sj) - (Ri - Si)
	private int calculateDifference(int i, int j){
		return ((timeNodesSuccess.get(j).recv - timeNodesSuccess.get(i).recv) - (timeNodesSuccess.get(j).send - timeNodesSuccess.get(i).send));
	}

	
	//J(i) = J(i-1) + ( |D(i-1,i)| - J(i-1))
	private void calculateAllJitter(){
			
		for(int i = 0; i < timeNodesSuccess.size(); i++){
			
			//The first difference in propagation delay must always be zero since we dont have any (i-1) to compare with
			if(i == 0){
				jitterBuffer.add(0);
			}else{
				jitterBuffer.add(jitterBuffer.get(i-1) + Math.abs(calculateDifference(i-1, i)) - jitterBuffer.get(i-1));
			}
		}
	}
	
	
	//This function print out the stored times of the succeded packets
	public void printTimes(){

		for(TimeNode t : timeNodesSuccess){
			if(t.recv == null){
				System.out.println("Seq="+t.seq + " - lost");
			}else{
				System.out.println("Seq="+t.seq + " 	S=" + t.send + " 	R=" + t.recv + "	J="+jitterBuffer.get(timeNodesSuccess.indexOf(t)));
			}
		}
	}

	//Insert all timestamps that are fulle received in the arraylist
	private void calcSucceededNodes(){
		for(TimeNode t : timeNodes){
			if(t.recv != null){
				timeNodesSuccess.add(t);
			}
		}
	}
	
	
	//This wraps up the calculation function for succeded nodes and the jiter calculations
	public void calculateStats() {
		calcSucceededNodes();
		calculateAllJitter();
	}

	
	//For a simpler way if inserting the data into the report, print it in a suitable format.
	public void printTimesLaTeXFormat() {
		for(TimeNode t : timeNodesSuccess){
			if(t.recv == null){
				System.out.println("Seq="+t.seq + " - lost");
			}else{
				System.out.println(t.seq + " 	&" + t.send + " 	&" + t.recv + "	&"+jitterBuffer.get(timeNodesSuccess.indexOf(t)) + "\\" + "\\");
			}
		}
	}
	
	
	//Just a testfunction to print jitters without anything else. This makes it possible for plotting if wanted.
	public void printTimesJitterPlot() {
		for(TimeNode t : timeNodesSuccess){
			if(t.recv == null){
				System.out.println("Seq="+t.seq + " - lost");
			}else{
				System.out.println(jitterBuffer.get(timeNodesSuccess.indexOf(t)));
			}
		}
	}


	public void printTimesReceive() {
		for(int i = 0; i < timeNodesSuccess.size()-1;i++){
			System.out.println(i + "," + (timeNodesSuccess.get(i+1).recv - timeNodesSuccess.get(i).recv));
		}
	}

}
