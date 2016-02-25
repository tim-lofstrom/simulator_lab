package Sim;

// This class represent a routing table entry by including
// the link connecting to an interface as well as the node 
// connected to the other side of the link

public class RouteTableEntry extends TableEntry{
	
	private SimEnt _link;
	private NetworkAddr _nodeAddress;

//	RouteTableEntry(SimEnt link, SimEnt node)
//	{
//		super(link, node);
//	}
	
	RouteTableEntry(SimEnt link, NetworkAddr nodeAddress)
	{
		super();
		_link = link;
		_nodeAddress = nodeAddress;
	}
	
	public NetworkAddr nodeAddress(){
		return _nodeAddress;
	}
	
	public SimEnt link()
	{
		return _link;
	}

	public SimEnt node()
	{
		return super.node();
	}

	public void setAddress(NetworkAddr newAddress) {
		_nodeAddress = newAddress;
	}
	
}
