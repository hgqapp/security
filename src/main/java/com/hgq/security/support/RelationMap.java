package com.hgq.security.support;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 关系映射处理，主要目的：不用连表关联查询， 获取关联的外键或者外键关联的对象，处理的关系类型可以是一对一、一对多、多对一
 * @param <K> 主外键类型
 */
public class RelationMap<K> {

    /** 存储主键到外键的关系 */
    private Map<K, Set<K>> mapKey = new HashMap<>();
    /** 存储所有外键 */
    private Set<K> relatedIds = new HashSet<>();

    /**
     * 添加一个关系
     *
     * @param baseObjectId    主键
     * @param relatedObjectId 要关联的外键
     */
    public void addRelation(K baseObjectId, K relatedObjectId) {
        Set<K> set = mapKey.computeIfAbsent(baseObjectId, k -> new HashSet<>());
        set.add(relatedObjectId);
        this.relatedIds.add(relatedObjectId);
    }

    /**
     * 添加多个关系
     *
     * @param baseObjectId     主键
     * @param relatedObjectIds 要关联的外键列表
     */
    public void addRelations(K baseObjectId, Collection<K> relatedObjectIds) {
        Set<K> set = mapKey.computeIfAbsent(baseObjectId, k -> new HashSet<>());
        set.addAll(relatedObjectIds);
        this.relatedIds.addAll(relatedObjectIds);
    }

    /**
     * 从一个关系对象列表创建 RelationMap，关系对象即同时包含主键和外键
     * @param relations 关系对象列表
     * @param baseField 主键Getter
     * @param foreignField 外键Getter
     * @param <K> 主外键的类型
     * @param <E> 关系对象
     * @return 返回创建的 RelationMap 对象
     */
    public static <K, E> RelationMap<K> create(Collection<E> relations, Function<E, K> baseField, Function<E, K> foreignField) {
        RelationMap<K> rm = new RelationMap<>();
        if (relations != null) {
            Map<K, List<K>> collect = relations.stream().collect(Collectors.groupingBy(baseField, Collectors.mapping(foreignField, Collectors.toList())));
            collect.forEach(rm::addRelations);
        }
        return rm;
    }

    /**
     * 获取某个主键关联的外键列表
     * @param baseObjectId 主键
     * @return 返回关联的外值列表
     */
    public Set<K> getRelatedIds(K baseObjectId) {
        return Collections.unmodifiableSet(this.mapKey.computeIfAbsent(baseObjectId, v -> Collections.emptySet()));
    }

    /**
     * 获取所有外键
     * @return 返回所有外键
     */
    public Set<K> getAllRelatedIds() {
        return Collections.unmodifiableSet(this.relatedIds);
    }

    /**
     * 是否包含某个主键
     * @param baseObjectId 主键
     * @return 如果包含则返回 true ， 否则返回 false
     */
    public boolean containsKey(K baseObjectId) {
        return this.mapKey.containsKey(baseObjectId);
    }

    /**
     * 是否有关联的外键
     * @return 如果有则返回 true ， 否则返回 false
     */
    public boolean hasRelatedIds() {
        return !this.relatedIds.isEmpty();
    }

    /**
     * 创建一个ValueMapper对象，首先从外键实体对象列表构建一个ValueMapper对象，以便将外键所在实体对象填充到目标对象上
     * @param source 外键实体对象列表
     * @param sourceKeyGetter 外键Getter
     * @param <S> 外键实体对象类型
     * @return 返回一个ValueMapper对象
     */
    public <S> ValueMapper<K, S> createValueMapper(Collection<S> source, Function<S, K> sourceKeyGetter) {
        return createValueMapper(source, sourceKeyGetter, t -> t);
    }

