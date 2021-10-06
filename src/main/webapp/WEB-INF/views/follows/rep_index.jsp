<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst, java.util.List" %>

<c:set var="actEmp" value="${ForwardConst.ACT_EMP.getValue()}" />
<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commNew" value="${ForwardConst.CMD_NEW.getValue()}" />
<c:set var="flagList" value="${flagList}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>フォローした従業員の日報　一覧</h2>
        <c:choose>
        	<c:when test="${reports_count != 0}">

		        <table id="report_list">
		            <tbody>
		                <tr>
		                    <th class="report_name">氏名</th>
		                    <th class="report_date">日付</th>
		                    <th class="report_title">タイトル</th>
		                    <th class="report_action">操作</th>
		                </tr>
		                <c:forEach var="report" items="${reports}" varStatus="status">
							<c:set var="flag" value="${flagList[status.count - 1]}"/>
		                    <c:choose>
		                    	<c:when test="${flag == 1}">
		                    		<c:out value="フォロー解除" />
		                    	</c:when>
		                    	<c:otherwise>
		                    		<c:out value="フォロー" />
		                    	</c:otherwise>
		                    </c:choose>
		                    <fmt:parseDate value="${report.reportDate}" pattern="yyyy-MM-dd" var="reportDay" type="date" />

		                    <tr class="row${status.count % 2}">
		                        <td class="report_name">
		                        <a href="<c:url value='?action=${actEmp}&command=${commShow}&id=${report.employee.id}' />"><c:out value="${report.employee.name}" /></a>
		                        </td>
		                        <td class="report_date"><fmt:formatDate value='${reportDay}' pattern='yyyy-MM-dd' /></td>
		                        <td class="report_title">${report.title}</td>
		                        <td class="report_action"><a href="<c:url value='?action=${actRep}&command=${commShow}&id=${report.id}' />">詳細を見る</a></td>
		                    </tr>
		                </c:forEach>
		            </tbody>
		        </table>
	        </c:when>
	        <c:otherwise>
	        	<p>フォローした従業員の日報はまだありません</p>
	        </c:otherwise>
        </c:choose>


    </c:param>
</c:import>