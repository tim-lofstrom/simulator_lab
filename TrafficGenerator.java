package Sim;

import java.util.ArrayList;
import java.util.Collections;

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
	
	protected void printTimesLatex() {
				
		for (int i = 0; i < generatedTimes.size(); i++) {
			System.out.println(i + "," + generatedTimes.get(i));
		}
	}
	
	protected void printTimesLatexSortedFalling() {
		
		ArrayList<Integer> sorted = new ArrayList<Integer>(generatedTimes);
		Collections.sort(sorted);
		
		for (int i = sorted.size()-1; i >=0; i--) {
			System.out.println((sorted.size()-1 - i) + "," + sorted.get(i));
		}
	}
	
	protected void printTimesLatexSorted() {
		
		ArrayList<Integer> sorted = new ArrayList<Integer>(generatedTimes);
		Collections.sort(sorted);
		
		for (int i = 0; i < sorted.size(); i++) {
			System.out.println(i+ "," + sorted.get(i));
		}
	}

}
