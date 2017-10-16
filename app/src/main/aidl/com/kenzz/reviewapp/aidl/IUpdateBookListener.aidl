// IUpdateBookListener.aidl
package com.kenzz.reviewapp.aidl;

// Declare any non-default types here with import statements
import com.kenzz.reviewapp.aidl.Book;
interface IUpdateBookListener {
   void onUpdateBook(in Book newBook);
}
