package Sim;

public class CBRGenerator extends TrafficGenerator{

	public CBRGenerator(int timeInterval){
		super.timeBetweenSending = timeInterval;
	}
	

	public double getTimeBetweenSending() {
		int temp = timeBetweenSending;
		generatedTimes.add(temp);
		return temp;
		
		
	}
}
