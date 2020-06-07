package com.ps.judge.web.auth.controller;

import com.ps.common.ApiResponse;
import com.ps.common.PageResult;
import com.ps.common.query.QueryVo;
import com.ps.judge.web.auth.service.AuthorityService;
import com.ps.judge.web.auth.service.RoleService;
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

    @GetMapping("/authorities/list")
    @PreAuthorize("hasAnyAuthority('role_add','role_modify')")
    public ApiResponse<List<FirstMenu>> authorities() {
        List<AuthAuthorityBO> authRoleBOList = authorityService.queryAll();
        List<FirstMenu> firstMenus = VOUtils.convertToMenuTree(authRoleBOList);
        return ApiResponse.success(firstMenus);
    }

    @PostMapping("/roles/list")
    @PreAuthorize("hasAuthority('role_list')")
    public ApiResponse<PageResult<AuthRoleVO>> roles(@RequestBody QueryVo<AuthRoleBO> queryVo) {
        queryVo.setQuery(null);
        return queryRoles(queryVo);
    }

    @PostMapping("/roles/all")
    @PreAuthorize("hasAuthority('user_add')")
    public ApiResponse<List<AuthRoleBO>> allRoles() {
        List<AuthRoleBO> roleBOList = roleService.queryAll();
        return ApiResponse.success(roleBOList);
    }

    @PostMapping("/roles/query")
    @PreAuthorize("hasAuthority('role_query')")
    public ApiResponse<PageResult<AuthRoleVO>> queryRoles(@RequestBody QueryVo<AuthRoleBO> queryVo) {
        List<AuthRoleBO> roleBOList = roleService.query(queryVo.convertToQueryParam());
        List<AuthRoleVO> authRoleVOS = VOUtils.convertToAuthRoleVOs(roleBOList);
        PageResult<AuthRoleVO> pageResult = new PageResult<>(queryVo.getCurPage(), queryVo.getPageSize(), authRoleVOS);
        return ApiResponse.success(pageResult);
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
    @GetMapping("/roles/get")
    @PreAuthorize("hasAnyAuthority('role_add','role_modify')")
    public ApiResponse<AuthRoleVO> role(@RequestParam int id) {
        AuthRoleBO authRoleBO = roleService.getById(id);
        AuthRoleVO authRoleVO = VOUtils.convertToAuthRoleVO(authRoleBO);
        return ApiResponse.success(authRoleVO);
    }

    @PostMapping("/roles/delete")
    @PreAuthorize("hasAuthority('role_delete')")
    public ApiResponse deleteRule(@RequestParam int id) {
        roleService.deleteRule(id);
        return ApiResponse.success();
    }
}
