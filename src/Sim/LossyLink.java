package Sim;

import java.util.Random;

// This class implements a link that can have loss, jitter or delay

public class LossyLink extends Link{
	private SimEnt _connectorA=null;
	private SimEnt _connectorB=null;
	private int _now=0;
	
	private double lossProbability = 0.01;
	private int maxDelay = 100;
	
	public LossyLink()
	{
		super();
	}
	
	// Connects the link to some simulation entity like
	// a node, switch, router etc.
	public void setConnector(SimEnt connectTo)
	{
		if (_connectorA == null) 
			_connectorA=connectTo;
		else
			_connectorB=connectTo;
	}

	// Called when a message enters the link
	public void recv(SimEnt src, Event ev)
	{
		
		if (ev instanceof Message)
		{
			//Dropped packet according to probability, just return and the packet wont be sent.
			if(packetDrop()){
				System.out.println("Link recv msg, but lost it");
				return;
			}
			
//			System.out.println("Link recv msg, passes it through");
			
			//adding propagation delay.
			_now += delayCalculate();
			
			if (src == _connectorA)
			{
				send(_connectorB, ev, _now);
			}
			else
			{
				send(_connectorA, ev, _now);
			}
		}
	}
	
	
	private double delayCalculate(){
		Random r = new Random();
		double x = r.nextDouble();
		double amplify = r.nextInt(maxDelay);
		return x * amplify;
	}
	
	//A function that randomly drops a packet.
	private boolean packetDrop(){
		
		Random r = new Random();
		double x = r.nextDouble();
		
		if(x < lossProbability){
			return true;			
		}else{
			return false;
		}
		
	}
	
}