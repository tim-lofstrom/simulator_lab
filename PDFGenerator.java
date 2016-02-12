package Sim;

import java.util.Random;

public class PDFGenerator extends TrafficGenerator{
	
	int _mean;
	Random r;
	
	public PDFGenerator(int mean){
		_mean = mean;
		r = new Random();
	}
	
	public double getTimeBetweenSending(){
		int temp =nextPoisson(_mean);
		generatedTimes.add(temp);
		return temp;
	}
	
	public int nextPoisson(int mean){
		double L = Math.exp(-mean);
		int k = 0;
		double p = 1.0;
		do {
			p = p * r.nextDouble();
			k++;
		}while(p > L);
		return k -1;
	}
	
}
