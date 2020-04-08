package com.github.unidbg.linux.android.dvm.api;

import com.github.unidbg.linux.android.dvm.DvmObject;
import com.github.unidbg.linux.android.dvm.VM;

import java.net.NetworkInterface;


class NetworkInterface_ extends DvmObject<NetworkInterface> {

    public NetworkInterface_(VM vm, NetworkInterface value) {
        super(vm.resolveClass("java/net/NetworkInterface"), value);
    }

    public byte[] getHardwareAddress() {
        return new byte[]{1, 2, 3, 4, 5, 6};
    }

}