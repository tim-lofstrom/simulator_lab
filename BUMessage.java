package Sim;

public class BUMessage extends Message{
	
	private NetworkAddr _cn;
	private NetworkAddr _mn;
	private NetworkAddr _homeAddress;
	private Node _node;
	
	public BUMessage(NetworkAddr homeAddress, NetworkAddr cn, NetworkAddr mn){
		super();
//		_newAddress = addr;
//		_node = node;
		_homeAddress = homeAddress;
		_cn = cn;
		_mn = mn;
	}
	
	public NetworkAddr source(){
		return _mn;
	}
	
	public NetworkAddr destination(){
		return _homeAddress;
	}
	
	public void setNode(Node node){
		_node = node;
	}
	
	public Node getNode(){
		return _node;
	}
	
	public NetworkAddr getMobileNodeAddress(){
		return _mn;
	}
	
	public NetworkAddr getCorrespondingNodeAddress(){
		return _cn;
	}

	public void entering(SimEnt locale) {
		
	}
}
