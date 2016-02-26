package Sim;

import java.util.ArrayList;

public class RIPMessage implements Event{

	private ArrayList <RouteTableEntryNext> _table;
	private NetworkAddr _dest;
	private NetworkAddr _src;
	
	RIPMessage(ArrayList <RouteTableEntryNext> table, NetworkAddr dest, NetworkAddr src)
	{
		super();
		_table = table;
		_dest = dest;
		_src = src;
	}
	
	public NetworkAddr dest(){
		return _dest;
	}
	
	public NetworkAddr src(){
		return _src;
	}
	
	public ArrayList <RouteTableEntryNext> table(){
		return _table;
	}

	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub
		
	}
}
