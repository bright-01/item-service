package hello.itemservice.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    private static final Map<Long, Item> store = new HashMap<>();
    private static long sequence = 0L;

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id){
        return store.get(id);
    }

    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Long itemId, Item updateIteParam) {
        // 여기서 정석은 updateItem이 Item 이 아니라 id를 뺀 3개의 상태값만 들어있는 별도의 객체를 만들어 주는 것이 좋음. 왜냐면 update처리시에는 id를 사용하지 않기 때문임.
        // 중복인것 같아도 명확한것이 우선!
        Item findItem = findById(itemId);
        findItem.setItemName(updateIteParam.getItemName());
        findItem.setPrice(updateIteParam.getPrice());
        findItem.setQuantity(updateIteParam.getQuantity());

    }

    public void clearStroe() {
        store.clear();
    }

}
