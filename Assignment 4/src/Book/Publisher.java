package Book;

import javafx.beans.property.SimpleStringProperty;

public class Publisher {
	private int id;
	private SimpleStringProperty publisherName;
	
	public Publisher() {
		id = 0;
		publisherName = new SimpleStringProperty();		
	}
	
	public Publisher(int id, String publisherName) {
		this();
		
		this.id = id;
		this.publisherName.set(publisherName);
	}
	
	@Override
	public String toString() {
		return publisherName.get();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Publisher other = (Publisher) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	// accessors
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getPublisherName() {
		return publisherName.get();
	}

	public void setSummary(String publisherName) {
		this.publisherName.set(publisherName);
	}
	
	public SimpleStringProperty publisherNameProperty() {
		return publisherName;
	}
}
