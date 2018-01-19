package cn.et.service;

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.et.dao.FoodsDao;
import cn.et.entity.Foods;
import cn.et.util.LuceneUtils;

@Service
public class FoodsServiceImpl implements FoodsService {

    @Autowired
    private FoodsDao foodsDao;

    /**
     * 分页读取数据写入索引库
     */
    @Override
    public void write() {
        int index = 0;
        int rows = 5;

        int counts = foodsDao.getCounts();

        while (index <= counts) {
            List<Foods> queryFoods = foodsDao.queryFoods(index, rows);
            for (Foods foods : queryFoods) {
                Document doc = new Document();
                Field field = new Field("foodid", String.valueOf(foods.getFoodid()), TextField.TYPE_STORED);
                doc.add(field);
                Field field1 = new Field("foodName", foods.getFoodName(), TextField.TYPE_STORED);
                doc.add(field1);
                Field field2 = new Field("price", String.valueOf(foods.getPrice()), TextField.TYPE_STORED);
                doc.add(field2);
                try {
                    LuceneUtils.write(doc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            index += rows;
        }

    }

}
