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
	<link rel="stylesheet" type="text/css" href="<dhome:url value="/resources/third-party/autocomplate/jquery.autocomplete.css"/>" />
	<link rel="stylesheet" type="text/css" href="<dhome:url value="/resources/css/tokenInput.css"/>"/>
	<link rel="stylesheet" type="text/css" href="<dhome:url value='/resources/third-party/datepicker/sample-css/page.css'/>"/>
	<link rel="stylesheet" type="text/css" href="<dhome:url value='/resources/third-party/datepicker/css/dp.css'/>"/>
</head>

<body class="dHome-body institu" data-offset="50" data-target=".subnav" data-spy="scroll">
	<jsp:include page="../../backendCommonBanner.jsp"></jsp:include>
	<div class="container">
<jsp:include page="../leftMenu.jsp"></jsp:include>
		<div class="ins_backend_rightContent">
			<ul class="nav nav-tabs">
				<jsp:include page="../../commonAcademicBackend.jsp"><jsp:param name="activeItem" value="modifyAcademic" /> </jsp:include>
			</ul>
			<form id="editForm" class="form-horizontal">
				<input type="hidden" name="id" id="id" value="${empty academic.id ? 0:academic.id}"/>
				
				<%-- <div class="control-group">
	       			<label class="control-label"><span class="red">*</span>组织名称：</label>
	       			<div class="controls">
	         			<input type="text" name="name" id="name" value="<c:out value="${academic.name}"/>"class="register-xlarge"/>
						<span id="name_error_place" class="error"></span>
	       			</div>
	       		</div> --%>
	       		
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>组织名称：</label>
	       			<div class="controls">
	         			<select id="name" name="name">
	         				<c:forEach items="${organizationNames.entrySet() }" var="data">
	         			         <option value="${data.key }" <c:if test="${data.key==academic.name }">selected="selected"</c:if>>${data.value.val}</option>
	         			 	</c:forEach>
						</select>
	       			</div>
	       		</div>
	       		
				<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>部门：</label>
	       			<div class="controls">
	         			<select id="departId" name="departId">
	         				<option value="0" <c:if test="${academic.departId==0 }">selected="selected"</c:if>>--</option>
	         				<c:forEach items="${deptMap.entrySet() }" var="data">
	         			         <option value="${data.key }" <c:if test="${data.key==academic.departId }">selected="selected"</c:if>>${data.value.shortName}</option>
	         			 	</c:forEach>
						</select>
	       			</div>
	       		</div>
	       		
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>职位：</label>
	       			<div class="controls">
	         			<select id="position" name="position">
	         				<c:forEach items="${positions.entrySet() }" var="data">
	         			         <option value="${data.key }" <c:if test="${data.key==academic.position }">selected="selected"</c:if>>${data.value.val}</option>
	         			 	</c:forEach>
						</select>
	       			</div>
	       		</div>
	       		
	       		<div class="control-group">
	       			<label class="control-label"><span class="red">*</span>任职人员：</label>
	       			<div class="controls">
	         			<input type="text" name="authorSearch" class="autoCompleteSearch register-xlarge" id="authorSearch"/>
						<p class="hint">请输入任职人员的姓名、邮箱或者单位，按回车确认添加。</p>
