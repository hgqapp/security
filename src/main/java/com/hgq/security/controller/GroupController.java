package com.hgq.security.controller;

import com.hgq.security.beans.condition.GroupCondition;
import com.hgq.security.beans.dto.GroupsDto;
import com.hgq.security.beans.vo.GroupsPageVo;
import com.hgq.security.beans.vo.GroupsVo;
import com.hgq.security.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author houguangqiang
 * @date 2018-11-30
 * @since 1.0
 */
@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Secured("GROUP-CREATE")
    @PostMapping("/create")
    public Long create(GroupsDto user) {
        return groupService.crate(user);
    }

    @Secured("GROUP-UPDATE")
    @PostMapping("/update")
    public Long update(GroupsDto user) {
        return groupService.update(user);
    }

    @Secured("GROUP-GET")
    @GetMapping("/get")
    public GroupsVo get(Long groupId) {
        return groupService.getByGroupId(groupId);
    }

    @Secured("GROUP-DELETE")
    @PostMapping("/delete")
    public Long delete(Long groupId) {
        return groupService.delete(groupId);
    }

    @Secured("GROUP-PAGE")
    @GetMapping("/page")
    public Page<GroupsPageVo> page(GroupCondition condition, Pageable pageable) {
        return groupService.page(condition, pageable);
    }
}
