<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="WEB-INF/tld/dhome.tld" prefix="dhome"%>

<!DOCTYPE html>
<dhome:InitLanuage useBrowserLanguage="true"/>
<html lang="en">
<head>
	<title><fmt:message key="institute.common.scholarEvent"/>-${institution.name }-<fmt:message key="institueIndex.common.title.suffix"/></title>
	<meta name="description" content="dHome" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<jsp:include page="../../commonheaderCss.jsp"></jsp:include>
	<link rel="stylesheet" href="<dhome:url value="/resources/css/lightbox.css"/>" type="text/css" media="screen" />
</head>

<body class="dHome-body gray" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="../../commonBanner.jsp"></jsp:include>
	
	<div class="container page-title">
		<div class="sub-title">${titleUser.zhName }<fmt:message key="common.dhome"/>
			<jsp:include page="../../showAudit.jsp"/>
		</div>
		<jsp:include page="../../commonMenu.jsp">
			<jsp:param name="activeItem" value="achievement" />
		</jsp:include>
	</div> 
	<div class="container canedit">
		<div class="row-fluid mini-layout center p-top">
			<div class="config-title">
			<h3>软件著作权管理</h3>
			</div>
			<jsp:include page="../menu.jsp"> <jsp:param name="activeItem" value="copyright" /> </jsp:include>
			<div class="span9 left-b">
				<div class="ins_backend_rightContent">
					<h4 class="detail">${copyright.name }</h4>
					<div class="form-horizontal">
						<div class="control-group">
			       			<label class="control-label">登记号：</label>
			       			<div class="controls padding">
			         			<c:out value="${copyright.registerNo }"/>
			       			</div>
			       		</div>
						<div class="control-group">
			       			<label class="control-label">部门：</label>
			       			<div class="controls padding">
			         			<c:out value="${deptMap[copyright.departId].shortName }"/>
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label">类别：</label>
			       			<div class="controls padding">
			         			<c:choose>
									<c:when test="${ copyright.type==0 }">
										未知
									</c:when>
									<c:otherwise>
										${types[copyright.type].val}
									</c:otherwise>
								</c:choose>
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label">等级：</label>
			       			<div class="controls padding">
			         			<c:choose>
									<c:when test="${ copyright.grade==0 }">
										未知
									</c:when>
									<c:otherwise>
										${grades[copyright.type].val}
									</c:otherwise>
								</c:choose>
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label">年度：</label>
			       			<div class="controls padding">
			         			<c:out value="${copyright.year }"/>
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label">软件设计人：</label>
			       			<div class="controls padding">
			         			<table id="authorTable" class="table" style="margin:0;width:550px; font-size:13px">
									<tbody id="authorContent">
									
									</tbody>
								</table>
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label">单位排序：</label>
			       			<div class="controls padding">
			         			<c:out value="${copyright.companyOrder}"/> 
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label">附件：</label>
			       			<div class="controls padding">
			         			<table id="clbTable">
									<tbody id="clbContent">
									
									</tbody>
								</table>
			       			</div>
			       		</div>
			       		<div class="control-group">
			       			<label class="control-label">&nbsp;</label>
			       			<div class="controls padding">
			         			<a class="btn btn-primary" href="<dhome:url value="/people/${domain}/admin/copyright/edit/${copyright.id }?returnPage=${returnPage }"/>">编辑</a>
								<a id="deleteCopyright" class="btn" href="<dhome:url value="../delete?id[]=${copyright.id }&page=${returnPage }"/>">删除</a>
								<a class="btn btn-link" onclick="javascript:history.go(-1);">返回</a>
			       			</div>
			       		</div>
					</div>	
						
					</div>
				</div>
			</div>
	</div>
	
