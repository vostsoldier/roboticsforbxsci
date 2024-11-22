public class Library {
    public static void main(String[] args) {
        LibraryItem book = new LibraryItem("The Great Gatsby", "B001");
        System.out.println("Is the book available? " + book.available());
        book.checkOut();
        System.out.println("Checked out the book.");
        System.out.println("Is the book available? " + book.available());
        book.returnItem();
        System.out.println("Returned the book.");
        System.out.println("Is the book available? " + book.available());
    }
}

class LibraryItem {
    public final String title;
    public final String itemId;
    protected boolean isCheckedOut = false;
    public LibraryItem(String title, String itemId) {
        this.title = title;
        this.itemId = itemId;
    }
    public boolean available() {
        return !isCheckedOut;
    }
    public void checkOut() {
        isCheckedOut = true;
    }
    public void returnItem() {
        isCheckedOut = false;
    }
}