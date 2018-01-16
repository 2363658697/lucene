package cn.et.dao;

import java.util.List;

import cn.et.entity.Foods;

public interface FoodsDao {

    public List<Foods> queryFoods(Integer  index,Integer rows);
    
    public Integer getCounts();
}