package com.springboot.template.common.querydsl.util;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.*;
import com.springboot.template.business.member.data.database.entity.MemberEntity;
import com.springboot.template.common.model.dto.PageSortDto;

import java.util.ArrayList;
import java.util.List;

public class QueryDslUtils {

    /**
     * begin <= target >= end
     * @author 이봉용
     * @date 25. 9. 28.
     */
    public static <T extends Comparable<T>> BooleanExpression between(T begin, DateTimePath<T> target, T end) {
        if(begin == null && end == null) return null;

        if(begin != null && end != null) {
            return target.between(begin, end);
        }
        if(begin != null) {
            return target.goe(begin);
        }

        return target.loe(end);
    }

    /**
     * start <= target >= end
     * @author 이봉용
     * @date 25. 9. 28.
     */
    public static <T extends Number & Comparable<T>> BooleanExpression between(T start, NumberPath<T> target, T end) {
        if(start == null && end == null) return null;

        if(start != null && end != null) {
            return target.between(start, end);
        }
        if(start != null) {
            return target.goe(start);
        }

        return target.loe(end);
    }

    /**
     * begin < target > end
     * @author 이봉용
     * @date 25. 9. 28.
     */
    public static <T extends Comparable<T>> BooleanExpression betweenExclusive(T begin, DateTimePath<T> target, T end) {
        if(begin == null && end == null) return null;

        if(begin != null && end != null) {
            return target.gt(begin).and(target.lt(end));
        }
        if(begin != null) {
            return target.gt(begin);
        }

        return target.lt(end);
    }

    /**
     * start < target > end
     * @author 이봉용
     * @date 25. 9. 28.
     */
    public static <T extends Number & Comparable<T>> BooleanExpression betweenExclusive(T start, NumberPath<T> target, T end) {
        if(start == null && end == null) return null;

        if(start != null && end != null) {
            return target.gt(start).and(target.lt(end));
        }
        if(start != null) {
            return target.gt(start);
        }

        return target.lt(end);
    }

    /**
     * target1 LIKE '%keyword%'<br/>
     * OR target2 LIKE '%keyword%'<br/>
     * OR target3 LIKE '%keyword%'<br/>
     * ...
     * @author 이봉용
     * @date 25. 9. 28.
     */
    public static BooleanExpression containsColumns(List<StringPath> targets, String keyword) {
        BooleanExpression result = null;
        for(StringPath target: targets) {
            if(result == null) result = contains(target, keyword);
            else result.or(contains(target, keyword));
        }
        return result;
    }

    /**
     * target LIKE '%keyword%'
     * @author 이봉용
     * @date 25. 9. 28.
     */
    public static BooleanExpression contains(StringPath target, String keyword) {
        if(keyword == null || keyword.isEmpty()) return null;
        return target.contains(keyword);
    }

    /**
     * target1 IN (keyword1,keyword2,keyword3, ...)<br/>
     * OR target2 IN (keyword1,keyword2,keyword3, ...)<br/>
     * OR target3 IN (keyword1,keyword2,keyword3, ...)<br/>
     * ...
     * @author 이봉용
     * @date 25. 9. 28.
     */
    public static BooleanExpression inColumns(StringPath[] targets, List<String> keywords) {
        BooleanExpression result = null;
        for(StringPath target: targets) {
            if(result == null) result = in(target, keywords);
            else result.or(in(target, keywords));
        }
        return result;
    }

    /**
     * target1 IN (number1,number2,number3, ...)<br/>
     * OR target2 IN (number1,number2,number3, ...)<br/>
     * OR target3 IN (number1,number2,number3, ...)<br/>
     * ...
     * @author 이봉용
     * @date 25. 9. 28.
     */
    public static <T extends Number & Comparable<T>> BooleanExpression inColumns(NumberPath<T>[] targets, List<T> keywords) {
        BooleanExpression result = null;
        for(NumberPath<T> target: targets) {
            if(result == null) result = in(target, keywords);
            else result.or(in(target, keywords));
        }
        return result;
    }

    /**
     * target IN (keyword1,keyword2,keyword3, ...)
     * @author 이봉용
     * @date 25. 9. 28.
     */
    public static BooleanExpression in(StringPath target, List<String> keywords) {
        if(keywords == null || keywords.isEmpty()) return null;
        return target.in(keywords);
    }

    /**
     * target IN (number1,number2,number3, ...)
     * @author 이봉용
     * @date 25. 9. 28.
     */
    public static <T extends Number & Comparable<T>> BooleanExpression in(NumberPath<T> target, List<T> keywords) {
        if(keywords == null || keywords.isEmpty()) return null;
        return target.in(keywords);
    }

    public static <T> OrderSpecifier<?>[] getOrderSpecs(Class<? extends T> type, String entityName, List<PageSortDto> sorts) {
        //String 으로 Q 객체의 컬럼을 가져오고 싶을때 사용
        PathBuilder<? extends T> entityPath = new PathBuilder<>(type, entityName);
        List<OrderSpecifier<?>> result = new ArrayList<>();
        for(PageSortDto dto: sorts) {
            if (dto.getSortColumn() != null && !dto.getSortColumn().isEmpty())
                result.add(new OrderSpecifier<>(dto.getSortDirection(), entityPath.getString(dto.getSortColumn())));
            else
                result.add(new OrderSpecifier<>(dto.getSortDirection(), entityPath.getString("createDate")));
        }

        return result.toArray(new OrderSpecifier<?>[0]);
    }
}
