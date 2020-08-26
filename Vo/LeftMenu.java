package capstion.fluseo.Vo;

import java.util.ArrayList;

public class LeftMenu {
    public ArrayList<String> child;
    public String groupName;
    public LeftMenu(String name){
        groupName = name;
        child = new ArrayList<String>();
    }

}
