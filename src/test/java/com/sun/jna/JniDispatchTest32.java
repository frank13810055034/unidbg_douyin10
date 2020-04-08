package com.sun.jna;

import com.github.unidbg.*;
import com.github.unidbg.debugger.DebuggerType;
import com.github.unidbg.linux.android.AndroidARMEmulator;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.dvm.*;
import com.github.unidbg.linux.android.dvm.StackTraceElement;
import com.github.unidbg.linux.android.dvm.api.ApplicationInfo;
import com.github.unidbg.linux.android.dvm.array.ArrayObject;
import com.github.unidbg.linux.android.dvm.array.ByteArray;
import com.github.unidbg.memory.Memory;
import com.github.unidbg.memory.MemoryBlock;

import java.io.File;
import java.io.IOException;

public class JniDispatchTest32 extends AbstractJni {
    private static final String APP_PACKAGE_NAME = "com.ss.android.ugc.aweme";

    private static LibraryResolver createLibraryResolver() {
        return new AndroidResolver(23);
    }

    private static AndroidEmulator createARMEmulator() {
        return new AndroidARMEmulator(APP_PACKAGE_NAME);
    }

    private final AndroidEmulator emulator;
    private final VM vm;
    private final Module module;

    private final DvmClass Native;


    private JniDispatchTest32() throws IOException {
        emulator = createARMEmulator();
        final Memory memory = emulator.getMemory();
        memory.setLibraryResolver(createLibraryResolver());
        memory.setCallInitFunction();

        vm = emulator.createDalvikVM(null);
        vm.setJni(this);
        vm.setVerbose(true);
        DalvikModule dm = vm.loadLibrary(new File("src/test/resources/example_binaries/armeabi-v7a/libcms.so"), false);
        dm.callJNI_OnLoad(emulator);
        module = dm.getModule();

        Native = vm.resolveClass("com/ss/sys/ces/a");
        Native.AddMethodMap("Francies", "()V");
        Native.AddStaticMethodMap("njss", "(ILjava/lang/Object;)java/lang/Object;");
        Native.AddStaticMethodMap("Zeoy", "()V");
        Native.AddStaticMethodMap("Louis", "()V");
        Native.AddStaticMethodMap("Bill", "()V");

        Symbol __system_property_get = module.findSymbolByName("__system_property_get", true);
        MemoryBlock block = memory.malloc(0x10);
        Number ret = __system_property_get.call(emulator, "ro.build.version.sdk", block.getPointer())[0];
        System.out.println("sdk=" + new String(block.getPointer().getByteArray(0, ret.intValue())) + ", libc=" + memory.findModule("libc.so"));
    }

    private void destroy() throws IOException {
        emulator.close();
        System.out.println("destroy");
    }

    public static void main(String[] args) throws Exception {

        JniDispatchTest32 test = new JniDispatchTest32();

        test.test();

        test.destroy();
    }

