package Sim;

public class MoveMessage implements Event{

	Node _node;
	int _toInterface;
	private NetworkAddr _source;
	private NetworkAddr _destination;
	boolean createHomeAgent;
	
	public MoveMessage(int toInterface, Node node, NetworkAddr src, boolean createHA){
		_toInterface = toInterface;
		_node = node;
		_source = src;
	}
		
	public void setDest(NetworkAddr addre){
		_destination = addre;
	}
	
	public NetworkAddr getDest(){
		return _destination;
	}
	
	public void setSrc(NetworkAddr _id) {
		_source = _id;
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
