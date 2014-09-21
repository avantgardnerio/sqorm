package net.squarelabs.sqorm.index;

import java.util.ArrayList;

public class Key extends ArrayList<Object> {

    @Override
    public int hashCode() {
        int hash = 0;
        for(Object obj : this) {
            if(obj != null) {
                hash += obj.hashCode();
            }
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Key == false) {
            return false;
        }
        Key other = (Key)obj;
        if(this.size() != other.size()) {
            return false;
        }
        for(int i = 0; i < size(); i++) {
            Object a = this.get(i);
            Object b = other.get(i);
            if(a == null && b == null) {
                continue;
            }
            if(a == null || b == null) {
                return false;
            }
            if(!a.equals(b)) {
                return false;
            }
        }
        return true;
    }

}
