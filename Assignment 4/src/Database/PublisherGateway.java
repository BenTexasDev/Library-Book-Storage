package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import App.AppException;
import Book.Publisher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class PublisherGateway {
	private static Logger logger = LogManager.getLogger();

	private Connection conn;
	
	public PublisherGateway(Connection conn) {
		this.conn = conn;
	}
	
	public Publisher getPublisherById(int id) throws AppException {
		Publisher publisher = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("select * from publisher where id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();

			rs.next();
			
			publisher = new Publisher(rs.getInt("id"), rs.getString("publisher_name"));
			
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return publisher;
		
	}
	
	public ObservableList<Publisher> getPublishers() throws AppException{
		ObservableList<Publisher> publishers = FXCollections.observableArrayList();
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("select * from publisher order by id");
			rs = st.executeQuery();

			while(rs.next()) {
				Publisher publisher = new Publisher(rs.getInt("id"), rs.getString("publisher_name"));
				publishers.add(publisher);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return publishers;
		
	}

}
