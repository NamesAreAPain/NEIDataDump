package dev.namesareapain.neidatadump;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class MasterItemList implements Iterable<ItemStack>{
    public static class MasterItemListIterator implements Iterator<ItemStack> {

        Iterator<Object> baseItems;
        Iterator<ItemStack> subItems;

        public MasterItemListIterator(){
            this.baseItems = Item.itemRegistry.iterator(); 
            this.subItems = (new ArrayList<ItemStack>()).iterator(); 
        }

        public boolean hasNext(){
            return baseItems.hasNext() || subItems.hasNext();
        }

        public ItemStack next() throws NoSuchElementException{
            if(this.subItems.hasNext()){
                return this.subItems.next();
            } else if(!this.baseItems.hasNext()){
                throw new NoSuchElementException();
            } else {
                Item baseItem = (Item) baseItems.next();
                    ArrayList<ItemStack> newSubItems = new ArrayList<ItemStack>();
                    baseItem.getSubItems(baseItem,null,newSubItems);
                    this.subItems = newSubItems.iterator();
                if (this.subItems.hasNext()){
                    return this.subItems.next();
                } else {
                    return new ItemStack(baseItem);
                }
            }

        }
    }

    public Iterator<ItemStack> iterator(){
        return new MasterItemListIterator();
    }

}
