package com.ps.judge.web.controller;

import com.ps.common.ApiResponse;
import com.ps.judge.web.auth.AuthorityService;
import com.ps.judge.web.auth.RoleService;
import com.ps.judge.web.auth.objects.AuthAuthorityBO;
import com.ps.judge.web.auth.objects.AuthRoleBO;
import com.ps.judge.web.auth.objects.AuthUserBO;
import com.ps.judge.web.auth.utils.VOUtils;
import com.ps.judge.web.auth.vo.AuthRoleVO;
import com.ps.judge.web.auth.vo.FirstMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class RoleController {

    @Autowired
    RoleService roleService;

    @Autowired
    AuthorityService authorityService;
    @GetMapping("/hello")
    public String hello() {
        AuthUserBO authUserBO = (AuthUserBO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "hello" + authUserBO.getName();
    }

    @GetMapping("/authorities")
//    @PreAuthorize("hasAuthority('authority_list')")
    public ApiResponse<List<FirstMenu>> authorities() {
        List<AuthAuthorityBO> authRoleBOList = authorityService.queryAll();
        List<FirstMenu> firstMenus = VOUtils.convertToMenuTree(authRoleBOList);
        return ApiResponse.success(firstMenus);
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('role_list')")
    public ApiResponse<List<AuthRoleBO>> roles() {
       List<AuthRoleBO> authRoleBOList = roleService.queryAll();
        return ApiResponse.success(authRoleBOList);
    }

    @GetMapping("/roles/get")
    @PreAuthorize("hasAuthority('role_list')")
    public ApiResponse<AuthRoleVO> role(@RequestParam int id) {
        AuthRoleBO authRoleBO = roleService.getById(id);
        AuthRoleVO authRoleVO = VOUtils.convertToAuthRoleVO(authRoleBO);
        return ApiResponse.success(authRoleVO);
    }

    @PostMapping("/roles/add")
    @PreAuthorize("hasAuthority('role_add')")
    public ApiResponse addRule(@RequestBody AuthRoleBO authRoleBO) {
        AuthUserBO authUserBO = (AuthUserBO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        authRoleBO.setOperator(authUserBO.getUsername());
        authRoleBO.setGmtCreated(LocalDateTime.now());
        authRoleBO.setGmtModified(LocalDateTime.now());
        roleService.addRule(authRoleBO);
        return ApiResponse.success();
    }
    @PostMapping("/roles/modify")
    @PreAuthorize("hasAuthority('role_modify')")
    public ApiResponse modifyRule(@RequestBody AuthRoleBO authRoleBO) {
        AuthUserBO authUserBO = (AuthUserBO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        authRoleBO.setOperator(authUserBO.getUsername());
        authRoleBO.setGmtModified(LocalDateTime.now());
        roleService.modifyRule(authRoleBO);
        return ApiResponse.success();
    }

    @PostMapping("/roles/delete")
    @PreAuthorize("hasAuthority('role_delete')")
    public ApiResponse deleteRule(@RequestParam int id) {
        roleService.deleteRule(id);
        return ApiResponse.success();
    }
}