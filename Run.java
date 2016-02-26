package Sim;


// An example of how to build a topology and starting the simulation engine

public class Run {
	public static void main (String [] args)
	{
 		//Creates links
 		Link link1 = new LossyLink(5,6,0);
		Link link2 = new LossyLink(5,7,0);
		Link link3 = new LossyLink(5,8,0);
		
		Link link4 = new LossyLink(5,6,0);
		Link link5 = new LossyLink(5,7,0);
		Link link6 = new LossyLink(5,8,0);
		
		// Create two end hosts that will be
		// communicating via the router
		Node host1 = new Node(1,1);
		Node host2 = new Node(2,1);
		
		//Measure statistics from host1 to host2 and vice verca
		Statistics.Initialize(host1, host2);
//		Statistics.Initialize(host2, host1);

		//Connect links to hosts
		host1.setPeer(link1);
		host2.setPeer(link2);

		// Creates as router and connect
		// links to it. Information about 
		// the host connected to the other
		// side of the link is also provided
		// Note. A switch is created in same way using the Switch class
		Router routeNode = new Router(3);
		routeNode.connectInterface(0, link1, host1);
		routeNode.connectInterface(1, link2, host2);
		routeNode.connectInterface(2, link3, null);
		
		Router routeNode2 = new Router(3);
		routeNode2.connectInterface(0, link3, null);
		routeNode2.connectInterface(1, link4, null);
		routeNode2.connectInterface(2, link5, null);
				
		//Creates a ConstantBitRate traffic generator with 100 ms interval
		TrafficGenerator cbr = new CBRGenerator(5);
		
		//Creates a NormalDistribution traffic generator with 100 ms interval and 20 ms deviation
		TrafficGenerator ndf = new NDFGenerator(100, 500);
		
		//Creates a PoissonDistributedFunction traffic generator with 100 ms interval and a deviation accoring to Poisson
		TrafficGenerator pdf = new PDFGenerator(100);
		
		// host1 will send 50 messages with ndf generator to network 2, node 1. Sequence starts with number 1
		host1.StartSending(2, 1, 10, cbr, 1);
		
		//Move host1 to interface 3 after 25 ms
		host2.Move(20, 2, routeNode, routeNode2);
		
		// host2 will send 50 messages with pdf generator to network 1, node 1. Sequence starts with number 50
//		host2.StartSending(1, 1, 10, cbr, 1); 
		
		// Start the simulation engine and of we go!
		Thread t=new Thread(SimEngine.instance());
	
		t.start();
		try
		{
			t.join();
			
			//When the simulation is done, print the statistics about generated traffic.
//			System.out.println("Times by NDF");
//			ndf.printTimes();
//			
//			System.out.println("Times by PDF");
//			pdf.printTimes();
			
			//Lab2
//			System.out.println("Times by PDF, Latex:");
//			ndf.printTimesLatex();
//			
//			System.out.println("Times by PDF, Latex Sorted");
//			ndf.printTimesLatexSorted();

//			System.out.println("Times by NDF, Latex");
//			ndf.printTimesLatex();
//			
//			System.out.println("Times by NDF, Latex Sorted");
//			ndf.printTimesLatexSorted();
			
			
			//print
//			Statistics.printTimesReceive();
//			Statistics.printTimes();
//			Statistics.printTimesLaTexFormat();
			
//			Statistics.printTimesReceivePlot();
			
			
			
		}
		catch (Exception e)
		{
			System.err.println(e);
			System.out.println("The motor seems to have a problem, time for service?");
		}		



	}
}
