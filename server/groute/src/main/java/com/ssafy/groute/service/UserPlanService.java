package com.ssafy.groute.service;

import com.ssafy.groute.dto.PlanLike;
import com.ssafy.groute.dto.RouteDetail;
import com.ssafy.groute.dto.User;
import com.ssafy.groute.dto.UserPlan;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserPlanService {
    void insertUserPlan(UserPlan userPlan, List<String> userIds, int planId) throws Exception;
    Map<String,Object> selectUserPlan(int id) throws Exception;
    List<UserPlan> selectAllUserPlan() throws Exception;
    void deleteUserPlan(int id) throws Exception;
    void updateUserPlan(UserPlan userPlan) throws Exception;
    List<UserPlan> selectAllUserPlanById(String id) throws Exception;
    void likePlan(PlanLike planLike) throws Exception;
    PlanLike isLike(PlanLike planLike) throws Exception;
    void copyPlan(UserPlan userPlan, int planId, int day) throws Exception;
    List<UserPlan> selectAllByPlaceId(List<Integer> placeIds, int flag) throws Exception;
    List<UserPlan> bestPlanList() throws Exception;
    List<UserPlan> findEndPlanById(String userId) throws Exception;
    List<UserPlan> findNotEndPlanById(String userId) throws Exception;
    List<UserPlan> selectTUserPlan() throws Exception;
    List<UserPlan> selectAllPlanByUserId(String userId) throws Exception;
    List<RouteDetail> shortestPath(int start, int end, int routeId) throws Exception;
    void deleteRouteDetail(int routeDetailId) throws Exception;
}