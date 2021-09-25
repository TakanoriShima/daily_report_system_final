<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst" %>

<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}" />
<c:set var="actFav" value="${ForwardConst.ACT_FAV.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commEdt" value="${ForwardConst.CMD_EDIT.getValue()}" />
<c:set var="commCrt" value="${ForwardConst.CMD_CREATE.getValue()}" />
<c:set var="commDtry" value="${ForwardConst.CMD_DESTROY.getValue()}" />


<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>

        <h2>日報 詳細ページ</h2>

        <table>
            <tbody>
                <tr>
                    <th>氏名</th>
                    <td><c:out value="${report.employee.name}" /></td>
                </tr>
                <tr>
                    <th>日付</th>
                    <fmt:parseDate value="${report.reportDate}" pattern="yyyy-MM-dd" var="reportDay" type="date" />
                    <td><fmt:formatDate value='${reportDay}' pattern='yyyy-MM-dd' /></td>
                </tr>
                <tr>
                    <th>内容</th>
                    <td><pre><c:out value="${report.content}" /></pre></td>
                </tr>
                <tr>
                    <th>出勤時間</th>
                    <fmt:parseDate value="${report.start_time}" pattern="HH:mm" var="start_time" type="date" />
                    <td><fmt:formatDate value='${start_time}' pattern='HH:mm' /></td>
                </tr>
                <tr>
                    <th>退勤時間</th>
                    <fmt:parseDate value="${report.end_time}" pattern="HH:mm" var="end_time" type="date" />
                    <td><fmt:formatDate value='${end_time}' pattern='HH:mm' /></td>
                </tr>
                <tr>
                    <th>登録日時</th>
                    <fmt:parseDate value="${report.createdAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="createDay" type="date" />
                    <td><fmt:formatDate value="${createDay}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                </tr>
                <tr>
                    <th>更新日時</th>
                    <fmt:parseDate value="${report.updatedAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="updateDay" type="date" />
                    <td><fmt:formatDate value="${updateDay}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                </tr>
                <tr>
                	<th>いいね数</th>
                	<td><c:out value="${favorites_count}" />人</td>
                </tr>
                <tr>
                	<th>いいねした従業員一覧</th>
                	<td>
                		<ul>
                			<c:forEach var="favorite" items="${favorites}">
                			<li><c:out value="${favorite.employee.name}"/></li>
                			</c:forEach>
                		</ul>

                	</td>
                </tr>
            </tbody>
        </table>

		<c:choose>
			<c:when test="${favorite_flag == false }" >
				<p>
					<form action="<c:url value='?action=${actFav}&command=${commCrt}' />" method="POST">
						<input type="hidden" name="id" value="${report.id}">
						<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
						<input type="submit" value="Like">
					</form>
				</p>
			</c:when>

			<c:otherwise>
				<p>
					<form action="<c:url value='?action=${actFav}&command=${commDtry}' />" method="POST">
						<input type="hidden" name="id" value="${report.id}">
						<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
						<input type="submit" value="UnLike">
					</form>
				</p>
			</c:otherwise>
		</c:choose>

        <c:if test="${sessionScope.login_employee.id == report.employee.id}">
            <p>
                <a href="<c:url value='?action=${actRep}&command=${commEdt}&id=${report.id}' />">この日報を編集する</a>
            </p>
        </c:if>

        <p>
            <a href="<c:url value='?action=${actRep}&command=${commIdx}' />">一覧に戻る</a>
        </p>
    </c:param>
</c:import>