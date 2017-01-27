package at.mtel.denza.alfresco.jpa;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.db.jpa.BasicLogEventEntity;

@Entity
@Table(name="application_log")
public class JpaLogEntity extends BasicLogEventEntity {
    private static final long serialVersionUID = 1L;
    private long id = 0L;
    //private long timeMilis = 0L;
    private Timestamp time = null;
    private Calendar calendar = Calendar.getInstance();
 
    public JpaLogEntity() {
        super(); 
    }
    
    public JpaLogEntity(LogEvent wrappedEvent) {
        super(wrappedEvent);
    }
 
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long getId() {
        return this.id;
    }
 
    public void setId(long id) {
        this.id = id;
    }
    
    @Column(name = "time")
    public Timestamp getTime() {
    	calendar.setTimeInMillis(super.getTimeMillis());
    	time = new java.sql.Timestamp(calendar.getTime().getTime());
        return time;
    }
    
    public void setTime(Timestamp time) {
    	calendar.setTimeInMillis(super.getTimeMillis());
    	time = new java.sql.Timestamp(calendar.getTime().getTime());
        this.time = time;
    }  
 
    // If you want to override the mapping of any properties mapped in BasicLogEventEntity,
    // just override the getters and re-specify the annotations.
}		