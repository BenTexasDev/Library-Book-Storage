package Book;

import App.AppController;
import App.AppException;
import Author.Author;
import Database.BookGateway;

public class AuthorBook {
	

	private Author author;
	private Book book;
	private int royalty;
	private boolean newRecord;
	private BookGateway gateway;
	
	//TO DO: constructor, setters/getters and any validation or methods. 
	public AuthorBook() {
		this.newRecord = true;
		this.gateway = new BookGateway(AppController.getInstance().getConnection());
	}
	
	public AuthorBook(Author author,Book book,int royalty,boolean newRecord ){
		this.author = author;
		this.book = book;
		this.royalty = royalty;
		this.newRecord = newRecord;
		this.gateway = new BookGateway(AppController.getInstance().getConnection());
	}
	
	public void save() throws AppException {
		if(newRecord == false) {			
			if( gateway !=null) {
			gateway.updateAuthorBook(this);
			}else {
				System.out.println("book gateway is null");
			}

		} else {
			gateway.insertAuthorBook(this);
			this.newRecord= false;
		}
	}
	@Override
	public String toString() {
		return author.getFirstName() + " " + author.getLastName() + " " +  this.royalty+ "%";
	}
	
	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public int getRoyalty() {
		return royalty;
	}

	public void setRoyalty(int royalty) {
		this.royalty = royalty;
	}

	public boolean isNewRecord() {
		return newRecord;
	}

	public void setNewRecord(boolean newRecord) {
		this.newRecord = newRecord;
	}
	
	
}
