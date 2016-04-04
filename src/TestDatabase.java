import java.sql.SQLException;

import model.AgeCategory;
import model.Database;
import model.EmploymentCategory;
import model.Gender;
import model.Person;


public class TestDatabase {
	public static void main(String[] args){
		System.err.println("RUNNING TEST");
		Database db = new Database();	
		try {
			db.connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.addPerson(new Person("Pete","Boss",AgeCategory.senior,EmploymentCategory.selfEmployed,"400",true,Gender.male));
		db.addPerson(new Person("Em","Student",AgeCategory.adult,EmploymentCategory.employed,null,false,Gender.female));
		
		try {
			db.save();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		try {
			db.load();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.disconnect();
	}
}
