package io.github.zeroaicy.aide.utils

import android.os.Build
import java.io.File


object FilesSystem {

    var pathArray: Array<String>? = null

    fun processPaths(str: String): MutableList<String> {
        val result = ArrayList<String>()
        // 双重检查锁定同步加载 pathArray
        if (pathArray == null) {
            synchronized(FilesSystem::class.java) {
                if (pathArray == null) {
                    pathArray = TREE_TEXT.split("\n").toTypedArray()
                }
            }
        }

        // 如果 pathArray 仍然为 null，则返回一个空的 ArrayList
        if (pathArray == null) {
            return ArrayList()
        }

        val splitStr = splitString(str, '/')
        var index = 0

        for (path in pathArray!!) {
            when {
                index >= splitStr.size -> {
                    if (index > 0 && path[index - 1] == '/') break
                    if (path.getOrNull(index) == '/') {
                        result.add(path.substring(index + 1, path.length - 1))
                    }
                }

                path.getOrNull(index) == '/' -> {
                    val segment = splitStr[index]
                    if (path.length - index - 2 == segment.length && path.startsWith(
                            segment,
                            index + 1
                        ) && path.last() == '/'
                    ) {
                        index += 1
                    }
                }
            }
        }


        return result
    }

    private fun splitString(str: String, delimiter: Char): Array<String> =
        str.split(delimiter).filter { it.isNotEmpty() }.toTypedArray()


}


const val TREE_TEXT = """/acct/
/apex/
/bin/
/cache/
/config/
/data/
+/app/
+/data/
+/local/
++/tmp/
+/system/
+/user/
++/0/
/data_mirror/
+/cur_profiles/
++/0/
+/data_ce/
++/null/
+++/0/
+++/10/
+++/999/
+/data_de/
++/null/
+++/0/
+++/10/
+++/999/
+/ref_profiles/
++/0/
/default.prop
/dev/
+/android_pipe/
+/ashmem
+/binder
+/blkio/
+/block/
+/bus/
++/usb/
+/console
+/cpuctl/
+/cpuset/
+/fd/
+/fscklogs/
+/full
+/fuse
+/hpet
+/hwbinder
+/hw_random
+/input/
+/ion
+/kmsg
+/kmsg_debug
+/loop-control
+/memcg
+/network_latency
+/network_throughput
+/null
+/ppp
+/ptmx
+/pts/
+/random
+/snd/
+/socket/
+/stderr
+/stdin
+/stdout
+/tty
+/tun
+/uhid
+/uinput
+/urandom
+/usb-ffs/
+/vndbinder
+/zero
/etc/
/init/
/init.environ.rc
/init.rc
/init.usb.configfs.rc
/init.usb.rc
/init.zygote32.rc
/mnt/
+/androidwritable/
+/appfuse/
+/asec/
+/media_rw/
+/obb/
+/pass_through/
+/product/
+/runtime/
+/sdcard/
+/secure/
+/user/
+/vendor/
/odm/
+/app/
+/bin/
+/etc/
+/firmware/
+/framework/
+/lib/
+/lib64/
+/overlay/
+/priv-app/
+/usr/
/oem/
/proc/
+/acpi/
+/asound/
+/bus/
++/input/
++/pci/
+/cgroups/
+/cmdline
+/config.gz
+/cpuinfo
+/devices/
+/dma/
+/driver/
+/fs/
+/iomem/
+/irq/
+/keys/
+/kmsg/
+/meminfo
+/misc/
+/modules
+/scsi/
+/self/
+/stat/
+/sys/
++/abi/
++/debug/
++/dev/
++/fs/
++/kernel/
++/net/
+++/core/
+++/ipv4/
+++/ipv6/
+++/netfilter/
+++/unix/
++/user/
++/vm/
+/tty/
+/uid/
+/version
/product/
+/app/
+/etc/
+/lib/
+/lib64/
+/media/
+/overlay/
+/priv-app/
/sbin/
/sdcard/
/storage/
+/emulated/
++/0/
++/10/
++/999/
++/legacy/
+/self/
++/primary/
/sys/
+/block/
+/bus/
++/acpi/
++/clockevents/
++/clocksource/
++/container/
++/cpu/
++/hdaudio/
++/hid/
++/i2c/
++/machinecheck/
++/nvmem/
++/pci/
++/platform/
++/pnp/
++/scsi/
++/serio/
++/usb/
++/virtio/
++/workqueue/
+/class/
++/android_usb/
++/bdi/
++/block/
++/bsg/
++/dma/
++/dmi/
++/drm/
++/hidraw/
++/ieee80211/
++/input/
++/konepure/
++/leds/
++/mem/
++/misc/
++/net/
++/pci_bus/
++/power_supply/
++/ppp/
++/rc/
++/rtc/
++/savu/
++/scsi_device/
++/scsi_disk/
++/scsi_generic/
++/scsi_host/
++/sound/
++/spi_host/
++/spi_transport/
++/thermal/
++/tty/
++/udc/
++/virtio-ports/
++/zram-control/
+/dev/
++/block/
++/char/
+/devices/
++/breakpoint/
++/cpu/
++/msr/
++/platform/
++/pnp0/
++/software/
++/system/
+++/clockevents/
+++/clocksource/
+++/container/
+++/cpu/
+++/machinecheck/
++/tracepoint/
++/virtual/
+++/android_usb/
+++/bdi/
+++/block/
+++/dmi/
+++/drm/
+++/mem/
+++/misc/
+++/net/
+++/ppp/
+++/sound/
+++/thermal/
+++/tty/
+++/workqueue/
+/firmware/
++/acpi/
++/devicetree/
++/dmi/
+/fs/
++/bpf/
++/cgroup/
++/ext4/
++/f2fs/
++/fuse/
++/selinux/
+/kernel/
+/module/
+/power/
/system/
+/apex/
+/app/
+/bin/
+/build.prop
+/etc/
++/audio_effects.conf
++/bluetooth/
++/boot-image.prof
++/cgroups.json
++/clatd.conf
++/fonts.xml
++/fstab.postinstall
++/hosts
++/init/
++/mke2fs.conf
++/NOTICE.xml.gz
++/permissions/
++/ppp/
++/prop.default
++/public.libraries.txt
++/security/
++/selinux/
++/sysconfig/
++/textclassifier/
++/vintf
+/fonts/
+/framework/
+/lib/
+/priv-app/
+/product/
++/app/
++/build.prop
++/etc/
++/lib/
++/media/
+++/audio/
++/overlay/
++/priv-app/
+/usr/
++/hyphen-data/
++/icu/
++/idc/
++/keychars/
++/keylayout/
++/share/
+/vendor/
+/xbin/
/system_ext/
+/bin/
+/etc/
+/framework/
+/lib/
+/lib64/
+/priv-app/
/ueventd.rc
/vendor/
+/bin/
+/build.prop
+/etc/
++/audio_policy.conf
++/group
++/init
++/permissions
++/selinux
++/wifi
+/lib/
+/manifest.xml
+/odm/
++/etc/
+++/build.prop
+/ueventd.rc
+/usr/
"""


fun String.fixApi30(hide: Boolean = true): String {
    if (!hide || Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
        return this.cleanPath()
    }
    val modified = this.replace("Android", "Android\u200d")
    return if (File(modified).canRead()) modified else this.cleanPath()
}

fun String.cleanPath(): String = replace("\u200d", "")