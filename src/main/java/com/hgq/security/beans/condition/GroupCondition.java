package com.hgq.security.beans.condition;


import com.hgq.security.model.QGroups;
import com.hgq.security.support.jpa.querydsl.Criterias;
import com.querydsl.core.BooleanBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.hgq.security.support.jpa.querydsl.Criterias.andCntains;

@Data
@NoArgsConstructor
public class GroupCondition implements Criterias {

    private String groupName;

    @Override
    public BooleanBuilder create() {
        QGroups groups = QGroups.groups;
        BooleanBuilder builder = new BooleanBuilder();
        andCntains(builder, groups.groupName, groupName);
        return builder;
    }
}
