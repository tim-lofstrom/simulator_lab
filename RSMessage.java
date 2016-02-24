package Sim;

public class RSMessage implements Event{
	
	Node _node;
	int _toInterface;
	
	public RSMessage(int toInterface, Node node){
		_toInterface = toInterface;
		_node = node;
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
