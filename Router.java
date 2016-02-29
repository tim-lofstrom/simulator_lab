package Sim;

import java.util.ArrayList;

// This class implements a simple router

public class Router extends SimEnt{

	private RouteTableEntry [] _routingTable;
	private ArrayList <RouteTableEntryNext> _next;
	private int _interfaces;
	private int _now=0;
	private NetworkAddr _id;
	private int _ripTimeUpdateInterval;
	private int _stopSendingAfter;
	private int _ripCounter;

	
	public void StartRIP(int number, int timeinterval){
		_stopSendingAfter = number;
		_ripTimeUpdateInterval = timeinterval;
		_ripCounter = 0;
		send(this,new TimerEvent(), _ripTimeUpdateInterval);
	}
	
	// When created, number of interfaces are defined
	
	Router(int interfaces, int network, int node)
	{
		_routingTable = new RouteTableEntry[interfaces];
		_next = new ArrayList<RouteTableEntryNext>();
		_interfaces=interfaces;
		_id = new NetworkAddr(network, node);
	}
	
	
	// This method connects links to the router and also informs the 
	// router of the host connects to the other end of the link
	
	public void connectInterface(int interfaceNumber, SimEnt link, SimEnt node)
	{
		if (interfaceNumber<_interfaces)
		{
			NetworkAddr addr = null;
			
			if(node != null){
				
				//Check if the node is another router or if it is a host
				if(node.getClass() == this.getClass()){
					addr = ((Router) node).getAddr();
				} else if ((node.getClass() == Node.class) || (node.getClass() == HomeAgent.class)){
					addr = ((Node) node).getAddr();
				}
				
			}
			_routingTable[interfaceNumber] = new RouteTableEntry(link, addr);
		}
		else{
			System.out.println("Trying to connect to port not in router");			
		}
		
		((Link) link).setConnector(this);
	}

	// This method searches for an entry in the routing table that matches
	// the network number in the destination field of a messages. The link
	// represents that network number is returned
	
	private NetworkAddr getAddr() {
		return _id;
	}


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
				
//				if( ((Node) _routingTable[i].node()) != null && (((Node) _routingTable[i].node()).getAddr().networkId() == networkAddress))
//				{
//					routerInterface = _routingTable[i].link();
//				}
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
		if(event instanceof TimerEvent){
			if(_ripCounter < _stopSendingAfter){
				for(int i = 0; i < _interfaces; i++){
					NetworkAddr dest = _routingTable[i].nodeAddress();
					if(dest != null){
						SimEnt _if = getInterface(dest.networkId());
						Event rip = new RIPMessage(_next, dest, _id);
						
						//Send RIP Message to all interfaces
						send(_if, rip, 0);						
					}

				}
				
				
				send(this, new TimerEvent(), _ripTimeUpdateInterval);
				_ripCounter++;
			}
		}
		if ((event instanceof Message) || (event instanceof BUMessage) || (event instanceof BAMessage))
		{
//			System.out.println("Router " + _id.networkId() + " handles packet with seq: " + ((Message) event).seq()+" from node: "+((Message) event).source().networkId()+"." + ((Message) event).source().nodeId() );
			SimEnt sendNext = getInterface(((Message) event).destination().networkId());
			if (sendNext == null){
				
				NetworkAddr nextHop = getNextHop(((Message) event).destination());
				
				if(nextHop != null){
					sendNext = getInterface(nextHop.networkId());
				}
				
				if(sendNext == null){
					System.out.println("No send next found for address " + ((Message) event).destination().networkId() + "." + ((Message) event).destination().nodeId());
				}else{
					send(sendNext, event, _now);
				}
				
			} else {
//				int interfaceID = getInterfaceId(((Message) event).destination().networkId());
//				System.out.println("Router sends to node: " + ((Message) event).destination().networkId()+"." + ((Message) event).destination().nodeId() + " on interface " + interfaceID);
				send(sendNext, event, _now);
			}
	
	
		} else if(event instanceof MoveMessage){

			MoveMessage m = ((MoveMessage) event);
			int fromInterface = getInterfaceId(m.getSrc().networkId());
			
			//Create HomeAgent
			if(m.createHomeAgent == true){
				HomeAgent agent = new HomeAgent(m.getSrc().networkId(), m.getSrc().nodeId());
				Link link = (Link) _routingTable[fromInterface].link();
				agent.forceSetPeer(link);
				connectInterface(fromInterface, link, agent);
				System.out.println("Home agent with address " + m.getSrc().networkId() + "." + m.getSrc().nodeId() + " created on router " + _id.networkId());
			}else {
				System.out.println("Node " + m.getSrc().networkId() + "." + m.getSrc().nodeId()+ " moved to interface " + m._toInterface + " at time " + SimEngine.getTime());
				switchInterface(m._toInterface, m._node);
			}
			

			
		} else if(event instanceof RSMessage){
			
			System.out.println("Router recv msg, router solicitation at time " + SimEngine.getTime());
					
			RSMessage r = ((RSMessage) event);
			
			//send RA to node
			SimEnt sendNext = (Link) _routingTable[r.toInterface()].link();
			NetworkAddr newAddress = getNewAddress();
			_routingTable[r.toInterface()].setAddress(newAddress);
			
			
			System.out.println("Router sent msg, router advertisement at time " + SimEngine.getTime());
			send(sendNext, new RAMessage(newAddress),0);
		} else if (event instanceof RIPMessage){
			
//			System.out.println("Router " + _id.networkId() + " recv msg, rip");
			
			RIPMessage r = ((RIPMessage)event);
			
			for(RouteTableEntryNext t : r.table()){
				addNetworkFromRIP(t.dest(), r.src());
			}
			addNetworkFromRIP(r.src(), r.src());
		}
	}
	
	private void addNetworkFromRIP(NetworkAddr dest, NetworkAddr nextHop){
		for(RouteTableEntryNext t : _next){
			if((t.dest() == dest) || (dest == _id)){
				//Update the entry here
				return;
				
			}
		}
		_next.add(new RouteTableEntryNext(dest, nextHop));
		
	}
	
	private NetworkAddr getNewAddress(){
		
		int _interface = 0; 
		
		// We define every router to have 9 usable addresses, get the first free one.
		for(int i = 1; i < 10; i++){
			
			_interface = getInterfaceId(i);

			// Interface ID is -1 if it is free
			if(_interface == -1){
				return new NetworkAddr(_id.networkId() + i, 1);
			}
		}
		
		return null;
	}

	private void clearTableEntry(int _toInterface, Node _node){
		
		//Clear Table entry
		int id = getInterfaceId(_node.getAddr().networkId());
		Link oldLink = (Link) _routingTable[id].link();
		_routingTable[id] = new RouteTableEntry(oldLink, null);		
	}

	private void switchInterface(int _toInterface, Node _node) {

		//Insert new link and node into table
		Link newLink = (Link) _routingTable[_toInterface].link();
		_node.forceSetPeer(newLink);
		connectInterface(_toInterface, newLink, _node);
	}
	
	public NetworkAddr getNextHop(NetworkAddr dest){
		//lookup in routetableentrynext
		NetworkAddr nextHopAddr = null;
		
		for(int i = 0; i < _next.size(); i++){
			if(dest.networkId()/10 == _next.get(i).dest().networkId()/10){
				nextHopAddr = _next.get(i).nextHop();
				return nextHopAddr;
			}
		}
		return nextHopAddr;
	}
	
	public void setPeer(SimEnt peer) {
//		_peer = peer;

		if (peer instanceof Link) {
			((Link) peer).setConnector(this);
		}
	}
	
}
