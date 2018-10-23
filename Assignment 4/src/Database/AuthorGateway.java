package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import App.AlertHelper;
import App.AppException;
import Author.Author;
import Author.authAuditTrail;
import Book.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AuthorGateway {
	private static Logger logger = LogManager.getLogger();

	private Connection conn;
	
	public AuthorGateway(Connection conn) {
		this.conn = conn;
	}
	
	public void deleteAuthor(Author author) throws AppException {
		PreparedStatement st = null;
		try {
			String sql = "delete from author where id = ? ";
			st = conn.prepareStatement(sql);
			st.setInt(1, author.getId());
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
	
	public void insertAuthor(Author author) throws AppException {
		//System.out.println("inside insertAuthor");
		PreparedStatement st = null;
		try {
			String sql = "insert into author "
					+ " (first_name, last_name, gender, web_site, dob) "
					+ " values (?, ?, ?, ?, ?) ";
			st = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			st.setString(1, author.getFirstName());
			st.setString(2, author.getLastName());
			st.setString(3, author.getGender());
			st.setString(4, author.getWebSite());
			//Date tempDate = java.sql.Date.valueOf(author.getDateOfBirth());
			st.setString(5, author.getDateOfBirth().toString());
			st.executeUpdate();
			ResultSet rs = st.getGeneratedKeys();
			rs.first();
			author.setId(rs.getInt(1));
			
			logger.info("new id is " + author.getId());
			
			rs.close();

		} catch (SQLException e) {
			System.out.println("sql error");
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

	public void updateAuthor(Author author) throws AppException {
		PreparedStatement st = null;
		try {
			Author dbAuthor = getAuthorById(author.getId());
			if ( dbAuthor.getLastModified() != author.getLastModified()) {
				AlertHelper.showWarningMessage("STOP!", "You have an old Author model version","Please go back to Author List and refresh to get updated Author model");
			}
			String sql = "update author "
					+ " set first_name = ?, last_name = ?, gender = ?, web_site = ?, dob = ? "
					+ " where id = ? ";
			st = conn.prepareStatement(sql);
			st.setString(1, author.getFirstName());
			st.setString(2, author.getLastName());
			st.setString(3, author.getGender());
			st.setString(4, author.getWebSite());
			st.setString(5, author.getDateOfBirth().toString());
			st.setInt(6, author.getId());
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

	public Author getAuthorById(int id) throws AppException {
		Author author = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from author where id = ?");
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			rs.next();
			author = new Author(rs.getString("first_name"), rs.getString("last_name"));
			author.setId(rs.getInt("id"));
			author.setGender(rs.getString("gender"));
			author.setWebSite(rs.getString("web_site"));
			//convert old Date object to a LocalDate
			if(rs.getString("dob") != null)
				author.setDateOfBirth(LocalDate.parse(rs.getString("dob")));
			if(rs.getString("last_modified") != null)
				author.setLastModified(rs.getTimestamp("last_modified").toLocalDateTime());
			author.setGateway(this);
			
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
		return author;
	}
	
	public ObservableList<Author> getAuthors() throws AppException {
		ObservableList<Author> authors = FXCollections.observableArrayList();
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from author");
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Author author = new Author(rs.getString("first_name"), rs.getString("last_name"));
				author.setId(rs.getInt("id"));
				author.setGender(rs.getString("gender"));
				author.setWebSite(rs.getString("web_site"));
				//convert old Date object to a LocalDate
				if(rs.getString("dob") != null)
					author.setDateOfBirth(LocalDate.parse(rs.getString("dob")));
				if(rs.getString("last_modified") != null)
					author.setLastModified(rs.getTimestamp("last_modified").toLocalDateTime());
				author.setGateway(this);
				authors.add(author);
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
		
		return authors;
	}
	
    public ObservableList<authAuditTrail> getAuthAuditTrails(Author author) throws AppException{
        ObservableList<authAuditTrail> authAudits = FXCollections.observableArrayList();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("select a.*, b.id "
                    + " from author_audit_trail a "
                    + " inner join author b on a.author_id = b.id "
                    + " where a.author_id = ?");
            st.setInt(1, author.getId());
            rs = st.executeQuery();
            //st = conn.prepareStatement("");
            while(rs.next()) {
                authAuditTrail audit = new authAuditTrail();
                audit.setAuthId(rs.getInt("id"));
                audit.setAuthDateAdded(rs.getTimestamp("date_Added"));
                audit.setAuthMessage(rs.getString("entry_msg"));
                authAudits.add(audit);
                
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
        return authAudits;
        
    }
	

	public List<Author> getAuthorByAuthorId(int authorId) {
		AuthorGateway gateway = new AuthorGateway(this.conn);
		return gateway.getAuthorByAuthorId(authorId);
	}
	public void insertIntoAuditTrail(int authorId, String message) {
		PreparedStatement st = null;
		try {
			String sql = "insert into author_audit_trail "
					+ " (author_id, entry_msg)" 
					+ " values (?, ?) ";
			st = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			st.setInt(1, authorId);
			st.setString(2, message);
			st.executeUpdate();
			
			ResultSet rs = st.getGeneratedKeys();
			rs.first();
			
			logger.info("updated audit trail of author with id " + authorId);
			
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
	
	public String createAuditMessage(Author oldAuthor, Author newAuthor) {
//		String result = "";
		StringBuilder sb =new StringBuilder();
		if(oldAuthor.getFirstName().compareTo(newAuthor.getFirstName()) != 0) {
			sb.append("First name changed to " + newAuthor.getFirstName() + ". ");
		}
		if(oldAuthor.getLastName().compareTo(newAuthor.getLastName()) != 0) {
			sb.append("Last name changed to " + newAuthor.getLastName() + ". ");
		}
		if(oldAuthor.getDateOfBirth() != newAuthor.getDateOfBirth()) {
			sb.append("Date of birth changed to " + newAuthor.getDateOfBirth() + ". ");
		}
		if(oldAuthor.getGender().compareTo(newAuthor.getGender()) != 0) {
			sb.append("Gender changed to " + newAuthor.getGender() + ". ");
		}
		if(oldAuthor.getWebSite().compareTo(newAuthor.getWebSite()) != 0) {
			sb.append("Website changed to " + newAuthor.getWebSite() + ". ");
		}
		
		
		String result = sb.toString();
		return result;
	}
}
