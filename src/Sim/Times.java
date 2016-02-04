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
	
	public Times(NetworkAddr source, NetworkAddr destination, int numMessages, int startSeq){
		this.source = source;
		this.destination=destination;
		this.numMessages = numMessages;
		this.startSeq = startSeq;
	}
	
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
			if(i == 0){
				jitterBuffer.add(0);
			}else{
				jitterBuffer.add(jitterBuffer.get(i-1) + Math.abs(calculateDifference(i-1, i)) - jitterBuffer.get(i-1));
			}
		}
	}
	
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
	
	public void calculateStats() {
		calcSucceededNodes();
		calculateAllJitter();
	}

}
