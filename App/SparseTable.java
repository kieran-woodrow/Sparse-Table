import java.util.*;
import java.lang.Math;

public class SparseTable{
    Node[] students;
    Node[] classes;

    int numStudents;
    int numClasses;

    int[] weights;

    public SparseTable(int s, int c){
        numStudents = s;
        numClasses = c;

        students = new Node[numStudents];
        classes = new Node[numClasses];

        for(int i = 0; i < numStudents; i++){
            students[i] = null;
        }

        for(int i = 0; i < numClasses; i++){
            classes[i] = null;
        }

        weights = new int[numClasses];
    }

    public void addMark(int s, int c, int m){//will add the marks in descending order
        if(m < 0){
            System.out.println("A mark for Student " + s + " for Class " + c + " cannot be negative");
        }

        //first check that the mark does not already exist
        if(checkExists(s, c) != null){
            System.out.println("A mark for Student " + s + " for Class " + c + " already exists");
            return;
        }
        
        Node newMark = new Node(s,c,m);

        //find the place to add the student

        //first - add to the student's list of class marks
        //students marks in descending order, most recent mark gets put earlier in the list
        
        if(students[s] == null){
            students[s] = newMark;
        }
        else{
            if(students[s].mark <= m){
                newMark.nextClass = students[s];
                students[s] = newMark;
            }
            else{
                Node myClasses = students[s];

                while(myClasses.nextClass != null && myClasses.nextClass.mark > m){
                    myClasses = myClasses.nextClass;
                }

                newMark.nextClass = myClasses.nextClass;
                myClasses.nextClass = newMark;
            }
        }

        //second - add to the classes marks
        //marks for the class in descending order, most recent mark gets put earlier in the list
        
        if(classes[c] == null){
            classes[c] = newMark;
        }
        else{
            if(classes[c].mark <= m){
                newMark.nextStudent = classes[c];
                classes[c] = newMark;
            }
            else{
                Node myStudents = classes[c];

                while(myStudents.nextStudent != null && myStudents.nextStudent.mark > m){
                    myStudents = myStudents.nextStudent;
                }

                newMark.nextStudent = myStudents.nextStudent;
                myStudents.nextStudent = newMark;
            }
        }
    }

    public void displayStudentResults(int s){
        if(s >= 0 && s < numStudents){
            if(students[s] != null){
                System.out.println("Student " + s);
                Node currentClass = students[s];

                while(currentClass.nextClass != null){
                    System.out.print(currentClass.mark + "(class: " + currentClass.classCode + "), ");
                    currentClass = currentClass.nextClass;
                }

                System.out.println(currentClass.mark + "(class: " + currentClass.classCode + ")");
                System.out.println();
            }
        }
    }

    public void displayClassResults(int c){
        if(c >= 0 && c < numClasses){
            if(classes[c] != null){
                System.out.println("Class " + c);
                Node currentStudent = classes[c];

                while(currentStudent.nextStudent != null){
                    System.out.print(currentStudent.mark + ", ");
                    currentStudent = currentStudent.nextStudent;
                }

                System.out.println(currentStudent.mark);
                System.out.println();
            }
        }
    }

    public void displayMark(int s, int c){
        if((s >= 0 && s < numStudents) && (c >= 0 && c < numClasses)){
            if(students[s] != null && classes[c] != null){
                if(students[s].classCode == c){
                    System.out.println("Mark for Student " + s + " for Class " + c + " is: "+ students[s].mark);
                    System.out.println();
                }
                else{
                    Node currentClass = students[s];

                    while(currentClass.nextClass != null && currentClass.nextClass.classCode != c){
                        currentClass = currentClass.nextClass;
                    }

                    if(currentClass.nextClass != null){
                        System.out.println("Mark for Student " + s + " for Class " + c + " is: "+ currentClass.nextClass.mark);
                        System.out.println();
                    }
                    else{
                        System.out.println("Mark for Student " + s + " for Class " + c + " was not found");
                        System.out.println();
                    }
                }
            }
        }
    }

