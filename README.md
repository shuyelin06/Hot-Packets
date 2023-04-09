# Hot Packets
##### Creators: Wendy Tu, Brian Xie, Anna Dai, Shu-Ye Lin

### About
Built using the Slick2D graphics library, Hot Packets is a network simulation tool envisioned during and developed at <a href = "https://bitcamp2023.devpost.com/?ref_feature=challenge&ref_medium=discover">Bitcamp 2023</a>. Winning second place in the **Best Bitcamp Hack** awards category (<a href = "https://devpost.com/software/hot-packets?ref_content=user-portfolio&ref_feature=in_progress">see here!</a>), Hot Packets simulates the intricacies of packet flow throughout a network system in an easy-to-understand manner, and even supports `iptable` filter and NAT rules.

To interact with the simulation, the user is provided with simple sliders (to adjust simulation settings) and a command prompt for additional functionality. 

Supported commands are given below:
- `create device [device ip]`: Creates a device on the network
- `connect [source ip] to [destination ip]`: Creates a connection from `source ip` outwards to `destination ip`. 
- `packet [source ip] to [destination ip] [protocol]`: Sends a packet from the source IP to the destination IP, under some protocol (`TCP` or `UDP`).
- `nat postroute on [host ip] [old ip] to [new ip]`: Adds a NAT postrouting rule onto the host device. If the destination IP on a packet matches `old ip`, it will be fowarded to `new ip`.
- `nat preroute on [host ip] [old ip] to [new ip]`: Adds a NAT prerouting rule onto the host device. If the source IP on a packet matches `old ip`, it will be forwarded to `new ip`.
- `filter on [host ip] [src ip] to [dest ip] [protocol] [rule]`: Adds a filter to a host device. If a packet's source, destination, and protocol match that of the filter's, the rule (`ACCEPT`, `REJECT`, `DROP`) will be applied to that packet.
- `ping from [host ip]`: Toggles a flag on devices, causing them to send packets outwards (to random connected devices). Useful for traffic congestion analysis.

Such a simulation tool may serve as a useful basis for testing and researching largescale networks without risking the integrity of the actual network. It can be used for traffic congestion analysis, attacker behavior analysis, and educational purposes.
