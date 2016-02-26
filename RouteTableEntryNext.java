package Sim;

public class RouteTableEntryNext extends TableEntry{
	
	private NetworkAddr _dest;
	private NetworkAddr _nextHop;
	
	RouteTableEntryNext(NetworkAddr dest, NetworkAddr nextHop)
	{
		super();
		_dest = dest;
		_nextHop = nextHop;
	}
	
	public NetworkAddr dest(){
		return _dest;
	}
	
	public NetworkAddr nextHop(){
		return _nextHop;
	}

}
