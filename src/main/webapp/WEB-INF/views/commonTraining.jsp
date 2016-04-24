<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>
<ul class="nav nav-tabs">
	<li>&nbsp;&nbsp;&nbsp;</li>
	<li <c:if test="${param.activeItem=='trainings' }">class="active"</c:if>>
	    <a href="<dhome:url value="/people/${domain }/admin/training/list/1"/>">学生 </a>
	</li>
	<c:if test="${param.activeItem=='modifyTraining' }">
		<li class="active">
		     <a href="<dhome:url value="/people/${domain }/admin/training/create"/>">
			    <c:choose>
					<c:when test="${op=='create' }">
						+ 手动添加学生
					</c:when>
					<c:otherwise>
						更改学生
					</c:otherwise>
				</c:choose>
			</a>
	    </li>
	 </c:if>
	 <c:if test="${param.activeItem!='modifyTraining' }">
		 <li>
		     <a href="<dhome:url value="/people/${domain }/admin/training/create"/>">+ 手动添加学生</a>
	    </li>
	 </c:if>
   	<li <c:if test="${param.activeItem=='addTraining' }">class="active"</c:if>>
	    <a href="<dhome:url value="/people/${domain}/admin/training/search"/>">+ 检索添加学生</a>
    </li>
    <li <c:if test="${param.activeItem=='grants' }">class="active"</c:if>>
	    <a href="<dhome:url value="/people/${domain }/admin/grants/list/1"/>">奖助学金发放 </a>
    </li>
</ul>