    private void test() {
        Native.callStaticJniMethod(emulator, "meta(ILandroid/content/Context;Ljava/lang/Object;)Ljava/lang/Object;", 100, 0, new StringObject(vm, "2222"));
        Native.callStaticJniMethod(emulator, "meta(ILandroid/content/Context;Ljava/lang/Object;)Ljava/lang/Object;", 101, 0, new StringObject(vm, "0"));
        Native.callStaticJniMethod(emulator, "meta(ILandroid/content/Context;Ljava/lang/Object;)Ljava/lang/Object;", 102, 0, new StringObject(vm, "1128"));
        Native.callStaticJniMethod(emulator, "meta(ILandroid/content/Context;Ljava/lang/Object;)Ljava/lang/Object;", 103, 0, new StringObject(vm, "51790275446"));
        Native.callStaticJniMethod(emulator, "meta(ILandroid/content/Context;Ljava/lang/Object;)Ljava/lang/Object;", 104, 0, new StringObject(vm, "110943176729"));
        Native.callStaticJniMethod(emulator, "meta(ILandroid/content/Context;Ljava/lang/Object;)Ljava/lang/Object;", 1020, 0, new StringObject(vm, ""));
        Native.callStaticJniMethod(emulator, "meta(ILandroid/content/Context;Ljava/lang/Object;)Ljava/lang/Object;", 105, 0, new StringObject(vm, "100501"));
        Native.callStaticJniMethod(emulator, "meta(ILandroid/content/Context;Ljava/lang/Object;)Ljava/lang/Object;", 106, 0, new StringObject(vm, "com.ss.android.ugc.aweme"));
        Native.callStaticJniMethod(emulator, "meta(ILandroid/content/Context;Ljava/lang/Object;)Ljava/lang/Object;", 107, 0, new StringObject(vm, "/data/user/0/com.ss.android.ugc.aweme/files"));
        Native.callStaticJniMethod(emulator, "meta(ILandroid/content/Context;Ljava/lang/Object;)Ljava/lang/Object;", 108, 0, new StringObject(vm, "/data/app/com.ss.android.ugc.aweme-1.apk"));
        Native.callStaticJniMethod(emulator, "meta(ILandroid/content/Context;Ljava/lang/Object;)Ljava/lang/Object;", 109, 0, new StringObject(vm, "/storage/emulated/0"));
        Native.callStaticJniMethod(emulator, "meta(ILandroid/content/Context;Ljava/lang/Object;)Ljava/lang/Object;", 110, 0, new StringObject(vm, "/data"));
        Native.callStaticJniMethod(emulator, "meta(ILandroid/content/Context;Ljava/lang/Object;)Ljava/lang/Object;", 111, 0, new StringObject(vm, "/23"));
        //Native.callStaticJniMethod(emulator, "meta(ILandroid/content/Context;Ljava/lang/Object;)Ljava/lang/Object;", 222, 0, new StringObject(vm, "SS-500"));
        Native.callStaticJniMethod(emulator, "meta(ILandroid/content/Context;Ljava/lang/Object;)Ljava/lang/Object;", 1213, 0, new StringObject(vm, "CZL-MLP-1"));
        //emulator.attach(DebuggerType.ANDROID_SERVER_V7);
        long start = System.currentTimeMillis();
        //emulator.attach(DebuggerType.ANDROID_SERVER_V7);
        byte[] data = "75e3ba5532ebc30fba36674de5b044bb00000000000000000000000000000000d4845ba4d4587a60e2b73030ecff23aee5afe8c143fcee165f7ab2c19a7562b6".getBytes();
        int time = (int) (start / 1000);
        Number ret = Native.callStaticJniMethod(emulator, "leviathan(II[B)[B", -1, 1586333406, new ByteArray(data));
        //Number ret = Native.callStaticJniMethod(emulator, "encode([B)[B","AAABB".getBytes());
        //Number ret = Native.callStaticJniMethod(emulator, "decode(I[B)[B", 1, "AAABB".getBytes());
        long address = ret.intValue() & 0xffffffffL;
        byte[] tt = (byte[]) vm.getObject(address).getValue();

        //ret: {X-Gorgon=040100a000016cfc1d803499e7a34c98c9bbb885f5bba357fe47, X-Khronos=1586327675}
        //040180f50000d8d61e96ee08fab828b97428470f5f3a6b5a1b67

        //Pointer pointer = UnicornPointer.pointer(emulator, ret.intValue() & 0xffffffffL);
        //assert pointer != null;
        String aa = new String(byte2Hex(tt));
        System.out.println(aa);

        vm.deleteLocalRefs();
        // System.out.println("getNativeVersion version=" + version.getValue() + ", offset=" + (System.currentTimeMillis() - start) + "ms");
    }

