package core.protocols;

import core.objects.Device;

public class NatRule extends Rule {
	public enum RuleType {PREROUTING, POSTROUTING};
	
	// Rule can be either PREOUTING or POSTROUTING
	private RuleType routingRule;
	
	// Holds the device that is the sender of the packet
	private int[] SNAT;	
	// Holds the device that is the reciever of the packet
	private int[] DNAT;
	
	public NatRule(RuleType rule, int[] sourceIP, Device destination) {
		routingRule = rule;
		SNAT = sourceIP;
		DNAT = destination.getIP();
	}
	
	public RuleType getRule() {
		return routingRule;
	}
	
	public int[] getSnat() {
		return SNAT;
	}
	
	public int[] getDnat() {
		return DNAT;
	}
}
