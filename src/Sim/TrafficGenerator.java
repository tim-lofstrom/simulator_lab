package Sim;

import java.util.ArrayList;

public abstract class TrafficGenerator {

	ArrayList<Integer> generatedTimes = new ArrayList<Integer>();
	protected int stopSendingAfter; // messages
	protected int timeBetweenSending; // time between messages
	protected int toNetwork;
	protected int toHost;

	protected TrafficGenerator() {

	}

	protected double getTimeBetweenSending() {
		return timeBetweenSending;
	}

	protected void printTimes() {
		for (int i = 0; i < generatedTimes.size(); i++) {
			System.out.println(generatedTimes.get(i));
		}
	}

}
