package Sim;

public class RAMessage implements Event {
	private NetworkAddr _newAddress;
	
	public RAMessage(NetworkAddr newAddress){
		_newAddress = newAddress;
	}
	
	public void setNewAddress(NetworkAddr addre){
		_newAddress = addre;
	}
	
	public NetworkAddr getNewAddress(){
		return _newAddress;
	}	
	
	public void entering(SimEnt locale) {
		
	}

}
