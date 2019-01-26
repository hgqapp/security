package com.hgq.security.beans.condition;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;

import javax.annotation.Nullable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@FunctionalInterface
public interface Criterias {

    BooleanBuilder create();

    /** == */
    static <T extends Comparable> void andEq(BooleanBuilder builder, SimpleExpression<T> expression, T value) {
        if (notEmpty(value)) {
            builder.and(expression.eq(value));
        }
    }

    /** != */
    static <T extends Comparable> void andNe(BooleanBuilder builder, SimpleExpression<T> expression, T value) {
        if (notEmpty(value)) {
            builder.and(expression.ne(value));
        }
    }

    /** is null */
    static <T extends Comparable> void andIsNull(BooleanBuilder builder, SimpleExpression<T> expression) {
        builder.and(expression.isNull());
    }

    /** is not null */
    static <T extends Comparable> void andIsNotNull(BooleanBuilder builder, SimpleExpression<T> expression) {
        builder.and(expression.isNotNull());
    }

    /** > */
    static <T extends Number & Comparable<?>> void andGt(BooleanBuilder builder, NumberExpression<T> expression, T value) {
        if (notEmpty(value)) {
            builder.and(expression.gt(value));
        }
    }

    /** < */
    static <T extends Number & Comparable<?>> void andLt(BooleanBuilder builder, NumberExpression<T> expression, T value) {
        if (notEmpty(value)) {
            builder.and(expression.lt(value));
        }
    }

    /** <= */
    static <T extends Number & Comparable<?>> void andLoe(BooleanBuilder builder, NumberExpression<T> expression, T value) {
        if (notEmpty(value)) {
            builder.and(expression.loe(value));
        }
    }

    /** >= */
    static <T extends Number & Comparable<?>> void andGoe(BooleanBuilder builder, NumberExpression<T> expression, T value) {
        if (notEmpty(value)) {
            builder.and(expression.goe(value));
        }
    }

    /** between */
    static <T extends Number & Comparable<?>> void andBetween(BooleanBuilder builder, NumberExpression<T> expression, T from, T to) {
        if (notEmpty(from) || notEmpty(to)) {
            builder.and(expression.between(from, to));
        }
    }

    /** not between */
    static <T extends Number & Comparable<?>> void andNotBetween(BooleanBuilder builder, NumberExpression<T> expression, T from, T to) {
        if (notEmpty(from) || notEmpty(to)) {
            builder.and(expression.notBetween(from, to));
        }
    }

    /** like */
    static void andLike(BooleanBuilder builder, StringExpression expression, String value) {
        if (notEmpty(value)) {
            builder.and(expression.like(value));
        }
    }

    /** like */
    static void andLike(BooleanBuilder builder, StringExpression expression, String value, char escape) {
        if (notEmpty(value)) {
            builder.and(expression.like(value, escape));
        }
    }

    /** like ignore case */
    static void andLikeIgnoreCase(BooleanBuilder builder, StringExpression expression, String value) {
        if (notEmpty(value)) {
            builder.and(expression.likeIgnoreCase(value));
        }
    }

    /** like ignore case */
    static void andLiandLikeIgnoreCaseke(BooleanBuilder builder, StringExpression expression, String value, char escape) {
        if (notEmpty(value)) {
            builder.and(expression.likeIgnoreCase(value, escape));
        }
    }

    /** contains */
    static void andCntains(BooleanBuilder builder, StringExpression expression, String value) {
        if (notEmpty(value)) {
            builder.and(expression.contains(value));
        }
    }

    /** contains ignore case */
    static void andContainsIgnoreCase(BooleanBuilder builder, StringExpression expression, String value) {
        if (notEmpty(value)) {
            builder.and(expression.containsIgnoreCase(value));
        }
    }

    /** endsWith */
    static void andEndsWith(BooleanBuilder builder, StringExpression expression, String value) {
        if (notEmpty(value)) {
            builder.and(expression.endsWith(value));
        }
    }

    /** endsWith ignore case */
    static void andEndsWithIgnoreCase(BooleanBuilder builder, StringExpression expression, String value) {
        if (notEmpty(value)) {
            builder.and(expression.endsWithIgnoreCase(value));
        }
    }

    /** startsWith */
    static void andStartsWith(BooleanBuilder builder, StringExpression expression, String value) {
        if (notEmpty(value)) {
            builder.and(expression.startsWith(value));
        }
    }

    /** startsWith ignore case */
    static void andStartsIgnoreCase(BooleanBuilder builder, StringExpression expression, String value) {
        if (notEmpty(value)) {
            builder.and(expression.startsWithIgnoreCase(value));
        }
    }

    /** equals ignore case */
    static void andEqualsIgnoreCase(BooleanBuilder builder, StringExpression expression, String value) {
        if (notEmpty(value)) {
            builder.and(expression.equalsIgnoreCase(value));
        }
    }

    /** not equals ignore case */
    static void andNotEqualsIgnoreCase(BooleanBuilder builder, StringExpression expression, String value) {
        if (notEmpty(value)) {
            builder.and(expression.notEqualsIgnoreCase(value));
        }
    }

    /** in */
    static <T> void andIn(BooleanBuilder builder, SimpleExpression<T> expression, T... value) {
        if (notEmpty(value)) {
            builder.and(expression.in(value));
        }
    }

    /** in */
    static <T> void andIn(BooleanBuilder builder, SimpleExpression<T> expression, Collection<? extends T> value) {
        if (notEmpty(value)) {
            builder.and(expression.in(value));
        }
    }

    /** not in */
    static <T> void andNotIn(BooleanBuilder builder, SimpleExpression<T> expression, T... value) {
        if (notEmpty(value)) {
            builder.and(expression.notIn(value));
        }
    }

    /** not in */
    static <T> void andNotIn(BooleanBuilder builder, SimpleExpression<T> expression, Collection<? extends T> value) {
        if (notEmpty(value)) {
            builder.and(expression.notIn(value));
        }
    }

    static boolean isEmpty(@Nullable Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof Optional) {
            return !((Optional) obj).isPresent();
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }

        // else
        return false;
    }

    static boolean notEmpty(@Nullable Object obj) {
        return !isEmpty(obj);
    }
}