<!-- 						<p class="notFind">没有找到您要选择的任职人员？点击这里<a id="addAuthor">添加任职人员</a></p> -->
						<span class="error" id="authorError"></span>
	       			</div>
	       			<table id="authorTable" class="table table-bordered">
						<tbody id="authorContent">
						
						</tbody>
					</table>
	       		</div>
	       		
	       		  <div class="control-group">
	       			<label class="control-label">开始时间：</label>
	       			<div class="controls">
	         			<select id="startYear" name="startYear" class="register-xsmall">
						</select>&nbsp;年&nbsp;&nbsp;
						
						<select id="startMonth" name="startMonth" class="register-xsmall">
						   <option value="0">--</option>
					    </select>&nbsp;月
					    <span class="error"></span>
	       			</div>
	       		</div>
	       		
	       		<div class="control-group">
	       			<label class="control-label">结束时间：</label>
	       			<div class="controls">
	         			<select id="endYear" name="endYear" class="register-xsmall">
						</select>&nbsp;年&nbsp;&nbsp;
						<select id="endMonth" name="endMonth" class="register-xsmall">
							<option value="0">--</option>
						</select>&nbsp;月 &nbsp;&nbsp;
						<span class="check"><input type="checkbox"  id="checkThis"/> 至今</span>
						<span class="error"></span>
	       			</div>
	       		</div>
	       		<div class="control-group" >
	       			<label class="control-label">&nbsp;</label>
	       			<div class="controls">
	         			 
	       			</div>
	       		</div>
	       		
	       		
	       		<div class="control-group">
	       			<label class="control-label">&nbsp;</label>
	       			<div class="controls">
	         			<input type="button" id="academicSaveBtn" value="提交" class="btn btn-primary"/>
	         			<a class="btn btn-link" href="<dhome:url value="/institution/${domain }/backend/academic/list/1"/>">取消</a>
	       			</div>
	       		</div>
				
			</form>
		</div>	
	</div>
