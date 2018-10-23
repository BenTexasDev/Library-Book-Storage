package Author;
import java.util.Date;

public class authAuditTrail {
	
	private int id;
	private Date dateAddedAuth;
	private String messageAuth;
	
	public authAuditTrail() {
		
	}
	public authAuditTrail(int id, Date dateAddedAuth, String messageAuth){
		this.id = id;
		this.dateAddedAuth = dateAddedAuth;
		this.messageAuth = messageAuth;
	}

	public int getAuthId() {
		return id;
	}

	public void setAuthId(int id) {
		this.id = id;
	}

	public Date getAuthDateAdded() {
		return dateAddedAuth;
	}

	public void setAuthDateAdded(Date dateAddedAuth) {
		this.dateAddedAuth = dateAddedAuth;
	}

	public String getAuthMessage() {
		return messageAuth;
	}

	public void setAuthMessage(String messageAuth) {
		this.messageAuth = messageAuth;
	}
	
	@Override
	public String toString() {
		return dateAddedAuth + " " +messageAuth;
	}


}
