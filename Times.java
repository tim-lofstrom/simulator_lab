package Sim;

import java.util.ArrayList;

public class Times {

	private class TimeNode {
		public TimeNode(int seq, int timestamp) {
			this.seq = seq;
			this.send = timestamp;
		}

		Integer recv;
		Integer send;
		Integer seq;
	}

	Node _source;
	Node _destination;

	ArrayList<TimeNode> timeNodes = new ArrayList<TimeNode>();
	ArrayList<TimeNode> timeNodesSuccess = new ArrayList<TimeNode>();
	ArrayList<Integer> jitterBuffer = new ArrayList<Integer>();

	public Times(Node host1, Node host2) {
		this._source = host1;
		this._destination = host2;
	}

	public void addSendTime(int seq, int timestamp) {
		timeNodes.add(new TimeNode(seq, timestamp));
	}

	public void addRecvTime(int seq, int timestamp) {
		for (int i = 0; i < timeNodes.size(); i++) {
			if (timeNodes.get(i).seq == seq) {
				timeNodes.get(i).recv = timestamp;
			}
		}
	}

	// D(i,j) = (Rj - Ri) - (Sj - Si) = (Rj - Sj) - (Ri - Si)
	private int calculateDifference(int i, int j) {
		return ((timeNodesSuccess.get(j).recv - timeNodesSuccess.get(i).recv)
				- (timeNodesSuccess.get(j).send - timeNodesSuccess.get(i).send));
	}

	// J(i) = J(i-1) + ( |D(i-1,i)| - J(i-1))
	private void calculateAllJitter() {

		for (int i = 0; i < timeNodesSuccess.size(); i++) {

			// The first difference in propagation delay must always be zero
			// since we dont have any (i-1) to compare with
			if (i == 0) {
				jitterBuffer.add(0);
			} else {
				jitterBuffer.add(
						jitterBuffer.get(i - 1) + Math.abs(calculateDifference(i - 1, i)) - jitterBuffer.get(i - 1));
			}
		}
	}

	// Insert all timestamps that are fulle received in the arraylist
	private void calcSucceededNodes() {
		for (TimeNode t : timeNodes) {
			if (t.recv != null) {
				timeNodesSuccess.add(t);
			}
		}
	}

	// This wraps up the calculation function for succeded nodes and the jiter
	// calculations
	public void calculateStats() {
		calcSucceededNodes();
		calculateAllJitter();
	}

	// This function print out the stored times of the succeded packets
	public void printTimes() {

		for (TimeNode t : timeNodes) {
			if (t.recv == null) {
				System.out.println("Seq=" + t.seq + " 	S=" + t.send + " - lost");
			} else {
				System.out.println("Seq=" + t.seq + " 	S=" + t.send + " 	R=" + t.recv + "	J="
						+ jitterBuffer.get(timeNodesSuccess.indexOf(t)));
			}
		}
	}

	// For a simpler way if inserting the data into the report, print it in a
	// suitable format.
	public void printTimesLaTeXFormat() {
		for (TimeNode t : timeNodesSuccess) {
			if (t.recv == null) {
				System.out.println("Seq=" + t.seq + " - lost");
			} else {
				System.out.println(t.seq + " 	&" + t.send + " 	&" + t.recv + "	&"
						+ jitterBuffer.get(timeNodesSuccess.indexOf(t)) + "\\" + "\\");
			}
		}
	}

	// Just a testfunction to print jitters without anything else. This makes it
	// possible for plotting if wanted.
	public void printTimesJitterPlot() {
		for (TimeNode t : timeNodesSuccess) {
			if (t.recv == null) {
				System.out.println("Seq=" + t.seq + " - lost");
			} else {
				System.out.println(jitterBuffer.get(timeNodesSuccess.indexOf(t)));
			}
		}
	}

	public void printTimesReceive() {
		for (int i = 0; i < timeNodesSuccess.size() - 1; i++) {
			System.out.println(i + "," + (timeNodesSuccess.get(i + 1).recv - timeNodesSuccess.get(i).recv));
		}
	}

	public void printTimesReceivePlot() {
		Integer recv;
		Integer send;
		for (int i = 0; i < timeNodes.size() - 1; i++) {
			recv = timeNodes.get(i).recv;
			send = timeNodes.get(i).send;
			if(recv != null){
				System.out.println(send + "," + recv);	
			}else {
				System.out.println(send + ",");
			}
		}
	}

}
