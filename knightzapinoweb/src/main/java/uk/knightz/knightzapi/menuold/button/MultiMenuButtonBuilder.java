package uk.knightz.knightzapi.menuold.button;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiMenuButtonBuilder extends MenuButtonBuilder {

    private Map<Integer, List<MenuButton>> buttonTree = new HashMap<>();

    public MenuButton buildCurrent(){
        return null;
    }
//    public List<MenuButton> buildAll(){
//
//    }
}
