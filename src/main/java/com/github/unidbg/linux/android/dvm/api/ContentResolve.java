
package com.github.unidbg.linux.android.dvm.api;

import com.github.unidbg.linux.android.dvm.DvmObject;
import com.github.unidbg.linux.android.dvm.VM;
import com.sun.jndi.toolkit.url.Uri;

import java.util.Properties;

public class ContentResolve extends DvmObject<String> {

    public ContentResolve(VM vm, String value) {
        super(vm.resolveClass("android/content/ContentResolver"), value);
    }

    public Object call(VM vm, String key) {
        return new ContentResolve(vm, key);
    }

    public Bundle call(VM vm, Uri uri, String str, String str2, Bundle bundle) {
        System.out.println("uri:" + uri.toString() + ",str:" + str + ",str2" + str2);
        return new Bundle(vm, new Properties());
    }

}