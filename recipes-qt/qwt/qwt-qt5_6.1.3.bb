SUMMARY = "Qt Widget Extension for Technical Applications"
SECTION = "libs"
HOMEPAGE = "http://qwt.sourceforge.net/index.html"

# LGPLv2.1 + some exceptions
LICENSE = "QWTv1.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=dac2743472b0462ff3cfb4af42051c88"

DEPENDS = "qtbase qtsvg qttools"

inherit qmake5

SRC_URI = " \
    ${SOURCEFORGE_MIRROR}/qwt/qwt-${PV}.tar.bz2;name=qwt \
    file://0001-Remove-rpath-from-binaries-they-point-to-buuild-area.patch \
"
SRC_URI[qwt.md5sum] = "19d1f5fa5e22054d22ee3accc37c54ba"
SRC_URI[qwt.sha256sum] = "f3ecd34e72a9a2b08422fb6c8e909ca76f4ce5fa77acad7a2883b701f4309733"

S = "${WORKDIR}/qwt-${PV}"

EXTRA_QMAKEVARS_PRE += " \
    QWT_CONFIG+=QwtPkgConfig \
    QWT_CONFIG+=QwtExamples \
"

do_configure:prepend() {
    sed -i 's:/usr/local/qwt-$$QWT_VERSION:${prefix}:' ${S}/*.pri
}

do_install:append() {
    # seems out of tree build confuses installation of examples
    # so install them manually
    install -d ${D}${bindir}/
    cp ${B}/examples/bin/* ${D}${bindir}/
}


PACKAGES:prepend = "${PN}-examples ${PN}-features ${PN}-plugins "
FILES:${PN}-examples = "${bindir}/*"
FILES:${PN}-features = "${prefix}/features"
FILES:${PN}-plugins = "${prefix}/plugins/designer/*.so"
FILES:${PN}-dbg += "${prefix}/plugins/designer/.debug"
FILES:${PN}-doc += "${prefix}/doc"

INSANE_SKIP:${PN}-plugins += "libdir"
INSANE_SKIP:${PN}-dbg += "libdir"

RPROVIDES:${PN}-dev = "libqwt-qt5-dev"
