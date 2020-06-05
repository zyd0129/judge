package com.ps.judge.web.controller;

import com.ps.common.ApiResponse;
import com.ps.common.exception.BizException;
import com.ps.judge.web.auth.UserService;
import com.ps.judge.web.auth.objects.AuthUserBO;
import com.ps.judge.web.auth.utils.VOUtils;
import com.ps.judge.web.auth.req.AuthUserResetPassReq;
import com.ps.judge.web.auth.req.AuthUserModifyReq;
import com.ps.judge.web.auth.vo.AuthUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;

    @GetMapping("/users/list")
    public ApiResponse<List<AuthUserVO>> users() {
        List<AuthUserBO> authUserBOList = userService.query();

        return ApiResponse.success(VOUtils.convertToAuthUserVOs(authUserBOList));
    }

    @GetMapping("/users/current/get")
    public ApiResponse<AuthUserVO> getUser() {

        AuthUserBO  currentUser = (AuthUserBO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ApiResponse.success(VOUtils.convertToAuthUserVO(currentUser));
    }

    @PostMapping("/users/add")
    @PreAuthorize("hasAuthority('user_add')")
    public ApiResponse addUser(@RequestBody AuthUserVO authUserVO) {
        AuthUserBO  currentUser = (AuthUserBO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        authUserVO.setGmtCreated(LocalDateTime.now());
        authUserVO.setGmtModified(LocalDateTime.now());
        authUserVO.setOperator(currentUser.getUsername());
        authUserVO.setPassword(passwordEncoder.encode(authUserVO.getRawPassword()));
        AuthUserBO authUserBO = VOUtils.convertToAuthUserBO(authUserVO);
        userService.addUser(authUserBO);

        return ApiResponse.success();
    }

    @PostMapping("/users/modify")
    @PreAuthorize("hasAuthority('user_add')")
    public ApiResponse modifyUser(@RequestBody AuthUserModifyReq authUserVO) {
        AuthUserBO  currentUser = (AuthUserBO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        authUserVO.setGmtModified(LocalDateTime.now());
        authUserVO.setOperator(currentUser.getUsername());
        AuthUserBO authUserBO = VOUtils.convertToAuthUserBO(authUserVO);
        userService.modifyUser(authUserBO);

        return ApiResponse.success();
    }

    @PostMapping("/users/delete")
    @PreAuthorize("hasAuthority('user_add')")
    public ApiResponse deleteUser(@RequestParam int id) {

        userService.deleteUser(id);

        return ApiResponse.success();
    }

    /**
     * 系统重置密码
     * @param authUserVO
     * @return
     */
    @PostMapping("/users/password/reset")
    @PreAuthorize("hasAuthority('user_add')")
    public ApiResponse resetPassword(@RequestBody AuthUserResetPassReq authUserVO) {

        AuthUserBO  currentUser = (AuthUserBO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        authUserVO.setGmtModified(LocalDateTime.now());
        authUserVO.setOperator(currentUser.getUsername());
        authUserVO.setPassword(passwordEncoder.encode(authUserVO.getRawPassword()));

        AuthUserBO authUserBO = VOUtils.convertToAuthUserBO(authUserVO);
        userService.changePassword(authUserBO);

        return ApiResponse.success();
    }

    /**
     * 用户修改密码
     * @param authUserVO
     * @return
     */
    @PostMapping("/users/password/change")
    @PreAuthorize("hasAuthority('user_add')")
    public ApiResponse changePassword(@RequestBody AuthUserResetPassReq authUserVO) {

        AuthUserBO  currentUser = (AuthUserBO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String prePasswordExact = currentUser.getPassword();
        String prePassowrdInput =passwordEncoder.encode(authUserVO.getRawPassword());
        if(!prePasswordExact.equals(prePassowrdInput)) throw new BizException(40001,"pre password not exact");

        authUserVO.setGmtModified(LocalDateTime.now());
        authUserVO.setOperator(currentUser.getUsername());
        authUserVO.setPassword(passwordEncoder.encode(authUserVO.getRawPassword()));

        AuthUserBO authUserBO = VOUtils.convertToAuthUserBO(authUserVO);
        userService.changePassword(authUserBO);

        return ApiResponse.success();
    }

    @GetMapping("/users/noDepartment/list")
    public ApiResponse<List<AuthUserVO>> queryDepartmentIsNull() {
        List<AuthUserBO> authUserBOList = userService.queryDepartmentIsEmpty();

        return ApiResponse.success(VOUtils.convertToAuthUserVOs(authUserBOList));
    }
}
