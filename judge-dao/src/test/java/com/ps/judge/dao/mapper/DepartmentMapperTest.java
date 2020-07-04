package com.ps.judge.dao.mapper;

import com.ps.common.query.DepartmentQuery;
import com.ps.common.query.QueryParams;
import com.ps.judge.dao.MybatisUtils;
import com.ps.judge.dao.entity.AuthDepartmentDO;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class DepartmentMapperTest {


//    @Test
//    public void queryTest() {
//        SqlSession sqlSession = MybatisUtils.getSqlSession();
//        DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
//        QueryParams<DepartmentQuery> queryParams = new QueryParams<>();
//        queryParams.setStartNo(0);
//        queryParams.setPageSize(10);
//        List<AuthDepartmentDO> results = mapper.query(queryParams);
//        results.forEach(System.out::println);
//    }
//
//    @Test
//    public void getByIdTest() {
//        SqlSession sqlSession = MybatisUtils.getSqlSession();
//        DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
//        System.out.println(mapper.getById(1));
//    }
//
//    @Test
//    public void countTest() {
//        SqlSession sqlSession = MybatisUtils.getSqlSession();
//        DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
//        QueryParams<DepartmentQuery> queryParams = new QueryParams<>();
//        System.out.println(mapper.count(queryParams));
//    }
//
//    @Test
//    public void allTest() {
//        SqlSession sqlSession = MybatisUtils.getSqlSession();
//        DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
//
//        System.out.println(mapper.all());
//    }
//
//    @Test
//    public void insertTest() {
//        SqlSession sqlSession = MybatisUtils.getSqlSession();
//        DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
//        AuthDepartmentDO authDepartmentDO = new AuthDepartmentDO();
//        authDepartmentDO.setName("ce");
//        authDepartmentDO.setManagerId(2);
//        authDepartmentDO.setOperator("ad");
//        authDepartmentDO.setGmtCreated(LocalDateTime.now());
//        authDepartmentDO.setGmtModified(LocalDateTime.now());
//        System.out.println(mapper.insert(authDepartmentDO));
//        sqlSession.commit();
//    }
//
//    @Test
//    public void deleteTest() {
//        SqlSession sqlSession = MybatisUtils.getSqlSession();
//        DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
//
//        System.out.println(mapper.delete(2));
//        sqlSession.commit();
//    }

}