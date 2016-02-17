package Sim;

// This class implements a simple router

public class Router extends SimEnt{

	private RouteTableEntry [] _routingTable;
	private int _interfaces;
	private int _now=0;

	// When created, number of interfaces are defined
	
	Router(int interfaces)
	{
		_routingTable = new RouteTableEntry[interfaces];
		_interfaces=interfaces;
	}
	
	
	public void Move(int time, int inteface, Node node){
//		send(this, new MoveMessage(inteface, node), time);
	}
	
	// This method connects links to the router and also informs the 
	// router of the host connects to the other end of the link
	
	public void connectInterface(int interfaceNumber, SimEnt link, SimEnt node)
	{
		if (interfaceNumber<_interfaces)
		{
			_routingTable[interfaceNumber] = new RouteTableEntry(link, node);
		}
		else
			System.out.println("Trying to connect to port not in router");
		
		((Link) link).setConnector(this);
	}

	// This method searches for an entry in the routing table that matches
	// the network number in the destination field of a messages. The link
	// represents that network number is returned
	
	private SimEnt getInterface(int networkAddress)
	{
		SimEnt routerInterface=null;
		for(int i=0; i<_interfaces; i++)
		{
			if (_routingTable[i] != null)
			{
//				if(((Node) _routingTable[i].node()) != null ){
//					System.out.println(((Node) _routingTable[i].node()).getAddr().networkId());					
//				}
//				
				
				if( ((Node) _routingTable[i].node()) != null && (((Node) _routingTable[i].node()).getAddr().networkId() == networkAddress))
				{
					routerInterface = _routingTable[i].link();
				}
			}
		}
		return routerInterface;
	}
	
	private int getInterfaceId(int networkAddress)
	{
		for(int i=0; i<_interfaces; i++)
		{
			if (_routingTable[i] != null)
			{
				if( ((Node) _routingTable[i].node()) != null && (((Node) _routingTable[i].node()).getAddr().networkId() == networkAddress))
				{
					return i;
				}
			}
		}
		return -1;
	}
	
	
	// When messages are received at the router this method is called
	
	public void recv(SimEnt source, Event event)
	{
		if (event instanceof Message)
		{
			System.out.println("Router handles packet with seq: " + ((Message) event).seq()+" from node: "+((Message) event).source().networkId()+"." + ((Message) event).source().nodeId() );
			SimEnt sendNext = getInterface(((Message) event).destination().networkId());
			if (sendNext == null){
				System.out.println("No host found at address and port");
			} else {
				int interfaceID = getInterfaceId(((Message) event).destination().networkId());
				System.out.println("Router sends to node: " + ((Message) event).destination().networkId()+"." + ((Message) event).destination().nodeId() + " on interface " + interfaceID);
				send(sendNext, event, _now);
			}
	
	
		}else if(event instanceof MoveMessage){
			System.out.println("Node: " + ((MoveMessage) event).node().getAddr().networkId() + " moved to interface " + ((MoveMessage) event)._toInterface + " at time " + SimEngine.getTime());
			MoveMessage m = ((MoveMessage) event);
			SimEnt sendNext = getInterface(((MoveMessage) event).getDest().networkId());
			send(sendNext, event, 0);
			switchInterface(m._toInterface, m._node);
		}
	}


	private void switchInterface(int _toInterface, Node _node) {

		//Clear the old table entry
		int id = getInterfaceId(_node.getAddr().networkId());
		Link oldLink = (Link) _routingTable[id].link();
		_routingTable[id] = new RouteTableEntry(oldLink, null);
		
		
		System.out.println("Node " + _node.getAddr().networkId()+"."+_node.getAddr().nodeId() + " got new address " + (_toInterface+1) + "." + _node.getAddr().nodeId());
		
		//Insert new link and node into table
		Link newLink = (Link) _routingTable[_toInterface].link();
		_node.setNewNetworkAddr(_toInterface+1, _node.getAddr().nodeId());
		_node.forceSetPeer(newLink);
		connectInterface(_toInterface, newLink, _node);
	}	
	
}
