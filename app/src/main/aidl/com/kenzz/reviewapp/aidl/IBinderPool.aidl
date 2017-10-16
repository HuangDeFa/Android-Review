// IBinderPool.aidl
package com.kenzz.reviewapp.aidl;

// Declare any non-default types here with import statements

interface IBinderPool {
  //根据Code获取对应的Binder
   IBinder queryBinder(int queryCode);
}
