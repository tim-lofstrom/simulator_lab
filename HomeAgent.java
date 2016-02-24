package Sim;


// This class implements a node (host) it has an address, a peer that it communicates with
// and it count messages send and received.

public class HomeAgent extends Node {
	
	private SimEnt _peer;
	private NetworkAddr _id;
	private NetworkAddr _cn;
	private NetworkAddr _mn;
	
	public HomeAgent(int network, int addr) {
		super(network, addr);
		_id = new NetworkAddr(network, addr);
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
	
	public void setMobileNode(NetworkAddr mn){
		_mn = mn;
	}
	
	public void setCorrespondingNode(NetworkAddr cn){
		_cn = cn;
	}

	// **********************************************************************************

	// This method is called upon that an event destined for this node triggers.

	public void recv(SimEnt src, Event ev) {

		
		if(ev instanceof BUMessage){
			
			BUMessage bu = ((BUMessage) ev);
			_mn = bu.getMobileNodeAddress();
			_cn = bu.getCorrespondingNodeAddress();

			//send to the _mn an BAMessage
			send(_peer, new BAMessage(_mn, _id),0);
			
		} else if (ev instanceof Message) {
			
			int network = ((Message) ev).source().networkId();
			
			//Pass the message to either MN or CN
			if ((_cn != null) && (network == _cn.networkId())){
				
				//if message is from CN, redirect to MN
				((Message) ev).setDestination(_mn);
				System.out.println("Home Agent passes to: " + _mn.networkId());
				send(_peer, ev, 0);
			} else if((_mn != null) && (network == _mn.networkId())){
				
				//if message is from MN, redirect to CN
				((Message) ev).setDestination(_cn);
				System.out.println("Home Agent passes to: " + _cn.networkId());
				send(_peer, ev, 0);
			} else {
				System.out.println("Packet lost... or something...?");
			}
			
			
		}
	}
	
}
