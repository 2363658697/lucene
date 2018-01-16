package cn.et.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import cn.et.entity.Foods;

@Mapper
public interface FoodsMapper {

    @Select("select * from foods limit  #{0},#{1}")
    public List<Foods> queryFoods(Integer index, Integer rows);
    
    @Select("select count(*) from foods")
    public Integer getCounts();
}
