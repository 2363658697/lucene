package cn.et.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.et.service.FoodsService;
import cn.et.util.LuceneUtils;

@RestController
public class FoodsController {
    @Autowired
    private FoodsService foodsService;

    @RequestMapping("/write")
    public String writeData() throws IOException {

        foodsService.write();

        return "写入数据成功";
    }

    @RequestMapping("/searchFood")
    public List<Map<String, String>> search(String keyWord) throws Exception {
        String field = "foodName";

        List<Map<String, String>> list = LuceneUtils.search(field, keyWord);
        return list;
    }

}
