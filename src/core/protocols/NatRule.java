package core.protocols;

import java.util.ArrayList;

import core.objects.Device;
import core.objects.Packet;
import engine.Simulation;

public class NatRule extends Rule {
	public enum RuleType {PREROUTING, POSTROUTING};
	public enum NatType {SNAT, DNAT};
	
	// Rule can be either PREOUTING or POSTROUTING
	private RuleType routingRule;
	
	// This rule performs either snat (changes source ip of packet) or dnat
	// (changes 
	private NatType natType;
	
	// Holds the new IP to be changed
	private int[] newIP;
	// Holds the IP to change;
	private int[] oldIP;
	
	public NatRule(NatType natType, int[] newIP, int[] oldIP) {
		this.natType = natType;
		this.newIP = newIP;
		this.oldIP = oldIP;
	}
	
	// Get Info for the Rule. Overridden
	@Override
	public void getInfo(ArrayList<String> info) {
		info.add("Rule - NAT");
		info.add("  " + (natType == NatType.SNAT ? "SNAT" : "DNAT"));
		info.add("  " + Simulation.IPStringCIDR(oldIP) + " -> " + Simulation.IPString(newIP));
	}
	
	public NatType getNatType() {
		return natType;
	}
	
	public int[] getNewIP() {
		return newIP;
	}
	
	public int[] getOldIP() {
		return oldIP;
	}

}
