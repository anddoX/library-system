import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
public class Library {
public static Map<Integer, Book> bookMap = new TreeMap<>();
public static ArrayList<Book> borrowedList = new ArrayList<>();
public static File currentFile;

public static void main(String[] args) {
  Scanner stdin = new Scanner(System.in);
  System.out.print("Please specify the datafile: ");
  String fileName = stdin.nextLine();
  while (!checkFileExists(fileName)) {
    System.out.println("The path " + fileName + " either does not exist or is inaccessible.");
    System.out.print("Please re-specify the datafile: ");
    fileName = stdin.nextLine();
  }
  File bookFile = new File(fileName);
  currentFile = bookFile;
  File borrowedBookFile = new File("borrowedBooks.txt");
  
  try {
    borrowedBookFile.createNewFile();
  } catch (IOException e) {
  // TODO Auto-generated catch block
  System.out.println(e.getMessage());
  }
  loadBorrowedData(borrowedBookFile);
  loadToMap(bookFile);
  System.out.println("Welcome to the Lavoisier Library Management System, please choose an option: ");
  StringBuilder builder = new StringBuilder();
  builder.append("1: List all books in the system\n");
  builder.append("2: Add a book\n");
  builder.append("3: Borrow a book\n");
  builder.append("4: Search for a book\n");
  builder.append("5: Save books to file\n");
  builder.append("6: Load books from file\n");
  builder.append("7. Return a book\n");
  System.out.println(builder);
  System.out.print("Make your selection now (ENTER to quit)>");
  String input = stdin.nextLine();
  while (!input.isEmpty()) {
    switch (input.charAt(0)) {
      case '1':
        // Display books loaded to system
        bookMap.forEach((k, v) -> System.out.println(k + "|" + v));
        break;
      case '2':
      // Add a new book to the system
        System.out.println("What is the book name?");
        String title = stdin.nextLine();
        System.out.println("Who is the author? Ideally, type using this format:"
        + "\nfirstName, lastName");
        String authorName = stdin.nextLine();
        System.out.println("What is the ISBN of the book?");
        String isbn = stdin.nextLine();
        System.out.println("Lastly, what is the publication year?");
        String publicationYear = stdin.nextLine();
        addBook(currentFile, new Book(title, "by " +authorName, isbn,
        publicationYear));
        break;
      case '3':
        System.out.printf("Make your choice [1-%d]\n", bookMap.size());
        try {
        int key = stdin.nextInt();
        stdin.nextLine();
        System.out.println(bookMap.get(key).getTitle() + " "
        +bookMap.get(key).getAuthor());
        borrowBook(borrowedBookFile, key);
        } catch (Exception e) {
        System.out.println("You must type an integer only!");
        stdin.nextLine();
        }
        break;
      case '4':
        System.out.print("Enter title/isbn/author> ");
        String choice = stdin.nextLine();
        List<Book> results = bookLookUp(bookMap, choice);
        if (!results.isEmpty()) {
          results.forEach(System.out::println);
        } else {
          System.out.println("No entries found for query: " + choice);
        }
        break;
      case '5':
          System.out.print("Enter filename to save to: ");
          String saveFile = stdin.nextLine();
          saveToFile(saveFile);
          break;
      case '6':
          System.out.print("Enter filename to load: ");
          String loadFile = stdin.nextLine();
          File newFile = new File(loadFile);
          if (checkFileExists(loadFile)) {
          currentFile = newFile;
          loadToMap(currentFile);
          } else {
          System.out.println("File does not exist.");
          }
          break;
      case '7':
        if (borrowedList.isEmpty()) {
          System.out.println("No books are currently marked as borrowed.");
          break;
        }
        System.out.println("\n--- Currently Borrowed Books ---");
        for (int i = 0; i < borrowedList.size(); i++) {
          System.out.println("[" + i + "] " + borrowedList.get(i));
        }
        System.out.print("Select the index number of the book to return: ");
        try {
          int returnIndex = stdin.nextInt();
          stdin.nextLine();
        if (returnIndex >= 0 && returnIndex < borrowedList.size())
        {
        returnBook(borrowedBookFile, returnIndex);
        } else {
        System.out.println("Invalid selection ID. Book index out of bounds.");
        }
        } catch (Exception e) {
          System.out.println("You must type an integer only!");
          stdin.nextLine();
        }
        break;
      default:
        System.out.println("Unknown option, please enter a number from 1 to 6");
      }
  System.out.println(builder);
  System.out.print("Make your selection now (ENTER to quit)>");
  input = stdin.nextLine();
  }

  stdin.close();
}
  private static boolean checkFileExists(String path) {
    try (Scanner ignored = new Scanner(new File(path))) {
      return true;
    } catch (Exception e) {
      return false;
    }
  }
public static void borrowBook(File f, int key) {
  Book book = bookMap.get(key);
  try {
  if (borrowedList.contains(book)) {
    System.out.println("The book is currently not available.");
  } else {
  BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
  bw.newLine();
  bw.write(book.getTitle() + ", " + book.getAuthor() + ", " +
  book.getIsbn()
  + ", " + book.getPublicYear());
  borrowedList.add(bookMap.get(key));
  bookMap.get(key).setAvailable(false);
  System.out.println("You are now borrowing " + book.getTitle() + " "
  + book.getAuthor());
  bw.close();
  }
  } catch (IOException e) {
    System.out.println(e.getMessage());
    }
}
  public static void addBook(File f, Book book) {
  try {
    BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
    bw.newLine();
    bw.write(book.getTitle() + ", " + book.getAuthor() + ", " +
    book.getIsbn()
    + ", " + book.getPublicYear());
    bw.close();
    loadToMap(f);
    System.out.println(book.getTitle() + " is now available.");
  } catch (IOException e) {
    System.out.println(e.getMessage());
    }
  }
  public static List<Book> bookLookUp(Map<Integer, Book> map, String info) {
    if (info.isBlank()) return new ArrayList<>();
    String query = info.toLowerCase();
    return map.values().stream().filter(x -> x.getTitle().toLowerCase().contains(query) || x.getAuthor().toLowerCase().contains(query) ||
  x.getIsbn().contains(query)).collect(Collectors.toList());
    }
  private static void loadToMap(File file) {
    try (Scanner fileReader = new Scanner(file)) {
    bookMap.clear();
    int bookId = 1;
    while (fileReader.hasNext()) {
      //System.out.println(fileReader.nextLine());
      String line = fileReader.nextLine().trim();
      if (line.isEmpty()) continue;
      String[] pointer = line.split("\\s*,\\s*");
    if (pointer.length >= 4) {
  // Title, year, and isbn remain unchanged
      String title = pointer[0];
      String year = pointer[pointer.length - 1];
      String isbn = pointer[pointer.length - 2];
  // Authors' names range from 1 to 4
      StringBuilder builder = new StringBuilder();
  for (int x = 1; x < pointer.length - 2; x++) {
    if (!builder.isEmpty()) 
      builder.append(", ");
    builder.append(pointer[x]);
  //System.out.println(authorArray[x]);
  }
  String author = builder.toString();
  bookMap.put(bookId++, new Book(title, author, isbn, year));
  } else {
    System.out.printf("Skipping line #%d, bad format.\n", bookId);
  }
  }
  System.out.println("Books successfully loaded from " + file.getName());
  System.out.println("Total books loaded: " + bookMap.size());
  } catch (FileNotFoundException e) {
    System.out.println(e.getMessage());
  }
  }
  private static void loadBorrowedData(File file) {
  try (Scanner fileReader = new Scanner(file)) {
    while (fileReader.hasNext()) {
      String line = fileReader.nextLine().trim();
      if (line.isEmpty()) continue;
      String[] pointer = line.split("\\s*,\\s*");
    if (pointer.length >= 4) {
    // Title, year, and isbn remain unchanged
      String title = pointer[0];
      String year = pointer[pointer.length - 1];
      String isbn = pointer[pointer.length - 2];
  // Authors' names range from 1 to 4
    StringBuilder builder = new StringBuilder();
    for (int x = 1; x < pointer.length - 2; x++) {
      if (!builder.isEmpty())
      builder.append(", ");
      builder.append(pointer[x]);
    }
    String author = builder.toString();
    Book bookEntry = new Book(title, author, isbn, year);
      if (borrowedList.contains(bookEntry)) {
        bookEntry.setAvailable(false);
      }
    borrowedList.add(bookEntry);
    }
  }
  } catch (FileNotFoundException e) {
    System.out.println(e.getMessage());
    }
  }
  public static void saveToFile(String filename) {
  try {
    BufferedWriter writer =
    new BufferedWriter(new FileWriter(filename));
    for (Map.Entry<Integer, Book> entry : bookMap.entrySet()) {
      Book book = entry.getValue();
      writer.write(book.getTitle() + ", " + book.getAuthor() + ", " + book.getIsbn() + ", " +  book.getPublicYear());
      writer.newLine();
  }
    writer.close();
    System.out.println("Books successfully saved to " + filename);
  } catch (IOException e) {
    System.out.println("Error saving file: " + e.getMessage());
    }
  }
  public static void returnBook(File borrowedFile, int listIndex) {
  // put book back into library, remove from the borrowed book list
    Book bookToReturn = borrowedList.remove(listIndex);
    bookToReturn.setAvailable(true);
    bookMap.put(bookMap.size() + 1, bookToReturn);
    System.out.println("Successfully returned: " + bookToReturn.getTitle());
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(borrowedFile, false))) {
      for (Book b : borrowedList) {
        bw.write(b.getTitle() + ", " + b.getAuthor() + ", " + b.getIsbn() + ", " + b.getPublicYear());
        bw.newLine();
      }
  } catch (IOException e) {
    System.out.println("Failed to rewrite tracking logs database: " +
    e.getMessage());
    }
  }
}
