#!/bin/sh 

PATH="/sbin:/usr/sbin:/bin:/usr/bin"
export PATH

reset_iptables() {
    iptables -P OUTPUT  DROP
    iptables -P INPUT   DROP
    iptables -P FORWARD DROP

    cat /proc/net/ip_tables_names | while read table; do
        iptables -t $table -L -n | while read c chain rest; do
            if test "X$c" = "XChain" ; then
                iptables -t $table -F $chain
            fi
        done
        iptables -t $table -X
    done
}

script_body() {
    # accept established sessions
    iptables -A INPUT   -m state --state ESTABLISHED,RELATED -j ACCEPT 
    iptables -A OUTPUT  -m state --state ESTABLISHED,RELATED -j ACCEPT 
    iptables -A FORWARD -m state --state ESTABLISHED,RELATED -j ACCEPT

    # 
    echo "... anti-spoofing rule (eth0)"
    # 
    iptables -A INPUT -i eth0   -s 192.0.2.1   -m state --state NEW  -j DROP
    iptables -A INPUT -i eth0   -s 192.168.1.0/24   -m state --state NEW  -j DROP
    #
    # <--- Dodajte ili modificirajte pravila 
	iptables -A INPUT -i eth0 -s 192.168.2.0/24 -m state --state NEW -j DROP
    iptables -A FORWARD -i eth0   -s 192.0.2.1   -m state --state NEW  -j DROP
    iptables -A FORWARD -i eth0   -s 192.168.1.0/24   -m state --state NEW  -j DROP
    iptables -A FORWARD -i eth0 -s 192.168.2.0/24 -m state --state NEW -j DROP 
    iptables -A FORWARD -i eth0   -s 192.0.2.1   -m state --state NEW  -j DROP
    #
    # <--- Dodajte ili modificirajte pravila 
    #

    # 
    echo "... accept all on loopback"
    # 
    iptables -A INPUT -i lo   -m state --state NEW  -j ACCEPT
    iptables -A OUTPUT -o lo   -m state --state NEW  -j ACCEPT

    # 
    echo "... ssh access to firewall only from int1"
    # 
    # <--- Dodajte pravila 
    iptables -A INPUT -i eth2 -p tcp -m tcp -s 192.168.1.21 -d 192.168.2.1 --dport 22 -m state --state NEW -j ACCEPT
    iptables -A OUTPUT -p tcp -m tcp -s 192.168.2.1 -d 192.168.1.21 --sport 22 -m state --state NEW -j ACCEPT
    #

    # 
    echo "... firewall uses one of the machines on internal network for DNS"
    #
    # <--- Dodajte pravila 
    iptables -A INPUT -s 192.0.2.1/24 -p udp --dport 53 -j ACCEPT
    iptables -A INPUT -s 192.0.2.1/24 -p tcp --dport 53 -j ACCEPT
	iptables -A OUTPUT -p tcp -m tcp --dport 53 -m state --state NEW -j ACCEPT
    iptables -A OUTPUT -p udp -m udp --dport 53 -m state --state NEW -j ACCEPT
    #

    # 
    echo "... RIP on FWs external interface"
    #
    iptables -A INPUT -i eth0  -p udp -m udp -s 192.0.2.100  -d 224.0.0.9  --sport 520 --dport 520   -m state --state NEW  -j ACCEPT
    iptables -A OUTPUT         -p udp -m udp -s 192.0.2.1    -d 224.0.0.9  --sport 520 --dport 520   -m state --state NEW  -j ACCEPT

    # 
    echo "... RIP on FWs internal interface"
    #
    iptables -A INPUT -i eth1  -p udp -m udp -s 192.168.2.2  -d 224.0.0.9  --sport 520 --dport 520   -m state --state NEW  -j ACCEPT
    iptables -A OUTPUT         -p udp -m udp -s 192.168.2.1  -d 224.0.0.9  --sport 520 --dport 520   -m state --state NEW  -j ACCEPT

    # 
    echo "... mail-relay on DMZ can accept connections from hosts on the Internet"
    #
    # <--- Dodajte pravila 
    iptables -A INPUT -p all -s 192.0.2.1/24 -d 192.168.2.10/24 -j ACCEPT
    #

    # 
    echo "... mail-relay needs DNS and can connect to mail servers on the Internet"
    #
    # <--- Dodajte pravila 
	iptables -A FORWARD -p tcp -m tcp --dport 53 -m state --state NEW -j ACCEPT
    iptables -A FORWARD -p udp -m udp --dport 53 -m state --state NEW -j ACCEPT
    iptables -A OUTPUT -s 192.168.2.10/24 -p udp --dport 25 -j ACCEPT
    #

    # 
    echo "... web server on DMZ must be reachable from the Internet"
    #
    # <--- Dodajte pravila 
    iptables -A INPUT -s 192.168.2.11/24 -p tcp -m tcp --sport 80 -j ACCEPT
    iptables -A OUTPUT -d 192.168.2.11/24 -p tcp -m tcp --dport 80 -j ACCEPT
    iptables -A INPUT -s 192.168.2.11/24 -p tcp -m tcp --sport 443 -j ACCEPT
    iptables -A OUTPUT -d 192.168.2.11/24 -p tcp -m tcp --dport 443 -j ACCEPT
    #

    echo "... SSH Access to mail-relay (on DMZ) is permitted only on port 1111"
    #
    # <--- Dodajte pravila
	iptables -A FORWARD -p tcp -m tcp -d 192.168.2.10 --dport 1111 -m state --state NEW -j ACCEPT
    #

    # 
    echo "... access from the internal NAT to the Internet"
    #
    iptables -A FORWARD  -s 192.168.2.2/32   -m state --state NEW  -j ACCEPT
}

block_action() {
    reset_iptables
}

stop_action() {
    reset_iptables
    iptables -P OUTPUT  ACCEPT
    iptables -P INPUT   ACCEPT
    iptables -P FORWARD ACCEPT
}

# missing argument is equivalent to 'start'
cmd=$1
test -z "$cmd" && {
    cmd="start"
}

case "$cmd" in
    start)
        reset_iptables 
        script_body
        echo 1 > /proc/sys/net/ipv4/ip_forward
        RETVAL=$?
        ;;

    stop)
        stop_action
        RETVAL=$?
        ;;

    block)
        block_action
        RETVAL=$?
        ;;

    reload)
        $0 stop
        $0 start
        RETVAL=$?
        ;;

    *)
        echo "Usage $0 [start|stop|block|reload]"
        ;;
esac

