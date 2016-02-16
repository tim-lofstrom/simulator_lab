package Sim;

public class MoveMessage implements Event{

	Node _node;
	int _toInterface;
	
	public MoveMessage(int toInterface, Node node){
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
