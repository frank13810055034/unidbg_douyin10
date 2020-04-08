package com.sun.jna;


import com.github.unidbg.*;
import com.github.unidbg.arm.ARMEmulator;
import com.github.unidbg.arm.HookStatus;
import com.github.unidbg.arm.context.RegisterContext;
import com.github.unidbg.file.FileResult;
import com.github.unidbg.file.IOResolver;
import com.github.unidbg.hook.HookContext;
import com.github.unidbg.hook.ReplaceCallback;
import com.github.unidbg.hook.hookzz.HookEntryInfo;
import com.github.unidbg.hook.hookzz.HookZz;
import com.github.unidbg.hook.hookzz.IHookZz;
import com.github.unidbg.hook.hookzz.WrapCallback;
import com.github.unidbg.hook.whale.IWhale;
import com.github.unidbg.hook.whale.Whale;
import com.github.unidbg.hook.xhook.IxHook;
import com.github.unidbg.linux.android.AndroidARMEmulator;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.XHookImpl;
import com.github.unidbg.linux.android.dvm.*;
import com.github.unidbg.linux.android.dvm.api.ApplicationInfo;
import com.github.unidbg.linux.android.dvm.array.ArrayObject;
import com.github.unidbg.linux.android.dvm.array.ByteArray;
import com.github.unidbg.memory.Memory;
import com.github.unidbg.memory.MemoryBlock;
import com.github.unidbg.pointer.UnicornPointer;
import com.github.unidbg.utils.Inspector;


import java.io.File;
import java.io.IOException;


public class soTest extends AbstractJni implements IOResolver {

    private static final String APP_PACKAGE_NAME = "com.ss.android.ugc.aweme";

    public static AndroidEmulator emulator;
    public static VM vm;
    public static DvmClass CMSVmClass;

    // private static final String APK_PATH = "src/test/resources/app/3.apk";
    private static final String APK_PATH = "src/test/resources/example_binaries/armeabi-v7a/libcms.so";
    private Module module_cms;

    private soTest() throws IOException {
        emulator = new AndroidARMEmulator(APP_PACKAGE_NAME);
        emulator.getSyscallHandler().addIOResolver(this);
        System.out.println("== init ===");
        emulator = new AndroidARMEmulator("");
        final Memory memory = emulator.getMemory();
        memory.setLibraryResolver(new AndroidResolver(23));
        memory.setCallInitFunction();

        vm = emulator.createDalvikVM(null);

        vm.setVerbose(true);
        vm.setJni(this);

        DalvikModule dm = vm.loadLibrary(new File(APK_PATH), false);
        dm.callJNI_OnLoad(emulator);
        CMSVmClass = vm.resolveClass("com/ss/sys/ces/a");

        String as = "a105f8066a99cef28b4888";
        byte[] data = as.getBytes();
        Number ret = CMSVmClass.callStaticJniMethod(emulator, "e([B)[B", vm.addLocalObject(new ByteArray(data)));
        long address = ret.intValue() & 0xffffffffL;
        byte[] tt = (byte[]) vm.getObject(address).getValue();
        String mas = new String(tt);
        System.out.println(mas);

    }

    private void destroy() throws IOException {
        emulator.close();
    }

    public static void main(String[] args) throws Exception {
        soTest test = new soTest();
        test.destroy();
    }

    @Override
    public FileResult resolve(Emulator emulator, String pathname, int oflags) {
        return null;
    }


    @Override
    public DvmObject callStaticObjectMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        if ("java/lang/Thread->currentThread()Ljava/lang/Thread;".equals(signature)) {
            return vm.resolveClass("java/lang/Thread").newObject(Thread.currentThread());
        }
        if ("android/app/Application->getApplicationInfo()Landroid/content/pm/ApplicationInfo;".equals(signature)) {
            return new ApplicationInfo(vm);
        }

        return super.callStaticObjectMethodV(vm, dvmClass, signature, vaList);
    }

    @Override
    public DvmObject callObjectMethodV(BaseVM vm, DvmObject dvmObject, String signature, VaList vaList) {
        switch (signature) {
            case "java/lang/Thread->getStackTrace()[Ljava/lang/StackTraceElement;":
                StackTraceElement[] elements = Thread.currentThread().getStackTrace();
                DvmObject[] objs = new DvmObject[elements.length];
                for (int i = 0; i < elements.length; i++) {
                    objs[i] = vm.resolveClass("java/lang/StackTraceElement").newObject(elements[i]);
                }
                return new ArrayObject(objs);
            case "java/lang/StackTraceElement->getClassName()Ljava/lang/String;":
                StackTraceElement element = (StackTraceElement) dvmObject.getValue();
                return new StringObject(vm, element.getClassName());
            case "android/app/Application->getApplicationInfo()Landroid/content/pm/ApplicationInfo;":
                return new ApplicationInfo(vm);
        }


        return super.callObjectMethodV(vm, dvmObject, signature, vaList);
    }

    @Override
    public DvmObject<?> getObjectField(BaseVM vm, DvmObject<?> dvmObject, String signature) {
        if ("android/app/ApplicationInfo->sourceDir:Ljava/lang/String;".equals(signature)) {
            return new StringObject(vm, APK_PATH);
        }
        if ("android/content/pm/ApplicationInfo->sourceDir:Ljava/lang/String;".equals(signature)) {
            return new StringObject(vm, APK_PATH);
        }
        return super.getObjectField(vm, dvmObject, signature);
    }

    public boolean callStaticBooleanMethodV(BaseVM vm, DvmClass dvmClass, String signature, VaList vaList) {
        if (signature.equals("android/os/Debug->isDebuggerConnected()Z"))
            return true;
        return false;
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