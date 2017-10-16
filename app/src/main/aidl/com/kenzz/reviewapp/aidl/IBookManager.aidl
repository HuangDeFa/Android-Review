// IBookManager.aidl
package com.kenzz.reviewapp.aidl;

// Declare any non-default types here with import statements
import com.kenzz.reviewapp.aidl.Book;
import com.kenzz.reviewapp.aidl.IUpdateBookListener;
interface IBookManager {

    void addBook(in Book book);

    List<Book> getBooks();

    void registerUpdateBookListener(in IUpdateBookListener listener);

    void unregisterUpdateBookListener(in IUpdateBookListener listene);
}
