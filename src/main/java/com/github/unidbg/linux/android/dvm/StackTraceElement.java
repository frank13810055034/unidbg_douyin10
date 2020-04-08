package com.github.unidbg.linux.android.dvm;


public class StackTraceElement extends DvmObject<String> {

    public StackTraceElement(VM vm, String value) {
        super(vm.resolveClass("java/lang/StackTraceElement"), value);

    }

    public String getClassName() {
        return this.value;
    }

}