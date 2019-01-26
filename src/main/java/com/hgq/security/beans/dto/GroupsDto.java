package com.hgq.security.beans.dto;


import com.hgq.security.support.validation.ValidationGroup.Create;
import com.hgq.security.support.validation.ValidationGroup.Update;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class GroupsDto {

    @NotNull(groups = Update.class)
    @Null(groups = Create.class)
    private Long groupId;
    @NotNull
    @Size(min = 1, max = 30)
    private String groupName;
}
