package com.hgq.security.service;

import com.hgq.security.beans.dto.GroupsDto;
import com.hgq.security.beans.vo.GroupsPageVo;
import com.hgq.security.beans.vo.GroupsVo;
import com.hgq.security.model.Groups;
import com.hgq.security.model.QGroups;
import com.hgq.security.repository.GroupRepository;
import com.hgq.security.support.jpa.querydsl.Criterias;
import com.hgq.security.support.validation.ValidationGroup;
import com.hgq.security.support.validation.ValidationGroup.Create;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.Optional;

import static org.springframework.util.Assert.isTrue;

@Transactional
@Validated
@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Nonnull
    @Validated({Create.class, Default.class})
    public Long crate(@Valid GroupsDto group) {
        checkInput(group);
        Groups groupModel = modelMapper.map(group, Groups.class);
        return groupRepository.save(groupModel).getGroupId();
    }

    @Nonnull
    @Validated({ValidationGroup.Update.class, Default.class})
    public Long update(@Valid GroupsDto group) {
        checkInput(group);
        Groups groupModel = modelMapper.map(group, Groups.class);
        groupRepository.save(groupModel);
        return group.getGroupId();
    }

    private void checkInput(GroupsDto group) {
        Long groupId = group.getGroupId();
        if (groupId != null) {
            Optional<Groups> dbGroup = groupRepository.findById(groupId);
            isTrue(dbGroup.isPresent(), () -> String.format("用户组id\"%s\"不存在", groupId));
        }
        String groupName = group.getGroupName();
        if (groupName != null) {
            Optional<Groups> dbGroup = groupRepository.findOne(QGroups.groups.groupName.eq(groupName));
            isTrue(!dbGroup.isPresent() || dbGroup.get().getGroupId().equals(group.getGroupId()), () -> String.format("用户组\"%s\"已经存在", groupName));
        }
    }

    @Nullable
    public GroupsVo getByGroupId(@NotNull Long groupId) {
        Optional<Groups> groups = groupRepository.findById(groupId);
        return groups.map(u -> modelMapper.map(u, GroupsVo.class)).orElse(null);
    }

    @Nonnull
    public Page<GroupsPageVo> page(@NotNull Criterias condition, @NotNull Pageable pageable) {
        Page<Groups> page = groupRepository.findAll(condition.create(), pageable);
        return page.map(GroupsPageVo::new);
    }

    @Nonnull
    public Long delete(@NotNull Long groupId) {
        groupRepository.findById(groupId).ifPresent(v -> groupRepository.delete(v));
        return groupId;
    }
}
