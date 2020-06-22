package models;

import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;

@Entity
public class Password extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String url;

    @Constraints.Required
    public String username;

    @Constraints.Required
    public String password;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar registrationDate;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar changeDate;
}
