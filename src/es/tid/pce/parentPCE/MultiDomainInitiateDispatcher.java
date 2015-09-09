package es.tid.pce.parentPCE;

import java.io.DataOutputStream;
import java.net.Inet4Address;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import es.tid.pce.computingEngine.IniProcessorThread;
import es.tid.pce.computingEngine.InitiationRequest;
import es.tid.pce.pcep.constructs.PCEPIntiatedLSP;
import es.tid.pce.pcep.messages.PCEPInitiate;
import es.tid.tedb.ReachabilityManager;

public class MultiDomainInitiateDispatcher {
	 /**
	  * Queue to add Initiate.
	  * This queue is read by the request processor threads. 
	  */
	 private LinkedBlockingQueue<InitiationRequest> lspIniRequestQueue;
	 
	 private Thread iniProcessorThread;
	 
	 private ChildPCERequestManager childPCERequestManager;
	 
	 /**
	  * The logger
	  */
	 private Logger log;
	 
	 private ReachabilityManager reachabilityManager;

	public MultiDomainInitiateDispatcher(ReachabilityManager reachabilityManager) {
		log=Logger.getLogger("PCEServer");
		this.reachabilityManager=reachabilityManager;
	    lspIniRequestQueue = new LinkedBlockingQueue<InitiationRequest>();
	    iniProcessorThread= new IniProcessorThread(lspIniRequestQueue, reachabilityManager, childPCERequestManager);
        iniProcessorThread.start();
	}
	
	
	public void dispathInitiate(PCEPInitiate iniMessage, DataOutputStream out, Inet4Address remotePeerIP)
	{	    	
		log.info("Dispatching Initiate message from "+remotePeerIP);	
		Iterator<PCEPIntiatedLSP> it=iniMessage.getPcepIntiatedLSPList().iterator();
		while (it.hasNext()){
			InitiationRequest ir=new InitiationRequest();
			ir.setOut(out);
			ir.setRemotePeerIP(remotePeerIP);
			ir.setLspIniRequest(it.next());
			lspIniRequestQueue.add(ir);
		}
		
	}
	 
	 

}
