import java.util.*;

public class Node{
    public int studentNumber;
    public int classCode;
    public int mark;

    public Node nextStudent;
    public Node nextClass;

    public Node(int s, int c, int m){
        studentNumber = s;
        classCode = c;
        mark = m;
        nextStudent = null;
        nextClass = null;
    }
}//class