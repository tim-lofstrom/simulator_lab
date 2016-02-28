package Sim;

// This class implements a node (host) it has an address, a peer that it communicates with
// and it count messages send and received.

public class Node extends SimEnt {
	private TrafficGenerator _generator;
	private NetworkAddr _id;
	private NetworkAddr _homeAddress;
	private SimEnt _peer;
	private int _sentmsg = 0;
	private int _seq = 0;
	
	private boolean routeOptimization = true;
	
	public Node(int network, int node) {
		super();
		_id = new NetworkAddr(network, node);
	}
	
		
	public void Move(int time, int inteface, Router fromRouteNode, Router toRouteNode){
		
		//set to trigger movemessage in router
		send(toRouteNode, new MoveMessage(inteface, this, _id, false), time);
		
		// send trigger for home node that we unplugged
		send(fromRouteNode, new MoveMessage(inteface, this, _id, true), time+1);
		
		//set to trigger movemessage in node
		send(this, new MoveMessage(inteface, this, _id, false), time+2);
	}
	
	public void setNewNetworkAddr(int network, int node){
		_id = new NetworkAddr(network, node);
	}

	// Sets the peer to communicate with. This node is single homed
	public void setPeer(SimEnt peer) {
		_peer = peer;

		if (_peer instanceof Link) {
			((Link) _peer).setConnector(this);
		}
	}
	
	public void forceSetPeer(SimEnt peer) {
		_peer = peer;

		if (_peer instanceof Link) {
			((Link) _peer).setConnectorA(this);
		}
	}

	public NetworkAddr getAddr() {
		return _id;
	}

	// **********************************************************************************
	// Just implemented to generate some traffic for demo.
	// In one of the labs you will create some traffic generators
	
	private int _stopSendingAfter = 0; // messages
	private int _toNetwork = 0;
	private int _toHost = 0;
	
	//	Lab 2
	//	TrafficGenerator
	public void StartSending(int network, int node, int number, TrafficGenerator generator, int startSeq){
		_generator = generator;
		_stopSendingAfter = number;
		_toNetwork = network;
		_toHost = node;
		_seq = startSeq;
			
		send(this, new TimerEvent(), 0);
	}

	// **********************************************************************************

	// This method is called upon that an event destined for this node triggers.

	public void recv(SimEnt src, Event ev) {
		if (ev instanceof TimerEvent) {
			if (_stopSendingAfter > _sentmsg) {
				_sentmsg++;
			
				//Retrieve the generators next-time for send-event to happen.
				double tempArriveTime = _generator.getTimeBetweenSending();
				NetworkAddr dest;
				
				//If home_address is null, then we dont have any home agent, send the message directly to cn
				
				
				if(_homeAddress == null){
					dest = new NetworkAddr(_toNetwork, _toHost);
				} else {
					dest = _homeAddress;
				}
				send(_peer, new Message(_id, dest, _seq), 0);
				send(this, new TimerEvent(), tempArriveTime);
				
				//add a time stamp for a sending a message
				Statistics.addSendTime(this, _seq, (int)SimEngine.getTime());
				
//				System.out.println("Node " + _id.networkId() + "." + _id.nodeId() + " sent message with seq: " + _seq
//						+ " at time " + SimEngine.getTime());
				_seq++;
			}
		}
		
		if(ev instanceof BAMessage){
			System.out.println("Node " + _id.networkId() + "." + _id.nodeId() + " recv msg, bind acknowledgement at time " + SimEngine.getTime());
//			BAMessage ba = ((BAMessage) ev);
			if(routeOptimization == true){
				System.out.println("Node " + _id.networkId() + "." + _id.nodeId() + " send msg, route optimize at time " + SimEngine.getTime() + " to node " + 
			_toNetwork+ "." + _toHost);				
				send(_peer, new Message(_id, new NetworkAddr(_toNetwork, _toHost),1),0);
			}
			
		} else if (ev instanceof Message) {

			System.out.println("Node " + _id.networkId() + "." + _id.nodeId() + " receives message with seq: "
					+ ((Message) ev).seq() + "" + " at time " + SimEngine.getTime());
			
		
			Statistics.addRecvTime(this, ((Message) ev).seq(), (int)SimEngine.getTime());
			
			
			int old = _toNetwork;
			
			_toNetwork = ((Message) ev).source().networkId();
			_toHost = ((Message) ev).source().nodeId();
			
			if(old != _toNetwork){
				System.out.println("Node " + _id.networkId() + "." + _id.nodeId() + " recv msg, update send address at time " + SimEngine.getTime() + " to " +
			_toNetwork + "." + _toHost);	
			}
			

		}else if(ev instanceof MoveMessage){
			MoveMessage m = ((MoveMessage) ev);
			
//			System.out.println(((LossyLink)_peer).maxDelay);
			
			//Send MoveEven directly to router and not via the Link.
//			send(m.getRouter(), m, 0);

//			System.out.println(((LossyLink)_peer).maxDelay);
			
			//Send router solicitation to the new router, this goes via the link.
			
			send(_peer, new RSMessage(m._toInterface,  _id), 0);
			
		} else if (ev instanceof RAMessage){
			_homeAddress = _id;
			_id = ((RAMessage) ev).getNewAddress();
			System.out.println("Node " + _homeAddress.networkId() + "." + _homeAddress.nodeId() +" got new address: " + _id.networkId() + "." + _id.nodeId() + " at time " + SimEngine.getTime());
			
			send(_peer, new BUMessage(_homeAddress, new NetworkAddr(_toNetwork, _toHost), _id), 0);
		}
	}

	
	//Compares two nodes by reference
	public boolean compareTo(Node node) {
		return this == node;
	}
	
}
