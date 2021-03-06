/**
 * 
 */
package gradebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Course contains information related to the users contained within it, as
 * well as methods for modifying fields directly contained within it.
 * 
 * @author Daniel Wolf <wolf@ccs.neu.edu>
 * @version April 11, 2014 */

public class MyGradeBook {
    private Map<String, Student> students;
    private Map<String, Assignment> assignments;
    private List<String> assignmentOrder;
    
    /** Creates an empty MyGradeBook */
    private MyGradeBook() {
        students = new HashMap<String, Student>();
        assignments = new HashMap<String, Assignment>();
        assignmentOrder = new ArrayList<String>();
    }
    
    /** Get a list of all assignments stored in MyGradeBook
     * 
     * @return a list of all assignments in MyGradeBook */
    protected Map<String, Assignment> getAssignments() {
        return assignments;
    }
    
    /** Get an Assignment object by its name in string form.
     * Case-insensitive.
     * 
     * @param name the name of the assignment.
     * @return the resulting assignment,
     * @throws NoSuchElementException if the assignment does not exist. */
    protected Assignment getAssignment(String name)
        throws NoSuchElementException {
        if (assignments.containsKey(name)) {
            return assignments.get(name);
        }
        else {
            throw new NoSuchElementException("Assignment does not exist");
        }
    }
    
    /** Adds a student to MyGradeBook
     * 
     * @param name the username of the student
     * @param student the Student object to add
     * @return success */
    protected boolean addStudent(String name, Student student) {
        students.put(name, student);
        return true;
    }
    
    /** Drops a student from the course.
     * 
     * @param student the student object to drop
     * @return success */
    protected boolean dropStudent(Student student) {
        students.remove(student);
        return true;
    }
    
    /** Add an assignment to the grade book.
     * 
     * @param name the assignment's name
     * @param assignment the assignment object to add
     * @return success */
    protected boolean addAssignment(String name, Assignment assignment) {
        assignments.put(name, assignment);
        assignmentOrder.add(name);
        return true;
    }
    
    /** Drop an assignment by name
     * 
     * @param name the assignment by name to drop
     * @return success */
    protected boolean dropAssignment(String name) {
        assignments.remove(name);
        assignmentOrder.remove(name);
        return true;
    }
    
    /** Get a student object by username
     * 
     * @param id the student's username
     * @return the student object */
    protected Student getStudent(String id) {
        return students.get(id);
    }
    
    /** Factory method to construct an empty MyGradebook
     * 
     * @return an empty MyGradeBook */
    public static MyGradeBook initialize() {
        return new MyGradeBook();
    }
    
    /** Factory method to construct a MyGradebook that contains the grade
     * book from filename
     * 
     * @param filename the filename for the file that contains the initial
     *            grade book, which is formatted like initial.txt
     * @return a MyGradebook that contains the grade book from filename */
    public static MyGradeBook initializeWithFile(String filename) {
        File f = new File(filename);
        Scanner s;
        try {
            s = new Scanner(f);
            String fileString = "";
            while (s.hasNextLine()) {
                fileString += s.nextLine() + "\n";
            }
            s.close();
            return initializeWithString(fileString);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return initialize();
        }
        
    }
    
