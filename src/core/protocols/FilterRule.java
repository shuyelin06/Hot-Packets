package core.protocols;

import core.objects.Packet;

public class FilterRule extends Rule {
public enum RuleType { ACCEPT, REJECT, DROP }
	
	// Either ACCEPT, REJECT, or DROP
	private RuleType rule;
	// Holds the device that is the sender of the packet
	private int[] sourceIP;
	private int netmask;
	// Holds the protocol that is filtered (TCP/UDP)
	private Packet.Protocol protocol;

	public FilterRule(RuleType rule, int[] sourceIP, int netmask, 
			Packet.Protocol protocol) {
		this.rule = rule;
		this.sourceIP = sourceIP;
		this.netmask = netmask;
		this.protocol = protocol;
	}
	
	// Returns rule (ACCEPT or DROP)
	public RuleType getRule() {
		return rule;
	}
	
	public int[] getSourceIP() {
		return sourceIP;
	}
	
	public int getNetmask() {
		return netmask;
	}
	
	public Packet.Protocol getProtocol() {
		return protocol;
	}
}
