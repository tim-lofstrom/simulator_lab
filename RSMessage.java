package Sim;

public class RSMessage implements Event{
	
	Node _node;
	int _toInterface;
	private NetworkAddr _source;
	
	public RSMessage(int toInterface, Node node, NetworkAddr source){
		_toInterface = toInterface;
		_node = node;
		_source = source;
	}
	
	public NetworkAddr getSrc(){
		return _source;
	}
	
	public Node node() {
		return _node;
	}

	public int toInterface() {
		return _toInterface;
	}	
	
	public void entering(SimEnt locale) {
		
	}

}