    /** Factory method to construct a MyGradebook that contains the grade
     * book from startingString
     * 
     * @param startingString String that contains the initial grade book,
     *            which is formatted like initial.txt
     * @return a MyGradebook that contains the grade book from
     *         startingString */
    public static MyGradeBook initializeWithString(String startingString) {
        MyGradeBook gb = MyGradeBook.initialize();
        String[] lines = startingString.split("\n");
        try {
            if (lines.length >= 4 && lines[0].equals("GRADEBOOK")) {
                String[] assignments = lines[1].split("\t");
                String[] pointsOutOf = lines[2].split("\t");
                String[] pointsGrade = lines[3].split("\t");
                
                for (int ai = 5; ai < assignments.length; ai++) {
                    gb.addAssignment(assignments[ai], new Assignment(
                            assignments[ai], "", "", new Double(
                                    pointsOutOf[ai]), new Double(
                                            pointsGrade[ai])));
                }
                // Parse assignment grades
                for (int l = 4; l < lines.length; l++) {
                    String[] student = lines[l].split("\t");
                    gb.addStudent(student[0], new Student(student[1],
                            student[2], student[3], student[0], student[4],
                            student[0]));
                    
                    for (int grade = 5; grade < student.length; grade++) {
                        gb.getAssignment(assignments[grade]).addGrade(
                                student[0], new Double(student[grade]));
                    }
                }
                
                return gb;
            }
            else {
                throw new RuntimeException("File is not a valid Gradebook");
            }
            
        }
        catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("File is not a valid Gradebook");
        }
    }
    
    /** Add to the state of this grade book---new assignments, new students,
     * new grades---by processing filename
     * 
     * @param filename the filename for a file that contains information
     *            that will be added to the grade book. The file could
     *            contain several different types of information---new
     *            assignments, new students, new grades. The file will be
     *            formatted like addAssignments.txt, addStudents.txt,
     *            gradesForAssignment1.txt, and gradesForStudent.txt. */
    public void processFile(String filename) {
        Scanner s = null;
        try {
            File f = new File(filename);
            s = new Scanner(f);
            
            String fileString = "";
            while (s.hasNextLine()) {
                fileString += s.nextLine() + "\n";
            }
            s.close();
            processString(fileString);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }
    
    /** Add to the state of this grade book---new assignments, new students,
     * new grades---by processing additionalString
     * 
     * @param additionalString String that contains information that will be
     *            added to the grade book. The String could contain several
     *            different types of information---new assignments, new
     *            students, new grades. The String will be formatted like
     *            addAssignments.txt, addStudents.txt,
     *            gradesForAssignment1.txt, and gradesForStudent.txt. */
    public void processString(String additionalString) {
        String[] lines = additionalString.split("\n");
        if (lines[0].equals("ASSIGNMENT")) {
            addAssignment(lines[1], new Assignment(lines[1], "", "",
                    new Double(lines[2]), new Double(lines[3])));
            for (Student student : students.values()) {
                assignments.get(lines[1]).addGrade(student.id, 0);
            }
            // if more steps need to be taken, combine lines and pass
            // recursively.
            if (lines.length > 4) {
                String next = "";
                for (int i = 4; i < lines.length; i++) {
                    next += lines[i] + "\n";
                }
                processString(next);
            }
        }
        else if (lines[0].equals("STUDENT")) {
            addStudent(lines[1], new Student(lines[2], lines[3], lines[4],
                    lines[1], lines[5], lines[1]));
            for (Assignment a : this.assignments.values()) {
                a.addGrade(lines[1], 0);
            }
            // if more steps need to be taken, combine lines and pass
            // recursively.
            if (lines.length > 6) {
                String next = "";
                for (int i = 6; i < lines.length; i++) {
                    next += lines[i] + "\n";
                }
                processString(next);
            }
        }
        else if (lines[0].equals("GRADES_FOR_ASSIGNMENT")) {
            Assignment a = getAssignment(lines[1]);
            for (int i = 2; i < lines.length; i = i + 2) {
                a.addGrade(lines[i], new Double(lines[i + 1]));
            }
        }
        else if (lines[0].equals("GRADES_FOR_STUDENT")) {
            String student = lines[1];
            for (int i = 2; i < lines.length; i = i + 2) {
                Assignment a = getAssignment(lines[i]);
                a.addGrade(student, new Double(lines[i + 1]));
            }
        }
        else if (lines[0].equals("")) {
            return;
        }
        else {
            throw new RuntimeException("Unexpected Operation Type");
        }
    }
    
    /** Changes the assignment (named assignmentName) grade for student
     * (whose username is equal to username) to newGrade
     * 
     * @param assignmentName name of the assignment
     * @param username username for the student
     * @param newGrade the new grade for the given assignment and student
     * @return whether there was a grade to change. Returns true if the
     *         given assignment/student combination exists, returns false
     *         otherwise
     * @throws NoSuchElementException if given assigmentName or username do
     *             not exist */
    public boolean changeGrade(String assignmentName, String username,
            double newGrade) throws NoSuchElementException {
        if (assignments.containsKey(assignmentName)) {
            assignments.get(assignmentName).setGrade(username, newGrade);
            return true;
        }
        else {
            throw new NoSuchElementException("Given username or assignment"
                    + "does not exist");
        }
    }
    
    /** Calculates the average across all students for a given assignment
     * 
     * @param assignmentName name of the assignment
     * @return the average across all students for assignmentName */
    public double average(String assignmentName) {
        return getAssignment(assignmentName).calculateAverage();
    }
    
    /** Calculates the median across all students for a given assignment
     * 
     * @param assignmentName name of the assignment
     * @return the median across all students for assignmentName */
    public double median(String assignmentName) {
        return getAssignment(assignmentName).calculateMedian();
    }
    
    /** Calculates the min across all students for a given assignment
     * 
     * @param assignmentName name of the assignment
     * @return the min across all students for assignmentName */
    public double min(String assignmentName) {
        return getAssignment(assignmentName).getMin();
    }
    
    /** Calculates the max across all students for a given assignment
     * 
     * @param assignmentName name of the assignment
     * @return the max across all students for assignmentName */
    public double max(String assignmentName) {
        return getAssignment(assignmentName).getMax();
    }
    
    /** Calculates the current grade for the given student
     * 
     * @param username username for the student
     * @return the current grade for student with username. The current
     *         grade is calculated based on the current assignment grades,
     *         assignment total points, assignment percent of semester. The
     *         current grade for a student is the sum of the relative
     *         assignment grades divided by the current percent of semester
     *         time 100. Since all grades may not currently be entered, we
     *         have to divide by the current percent. The relative
     *         assignment grade is the student's assignment grade divide by
     *         total point value for the assignment times the percent of
     *         semester. */
    public double currentGrade(String username) {
        Double sumRelativeAssignmentGrades = new Double(0);
        Double sumWeights = new Double(0);
        
        for (Assignment a : assignments.values()) {
            try {
                sumRelativeAssignmentGrades +=
                        ((a.getGrade(username) / a.getTotalPointsPossible()) * a
                                .getWeight());
                sumWeights += a.getWeight();
            }
            catch (NoSuchElementException e) {
                // Do nothing, the student just missed this assignment and
                // will be deducted full credit
            }
        }
        Double ret = 100 * sumRelativeAssignmentGrades / sumWeights;
        ret = Math.round(ret * 100) / 100.0;
        return ret;
    }
    
    /** Calculates the current grade for all students
     * 
     * @return HashMap of the current grades for all students. The key of
     *         the HashMap is the username of the student. The value is the
     *         current grade for the associated student. The current grade
     *         is calculated based on the current assignment grades,
     *         assignment total points, assignment percent of semester. The
     *         current grade for a student is the sum of the relative
     *         assignment grades divided by the current percent of semester
     *         time 100. Since all grades may not currently be entered, we
     *         have to divide by the current percent. The relative
     *         assignment grade is the student's assignment grade divide by
     *         total point value for the assignment times the percent of
     *         semester. */
    public HashMap<String, Double> currentGrades() {
        HashMap<String, Double> currentGrades = new HashMap<String, Double>();
        for (String studentID : students.keySet()) {
            currentGrades.put(studentID, currentGrade(studentID));
        }
        return currentGrades;
    }
    
    /** Provides the grade earned by the given student for the given
     * assignment
     * 
     * @param assignmentName name of the assignment
     * @param username username for the student
     * @return the grade earned by username for assignmentName */
    public double assignmentGrade(String assignmentName, String username) {
        return assignments.get(assignmentName).getGrade(username);
    }
    
    /** Provide a String that contains the current grades of all students in
     * the course
     * 
     * @return a String that contains the current grades of all students in
     *         the course. The String should be formatted like
     *         currentGrades.txt---CURRENT_GRADES heading and each row:
     *         username followed by tab and current grade. The usernames
     *         will be listed alphabetically. */
    public String outputCurrentGrades() {
        HashMap<String, Double> grades = currentGrades();
        
        String output = "CURRENT_GRADES\n";
        for (String student : grades.keySet()) {
            output += student + "\t" + grades.get(student) + "\n";
        }
        
        return output;
    }
    
    /** Provide a String that contains the current grades of the given
     * student
     * 
     * @param username username for student
     * @return a String that contains the current grades of username. The
     *         String should be formatted like
     *         studentGrades.txt---STUDENT_GRADES heading, student info,
     *         dividers, each assignment (assignment name followed by tab
     *         and assignment grade), and current grade. Assignments are to
     *         remain in the same order as given.
     * @throws NoSuchElementException is a student without given username
     *             does not exist */
    public String outputStudentGrades(String username)
        throws NoSuchElementException {
        if (students.containsKey(username)) {
            Student s = getStudent(username);
            String export = "STUDENT_GRADES\n";
            
            export += s.getID() + "\n";
            export += s.getFirstName() + "\n";
            export += s.getLastName() + "\n";
            export += s.getAdvisor() + "\n";
            export += s.getGraduationYear() + "\n";
            
            export += "----\n";
            
            for (String assignmentName : assignmentOrder) {
                export +=
                        assignmentName + "\t"
                                + assignmentGrade(assignmentName, username)
                                + "\n";
                
            }
            
            export += "----\n";
            
            export += "CURRENT GRADE\t" + currentGrade(username);
            
            return export;
        }
        else {
            throw new NoSuchElementException("Student with given username "
                    + "does not exist");
        }
    }
    
    /** Provide a String that contains the assignment grades of all students
     * in the course for the given assignment
     * 
     * @param assignName name of the assignment
     * @return a String that contains the assignment grades of all students
     *         in the course for assignName. The String should be formatted
     *         like assignmentGrade.txt---ASSIGNMENT_GRADES heading,
     *         assignment info, dividers, each student (username followed by
     *         tab and assignment grade), and assignment stats. The
     *         usernames will be listed alphabetically while assignments are
     *         to remain in the same order as given.
     * @throws NoSuchElementException if given assignmentName does not exist */
    public String outputAssignmentGrades(String assignName)
        throws NoSuchElementException {
        if (assignments.containsKey(assignName)) {
            String export = "ASSIGNMENT_GRADES\n";
            Assignment a = getAssignment(assignName);
            
            export += assignName + "\n";
            export += a.getTotalPointsPossible() + "\n";
            export += a.getWeight() + "\n";
            
            export += "----\n";
            // Get and sort students by alphabetical, case insensitive
            // order.
            List<String> sts = new ArrayList<String>();
            for (String str : students.keySet()) {
                sts.add(str);
            }
            Collections.sort(sts, String.CASE_INSENSITIVE_ORDER);
            for (String student : sts) {
                export += student + "\t" + a.getGrade(student) + "\n";
            }
            
            export += "----\n";
            
            export += "STATS\n";
            export += "Average\t" + average(assignName) + "\n";
            export += "Median\t" + median(assignName) + "\n";
            export += "Max\t" + max(assignName) + "\n";
            export += "Min\t" + min(assignName) + "\n";
            
            return export;
        }
        else {
            throw new NoSuchElementException("Assignment does not exist");
        }
    }
    
    /** Provide a String that contains the current grade book. This String
     * could be used to initialize a new grade book.
     * 
     * @return a String that contains the current grade book. This String
     *         could be used to initialize a new grade book. The String
     *         should be formatted like gradebook.txt. The usernames will be
     *         listed alphabetically. */
    public String outputGradebook() {
        String export = "GRADEBOOK\n";
        // Assignments
        export += "\t\t\t\t";
        for (String assn : assignmentOrder) {
            export += "\t" + assn;
        }
        export += "\n";
        
        // Max points
        export += "\t\t\t\t";
        for (String assn : assignmentOrder) {
            export += "\t" + getAssignment(assn).getTotalPointsPossible();
        }
        export += "\n";
        
        // Weights
        export += "\t\t\t\t";
        for (String assn : assignmentOrder) {
            export += "\t" + getAssignment(assn).getWeight();
        }
        export += "\n";
        // Students and grades
        // Get and sort students by alphabetical, case insensitive order.
        List<String> sts = new ArrayList<String>();
        for (String str : students.keySet()) {
            sts.add(str);
        }
        Collections.sort(sts, String.CASE_INSENSITIVE_ORDER);
        for (String s : sts) {
            Student st = getStudent(s);
            export +=
                    s + "\t" + st.getFirstName() + "\t" + st.getLastName()
                            + "\t" + st.getAdvisor() + "\t"
                            + st.getGraduationYear();
            
            for (String assn : assignmentOrder) {
                export += "\t" + assignmentGrade(assn, s);
            }
            export += "\n";
        }
        
        return export;
    }
    
    /** Get the order of assignments as assigned.
     * 
     * @return the order of assignments. */
    public List<String> getAssignmentOrder() {
        return this.assignmentOrder;
    }
    
    /** Checks if this MyGradeBook is equal to given Object
     * 
     * @param obj the object equality is being checked against 
     * @return whether the objects are equal */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MyGradeBook) {
            MyGradeBook temp = (MyGradeBook) obj;
            return this.students.equals(temp.students)
                    && this.assignments.equals(temp.assignments);
        }
        else {
            return false;
        }
    }
    
    /** creates an int hash of this MyGradeBook
     * 
     * @return an int hash */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += assignments.hashCode() * 2411;
        hash += students.hashCode() * 2371;
        return hash;
    }
}
