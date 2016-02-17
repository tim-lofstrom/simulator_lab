package Sim;

// This class implements a node (host) it has an address, a peer that it communicates with
// and it count messages send and received.

public class Node extends SimEnt {
	private TrafficGenerator _generator;
	private NetworkAddr _id;
	private SimEnt _peer;
	private int _sentmsg = 0;
	private int _seq = 0;
	
	public Node(int network, int node) {
		super();
		_id = new NetworkAddr(network, node);
	}
	
	public void Move(int time, int inteface){
		send(this, new MoveMessage(inteface, this), time);
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
		
		//Initialize the statistics so it knows source, destination and how many packets that will be sent
		Statistics.Initialize(_id, new NetworkAddr(network, node),_stopSendingAfter, startSeq);
		
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
				send(_peer, new Message(_id, new NetworkAddr(_toNetwork, _toHost), _seq), 0);
				send(this, new TimerEvent(), tempArriveTime);
				
				//add a time stamp for a sending a message
				Statistics.addTime(_id, new NetworkAddr(_toNetwork, _toHost), _seq, (int)SimEngine.getTime());
				
				System.out.println("Node " + _id.networkId() + "." + _id.nodeId() + " sent message with seq: " + _seq
						+ " at time " + SimEngine.getTime());
				
				_seq++;
			}
		}
		if (ev instanceof Message) {

			
//			System.out.println("Node " + _id.networkId() + "." + _id.nodeId() + " receives message with seq: "
//					+ ((Message) ev).seq() + "" + " at time " + SimEngine.getTime());
			
			
			//add a timestamp for a Received message
			Statistics.addTime(((Message) ev).source(), _id, ((Message) ev).seq(), (int)SimEngine.getTime());

		}else if(ev instanceof MoveMessage){
			
			MoveMessage m = ((MoveMessage) ev);
			
			if (m._node == this){
				send(_peer, m, 0);
				
			} else if ((m._node.getAddr().networkId() == _toNetwork) && (m._node.getAddr().nodeId() == _toHost)){
				System.out.println("the host we are sending to moved");
			}
			
			
			
//			System.out.println("Node: " + ((MoveMessage) ev).node().getAddr().networkId() + " moved to interface "
//					+ "" + ((MoveMessage) ev)._toInterface + " at time " + SimEngine.getTime());
//			MoveMessage m = ((MoveMessage) ev);
//			switchInterface(m._toInterface, m._node);
		}
	}
	
}
