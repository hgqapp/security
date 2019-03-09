package com.hgq.security.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author houguangqiang
 * @date 2019-02-13
 * @since 1.0
 */
@Service
public class PermissionManager {

    private static final Logger logger = LoggerFactory.getLogger(PermissionManager.class);

    private MutableAclService mutableAclService;
    private PermissionEvaluator permissionEvaluator;

    public PermissionManager(MutableAclService mutableAclService, PermissionEvaluator permissionEvaluator) {
        this.mutableAclService = mutableAclService;
        this.permissionEvaluator = permissionEvaluator;
    }

    public void addPermission(Serializable targetId, String targetType, Sid recipient, Permission permission) {
        ObjectIdentity oid = new ObjectIdentityImpl(targetType, targetId);
        MutableAcl acl;
        try {
            acl = (MutableAcl) mutableAclService.readAclById(oid);
        }
        catch (NotFoundException nfe) {
            acl = mutableAclService.createAcl(oid);
        }
        acl.insertAce(acl.getEntries().size(), permission, recipient, true);
        mutableAclService.updateAcl(acl);
        logger.debug("添加数据权限: {}, Sid: {}, Type: {}, Id: {}", permission, recipient ,targetType, targetId);
    }

    public void checkPermission(Serializable targetId, String targetType, Object permission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!permissionEvaluator.hasPermission(authentication, targetId, targetType, permission)) {
            throw new AccessDeniedException("不允许访问");
        }
    }
    public void checkPermission(Serializable targetId, Class<?> targetType, Object permission) {
        checkPermission(targetId, targetType.getName(), permission);
    }

    public void checkPermission(Object targetDomainObject, Object permission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!permissionEvaluator.hasPermission(authentication, targetDomainObject, permission)) {
            throw new AccessDeniedException("不允许访问");
        }
    }

    public void deleteAcl(Serializable targetId, String targetType) {
        deleteAcl(targetId, targetType, true);
    }

    public void deleteAcl(Serializable targetId, String targetType, boolean deleteChildren) {
        ObjectIdentity oid = new ObjectIdentityImpl(targetType, targetId);
        mutableAclService.deleteAcl(oid, deleteChildren);
    }

    public void deletePermission(Serializable targetId, String targetType, Sid recipient, Permission permission) {
        ObjectIdentity oid = new ObjectIdentityImpl(targetType, targetId);
        MutableAcl acl = (MutableAcl) mutableAclService.readAclById(oid);
        List<AccessControlEntry> entries = acl.getEntries();
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getSid().equals(recipient)
                    && entries.get(i).getPermission().equals(permission)) {
                acl.deleteAce(i);
            }
        }
        mutableAclService.updateAcl(acl);
        logger.debug("删除数据权限: {}, Sid: {}, Type: {}, Id: {}", permission, recipient ,targetType, targetId);
    }


}
