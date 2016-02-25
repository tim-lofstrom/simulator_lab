package Sim;

public class BAMessage extends Message{

	private NetworkAddr _destination;
	
	public BAMessage(NetworkAddr dest){
		_destination = dest;
	}

	public NetworkAddr destination(){
		return _destination;
	}

	public void entering(SimEnt locale) {
	}
}
