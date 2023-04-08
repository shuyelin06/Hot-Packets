package engine;

import core.objects.Device;

/* An iptable rule with ACCEPT and DROP options
 * Can filter the sender
 */
public class Rule {
	
	// Either ACCEPT, REJECT, or DROP
	private String rule;
	// Holds the device that is the sender of the packet
	private Device source;
	// Holds the protocol that is filtered (TCP/UDP)
	private String protocol;

	public Rule(String rule, Device source, String protocol) {
		this.rule = rule;
		this.source = source;
		this.protocol = protocol;
	}
	
	// Returns rule (ACCEPT or DROP)
	public String getRule() {
		return rule;
	}
	
	public Device getSource() {
		return source;
	}
	
	public String getProtocol() {
		return protocol;
	}
}