    /**
     * 　　* 将byte转为16进制
     * 　　* @param bytes
     * 　　* @return
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }


    @Override
    public DvmObject callStaticObjectMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList varArg) {
        if ("java/lang/System->getProperty(Ljava/lang/String;)Ljava/lang/String;".equals(signature)) {
            StringObject string = varArg.getObject(0);
            String string2;
            if (string.getValue().equals("http.proxyHost")) {
                string2 = "";
            } else if (string.getValue().equals("http.proxyPort")) {
                string2 = "80";
            } else if (string.getValue().equals("java.vm.version")) {
                string2 = "1.8.0";
            } else {
                string2 = System.getProperty(string.getValue());
            }
            return new StringObject(vm, string2);
        }

        if ("java/lang/Thread->currentThread()Ljava/lang/Thread;".equals(signature)) {
            return vm.resolveClass("java/lang/Thread").newObject(Thread.currentThread());
        }
        if ("android/app/Application->getApplicationInfo()Landroid/content/pm/ApplicationInfo;".equals(signature)) {
            return new ApplicationInfo(vm);
        }

        return super.callStaticObjectMethodV(vm, dvmClass, signature, varArg);
    }

    @Override
    public DvmObject callObjectMethodV(BaseVM vm, DvmObject dvmObject, String signature, VaList vaList) {
        switch (signature) {
            case "java/lang/Thread->getStackTrace()[Ljava/lang/StackTraceElement;":
                DvmObject[] objs = new DvmObject[14];
                objs[0] = new StackTraceElement(vm, "dalvik.system.VMStack.getThreadStackTrace(Native Method)");
                objs[1] = new StackTraceElement(vm, "java.lang.Thread.getStackTrace(Thread.java:580)");
                objs[2] = new StackTraceElement(vm, "com.ss.sys.ces.a.leviathan(Native Method)");
                objs[3] = new StackTraceElement(vm, "com.ss.sys.ces.gg.tt$1.a(Unknown Source)");
                objs[4] = new StackTraceElement(vm, "com.bytedance.frameworks.baselib.network.http.e.a(SourceFile:33947656)");
                objs[5] = new StackTraceElement(vm, "com.bytedance.ttnet.a.a.onCallToAddSecurityFactor(SourceFile:33816621)");
                objs[6] = new StackTraceElement(vm, "android.support.v7.app.AppCompatViewInflater$DeclaredOnClickListener");
                objs[7] = new StackTraceElement(vm, "java.lang.reflect.Method.invoke(Native Method)");
                objs[8] = new StackTraceElement(vm, "com.ttnet.org.chromium.base.Reflect.on(SourceFile:50659347)");
                objs[9] = new StackTraceElement(vm, "com.ttnet.org.chromium.base.Reflect.call(SourceFile:50528262)");
                objs[10] = new StackTraceElement(vm, "org.chromium.c.a(SourceFile:33882174)");
                objs[11] = new StackTraceElement(vm, "org.chromium.e.onCallToAddSecurityFactor(SourceFile:33685508)");
                objs[12] = new StackTraceElement(vm, "com.ttnet.org.chromium.net.impl.CronetUrlRequestContext.onCallToAddSecurityFactor(SourceFile:33685512)");
                objs[13] = new StackTraceElement(vm, "com.ttnet.org.chromium.net.impl.CronetUrlRequest.addSecurityFactor(SourceFile:33882142)");


                return new ArrayObject(objs);
            case "java/lang/StackTraceElement->getClassName()Ljava/lang/String;":
                //StackTraceElement element = (StackTraceElement) dvmObject.getValue();
                return new StringObject(vm, (String) dvmObject.getValue());
            case "android/app/Application->getApplicationInfo()Landroid/content/pm/ApplicationInfo;":
                return new ApplicationInfo(vm);
        }


        return super.callObjectMethodV(vm, dvmObject, signature, vaList);
    }

    @Override
    public DvmObject<?> getObjectField(BaseVM vm, DvmObject<?> dvmObject, String signature) {
        if ("android/app/ApplicationInfo->sourceDir:Ljava/lang/String;".equals(signature)) {
            return new StringObject(vm, "/data/data/");
        }
        if ("android/content/pm/ApplicationInfo->sourceDir:Ljava/lang/String;".equals(signature)) {
            return new StringObject(vm, "/data/data/");
        }
        return super.getObjectField(vm, dvmObject, signature);
    }

    public boolean callStaticBooleanMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        if (signature.equals("android/os/Debug->isDebuggerConnected()Z"))
            return true;
        return false;
    }

    private static final String APK_PATH = "src/test/resources/example_binaries/armeabi-v7a/libcms.so";
}