    /**
     * 创建一个ValueMapper对象，首先从外键实体对象列表构建一个ValueMapper对象，以便将外键所在实体对象填充到目标对象上
     * @param source 外键实体对象列表
     * @param sourceKeyGetter 外键Getter
     * @param transfer 外键实体对象的转换器，可以将外键实体对象转换成另外一个最终需要的对象，以便填充到目标实体对象中
     * @param <S> 外键实体对象类型
     * @param <R> 实体对象最终需要的外键实体对象
     * @return 返回一个ValueMapper对象
     */
    public <S, R> ValueMapper<K, R> createValueMapper(Collection<S> source, Function<S, K> sourceKeyGetter, Function<S, R> transfer) {
        Map<K, R> valueMap = source.stream().collect(Collectors.toMap(sourceKeyGetter, transfer, (oldValue, newValue) -> oldValue));
        return new ValueMapper<>(mapKey, valueMap);
    }

    /**
     * 外键
     * @param <K>
     * @param <S>
     */
    public static class ValueMapper<K, S> {

        private Map<K, Set<K>> mapKey;
        private Map<K, S> valueMap;

        ValueMapper(Map<K, Set<K>> mapKey, Map<K,S> valueMap) {
            this.mapKey = mapKey;
            this.valueMap = valueMap;
        }

        /**
         * 填充List, 填充主键所在的实体对象列表中的所有外键关联的对象
         * @param targets 主键关联的实体对象列表
         * @param targetKeyGetter 主键Getter
         * @param valueSetter 设置外键关联的实体对象的Setter
         * @param <T> 主键所在的实体类型
         */
        public <T> void populateList(Collection<T> targets, Function<T, K> targetKeyGetter, BiConsumer<T, List<S>> valueSetter) {
            targets.forEach(item -> {
                K key = targetKeyGetter.apply(item);
                Set<K> rids = mapKey.get(key);
                if (rids != null) {
                    List<S> relatedObjects = new ArrayList<>(rids.size());
                    forEachAddRelatedObject(rids, relatedObjects);
                    valueSetter.accept(item, relatedObjects);
                }
            });
        }

        /**
         * 填充单个关联对象, 填充主键所在的实体对象列表中的所有外键关联的对象
         * @param targets 主键关联的实体对象列表
         * @param targetKeyGetter 主键Getter
         * @param valueSetter 设置外键关联的实体对象的Setter
         * @param <T> 主键所在的实体类型
         */
        public <T> void populateSingle(Collection<T> targets, Function<T, K> targetKeyGetter, BiConsumer<T, S> valueSetter) {

            targets.forEach(item -> {
                K key = targetKeyGetter.apply(item);
                Set<K> rids = mapKey.get(key);
                if (rids != null) {
                    List<S> relatedObjects = new ArrayList<>(rids.size());
                    forEachAddRelatedObject(rids, relatedObjects);
                    if (!relatedObjects.isEmpty()) {
                        valueSetter.accept(item, relatedObjects.iterator().next());
                    }
                }
            });
        }

        /**
         * 填充Set, 填充主键所在的实体对象列表中的所有外键关联的对象
         * @param targets 主键关联的实体对象列表
         * @param targetKeyGetter 主键Getter
         * @param valueSetter 设置外键关联的实体对象的Setter
         * @param <T> 主键所在的实体类型
         */
        public <T> void populateSet(Collection<T> targets, Function<T, K> targetKeyGetter, BiConsumer<T, Set<S>> valueSetter) {
            targets.forEach(item -> {
                K key = targetKeyGetter.apply(item);
                Set<K> rids = mapKey.get(key);
                if (rids != null) {
                    Set<S> relatedObjects = new HashSet<>(rids.size());
                    forEachAddRelatedObject(rids, relatedObjects);
                    valueSetter.accept(item, relatedObjects);
                }
            });
        }

        private void forEachAddRelatedObject(Set<K> relatedIds, Collection<S> relatedObjects) {
            if (relatedIds != null) {
                relatedIds.forEach(relatedId -> {
                    S v = valueMap.get(relatedId);
                    if (v != null) {
                        relatedObjects.add(v);
                    }
                });
            }
        }
    }
}