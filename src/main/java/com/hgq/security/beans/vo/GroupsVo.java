package com.hgq.security.beans.vo;


import com.hgq.security.support.validation.ValidationGroup.Create;
import com.hgq.security.support.validation.ValidationGroup.Update;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class GroupsVo {

    private Long groupId;
    private String groupName;
}
