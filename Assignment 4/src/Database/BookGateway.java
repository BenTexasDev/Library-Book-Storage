package Database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import App.AppException;
import Author.Author;
import Book.AuditTrailEntry;
import Book.AuthorBook;
import Book.Book;
import Book.Publisher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class BookGateway {
	private static Logger logger = LogManager.getLogger();

	private Connection conn;
	
	public BookGateway(Connection connection) {
		this.conn = connection;
	}
	
	public void deleteBook(Book book) throws AppException {
		PreparedStatement st = null;
		try {
			String sql = "delete from book where id = ? ";
			st = conn.prepareStatement(sql);
			st.setInt(1, book.getId());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new AppException(e);
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
	}
	
	public void insertBook(Book book) throws AppException{
		PreparedStatement st = null;
		try {
			String sql = "insert into book "
					+ " (title, summary, year_published, publisher_id, isbn)" //date_added might have to add? default should be fine
					+ " values (?, ?, ?, ?, ?) ";
			st = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			st.setString(1, book.getTitle());
			st.setString(2, book.getSummary());
			st.setInt(3, book.getYearPublished());		//might have to change setInt
			st.setInt(4, book.getPublisher().getId());  //might have to change setInt or get publisher id different way
			st.setString(5, book.getISBN());
			st.executeUpdate();
			
			ResultSet rs = st.getGeneratedKeys();
			rs.first();
			book.setId(rs.getInt(1));
			
			logger.info("new id is " + book.getId());
			
			rs.close();
			
		}catch (SQLException e) {
			throw new AppException(e);
		}finally {
			try {
				st.close();
			}catch(SQLException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		
	}
	
	public void updateBook(Book book) throws AppException{
		PreparedStatement st = null;
		try {
			String sql = "update book "
					+ " set title = ?, summary = ?, year_published = ?, publisher_id = ?, isbn = ?"
					+ " where id = ? ";
			st = conn.prepareStatement(sql);
			st.setString(1, book.getTitle());
			st.setString(2, book.getSummary());
			st.setInt(3, book.getYearPublished());
			st.setInt(4, book.getPublisher().getId());
			st.setString(5, book.getISBN());
			//st.setString(6, book.getDateAdded().toString()); only should be assigned by mysql server
			st.setInt(6, book.getId());
			st.executeUpdate();

		} catch (SQLException e) {
			throw new AppException(e);
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		
	}
	
	public Book getBookById(int id) throws AppException {
		Book book = null;
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from book where id = ?");
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			rs.next();
			book = new Book(rs.getString("title"));
			book.setId(rs.getInt("id"));
			book.setSummary(rs.getString("summary"));
			book.setYearPublished(rs.getInt("publisher_id"));
			book.setISBN(rs.getString("isbn"));
		
			Publisher publisher= new PublisherGateway(this.conn).getPublisherById(rs.getInt("publisher_id"));
			book.setPublisher(publisher);
			book.setGateway(this);
			
			//convert old Date object to a LocalDate
			if(rs.getString("date_added") != null)
				book.setDateAdded((rs.getTimestamp("date_added").toLocalDateTime().toLocalDate()));
			//myAuthor.setLastModified(myResultset.getTimestamp("last_modified").toL ocalDateTime());
			book.setGateway(this);
			rs.close();
		} catch (SQLException e) {
			throw new AppException(e);
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}		
		return book;
	}
	public ObservableList<Book> getBooks() throws AppException {
		ObservableList<Book> books = FXCollections.observableArrayList();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from book order by title");
			ResultSet rs = st.executeQuery();
		
		while(rs.next()) {
			Book book = new Book(rs.getString("title"));
			book.setId(rs.getInt("id"));
			book.setSummary(rs.getString("summary"));
			book.setYearPublished(rs.getInt("year_published"));
			book.setISBN(rs.getString("isbn"));
			Publisher publisher= new PublisherGateway(this.conn).getPublisherById(rs.getInt("publisher_id"));
			book.setPublisher(publisher);
			//if(rs.getString("date_added") != null)
			//	book.setDateAdded((LocalDate.parse(rs.getString("date_added"))));
			book.setGateway(this);
			books.add(book);
		}
		rs.close();
	} catch (SQLException e) {
		throw new AppException(e);
	} finally {
		try {
			st.close();
		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		}
	}
	System.out.print("books list =" + books);
	return books;
}
	
	public ObservableList<Book> searchBooks(String searchTitle) throws AppException {
		ObservableList<Book> books = FXCollections.observableArrayList();
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			if (searchTitle.length() > 0) {
				System.out.println("searching like title = " + searchTitle);
				st = conn.prepareStatement("select * from book where title like ?");
				st.setString(1, "%" + searchTitle + "%");
				rs = st.executeQuery();
				//System.out.println("rs = " +rs.next());
				if(rs.wasNull()) {
					//System.out.println("rs is false");
					st.close();
					rs.close();
					st = conn.prepareStatement("select * from book ");
					rs = st.executeQuery();
				}
			}else {
				st = conn.prepareStatement("select * from book ");
				rs = st.executeQuery();
			}
			//st = conn.prepareStatement("select * from book where title like ?");
			//st.setString(1, "%" + likeSanitize(title) + "%");
			
			while(rs.next()) {
				Book book = new Book(rs.getString("title"));
				book.setId(rs.getInt("id"));
				book.setSummary(rs.getString("summary"));
				book.setYearPublished(rs.getInt("year_published"));
				book.setISBN(rs.getString("isbn"));
				Publisher publisher= new PublisherGateway(this.conn).getPublisherById(rs.getInt("publisher_id"));
				book.setPublisher(publisher);
				//if(rs.getString("date_added") != null)
				//	book.setDateAdded((LocalDate.parse(rs.getString("date_added"))));
				book.setGateway(this);
				
				books.add(book);
			}
			rs.close();
		} catch (SQLException e) {
			throw new AppException(e);
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		System.out.print("books list =" + books);
		return books;
	}
	public ObservableList<AuditTrailEntry> getAuditTrails(Book book) throws AppException{
		ObservableList<AuditTrailEntry> audits = FXCollections.observableArrayList();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("select a.*, b.id "
					+ " from book_audit_trail a "
					+ " inner join book b on a.book_id = b.id "
					+ " where a.book_id = ?");
			st.setInt(1, book.getId());
			rs = st.executeQuery();
			//st = conn.prepareStatement("");
			while(rs.next()) {
				AuditTrailEntry audit = new AuditTrailEntry();
				audit.setId(rs.getInt("id"));
				audit.setDateAdded(rs.getTimestamp("date_Added"));
				audit.setMessage(rs.getString("entry_msg"));
				audits.add(audit);
				
			}
		} catch (SQLException e) {
			throw new AppException(e);
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		return audits;
		
	}
	public static String likeSanitize(String input) {
	    return input
	       .replace("!", "!!")
	       .replace("%", "!%")
	       .replace("_", "!_")
	       .replace("[", "![");
	} 
	//TO DO:
	//getBooksByBookId
	public List<Book> getBookByBookId(int bookId) {
		BookGateway gateway = new BookGateway(this.conn);
		return gateway.getBookByBookId(bookId);
	}
	
	public ObservableList<AuthorBook> getAuthorsForBook(int bookId) {
		ObservableList<AuthorBook> authors = FXCollections.observableArrayList();
		//Book book = getBookById(bookId);
		//AuthorBook authBook = new AuthorBook();
		//AuthorGateway authGateway = new AuthorGateway(conn);
		PreparedStatement st = null;
		ResultSet rs = null;
		
		// return all books matching id
		// for each book getAuthorby id
		// set AuthorBook model
		// add to list
		try {
			st = conn.prepareStatement("select * from author_book where book_id = ?");
			st.setInt(1, bookId);
			rs = st.executeQuery();
			while(rs.next()) {
				//AuthorBook authBook = new AuthorBook();
				Book book = getBookById(rs.getInt("book_id"));
				AuthorGateway authGateway = new AuthorGateway(conn); //mignt have to make new connection
				Author author =  authGateway.getAuthorById(rs.getInt("author_id"));
				double royalty = ((rs.getDouble("royalty")) * 100);
				AuthorBook authBook = new AuthorBook(author,book,(int)royalty,false);
				authors.add(authBook);
			}
		} catch (SQLException e) {
			throw new AppException(e);
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		return authors;
	}
	
	public void insertIntoAuditTrail(int bookId, String message) {
		PreparedStatement st = null;
		try {
			String sql = "insert into book_audit_trail "
					+ " (book_id, entry_msg)" 
					+ " values (?, ?) ";
			st = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			st.setInt(1, bookId);
			st.setString(2, message);
			st.executeUpdate();
			
			ResultSet rs = st.getGeneratedKeys();
			rs.first();
			
			logger.info("updated audit trail of book with id " + bookId);
			
			rs.close();
			
		}catch (SQLException e) {
			throw new AppException(e);
		}finally {
			try {
				st.close();
			}catch(SQLException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
	}
	
	public String createAuditMessage(Book oldBook, Book newBook) {
//		String result = "";
		StringBuilder sb =new StringBuilder();
		if(oldBook.getTitle().compareTo(newBook.getTitle()) != 0) {
			sb.append("Title changed to " + newBook.getTitle() + ". ");
		}
		if(oldBook.getSummary().compareTo(newBook.getSummary()) != 0) {
			sb.append("Book Summary changed. ");
		}
		if(oldBook.getYearPublished() != newBook.getYearPublished()) {
			sb.append("Year published changed to " + newBook.getYearPublished() + ". ");
		}
		if(oldBook.getISBN().compareTo(newBook.getISBN()) != 0) {
			sb.append("ISBN changed from " + oldBook.getISBN() + " to " + newBook.getISBN());
		}
		
		
		
		String result = sb.toString();
		return result;
	}
	
	public void deleteAuthorBook(Author author, Book book) throws AppException {
		PreparedStatement st = null;
		try {
			String sql = "delete from author_book where author_id = ? and book_id = ?";
			st = conn.prepareStatement(sql);
			st.setInt(1, author.getId());
			st.setInt(2, book.getId());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new AppException(e);
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
	}
	public void updateAuthorBook(AuthorBook author) throws AppException{
		PreparedStatement st = null;
		try {
			
			System.out.println(author.getRoyalty()*.01);
			String sql = "update author_book "
					+ " set author_id = ?, royalty = ?"
					+ " where author_id = ? and book_id = ?";
			st = conn.prepareStatement(sql);
			st.setInt(1, author.getAuthor().getId());
			st.setDouble(2, author.getRoyalty()*.01);
			st.setInt(3, author.getAuthor().getId());
			st.setInt(4, author.getBook().getId());
			
			st.executeUpdate();

		} catch (SQLException e) {
			throw new AppException(e);
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		
	}
	public void insertAuthorBook(AuthorBook author) throws AppException{
		PreparedStatement st = null;
		try {
			String sql = "insert into author_book "
					+ " (author_id, book_id, royalty)" //date_added might have to add? default should be fine
					+ " values (?, ?, ?) ";
			st = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			st.setInt(1, author.getAuthor().getId());
			st.setInt(2, author.getBook().getId());
			st.setDouble(3, author.getRoyalty()*.01);		
			st.executeUpdate();
			
			ResultSet rs = st.getGeneratedKeys();
			rs.first();
			
			rs.close();
			
		}catch (SQLException e) {
			throw new AppException(e);
		}finally {
			try {
				st.close();
			}catch(SQLException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		
	}
}