<!-- <div id="edit-popup" tabindex="-1" class="modal hide fade" style="width:750px;"> -->
<!-- 		<div class="modal-header"> -->
<!--            <button type="button" class="close" data-dismiss="modal">×</button> -->
<!--            <h3>更改用户信息</h3> -->
<!--         </div> -->
<%--         	<form id="editForm" method="post" action="<dhome:url value="/institution/backend/member/update/${member.umtId }"/>"> --%>
<!-- 			<fieldset> -->
<%-- 			<input type="hidden" name="umtId" value="${member.umtId }"/> --%>
<!-- 			<div class="modal-body"> -->
<!-- 				<div class="control-group"> -->
<!--          			<label class="control-label">姓名：</label> -->
<!--           			<div class="controls"> -->
<!--             			<input maxlength="254" type="text" id="trueName" name="trueName" value='' class="register-xlarge"/> -->
<!--           				<span id="trueName_error_place" class="error"></span> -->
<!--           			</div> -->
<!--         		</div> -->
<!--         		<div class="control-group"> -->
<!--          			<label class="control-label">性别：</label> -->
<!--           			<div class="controls"> -->
<!--           				<select name="sex" id="sex"> -->
<!--           					<option value="">--</option> -->
<!--           					<option value="male">男</option> -->
<!--           					<option value="female">女</option> -->
<!--           				</select> -->
<!--           			</div> -->
<!--         		</div> -->
<!--         		<div class="control-group"> -->
<!--          			<label class="control-label">职称：</label> -->
<!--           			<div class="controls"> -->
<!--             			<input maxlength="250" type="text" name="title" id="title" value='' class="register-xlarge"/> -->
<!--           			</div> -->
<!--         		</div> -->
<!--         		<div class="control-group"> -->
<!--          			<label class="control-label">办公室地址：</label> -->
<!--           			<div class="controls"> -->
<!--           				<input maxlength="250" type="text" name="officeAddress" id="officeAddress" value='' class="register-xlarge"/> -->
<!--           			</div> -->
<!--         		</div> -->
<!--         		<div class="control-group"> -->
<!--          			<label class="control-label">办公室电话：</label> -->
<!--           			<div class="controls"> -->
<!--             			<input maxlength="250" type="text" name="officeTelephone" id="officeTelephone" class="register-xmiddle"/> -->
<!--           			</div> -->
<!--         		</div> -->
<!--         		<div class="control-group"> -->
<!--          			<label class="control-label">手机：</label> -->
<!--           			<div class="controls"> -->
<!--           				<input maxlength="250" type="text" name="mobilePhone" id="mobilePhone" class="register-xmiddle"/> -->
<!--           			</div> -->
<!--         		</div> -->
<!-- 			</div> -->
<!-- 			<div class="modal-footer"> -->
<%-- 				<a data-dismiss="modal" class="btn" href="#"><fmt:message key='common.cancel'/></a> --%>
<%-- 				<button type="button" id="saveBtn" class="btn btn-primary"><fmt:message key='common.save'/></button> --%>
<!-- 	        </div> -->
<!-- 	        </fieldset> -->
<!-- 	        </form> -->
<!-- 	</div> -->
	
	</body>
	<jsp:include page="../../commonheader.jsp"></jsp:include> 
	<script src="<dhome:url value='/resources/scripts/jquery/1.7.2/jquery.tmpl.higher.min.js'/>" type="text/javascript" ></script>
	<script src="<dhome:url value="/resources/scripts/jquery/jquery.lightbox.js"/>" type="text/javascript"></script>
	<script>
		$(document).ready(function(){
			$('#deleteCopyright').on('click',function(){
				return (confirm("软件著作权删除以后不可恢复，确认删除吗？"));
			});
			
			//软件设计人队列
			var author={
					//数据
					data:[],
					//重新排序
					sort:function(){
					},
					remove:function(id){
						for(var i in this.data){
							if(this.data[i].id==id){
								this.data.splice(i,1);
								this.render();
								return;
							}
						}
					},
					//获得最后一个order
					newOrder:function(){
						if(this.data.length==0){
							return 1;
						}
						return this.data[this.data.length-1].order+1;
					},
					//替换
					replace:function(item){
						for(var i in this.data){
							if(this.data[i].id==item.id){
								this.data[i]=item;
								return;
							}
						}
					},
					//插入到末尾
					append:function(item){
						if(this.isContain(item)){
							return false;
						}
						if(!item.order){
							item.order=this.newOrder();
						}
						this.data.push(item);
						this.render();
						return true;
					},
					//是否已存在
					isContain:function(item){
						if(this.data.length==0){
							return false;
						}
						for(var i in this.data){
							if(this.data[i].id==item.id){
								return true;
							}
						}
						return false;
					},
					//排序是否已存在
					hasOrder:function(order){
						if(this.data.length==0){
							return false;
						}
						for(var i in this.data){
							if(this.data[i].order==order){
								return true;
							}
						}
						return false;
					},
					//控制表格显示
					render:function(){
						if(this.data.length==0){
							$('#authorTable').hide();
							return;
						}else{
							this.data.sort(function(a,b){
								if(a.order>b.order){
									return 1;
								}else{
									return -1;
								}
							});
							$('#authorTable').show();
							$('#authorTable').html($('#authorTemplate').tmpl(this.data,{
								judiceBool:function(bool){
									return bool?'是':'否';
								}
							})); 
						}
					}
			};
			//附件队列
			var attachment ={
					//数据
					data:[],
					
					remove:function(clbId){
						for(var i in this.data){
							if(this.data[i].clbId==clbId){
								this.data.splice(i,1);
								this.render();
								return;
							}
						}
					},
					
					//插入到末尾
					append:function(item){
						if(this.isContain(item)){
							return false;
						}
						this.data.push(item);
						this.render();
						return true;
					},
					//是否已存在
					isContain:function(item){
						if(this.data.length==0){
							return false;
						}
						for(var i in this.data){
							if(this.data[i].clbId==item.clbId){
								return true;
							}
						}
						return false;
					},
					//控制表格显示
					render:function(){
						var item = $('#clbTemplate').tmpl(this.data,{
							judiceBool:function(bool){
								return bool?'是':'否';
							}
						});
						item.find('.previewBtn').lightbox({
						    fitToScreen: true,
						    fileLoadingImage: "<dhome:url value='/resources/images/loading.gif'/>",
				            fileBottomNavCloseImage: "<dhome:url value='/resources/images/closelabel.gif'/>",
						    imageClickClose: false
					    });
						
						$('#clbContent').html(item); 
					}
			};
			//渲染软件设计人
			$.get('<dhome:url value="/people/${domain}/admin/copyright/authors/${copyright.id}"/>').done(function(data){
				if(data.success){
					author.data=data.data;
				}else{
					alert(data.desc);
				}
				author.render();
			});
			$.get('<dhome:url value="/people/${domain}/admin/copyright/attachments/${copyright.id}"/>').done(function(data){
				if(data.success){
					attachment.data=data.data;
				}else{
					alert(data.desc);
				}
				attachment.render();
			});
			$('#copyrightMenu').addClass('active');
		});
	</script>
	<!-- 软件设计人表格模板 -->
	<script type="text/x-jquery-tmpl" id="authorTemplate">
		<tr>
			<td class="order">第{{= order}}软件设计人：</td>
			<td class="author">
				{{= authorName}}
				<span class="mail">({{= authorEmail}})</span>
				<span class="company">-[{{= authorCompany}}]</span>
			</td>
		</tr>
	</script>
	<script type="text/x-jquery-tmpl" id="clbTemplate">
        <tr>
           <td>
            {{= fileName}}
			{{if fileType == 1}}
               <a href="<dhome:url value="/system/img?imgId={{= clbId}}"/>" class="previewBtn" title="{{= fileName}}" data-uid="{{= clbId}}">预览</a>
            {{/if}}
			{{if clbId != 0}}
                 <a href="<dhome:url value="/system/file?fileId={{= clbId }}"/>" target="_blank">附件下载</a>
            {{/if}}
          </td>
        </tr>
    </script>
	
</html>