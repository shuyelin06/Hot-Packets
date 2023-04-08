package core.protocols;

import java.util.ArrayList;

import core.objects.Device;
import core.objects.Packet;

/* An iptable rule with ACCEPT and DROP options
 * Can filter the sender
 */
public class Rule {
	public void getInfo(ArrayList<String> info) {
		info.add("Rule: NULL");
	}
	
}
