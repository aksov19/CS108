package hw05;

import java.util.HashMap;

public class ShoppingCart {
    private final HashMap<String, Integer> cart;


    public ShoppingCart(){
        cart = new HashMap<>();
    }


    public void addItem(String id){
        if(cart.containsKey(id)){
            cart.put(id, cart.get(id)+1);
        }
        else{
            cart.put(id, 1);
        }
    }


    public void setItemCount(String id, int count){
        if (count <= 0){
            cart.remove(id);
        }
        else {
            cart.put(id, count);
        }
    }


    public HashMap<String, Integer> getItems(){
        return cart;
    }
}
