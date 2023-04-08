package core.protocols;

import java.util.ArrayList;

import core.objects.Packet;
import engine.Simulation;

public class FilterRule extends Rule {
public enum RuleType { ACCEPT, REJECT, DROP }
	
	// Either ACCEPT, REJECT, or DROP
	private RuleType rule;
	// Holds the device that is the sender of the packet
	private int[] sourceIP;
	
	// Holds the destination IP;
	private int[] destIP;
	
	private int sNetmask;
	private int dNetmask;
	
	// Holds the protocol that is filtered (TCP/UDP)
	private Packet.Protocol protocol;

	public FilterRule(RuleType rule, int[] sourceIP, int sNetmask, int[] destIP, 
			int dNetmask, Packet.Protocol protocol) {
		this.rule = rule;
		this.sourceIP = sourceIP;
		this.destIP = destIP;
		this.sNetmask = sNetmask;
		this.dNetmask = dNetmask;
		this.protocol = protocol;
	}
	
	// Get Info for the Rule. Overridden
	@Override
	public void getInfo(ArrayList<String> info) {
		info.add("Rule - Filter");
		info.add("  Block: TCP");
		info.add("  From: " + Simulation.IPStringCIDR(sourceIP));
		info.add("  To: 2.2.2.2/32");
	}
	
	// Returns rule (ACCEPT or DROP)
	public RuleType getRule() {
		return rule;
	}
	
	public int[] getSourceIP() {
		return sourceIP;
	}
	
	public int[] getDestIP() {
		return destIP;
	}
	
	public int getSNetmask() {
		return sNetmask;
	}
	
	public int getDNetmask() {
		return dNetmask;
	}
	
	public Packet.Protocol getProtocol() {
		return protocol;
	}
}
