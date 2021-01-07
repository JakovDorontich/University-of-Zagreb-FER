#! /bin/sh


scan() {
    PORTOVI="-p20-25,53,80,443"
    himage $1 nmap -n -Pn $PORTOVI $2 | egrep --color=never '(open|filte|close)'
}

start_fw() {
    echo ""
    echo "Starting FW..."
    hcp FW.sh FW:
    himage FW ./FW.sh reload
}

start_fw_int() {
    echo ""
    echo "Starting FW_int..."
    hcp FW_int.sh FW_int:
    himage FW_int ./FW_int.sh reload
}

inside() {
    echo ""
    echo "*****"
    echo "* int1 (LAN) --> web server (DMZ)"
    echo "* ftp, ssh and http should be open (other ports are closed)"
    echo "*"
    scan int1 192.168.2.11

    echo ""
    echo "*****"
    echo "* int1 (LAN) --> mail-relay (DMZ)"
    echo "* ssh and smtp should be open (other ports are closed)"
    echo "*"
    scan int1 192.168.2.10

    echo ""
    echo "*****"
    echo "* int1 (LAN) --> mail (LAN)"
    echo "* ssh, telnet and smtp should be open (other ports are closed)"
    echo "*"
    scan int1 192.168.1.10

    echo ""
    echo "*****"
    echo "* int1 (LAN) --> FW"
    echo "* only ssh should be open (other ports are filtered)"
    echo "*"
    scan int1 192.168.2.1

    echo ""
    echo "*****"
    echo "* int2 (LAN) --> FW"
    echo "* everything should be filtered"
    echo "*"
    scan int2 192.168.2.1
}

outside() {
    echo ""
    echo "*****"
    echo "* pc (Internet) --> web server (DMZ)"
    echo "* http should be open, https closed (other ports are filtered)"
    echo "*"
    scan pc 192.168.2.11

    echo ""
    echo "*****"
    echo "* pc (Internet) --> mail-relay (DMZ)"
    echo "* smtp should be open (other ports are filtered)"
    echo "*"
    scan pc 192.168.2.10
 
    echo ""
    echo "*****"
    echo "* pc (Internet) --> mail (LAN)"
    echo "* everything should be filtered"
    echo "*"
    scan pc 192.168.1.10

    echo ""
    echo "*****"
    echo "* pc (Internet) --> NAT mail (LAN)"
    echo "* everything should be filtered"
    echo "*"
    scan pc 192.168.2.2

    echo ""
    echo "*****"
    echo "* pc (Internet) --> FW (eth0)"
    echo "* everything should be filtered"
    echo "*"
    scan pc 192.0.2.1

    echo ""
    echo "*****"
    echo "* pc (Internet) --> FW (eth1)"
    echo "* everything should be filtered"
    echo "*"
    scan pc 192.168.2.1

    echo ""
    echo "*****"
    echo "* pc (Internet) --> FW_int"
    echo "* everything should be filtered"
    echo "*"
    scan pc 192.168.2.2
}

test_all() {
    start_fw
    start_fw_int
    inside
    outside
}

cmd=$1
test -z "$cmd" && {
    cmd="all"
}

case "$cmd" in
    outside)
        outside
        ;;

    inside)
        inside
        ;;

    all)
        test_all
        ;;

    start_fw)
        start_fw
        ;;

    start_fw_int)
        start_fw_int
        ;;

    *)
        echo "Usage $0 [outside|inside|all|start_fw|start_fw_int]"
        ;;
esac

