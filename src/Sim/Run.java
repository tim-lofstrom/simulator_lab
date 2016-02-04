package Sim;

// An example of how to build a topology and starting the simulation engine

public class Run {
	public static void main (String [] args)
	{
 		//Creates two links
 		Link link1 = new Link();
		Link link2 = new Link();
		
		//Creates two LossyLinks
/*		Link link1 = new LossyLink();
		Link link2 = new LossyLink();*/
		
		// Create two end hosts that will be
		// communicating via the router
		Node host1 = new Node(1,1);
		Node host2 = new Node(2,1);

		//Connect links to hosts
		host1.setPeer(link1);
		host2.setPeer(link2);

		// Creates as router and connect
		// links to it. Information about 
		// the host connected to the other
		// side of the link is also provided
		// Note. A switch is created in same way using the Switch class
		Router routeNode = new Router(2);
		routeNode.connectInterface(0, link1, host1);
		routeNode.connectInterface(1, link2, host2);
		
		// Generate some traffic
		// host1 will send 3 messages with time interval 5 to network 2, node 1. Sequence starts with number 1
		//host1.StartSending(2, 2, 3, 5, 1); 
		
		TrafficGenerator cbr = new CBRGenerator(100);
		TrafficGenerator ndf = new NDFGenerator(100, 500);
		TrafficGenerator pdf = new PDFGenerator(100);
		
		host1.StartSending(2, 1, 10, ndf, 1);
		// host2 will send 2 messages with time interval 10 to network 1, node 1. Sequence starts with number 10
		host2.StartSending(1, 1, 10, pdf, 10); 
		
		// Start the simulation engine and of we go!
		Thread t=new Thread(SimEngine.instance());
	
		t.start();
		try
		{
			t.join();
			
			//When the simulation is done, print the statistics about generated traffic.
			ndf.printTimes();
			pdf.printTimes();
			Statistics.printTimes();
		}
		catch (Exception e)
		{
			System.out.println(e);
			System.out.println("The motor seems to have a problem, time for service?");
		}		



	}
}
