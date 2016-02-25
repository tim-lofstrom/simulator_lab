package Sim;

public class RAMessage implements Event {
	private NetworkAddr _newAddress;
	
	public RAMessage(NetworkAddr newAddress){
		_newAddress = newAddress;
	}
		
	public NetworkAddr getNewAddress(){
		return _newAddress;
	}	
	
	public void entering(SimEnt locale) {
		
	}

}
