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
	
	
	// This method connects links to the router and also informs the 
	// router of the host connects to the other end of the link
	
	public void connectInterface(int interfaceNumber, SimEnt link, SimEnt node)
	{
		if (interfaceNumber<_interfaces)
		{
			NetworkAddr addr = null;
			
			if(node != null){
				addr = ((Node) node).getAddr();
			}
			
//			_routingTable[interfaceNumber] = new RouteTableEntry(link, node);
			_routingTable[interfaceNumber] = new RouteTableEntry(link, addr);
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
				if(((NetworkAddr)_routingTable[i].nodeAddress() != null) && (((NetworkAddr)_routingTable[i].nodeAddress()).networkId() == networkAddress)){
					routerInterface = _routingTable[i].link();
					return routerInterface;
				}
				
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
				
				if(((NetworkAddr)_routingTable[i].nodeAddress() != null) && (((NetworkAddr)_routingTable[i].nodeAddress()).networkId() == networkAddress))
				{
					return i;
				}
				
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
		
		if ((event instanceof Message) || (event instanceof BUMessage) || (event instanceof BAMessage))
		{
//			System.out.println("Router handles packet with seq: " + ((Message) event).seq()+" from node: "+((Message) event).source().networkId()+"." + ((Message) event).source().nodeId() );
			SimEnt sendNext = getInterface(((Message) event).destination().networkId());
			if (sendNext == null){
				System.out.println("No host interface found for address " + ((Message) event).destination().networkId() + "." + ((Message) event).destination().nodeId());
			} else {
//				int interfaceID = getInterfaceId(((Message) event).destination().networkId());
//				System.out.println("Router sends to node: " + ((Message) event).destination().networkId()+"." + ((Message) event).destination().nodeId() + " on interface " + interfaceID);
				send(sendNext, event, _now);
			}
	
	
		} else if(event instanceof MoveMessage){

			MoveMessage m = ((MoveMessage) event);
			
			int fromInterface = getInterfaceId(m.getSrc().networkId());

			System.out.println("Node " + m.getSrc().networkId() + "." + m.getSrc().nodeId()+ " moved to interface " + m._toInterface + " at time " + SimEngine.getTime());
			switchInterface(m._toInterface, m._node);
			
			//Create HomeAgent
			HomeAgent agent = new HomeAgent(m.getSrc().networkId(), m.getSrc().nodeId());
			Link link = (Link) _routingTable[fromInterface].link();
			agent.forceSetPeer(link);
			connectInterface(fromInterface, link, agent);
			
			
		} else if(event instanceof RSMessage){
			
			System.out.println("Router recv msg, router solicitation at time " + SimEngine.getTime());
					
			RSMessage r = ((RSMessage) event);
			
			//send RA to node
			SimEnt sendNext = (Link) _routingTable[r.toInterface()].link();
			NetworkAddr newAddress = new NetworkAddr((r.toInterface()+1), r.getSrc().nodeId());
			_routingTable[r.toInterface()].setAddress(newAddress);
			
			
			System.out.println("Router sent msg, router advertisement at time " + SimEngine.getTime());
			send(sendNext, new RAMessage(newAddress),0);
		}
	}


	private void switchInterface(int _toInterface, Node _node) {

		//Clear the old table entry
		int id = getInterfaceId(_node.getAddr().networkId());
		Link oldLink = (Link) _routingTable[id].link();
		_routingTable[id] = new RouteTableEntry(oldLink, null);
		
		//Insert new link and node into table
		Link newLink = (Link) _routingTable[_toInterface].link();
		_node.forceSetPeer(newLink);
		connectInterface(_toInterface, newLink, _node);
	}	
	
}
