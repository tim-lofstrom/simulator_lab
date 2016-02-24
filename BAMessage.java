package Sim;

public class BAMessage extends Message{
	
	private NetworkAddr _haAddress;
	private Node _HA;
	private NetworkAddr _destination;
	
	public BAMessage(NetworkAddr dest, NetworkAddr newAddress){
		_destination = dest;
		_haAddress =  newAddress;
	}
	
	public Node getHA(){
		return _HA;
	}
	
	public NetworkAddr destination(){
		return _destination;
	}
	
	public NetworkAddr getNewAddr(){
		return _haAddress;
	}

	public void entering(SimEnt locale) {
	}
}