    public Node checkExists(int s, int c){
        if(students[s] != null && classes[c] != null){
            //loop through the student's marks
            if(students[s].classCode == c){
                return students[s];
            }
            else{
                Node currentClass = students[s];

                while(currentClass.nextClass != null && currentClass.nextClass.classCode != c){
                    currentClass = currentClass.nextClass;
                }

                return (currentClass.nextClass);
            }
        }
        else{
            return null;
        }
    }

    public void updateMark(int s, int c, int m){
        Node node = checkExists(s, c);

        if(node != null){
            node.mark = m;
        }
    }

    public int getClassCode(int s, int m){
        for(int i = 0; i < numClasses; i++){
            Node node = checkExists(s, i);

            if(node != null && node.mark == m){
                return node.classCode;
            }
        }

        return -1;
    }

    public int getMark(int s, int c){
        Node node = checkExists(s, c);

        if(node != null){
            return node.mark;
        }

        return -1;
    }

    public void setClassWeighting(int c, int w){
        weights[c] = w;
    }

    public void displayClassWeightings(){
        System.out.println("Class\t" + "Weighting");

        for(int i = 0; i < numClasses; i++){
            System.out.println(i + "\t" + weights[i]);
        }

        System.out.println();
    }

    public void displayStudentAverages(){
        System.out.println("Student\t" + "Average(%)\t" + "Weighted Average(%)");
        System.out.println();

        for(int i = 0; i < numStudents; i++){
            System.out.print(i + "\t");

            if(students[i] != null){
                int total = 0;
                int numOfClasses = 0;
                int weightedTotal = 0;
                int totalCredits = 0;
                Node currentClass = students[i];

                while(currentClass != null){
                    numOfClasses++;
                    total += currentClass.mark;

                    weightedTotal += currentClass.mark*(weights[currentClass.classCode]);
                    totalCredits += weights[currentClass.classCode];

                    currentClass = currentClass.nextClass;
                }

                double average = total/numOfClasses;
                double weightedAverage = Math.round((Double.valueOf(weightedTotal)/totalCredits)*100d)/100d;
                System.out.println(average + "\t\t" + weightedAverage);
            }
            else{
                System.out.println("N/A" + "\t\t" + "N/A");
            }
        }
    }

	public static void main(String[] args) {
        int s = 10;
        int c = 5;
    
        System.out.println("output for SparseTable table");
        System.out.println();

        SparseTable table = new SparseTable(s, c);

        table.setClassWeighting(0, 4);
        table.setClassWeighting(1, 6);
        table.setClassWeighting(2, 13);
        table.setClassWeighting(3, 16);
        table.setClassWeighting(4, 8);

        table.displayClassWeightings();

        table.addMark(0, 0, 76);
        table.addMark(0, 3, 82);
        table.addMark(0, 3, 82);
        table.addMark(0, 1, 74);
        table.addMark(0, 2, 80);
        table.addMark(0, 4, 88);

        table.addMark(1, 0, 84);
        table.addMark(1, 1, 78);
        table.addMark(1, 4, 72);
        table.addMark(1, 2, 95);
        table.addMark(1, 2, -1);

        table.addMark(3, 4, 97);

        table.addMark(2, 3, 85);
        table.addMark(2, 2, 96);

        table.addMark(4, 4, 74);
        table.addMark(4, 0, 88);
        table.addMark(4, 3, 74);

        table.updateMark(2, 3, 87);

        System.out.println();
        System.out.println("displaying the averages for all of the students");
        table.displayStudentAverages();
        System.out.println();
        
        System.out.println("displaying the results of all the students");
        System.out.println();

        for(int i = 0; i < table.numStudents; i++)
                table.displayStudentResults(i);
        
        System.out.println("displaying the results for all of the classes");
        System.out.println();

        for(int i = 0; i < table.numClasses; i++)
                table.displayClassResults(i);

        table.displayMark(3, 4);
        table.displayMark(2, 4);
	}//main
}//class