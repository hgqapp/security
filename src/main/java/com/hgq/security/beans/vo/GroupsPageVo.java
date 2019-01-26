package com.hgq.security.beans.vo;


import com.hgq.security.model.Groups;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupsPageVo {

    private Long groupId;
    private String groupName;

    public GroupsPageVo(Groups groups) {
        this.groupId = groups.getGroupId();
        this.groupName = groups.getGroupName();
    }
}
