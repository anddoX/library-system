public class Book {
    private String title;
    private String author;
    private String isbn;
    private String publicYear;
    private boolean available;

    public Book() {
    }

    public Book(String title, String author, String isbn, String publicYear) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicYear = publicYear;
        this.available = true;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPublicYear(String publicYear) {
        this.publicYear = publicYear;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPublicYear() {
        return publicYear;
    }

    @Override
    public boolean equals(Object o) {

    	//Comparing fields for contains() to work
    	Book book = (Book)o;
    	return this.author.equals(book.author) && 
    	this.title.equals(book.title) && 
    	this.isbn.equals(book.isbn) && 
    	this.publicYear.equals(book.publicYear);
    }
    
    @Override
    public String toString() {
        return String.format("Title: %s\nAuthor: %s\nISBN: %s\nYear: %s\nAvailability: %s",
                title, author, isbn, publicYear, available);
    }
}
