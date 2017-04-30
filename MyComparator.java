package project2;
import java.util.Comparator;

public class MyComparator implements Comparator<Event>{
    @Override
    public int compare(Event e1, Event e2){
        if (e1.getTime() > e2.getTime()){
            return 1;
        }
        else if (e1.getTime() < e2.getTime()){
            return -1;
        }
        else{
        	if(e1.getStatus() > e2.getStatus()){
            	return 1;
            }
            else if(e1.getStatus() < e2.getStatus()){
            	return -1;
            }
            else{
            	if (e1.getID().compareTo(e2.getID()) > 0){
                    return 1;
                }
                else if (e1.getID().compareTo(e2.getID()) < 0){
                    return -1;
                }
                else{
                    return 0;
                }
            }
        }
    }
}
