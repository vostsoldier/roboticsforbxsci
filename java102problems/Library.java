import java.util.ArrayList;

public class Library {
    public static void main(String[] args) {
        Book book1 = new Book("Book 1", "B001", "Bob", 180);
        Book book2 = new Book("Book 2", "B002", "Henry", 328);
        Book book3 = new Book("Book 3", "B003", "Chase", 281);
        DVD dvd1 = new DVD("DVD 1", "D001", 148);
        DVD dvd2 = new DVD("DVD 2", "D002", 136);
        DVD dvd3 = new DVD("DVD 3", "D003", 169);

        book1.checkOut();
        dvd2.checkOut();
        LibraryItem[] items = {book1, book2, book3, dvd1, dvd2, dvd3};

        returnAll(items);

        System.out.println("Is book1 available? " + book1.available());
        System.out.println("Is dvd2 available? " + dvd2.available());

        ArrayList<LibraryItem> availableItems = availableItems(items);
        System.out.println("Available items:");
        for (LibraryItem item : availableItems) {
            System.out.println(item);
        }
    }

    public static void returnAll(LibraryItem[] items) {
        for (LibraryItem item : items) {
            item.returnItem();
        }
    }

    public static ArrayList<LibraryItem> availableItems(LibraryItem[] items) {
        ArrayList<LibraryItem> availableItems = new ArrayList<>();
        for (LibraryItem item : items) {
            if (item.available()) {
                availableItems.add(item);
            }
        }
        return availableItems;
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

    @Override
    public String toString() {
        return title + " (ID: " + itemId + ")";
    }
}

class Book extends LibraryItem {
    public final String author;
    public final int pageCount;

    public Book(String title, String itemId, String author, int pageCount) {
        super(title, itemId);
        this.author = author;
        this.pageCount = pageCount;
    }

    @Override
    public String toString() {
        return "Book: " + title + " by " + author + ", " + pageCount + " pages";
    }
}

class DVD extends LibraryItem {
    public final double runtime;

    public DVD(String title, String itemId, double runtime) {
        super(title, itemId);
        this.runtime = runtime;
    }

    @Override
    public String toString() {
        return "DVD: " + title + ", Runtime: " + runtime + " minutes";
    }
}