<!-- 添加任职人员 -->
	<div id="add-author-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button" class="close" data-dismiss="modal">×</button>
           <h3>添加任职人员</h3>
        </div>
        	<form id="addAuthorForm" class="form-horizontal" method="post">
			<div class="modal-body">
				<div class="control-group">
         			<label class="control-label">姓名：</label>
          			<div class="controls">
            			<input maxlength="254" type="text" id="authorName" name="authorName" value='' class="register-xlarge"/>
          				<span id="authorName_error_place" class="error"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">邮箱：</label>
          			<div class="controls">
            			<input maxlength="254" type="text" id="authorEmail" name="authorEmail" value='' class="register-xlarge"/>
          				<span id="authorEmail_error_place" class="error"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">单位：</label>
          			<div class="controls">
            			<input maxlength="254" type="text" id="authorCompany" name="authorCompany" value='' class="register-xlarge"/>
          				<span id="authorCompany_error_place" class="error"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">作者排序：</label>
          			<div class="controls">
          				第
            			<input maxlength="254" type="text" id="order" name="order" value='0' style="width:20px;"/>
          				作者
          				<span id="order_error_place" class="error"></span>
          			</div>
        		</div>
        		
			</div>
			<div class="modal-footer">
				<a data-dismiss="modal" class="btn" href="#"><fmt:message key='common.cancel'/></a>
				<button type="button" id="authorSaveBtn" class="btn btn-primary"><fmt:message key='common.save'/></button>
	        </div>
	        </form>
	</div>
	<!-- 编辑作者 -->
	 <div id="edit-author-popup" tabindex="-1" class="modal hide fade" style="width:750px;">
		<div class="modal-header">
           <button type="button" class="close" data-dismiss="modal">×</button>
           <h3>编辑任职人员</h3>
        </div>
        	<form id="editAuthorForm" class="form-horizontal" method="post">
			<div class="modal-body">
				<div class="control-group">
         			<label class="control-label">姓名：</label>
          			<div class="controls padding">
          				<span id="editTrueName"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">邮箱：</label>
          			<div class="controls padding">
          				<span id="editEmail"></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">单位：</label>
          			<div class="controls padding">
            			<span id="editAuthorCompany" ></span>
          			</div>
        		</div>
        		<div class="control-group">
         			<label class="control-label">作者排序：</label>
          			<div class="controls padding">
          				第
            			<input maxlength="254" type="text" id="editOrder" name="editOrder" value='0' style="width:20px;"/>
          				作者
          				<span id="editOrder_error_place" class="error"></span>
          			</div>
          			
        		</div>
        		
			</div>
			<div class="modal-footer">
				<a data-dismiss="modal" class="btn" href="#"><fmt:message key='common.cancel'/></a>
				<button type="button" id="editAuthorSaveBtn" class="btn btn-primary"><fmt:message key='common.save'/></button>
	        </div>
	        </form>
	</div> 
	
	</body>
	<jsp:include page="../../commonheader.jsp"></jsp:include> 
	<script src="<dhome:url value="/resources/scripts/leftMenuHeight.js"/>" type="text/javascript" ></script>
	<script src="<dhome:url value="/resources/third-party/datepicker/src/Plugins/datepicker_lang_HK.js"/>" type="text/javascript"></script>
	<script src="<dhome:url value="/resources/third-party/datepicker/src/Plugins/jquery.datepicker.js"/>" type="text/javascript"></script>
	<script src="<dhome:url value="/resources/third-party/autocomplate/jquery.autocomplete.js"/>"></script>
	<script type="text/javascript" src="<dhome:url value="/resources/scripts/tokenInput/toker-jQuery.js"/>"></script>
	<script src="<dhome:url value='/resources/scripts/jquery/1.7.2/jquery.tmpl.higher.min.js'/>" type="text/javascript" ></script>
	<script>
		$(document).ready(function(){
			//初始化时间控件
			$("#citationQueryTime").datepicker({ picker: "<img class='picker' align='middle' src='<dhome:url value='/resources/third-party/datepicker/sample-css/cal.gif'/>' alt=''>",applyrule:function(){return true;}});

			//token控件初始化，关键字
			var $tokenObj = $("#keywordDisplay").tokenInput("<dhome:url value='/institution/${domain}/backend/paper/search/keyword'/>", {
				theme:"facebook",
				hintText: "请输入关键字",
				searchingText: "正在查询...",
				noResultsText: "未查询到结果",
				preventDuplicates: true,
				queryParam:"q"
			});
			
			
			//作者队列
			var author={
					//数据
					data:[],
					//重新排序
					sort:function(){
					},
					remove:function(umtId){
						for(var i in this.data){
							if(this.data[i].umtId==umtId){
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
							if(this.data[i].umtId==item.umtId){
								this.data[i]=item;
								return;
							}
						}
					},
					//插入到末尾
					append:function(item){
						$("#authorError").empty();
						
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
							if(this.data[i].umtId==item.umtId){
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
							$('#authorContent').html($('#authorTemplate').tmpl(this.data,{
								judiceBool:function(bool){
									return bool?'是':'否';
								}
							})); 
						}
					}
			};
			
			//删除作者
			$('.removeAuthor').live('click',function(){
				if(confirm("确定移除该任职人员？")){
					var email=$(this).data('uid');
					author.remove(email);
				}
			});
			//查询用户
			$("#authorSearch").autocomplete('<dhome:url value="/institution/${domain}/backend/academic/search/user"/>',
			            {
					  		width:400,
							parse:function(data){
									return $.map(data, function(item) {
										return {
											data : item,
											result : '',
											value:item.trueName
										};
									});
							},
							formatItem:function(row, i, max) {
			    				return  row.trueName+"("+row.cstnetId+")";
			 				},
							formatMatch:function(row, i, max) {
			    				return row.trueName + " " + row.cstnetId;
			 				},
							formatResult: function(row) {
			    				return row.trueName; 
			 				}
						}).result(function(event,item){
							if(author.data.length==0){
								var success=author.append(item);
							}else{
								alert('任职人员只能添加一个');
								return false;
							}
							
							if(!success){
								alert('请勿重复添加');
							}
						});
// 			//查询作者
// 			$("#authorSearch").autocomplete('<dhome:url value="/institution/${domain}/backend/paper/search/author"/>',
// 			            {
// 					  		width:400,
// 							parse:function(data){
// 									return $.map(data, function(item) {
// 										return {
// 											data : item,
// 											result : '',
// 											value:item.authorName
// 										};
// 									});
// 							},
// 							formatItem:function(row, i, max) {
// 			    				return  row.authorName+"("+row.authorEmail+")" + "- [" + row.authorCompany+"]";
// 			 				},
// 							formatMatch:function(row, i, max) {
// 			    				return row.authorName + " " + row.authorEmail+""+row.authorCompany;
// 			 				},
// 							formatResult: function(row) {
// 			    				return row.authorName; 
// 			 				}
// 						}).result(function(event,item){
// 							var success=author.append(item);
// 							if(!success){
// 								alert('请勿重复添加');
// 							}
// 						});
			//添加作者
			$('#addAuthor').on('click',function(){
				$('#addAuthorForm').get(0).reset();
				$('#add-author-popup').modal('show');
				$('#order').val(author.newOrder());
				judiceCheckbox('create');
			});
			//编辑-保存作者
			$('#editAuthorSaveBtn').on('click',function(){
				if(!$('#editAuthorForm').valid()){
					return;
				}
				var $data=$(this).closest('form').data('author');
				var order=parseInt($('#editOrder').val());
				if($data.order!=order&&author.hasOrder(order)){
					alert('第'+order+'作者已存在');
					return;
				}
				$data.order=order;
				$data.communicationAuthor=$('#editCommunicateAuthor input[type=checkbox]').is(':checked');
				$data.authorStudent=$('#editAuthorStudentL input[type=checkbox]').is(':checked');
				$data.authorTeacher=$('#editAuthorTeacherL input[type=checkbox]').is(':checked');

				author.replace($data);
				author.render();
				$('#edit-author-popup').modal('hide');
			});
			//保存作者
			$('#authorSaveBtn').on('click',function(){
				if(!$('#addAuthorForm').valid()){
					return;
				}
				var order=parseInt($('#order').val());
				if(author.hasOrder(order)){
					alert('第'+order+'作者已存在');
					return;
				}
				$.post('<dhome:url value="/institution/${domain}/backend/academic/author/create"/>',$('#addAuthorForm').serialize()).done(function(data){
					if(data.success){
						author.append(data.data);
						$('#add-author-popup').modal('hide');
					}else{
						alert(data.desc);
					}
				});
			});
			$('#order').on('keyup',function(){
				judiceCheckbox('create');
			});
			$('#editOrder').on('keyup',function(){
				judiceCheckbox('update');
			});
			
			//判定添加作者，checkbox显隐逻辑
			function judiceCheckbox(oper){
				function hideAndUnchecked($0){
					$0.hide().find('input[type=checkbox]').removeAttr('checked');
				}
				var orderStr='';
				var studentLabel='';
				var teacherLabel='';
				if(oper=='create'){
					orderStr=$('#order').val();
					studentLabel='authorStudentL';
					teacherLabel='authorTeacherL'
					
				}else{
					orderStr=$('#editOrder').val();
					studentLabel='editAuthorStudentL';
					teacherLabel='editAuthorTeacherL'
				}
				if(orderStr==''){
					hideAndUnchecked($('#'+studentLabel));
					hideAndUnchecked($('#'+teacherLabel));
					return;
				}
				var order=parseInt(orderStr);
				//非正常的数字
				if(!order){
					hideAndUnchecked($('#'+studentLabel));
					hideAndUnchecked($('#'+teacherLabel));
					return;
				}
				if(order==1){
					$('#'+studentLabel).show();
					hideAndUnchecked($('#'+teacherLabel));
					return;
				} 
				if(order==2){
					$('#'+teacherLabel).show();
					hideAndUnchecked($('#'+studentLabel));
					return;
				}
				hideAndUnchecked($('#'+studentLabel));
				hideAndUnchecked($('#'+teacherLabel));
			}
			//编辑作者
			$('.editAuthor').live('click',function(){
				$('#edit-author-popup').modal('show');
				var $data=$(this).closest('tr').data('tmplItem').data;
				$('#editAuthorForm').data('author',$data).get(0).reset();
				$('#editTrueName').html($data.authorName);
				$('#editEmail').html($data.authorEmail);
				$('#editAuthorCompany').html($data.authorCompany); 
				$('#editOrder').val($data.order);
				judiceCheckbox('editAuthorStudentL','editAuthorTeacherL');
				if($data.communicationAuthor){
					$('#editCommunicateAuthor input[type="checkbox"]').click();
				} 
				if($data.authorStudent){
					$('#editAuthorStudentL input[type=checkbox]').click();
				}
				if($data.authorTeacher){
					$('#editAuthorTeacherL input[type=checkbox]').click();
				}
			});
			//编辑作者验证
			$('#editAuthorForm').validate({
				 submitHandler :function(form){
					 form.submit();
				 },
				 rules: {
					 editOrder:{
				 		required:true,
				 		min:1,
				 		digits:true,
				 		max:99999999
				 	}
				 },
				 messages:{
					 editOrder:{
					 		required:'作者排序不允许为空',
					 		min:'超出允许值',
					 		digits:'必须为正整数',
					 		max:'超出允许值'
					 	}
				 },
				 errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#"+$(element).attr("name")+sub;
					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			});
			//添加作者Dialog，验证
			$('#addAuthorForm').validate({
				 submitHandler :function(form){
					 form.submit();
				 },
				 rules: {
					 authorName:{
						 required:true
						 },
					authorEmail:{
						 required:true,
						 email:true
						 },
				 	
				 	authorCompany:{
				 		required:true
				 	},
				 	order:{
				 		required:true,
				 		min:1,
				 		digits:true,
				 		max:99999999
				 	}
				 },
				 messages:{
					 authorName:{
						 required:'姓名不允许为空'
						 },
						 authorEmail:{
							 required:'邮箱不允许为空',
							 email:'非法的邮箱格式'
						 },
					 	authorCompany:{
					 		required:'单位不允许为空'
					 	},
					 	order:{
					 		required:'作者排序不允许为空',
					 		min:'超出允许值',
					 		digits:'必须为正整数',
					 		max:'超出允许值'
					 	}
				 },
				 errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#"+$(element).attr("name")+sub;
					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			});
			
			//设置发表/结束年份
			(function(loop,startYear,endYear){
				var year=new Date().getFullYear();
				for(var i=year-loop+2;i<=year;i++){
					$('#startYear').append('<option value="'+i+'">'+i+'</option')
					$('#endYear').append('<option value="'+i+'">'+i+'</option')
				}
				if(!startYear){
					startYear=year;
				}
				
				if(!endYear){
					endYear = year;
				}
				
				$('#startYear').val(startYear);
				$('#endYear').val(endYear);
			})(15,parseInt('${academic.startYear}'),parseInt('${academic.endYear}')); 
			
			//发表/结束月份
			(function(loop,startMonth,endMonth){
				for(var i=1;i<loop+1;i++){
					$('#startMonth').append('<option value="'+i+'">'+i+'</option');
					$('#endMonth').append('<option value="'+i+'">'+i+'</option');
				}
				
// 				if(!startMonth){
// 					startMonth= 1;
// 				}
				
// 				if(!endMonth){
// 					endMonth = 1;
// 				}
				
				$('#startMonth').val(startMonth);
				$('#endMonth').val(endMonth);
			})(12,parseInt('${academic.startMonth}'),parseInt('${academic.endMonth}')); 
			
			/* 检查学术任职、期刊任职的开始结束时间 */
			function checkTime(popup){
				var beginTimeYear = $(popup+" select[id=startYear]").val();
				var beginTimeMonth = $(popup+" select[id=startMonth]").val();
				var endTimeYear = $(popup+" select[id=endYear]").val();
				var endTimeMonth = $(popup+" select[id=endMonth]").val();
				
				if($('#checkThis').attr("checked")){
					endTimeYear = '3000'; 
					endTimeMonth = '1';
				}
				if(endTimeYear<beginTimeYear || (endTimeYear == beginTimeYear && endTimeMonth<beginTimeMonth )){
					var $parent = $(popup+" select[id=endYear]").parents("div.control-group");
					$parent.find("span.error").html("").append("<fmt:message key='personalWorkInfo.warning.endAfterBegin'/>");
					return false;
				}
				
				return true;
			}
			
		   //提交学术任职
			$('#academicSaveBtn').on('click',function(){
				if($('#editForm').valid()){
					if(!checkTime('#editForm')){
						return false;
					}
					var data=$('#editForm').serialize();
					if($('#checkThis').attr("checked")){
						data +='&endYear=' + 3000;
						data +='&endMonth='+ new Date().getMonth() + 1;
					}
					if(author.data.length!=0){
						for(var i in author.data){
							var auth=author.data[i];
							data+='&uid[]='+auth.umtId.toString();
// 							data+='&order[]='+auth.order;
// 							data+='&communicationAuthor[]='+auth.communicationAuthor;
// 							data+='&authorStudent[]='+auth.authorStudent;
// 							data+='&authorTeacher[]='+auth.authorTeacher;
						}
					}else{
						$("#authorError").html("").append("任职人员不允许为空");
						return;
					}
					$.post('<dhome:url value="/institution/${domain}/backend/academic/submit"/>',data).done(function(data){
						if(data.success){
							window.location.href='<dhome:url value="/institution/${domain}/backend/academic/list/1"/>'
						}else{
							alert(data.desc);
						}
					});
				}
			});
			 
			 /* editForm */
			$("#editForm").validate({
				submitHandler:function(form){
					form.submit();
				},
				rules:{
					name:{required:true},
					position:{required:true},
					startYear:{required:true},
					startMonth:{required:true},
					endYear:{required:true},
					endMonth:{required:true}
				},
				messages:{
					name:"组织名称不能为空",
					position:"职位不能为空",
					startYear:"<fmt:message key='personalWorkInfo.warning.empty.beginTime'/>",
					startMonth:"<fmt:message key='personalWorkInfo.warning.empty.beginTime'/>",
					endYear:{required:"<fmt:message key='personalWorkInfo.warning.empty.endTime'/>"},
					endMonth:{required:"<fmt:message key='personalWorkInfo.warning.empty.endTime'/>"}
				},
				success:function(label){
					checkTimeError(label);
				},
				errorPlacement:function(error, element){
					var $parent = element.parents("div.control-group");
					$parent.find("span.error").html("").append(error);
					
					var sub="_error_place";
					 var errorPlaceId="#academic-popup span[id="+$(element).attr("name")+sub+"]";

				 	 $(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));	
				},
				onclick:false
			});
			
			 
			function checkTimeError(label){
				var $parent = label.parents("div.control-group");
				if(label.attr("for")=="startYear" && $parent.find("select[id=startMonth]").val() == ''){
					$parent.find("span.error").html("").append("请选择开始时间月份");
				}else if(label.attr("for")=="startMonth" && $parent.find("select[id=startYear]").val() == ''){
					$parent.find("span.error").html("").append("请选择开始时间年份");
				}else if(label.attr("disabled")){
					if(label.attr("for")=="endYear" && $parent.find("select[id=endMonth]").val() == ''){
						$parent.find("span.error").html("").append("请选择结束时间月份");
					}else if(label.attr("for")=="endMonth" && $parent.find("select[id=endYear]").val() == ''){
						$parent.find("span.error").html("").append("请选择结束时间年份");
					}
				}else if(label.attr("for")=="endYear" && $parent.find("select[id=endMonth]").val() == ''){
					$parent.find("span.error").html("").append("请选择结束时间月份");
				}else if(label.attr("for")=="endMonth" && $parent.find("select[id=endYear]").val() == ''){
					$parent.find("span.error").html("").append("请选择结束时间年份");
				}
			}
			 
			//左栏置为选中
			$('#academicMenu').addClass('active');
			
		 	$('#checkThis').click(function(){
				if($('#checkThis').attr("checked")){
					$("#endYear").attr("disabled","disabled");  
					$("#endMonth").attr("disabled","disabled");  
				}else{
					$("#endYear").removeAttr("disabled");  
					$("#endMonth").removeAttr("disabled");  
				}
			}); 
			 
			//编辑期刊任职加载作者
			if('${op}'=='update'){
				//渲染作者
				$.get('<dhome:url value="/institution/${domain}/backend/academic/authors/${academic.id}"/>').done(function(data){
					if(data.success){
						author.data=data.data;
					}else{
						alert(data.desc);
					}
					author.render();
				});
				
				if('${academic.endYear}' == 3000){
					$("#endYear").attr("disabled", true);
					$("#endMonth").attr("disabled", true);
					$("#checkThis").attr("checked", true);
				}
			}else{
				author.render();
			}
		});
	</script>
	<!-- 作者表格模板 -->
	<script type="text/x-jquery-tmpl" id="authorTemplate">
		<tr>
			<td class="author">
				{{= trueName}}
				<span class="mail">({{= cstnetId}})</span>
			</td>
			<td>
				<a class="removeAuthor" data-uid="{{= umtId}}"><i class="icon icon-trash"></i></a>
			</td>
		</tr>
	</script>
	
</html>