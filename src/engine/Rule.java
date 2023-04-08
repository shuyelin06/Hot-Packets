package engine;

import core.objects.Device;
import core.objects.Packet;

/* An iptable rule with ACCEPT and DROP options
 * Can filter the sender
 */
public class Rule {
	
	public enum RuleType { ACCEPT, REJECT, DROP }
	
	// Either ACCEPT, REJECT, or DROP
	private RuleType rule;
	// Holds the device that is the sender of the packet
	private Device source;
	// Holds the protocol that is filtered (TCP/UDP)
	private Packet.Protocol protocol;

	public Rule(RuleType rule, Device source, Packet.Protocol protocol) {
		this.rule = rule;
		this.source = source;
		this.protocol = protocol;
	}
	
	// Returns rule (ACCEPT or DROP)
	public RuleType getRule() {
		return rule;
	}
	
	public Device getSource() {
		return source;
	}
	
	public Packet.Protocol getProtocol() {
		return protocol;
	}
}
