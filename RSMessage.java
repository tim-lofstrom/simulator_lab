package Sim;

public class RSMessage implements Event{
	
	private int _toInterface;
	private NetworkAddr _source;
	
	public RSMessage(int toInterface,NetworkAddr source){
		_toInterface = toInterface;
		_source = source;
	}
	
	public NetworkAddr getSrc(){
		return _source;
	}
	
	public int toInterface() {
		return _toInterface;
	}	
	
	public void entering(SimEnt locale) {
		
	}

}
