package models;

import io.ebean.Model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseModel extends Model {

   @Id
   @GeneratedValue
   public Long id;

}
