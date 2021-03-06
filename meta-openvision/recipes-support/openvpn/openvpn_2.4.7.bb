SUMMARY = "A full-featured SSL VPN solution via tun device."
HOMEPAGE = "https://openvpn.net/"
SECTION = "net"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=7aee596ed2deefe3e8a861e24292abba"
DEPENDS = "lzo openssl iproute2 ${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'libpam', '', d)}"

inherit autotools systemd update-rc.d upx_compress

SRC_URI = "http://swupdate.openvpn.org/community/releases/${BP}.tar.gz \
           file://openvpn \
           file://openvpn@.service \
           file://openvpn-volatile.conf"

SRC_URI[md5sum] = "9d67cabc9b0441062ebd4e12bb7dfedb"
SRC_URI[sha256sum] = "73dce542ed3d6f0553674f49025dfbdff18348eb8a25e6215135d686b165423c"

SYSTEMD_SERVICE_${PN} += "openvpn@loopback-server.service openvpn@loopback-client.service"
SYSTEMD_AUTO_ENABLE = "disable"

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME_${PN} = "openvpn"
INITSCRIPT_PARAMS_${PN} = "start 10 2 3 4 5 . stop 70 0 1 6 ."

CFLAGS += "-fno-inline"

# I want openvpn to be able to read password from file (hrw)
EXTRA_OECONF += "--enable-iproute2"
EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES', 'pam', '', '--disable-plugin-auth-pam', d)}"

# Explicitly specify IPROUTE to bypass the configure-time check for /sbin/ip on the host.
EXTRA_OECONF += "IPROUTE=${base_sbindir}/ip"

do_install_append() {
    install -d ${D}${sysconfdir}/init.d
    install -m 755 ${WORKDIR}/openvpn ${D}${sysconfdir}/init.d

    install -d ${D}${sysconfdir}/openvpn
    install -d ${D}${sysconfdir}/openvpn/sample
    install -m 755 ${S}/sample/sample-config-files/loopback-server  ${D}${sysconfdir}/openvpn/sample/loopback-server.conf
    install -m 755 ${S}/sample/sample-config-files/loopback-client  ${D}${sysconfdir}/openvpn/sample/loopback-client.conf
    install -dm 755 ${D}${sysconfdir}/openvpn/sample/sample-keys
    install -m 644 ${S}/sample/sample-keys/* ${D}${sysconfdir}/openvpn/sample/sample-keys
}

PACKAGES =+ " ${PN}-sample "

RDEPENDS_${PN} = "kernel-module-tun"

FILES_${PN}-dbg += "${libdir}/openvpn/plugins/.debug"
FILES_${PN} += "${systemd_unitdir}/system/openvpn@.service \
                ${sysconfdir}/tmpfiles.d \
               "
FILES_${PN}-sample += "${systemd_unitdir}/system/openvpn@loopback-server.service \
                       ${systemd_unitdir}/system/openvpn@loopback-client.service \
                       ${sysconfdir}/openvpn/sample/"
