package Sim;

public class BAMessage implements Event{
	
	private NetworkAddr _newAddress;
	private Node _HA;
	
	public BAMessage(NetworkAddr addr, Node HA){
		_newAddress = addr;
		_HA = HA;
	}
	
	public Node getHA(){
		return _HA;
	}
	
	public NetworkAddr getNewAddr(){
		return _newAddress;
	}

	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub
		
	}
}
