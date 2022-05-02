package hello.itemservice.web.basic;

import hello.itemservice.domain.Item;
import hello.itemservice.domain.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    // @RequiredArgsConstructor 를 붙이면
    /**
     * final 이 붙은 멤버변수만 사용해서 생성자를 자동으로 만들어준다.
     * public BasicItemController(ItemRepository itemRepository) {
     * this.itemRepository = itemRepository;
     * }
     *
     * */

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);

        return "basic/items";
    }


    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName, @RequestParam int price, @RequestParam Integer quantity, Model model) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item) {

        // ModelAttribute
        // 1. 요청 파라미터 - Item 객체를 생성하고, 요청 파라미터의 값을 프로퍼티 접근법 ( setXxx) 로 입력 해준다
        // 2. Model 추가 - Model에 지정한 객체를 자동으로 넣어준다. 아래의 addAttribute

        itemRepository.save(item);
//        model.addAttribute("item", item); // 자동 추가, 생략가능

        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) { // @ModelAttribute 의 값을 파라미터 객체의 첫글자를 소문자로 바꾼 값을 model에 담는다.
        //        model.addAttribute("item", item); // 이렇게 담기고
        //        model.addAttribute("helloData", item); // HelloData -> helloData 로 담기게 된다
        itemRepository.save(item);
        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV4(Item item) { // @ModelAttribute 생략이 가능.. model attribute 에담기는 값은 위랑 동일한 규칙이 적용
        itemRepository.save(item);
        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV5(Item item) { // PRG 패턴 적용
        itemRepository.save(item);
        return "redirect:/basic/items/"+item.getId();
    }

    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) { // PRG 패턴 적용
        Item save = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", save.getId()); // url encoding 해결
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item,  Model model){
        itemRepository.update(itemId, item);

        return "redirect:/basic/items/{itemId}";
    }




    /**
     * 테스트 데이터 추가
     * */
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("testA", 10000, 10));
        itemRepository.save(new Item("testB", 20000, 20));
    }

}
