package com.sun.jna;


import com.github.unidbg.AndroidEmulator;
import com.github.unidbg.Module;
import com.github.unidbg.linux.android.AndroidARMEmulator;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.dvm.DalvikModule;
import com.github.unidbg.linux.android.dvm.DvmClass;
import com.github.unidbg.linux.android.dvm.VM;
import com.github.unidbg.linux.android.dvm.array.ByteArray;
import com.github.unidbg.memory.Memory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by xxx on 2020/3/29.
 */
class Dy {
    private final AndroidEmulator emulator;

    private final VM vm;
    private final Module module;

    private final DvmClass TTEncryptUtils;

    public Dy() throws IOException {
        emulator = new AndroidARMEmulator("com.ss.android.ugc.aweme");
        Memory memory = emulator.getMemory();
        memory.setLibraryResolver(new AndroidResolver(23));
        memory.setCallInitFunction();
        vm = emulator.createDalvikVM(null);
        DalvikModule dm = vm.loadLibrary(new File("src/test/resources/example_binaries/armeabi-v7a/libcms.so"), false);
        dm.callJNI_OnLoad(emulator);
        module = dm.getModule();
        TTEncryptUtils = vm.resolveClass("com/ss/sys/ces/a");
    }

    private void destroy() throws IOException {
        emulator.close();
        System.out.println("destroy");
    }

    public static void main(String[] args) throws IOException {
        Dy t = new Dy();
        t.encodeByte();
        t.destroy();
    }

    private String encodeByte() {
        byte[] byteArs = "acde74a94e6b493a3399fac83c7c08b35D58B21D9582AF77647FC9902E36AE70f9c001e9334e6e94916682224fbe4e5f00000000000000000000000000000000".getBytes(StandardCharsets.UTF_8);
        Number ret = TTEncryptUtils.callStaticJniMethod(emulator, "leviathan(II[B)[B",
                -1, ((int) (System.currentTimeMillis() / 1000)), vm.addLocalObject(new ByteArray(byteArs)));

        long address = ret.intValue() & 0xffffffffL;
        byte[] tt = (byte[]) vm.getObject(address).getValue();

        return "";
    }


    public static byte[] StrToByte(String str) {
        String str2 = str;
        Object[] objArr = new Object[1];
        int i = 0;
        objArr[0] = str2;

        int length = str.length();
        byte[] bArr = new byte[(length / 2)];
        while (i < length) {
            bArr[i / 2] = (byte) ((Character.digit(str2.charAt(i), 16) << 4) + Character.digit(str2.charAt(i + 1), 16));
            i += 2;
        }
        return bArr;
    }
}