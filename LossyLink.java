package Sim;

import java.util.Random;

// This class implements a link that can have loss, jitter or delay

public class LossyLink extends Link{
	private SimEnt _connectorA=null;
	private SimEnt _connectorB=null;
	private int _now=0;
	
	//Constants for the simulation to use
	private double lossProbability = 0.0;
	int maxDelay;
	private int minDelay;
	
	public LossyLink(int minDelay, int maxDelay, double lossRate)
	{
		super();
		this.maxDelay = maxDelay;
		this.minDelay = minDelay;
		this.lossProbability = lossRate;
	}
	
	public void setConnectorA(SimEnt connectTo) {
		_connectorA=connectTo;		
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
		
		// If it is a MoveMessage we want to pass directly since thats is a simulator modification message.
		if(ev instanceof MoveMessage){
			if (src == _connectorA)
			{
				send(_connectorB, ev, _now);
			}
			else
			{
				send(_connectorA, ev, _now);
			}
		}else if  ((ev instanceof Message) ||(ev instanceof RSMessage) || (ev instanceof BAMessage) ||
				(ev instanceof BUMessage)|| (ev instanceof RAMessage) || (ev instanceof RIPMessage)){
			//Dropped packet according to probability, just return and the packet wont be sent.
			if(packetDrop()){
//				System.out.println("Link recv msg, but lost it");
				return;
			}
			
			
//			System.out.println("Link recv msg, passes it through");
			
			
			//Here we add the delay to the send, this depends on what random number the delayCalculate will return.
			if (src == _connectorA)
			{
				send(_connectorB, ev, _now + delayCalculate());
			}
			else
			{
				send(_connectorA, ev, _now + delayCalculate());
			}
		}
	}
	
	//Calculate a random delay to be added as propagation delay.
	private double delayCalculate(){
		Random r = new Random();
		return r.nextInt(maxDelay - minDelay) + minDelay;
	}
	
	//A function that determines when to randomly drops a packet.
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