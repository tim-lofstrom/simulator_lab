package Sim;

public class MoveMessage implements Event{

	Node _node;
	int _toInterface;
	private NetworkAddr _source;
	private NetworkAddr _destination;
	
	public MoveMessage(int toInterface, Node node, NetworkAddr dest){
		_toInterface = toInterface;
		_node = node;
		_destination = dest;
	}
	
	public void setDest(NetworkAddr addre){
		_destination = addre;
	}
	
	public NetworkAddr getDest(){
		return _destination;
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
