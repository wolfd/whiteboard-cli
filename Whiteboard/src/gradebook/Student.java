/**
 * 
 */
package gradebook;

/** Class representing a Student
 * 
 * @author Daniel Wolf <wolf@ccs.neu.edu>
 * @version Mar 30, 2014 */
public class Student extends User {
    /** Graduation year of the student */
    protected String graduationYear;
    /** The User's advisor's name, as a String */
    protected String advisor;
    
    /** Creates a new Student with given parameters
     * 
     * @param fn
     *            the Student's first name
     * @param ln
     *            the Student's last name
     * @param adv
     *            the Student's advisor's name
     * @param i
     *            the Student's ID number
     * @param grad
     *            the Student's graduation year
     * @param pass
     *            the Student's password */
    protected Student(String fn, String ln, String adv, String i,
            String grad, String pass) {
        this.firstName = fn;
        this.advisor = adv;
        this.lastName = ln;
        this.id = i;
        this.graduationYear = grad;
        this.password = pass;
    }
    
    /** Checks if this User is a Teacher
     * 
     * @return true if User is a Teacher, else false */
    protected boolean isTeacher() {
        return false;
    }
    
    /** Get the User's graduation year
     * 
     * @return a String, representing */
    protected String getGraduationYear() {
        return graduationYear;
    }
    
    /** Set the student's graduation year with a String (e.g. "2017")
     * 
     * @param grad
     *            the year to set graduation year to */
    protected void setGraduationYear(String grad) {
        graduationYear = grad;
    }
    
    /** Gets this User's advisor's name
     * 
     * @return a String, this User's advisor's name */
    protected String getAdvisor() {
        return this.advisor;
    }
    
    /** Sets this user's advisor's name
     * 
     * @param adv
     *           the User's new advisor */
    protected void setAdvisor(String adv) {
        this.advisor = adv;
    }
    
}
