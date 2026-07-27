import java.util.List;

public class Sort {
    // selection sort — category first, then part code
    public static void sortInventoryByCategoryThenCode(List<InventoryItem> items){
        for (int i = 0; i < items.size() - 1; i++){
            int smallest = i;
            for (int j=i+1; j<items.size(); j++){
                InventoryItem current=items.get(j);
                InventoryItem smallestItem=items.get(smallest);
                int compare=current.getCategory().compareToIgnoreCase(smallestItem.getCategory());
                boolean shouldSwap=false;
                if (compare<0){
                    shouldSwap=true;
                } else if (compare==0) {
                    if (current.getCode().compareToIgnoreCase(smallestItem.getCode())<0) {
                        shouldSwap=true;
                    }
                }
                if (shouldSwap){
                    smallest=j;
                }
            }
            if (smallest != i){
                InventoryItem temp=items.get(i);
                items.set(i, items.get(smallest));
                items.set(smallest, temp);
            }
        }
    }
}
