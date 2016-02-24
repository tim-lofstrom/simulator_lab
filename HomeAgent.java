package Sim;


// This class implements a node (host) it has an address, a peer that it communicates with
// and it count messages send and received.

public class HomeAgent extends Node {
	
	private SimEnt _peer;
	private NetworkAddr _id;
	private NetworkAddr _cn;
	private NetworkAddr _mn;
	
	public HomeAgent(int network, int node) {
		super(network, node);
		_id = new NetworkAddr(network, node);
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

	// This method is called upon that an event destined for this node triggers.

	public void recv(SimEnt src, Event ev) {

		if (ev instanceof Message) {
			
			int network = ((Message) ev).source().networkId();
			
			//Pass the message to either MN or CN
			if(network == _cn.networkId()){
				
				//if message is from CN, redirect to MN
				((Message) ev).setDestination(_mn);
				send(_peer, ev, 0);
			} else if(network == _mn.networkId()){
				
				//if message is from MN, redirect to CN
				((Message) ev).setDestination(_cn);
				send(_peer, ev, 0);
			}
			
			
		} else if (ev instanceof BUMessage){
			
			//send to the _mn an BAMessage
			send(_peer, new BUMessage(_mn),0);
		}
	}
	
}
