FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "\
	file://04-default-is-optimized.patch \
	file://99-ignore-optimization-flag.patch \
	file://no-ldconfig.patch \
	file://setuptweaks-2.patch \
	file://pgettext.patch \
	"

EXTRA_OECONF += "\
	ac_cv_file__dev_ptmx=yes \
	ac_cv_file__dev_ptc=no \
	ac_cv_no_strict_aliasing_ok=yes \
	ac_cv_pthread=yes \
	ac_cv_cxx_thread=yes \
	ac_cv_sizeof_off_t=8 \
	"

PACKAGES =+ "${PN}-src"
RDEPENDS_{PN}-src = "${PN}"
FILES_${PN}-src += "${libdir}/python${PYTHON_MAJMIN}/*.py"
FILES_${PN}-src += "${libdir}/python${PYTHON_MAJMIN}/*/*.py"
FILES_${PN}-src += "${libdir}/python${PYTHON_MAJMIN}/*/*/*.py"
FILES_${PN}-src += "${libdir}/python${PYTHON_MAJMIN}/*.exe"
FILES_${PN}-src += "${libdir}/python${PYTHON_MAJMIN}/*/*.exe"
FILES_${PN}-src += "${libdir}/python${PYTHON_MAJMIN}/*/*/*.exe"
FILES_${PN}-src += "${libdir}/python${PYTHON_MAJMIN}/*.whl"
FILES_${PN}-src += "${libdir}/python${PYTHON_MAJMIN}/*/*.whl"
FILES_${PN}-src += "${libdir}/python${PYTHON_MAJMIN}/*/*/*.whl"
FILES_${PN}-src += "${libdir}/python${PYTHON_MAJMIN}/config/*.!(a)"

# some additional tests
FILES_${PN}-tests += "${libdir}/python${PYTHON_MAJMIN}/*/test* ${libdir}/python${PYTHON_MAJMIN}/*/*/test*"
