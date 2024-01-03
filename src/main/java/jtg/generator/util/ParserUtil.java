package jtg.generator.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: JTestGenerator
 * @description:
 * @author: 作者
 * @create: 2024-01-04 02:31
 */
public class ParserUtil {

    public static Set<List<String>> parseStringToJson(String input) {
        // 解析字符串为JSONArray
        JSONArray jsonArray = JSON.parseArray(input);

        // 使用HashSet来存储结果，确保唯一性
        Set<List<String>> resultSet = new HashSet<>();

        // 遍历JSONArray
        for (int i = 0; i < jsonArray.size(); i++) {
            // 获取每个内层的JSONArray，并将其转换为List<String>
            List<String> innerList = jsonArray.getJSONArray(i).stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
            // 将转换后的List添加到结果集
            resultSet.add(innerList);
        }
        return resultSet;
    }

    public static void main(String[] args) {
        String input = "[[124,true],[124,false],[0,true]]";
        Set<List<String>> resultSet = parseStringToJson(input);
        System.out.println(resultSet);
    }


}
