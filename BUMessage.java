package Sim;

public class BUMessage implements Event{
	
	private NetworkAddr _newAddress;
	private Node _node;
	
	public BUMessage(NetworkAddr addr, Node node){
		_newAddress = addr;
		_node = node;
	}
	
	public void setNode(Node node){
		_node = node;
	}
	
	public Node getNode(){
		return _node;
	}
	
	public NetworkAddr getNewAddr(){
		return _newAddress;
	}

	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub
		
	}
}
