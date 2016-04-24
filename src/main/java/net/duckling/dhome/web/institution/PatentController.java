/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package net.duckling.dhome.web.institution;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.JSONHelper;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.institution.InstitutionAttachment;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.dhome.domain.institution.InstitutionOption;
import net.duckling.dhome.domain.institution.InstitutionOptionVal;
import net.duckling.dhome.domain.institution.InstitutionPatent;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.SearchInstitutionCondition;
import net.duckling.dhome.domain.object.JsonResult;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IInstitutionBackendPatentService;
import net.duckling.dhome.service.IInstitutionBackendPaperService;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionOptionValService;
import net.duckling.dhome.service.IInstitutionPeopleService;
import net.duckling.dhome.service.impl.ClbFileService;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


@Controller
@NPermission(authenticated = true,member = "iap")
@RequestMapping("people/{domain}/admin/patent")

public class PatentController {

	private static final Logger LOG=Logger.getLogger(PatentController.class);
	
	@Autowired
	private IInstitutionBackendPatentService patentService;
	@Autowired
	private IInstitutionHomeService homeService;
	@Autowired
	private IInstitutionBackendService backendService;
	@Autowired
    private ClbFileService resourceService;
	@Autowired
	private IInstitutionBackendPaperService paperService;
	@Autowired
	private IInstitutionPeopleService peopleService;
	@Autowired
	private IInstitutionOptionValService optionValService;
	
	public void addDeptMap(ModelAndView mv,SimpleUser user){
		List<InstitutionDepartment> depts=backendService.getVmtDepartment(user.getInstitutionId());
	    mv.addObject("deptMap",extractDept(depts));
	}
	private Map<Integer,InstitutionDepartment> extractDept(List<InstitutionDepartment> depts){
		if(CommonUtils.isNull(depts)){
			return Collections.emptyMap();
		}
		Map<Integer,InstitutionDepartment> map=new LinkedHashMap<Integer,InstitutionDepartment>();
		for(InstitutionDepartment dept:depts){
			map.put(dept.getId(),dept);
		}
		return map;
	}

//	private boolean validatePrefix(String fileName){
//		String lower=fileName.toLowerCase();
//		return lower.endsWith(".pdf")||lower.endsWith(".doc")||lower.endsWith(".docx");
//	}
	@RequestMapping(value="/upload",method = RequestMethod.POST,headers = { "X-File-Name" })
	@ResponseBody
	public JsonResult uploadXls(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestHeader("X-File-Name") String fileName) throws IOException{
//		if(!validatePrefix(fileName)){
//			return new JsonResult("请上传docx,doc,pdf格式文件");
//		}
		if(request.getContentLength()<=0){
			return new JsonResult("请勿上传空文件");
		}
		int clbId=resourceService.createFile(URLDecoder.decode(fileName, "UTF-8"), request.getContentLength(), request.getInputStream());
		JsonResult jr=new JsonResult();
		//jr.setData(clbId);
		InstitutionAttachment attachment = new InstitutionAttachment();
		attachment.setCreateTime(new Timestamp(System.currentTimeMillis()));
		attachment.setFileName(fileName);
		attachment.setClbId(clbId);
		jr.setData(attachment);
		return jr;
	}
	
	@RequestMapping(value="/upload",method = RequestMethod.POST)
	@ResponseBody
	public void uploadXls(
			@RequestParam("qqfile") MultipartFile uplFile,
			HttpServletResponse response) throws IOException {
		JsonResult jr=new JsonResult();
//		if(!validatePrefix(uplFile.getOriginalFilename())){
//			jr.setDesc("请上传docx,doc,pdf格式文件");
//		}else 
		if(uplFile.getSize()<=0){
			jr.setDesc("请勿上传空文件");
		}else{
			int clbId=resourceService.createFile(URLDecoder.decode(uplFile.getOriginalFilename(), "UTF-8"), (int)uplFile.getSize(), uplFile.getInputStream());
			jr.setSuccess(true);
			//jr.setData(clbId);
			InstitutionAttachment attachment = new InstitutionAttachment();
			attachment.setCreateTime(new Timestamp(System.currentTimeMillis()));
			attachment.setFileName(uplFile.getOriginalFilename());
			attachment.setClbId(clbId);
			jr.setData(attachment);
		}
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		response.setCharacterEncoding("UTF-8");
		//IE兼容，不这么整，会下载json
		try{
			JSONHelper.writeJSONObject(response, jr.toJSON());
		}catch(Exception e){
			LOG.error(e.getMessage(),e);
		}
	}
	
	@RequestMapping("list/{page}")
	public ModelAndView list(@PathVariable("domain")String domain,
			@PathVariable("page")int page,
			SearchInstitutionCondition condition,
			HttpServletRequest request){
		
		SimpleUser user = SessionUtils.getUser(request);
		ModelAndView mv=new ModelAndView("institution/patent/list");
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null||page<0){
//			return null;
//		}
		PageResult<InstitutionPatent> result=patentService.getPatentByUser(user.getId(), page,condition);
		Map<Integer,Integer> pubGradeMap=patentService.getGradesMapByUser(user.getId());
		mv.addObject("gradeMap",pubGradeMap);
		mv.addObject("condition",condition);
		mv.addObject("page",result);
		mv.addObject("domain",domain);
		mv.addObject("titleUser",user);
		addBaseData(mv,user);
		
//		List<Integer> topicId=CommonUtils.extractSthField(result.getDatas(),"id");
//		Map<Integer,List<InstitutionAuthor>> authorMap=backEndTopicService.getListAuthorsMap(topicId);
//		mv.addObject("authorMap",authorMap);
		return mv;
	}
	private void addBaseData(ModelAndView mv,SimpleUser user){
		//类别
		Map<Integer,InstitutionOptionVal> types= optionValService.getMapByOptionId(InstitutionOption.PATENT_TYPE, user.getInstitutionId());
		mv.addObject("types",types);

		//等级
		Map<Integer,InstitutionOptionVal> grades= optionValService.getMapByOptionId(InstitutionOption.PATENT_GRADE, user.getInstitutionId());
		mv.addObject("grades",grades);
		
		//专利状态
		Map<Integer,InstitutionOptionVal> statusMap= optionValService.getMapByOptionId(InstitutionOption.PATENT_STATUS, user.getInstitutionId());
		mv.addObject("statusMap",statusMap);
	}
	@RequestMapping("delete")
	public ModelAndView delete(@PathVariable("domain")String domain,@RequestParam("id[]")int[] id,@RequestParam("page")String page,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			return null;
//		}
		patentService.deletePatentUser(id);
//		patentService.deleteByTopicId(id);
		return new ModelAndView(new RedirectView(request.getContextPath()+"/people/"+domain+"/admin/patent/list/"+page));
	}
	@RequestMapping("submit")
	public JsonResult submit(InstitutionPatent patent,@PathVariable("domain")String domain,
			@RequestParam("uid[]")int[] uid,
			@RequestParam("order[]")int[] order,
			@RequestParam("clbId[]")  int[] clbIds,
			@RequestParam("fileName[]") String[] fileNames,
			HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		SimpleUser user = SessionUtils.getUser(request);
//		if(home==null){
//			LOG.error("submit:home is null["+domain+"]");
//			return new JsonResult("找不到组织机构");
//		}
//		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			LOG.error("submit:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return new JsonResult("权限不足");
//		}
		patent.setInstitutionId(user.getInstitutionId());
		if(patent.getId()==0){
			patent.setId(patentService.insert(patent, uid, order, clbIds, fileNames));
			patentService.insertPatentUser(patent.getId(), user.getId());
		}else{
			patentService.updatePatent(patent.getId(), patent);
			patentService.deleteRef(patent.getId());
			patentService.createRef(patent.getId(), uid, order);
			patentService.deleteAttachments(patent.getId());
			patentService.insertAttachments(patent.getId(), clbIds, fileNames);
		}
		return new JsonResult();
	}
	
	@RequestMapping("edit/{id}")
	@ResponseBody
	public ModelAndView edit(@RequestParam("returnPage") int returnPage,@PathVariable("domain")String domain,@PathVariable("id")int id,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			return null;
//		}
//		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			return null;
//		} 
		SimpleUser user = SessionUtils.getUser(request);
		InstitutionPatent patent = patentService.getPatentById(id);
		int objType=2;
		List<InstitutionAttachment> files = patentService.getAttachment(objType, id);
		if(patent==null){
			LOG.error("update:paper["+id+"] is not found!");
			return null;
		}
		ModelAndView mv=new ModelAndView("institution/patent/modify");
		mv.addObject("op","update");
		mv.addObject("patent",patent);
		mv.addObject("files",files);
		mv.addObject("returnPage",returnPage);
		mv.addObject("titleUser",user);
		addBaseData(mv,user);
		addDeptMap(mv,user);
		return mv;
	}
	@RequestMapping("update/{id}")
	@ResponseBody
	public ModelAndView update(InstitutionPatent patent,@PathVariable("domain")String domain,@PathVariable("id")int id,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			LOG.error("create:home is null["+domain+"]");
//		}
		
//		if(CommonUtils.isNull(topic.getId())||!topic.getId().equals(id)){
//			LOG.error("update.error:id not found["+id+"]");
//			return false;
//		}
//		if(CommonUtils.isNull(vmtMember.getTrueName())){
//			LOG.error("update.error:trueName can't be null");
//			return false;
//		}
		
		patentService.updatePatent(id, patent);
		return new ModelAndView(new RedirectView(request.getContextPath()+"/people/"+domain+"/admin/patent/modify"));
	}
	@RequestMapping("create")
	public ModelAndView create(@PathVariable("domain")String domain,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		SimpleUser user = SessionUtils.getUser(request);
//		if(home==null){
//			LOG.error("create:home is null["+domain+"]");
//			return null;
//		}
		
		ModelAndView mv=new ModelAndView("institution/patent/modify");
		mv.addObject("patentCount",patentService.getPatentCountByUser(user.getId()));
		mv.addObject("op","create");
		mv.addObject("domain",domain);
		mv.addObject("titleUser",user);
		addBaseData(mv,user);
		addDeptMap(mv,user);
		return mv;
	}
	
	@RequestMapping("/authors/{patentId}")
	@ResponseBody
	public JsonResult getAuthors(@PathVariable("domain")String domain,@PathVariable("patentId")int patentId,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			LOG.error("update:home is null["+domain+"]");
//			return new JsonResult("not found institution");
//		}
//		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			LOG.error("update:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return new JsonResult("权限不足");
//		}

		List<InstitutionAuthor> authors=patentService.getAuthorsByPatentId(patentId);
		JsonResult jr=new JsonResult();
		jr.setData(authors);
		return jr;
	}
	@RequestMapping("author/create")
	@ResponseBody
	public JsonResult createAuthor(@PathVariable("domain")String domain,InstitutionAuthor author,HttpServletRequest request){
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			LOG.error("create.author:home is null["+domain+"]");
//			return new JsonResult("找不到组织机构");
//		}
		int uid=SessionUtils.getUser(request).getId();
		author.setCreator(uid);
//		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			LOG.error("create.author:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
//			return new JsonResult("权限不足");
//		}
		patentService.createAuthor(author);
		JsonResult jr=new JsonResult();
		jr.setData(author);
		return jr;
	}
	/**
	 * 查询附件
	 * @param domain
	 * @param patentId
	 * @param request
	 * @return
	 */
	@RequestMapping("attachments/{patentId}")
	@ResponseBody
	public JsonResult getAttchments(@PathVariable("domain") String domain,
			@PathVariable("patentId") int patentId,HttpServletRequest request){
//		InstitutionHome home = homeService.getInstitutionByDomain(domain);
//		if(home == null){
//			LOG.error("authors :home is null["+domain+"]");
//			return new JsonResult("找不到机构主页");
//		}
		
//		if(!backendService.isAdmin(home.getInstitutionId(), SessionUtils.getUser(request).getEmail())){
//			LOG.error("authors : user["+SessionUtils.getUserId(request)+"] is not admin@["+domain+"]");
//			return new JsonResult("权限不足");
//		}
		int objType=2;
		List<InstitutionAttachment> attachments = patentService.getAttachment(objType, patentId);
		JsonResult result = new JsonResult();
		result.setData(attachments);
		return result;
	}
	@RequestMapping("detail/{patentId}")
	@ResponseBody
	public ModelAndView detail(@RequestParam("returnPage") int returnPage,@PathVariable("domain")String domain,@PathVariable("patentId")int patentId,HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution/patent/detail");
//		InstitutionHome home=homeService.getInstitutionByDomain(domain);
//		if(home==null){
//			return null;
//		}
//		if(!backendService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
//			return null;
//		} 
		SimpleUser user = SessionUtils.getUser(request);
		InstitutionPatent patent = patentService.getPatentById(patentId);
		mv.addObject("patent",patent);
		mv.addObject("returnPage",returnPage);
		mv.addObject("domain",domain);
		mv.addObject("titleUser",user);
		addBaseData(mv,user);
		addDeptMap(mv,user);
		return mv;
	}
//	@RequestMapping("search/author")
//	@ResponseBody
//	public List<InstitutionAuthor> searchAuthor(@RequestParam("q")String keyword,HttpServletRequest request){
//		List<InstitutionAuthor> authors=paperService.searchAuthor(CommonUtils.trim(keyword));
//		return authors;
//		
//	}
	@RequestMapping("search/patent")
	public String searchPatent(Model model,@RequestParam("offset") int offset,@RequestParam("size")int size,
			HttpServletRequest request){
//		ModelAndView mv=new ModelAndView("institution/patent/add");
//		List<InstitutionPatent> patents=patentService.getPatentByKeyword(offset, size, CommonUtils.trim(keyword));
//		JsonResult result = new JsonResult();
//		result.setData(patents);
//		return result;
		String keyword = CommonUtils.trim(request.getParameter("keyword"));
		SimpleUser user = SessionUtils.getUser(request);
		List<Integer> existPatentIds=patentService.getExistPatentIds(user.getId());
		List<InstitutionPatent> list = null;
		if(CommonUtils.isNull(keyword)){
			list = patentService.getPatentByInit(offset, size, CommonUtils.trim(user.getZhName()));
			keyword = user.getZhName();
		}else{
			list = patentService.getPatentByKeyword(offset, size, CommonUtils.trim(keyword));
		}
//		array = filter(array, request);
		JSONArray array =new JSONArray();
		array.addAll(list);
		array = filter(array,request);
		array = filterExist(array,existPatentIds);
		model.addAttribute("result", array);
		model.addAttribute("searchKeywords", keyword);
		return "jsontournamenttemplate";
	}
	/**
	 * 过滤掉页面中已经选中的Paper
	 * @param array 查询到的Paper信息
	 * @param request 参数中包含了existPapers，即为需要过滤的Paper ID
	 * @return 已经过滤后的Paper信息
	 */
	private JSONArray filter(JSONArray array, HttpServletRequest request){
		String existPatents = CommonUtils.trim(request.getParameter("existPatents"));
		if(null == existPatents || "".equals(existPatents)){
			return array;
		}
		JSONArray result = new JSONArray();
		String[] temp = existPatents.split(",");
		int size = array.size();
		for(int i=0; i<size; i++){
			InstitutionPatent obj = (InstitutionPatent) array.get(i);
			int patentId = obj.getId();
			boolean exist = false;
			for(String patent: temp){
				if(!CommonUtils.isNull(patent) && patentId == Integer.parseInt(patent)){
					exist = true;
					break;
				}
			}
			if(!exist){
				result.add(obj);
			}
		}
		return result;
	}
	/**
	 * 过滤掉页面中已经添加的Paper
	 * @param result 查询到的Paper信息
	 * @param existIds 查询到的已经存在的id
	 * @return 已经过滤后的Paper信息
	 */
	private JSONArray filterExist(JSONArray result, List<Integer> existIds) {
		if (null == result || result.size() <= 0 || null == existIds || existIds.isEmpty()) {
			return result;
		}
		int size = result.size();
		JSONArray array = new JSONArray();
		for (int i = 0; i < size; i++) {
			InstitutionPatent obj = (InstitutionPatent) result.get(i);
			int patentId = obj.getId();
			if (!existIds.contains(patentId)) {
				array.add(obj);
			}
		}
		return array;
	}
	@RequestMapping("search")
	@ResponseBody
	public ModelAndView detail(@PathVariable("domain")String domain,HttpServletRequest request){
		SimpleUser user = SessionUtils.getUser(request);
		ModelAndView mv=new ModelAndView("institution/patent/add");
//		List<InstitutionPatent> list = patentService.getPatentByInit(offset, size, CommonUtils.trim(user.getZhName()));
		mv.addObject("domain",domain);
		addBaseData(mv,user);
		return mv;
	}
	@RequestMapping("search/submit")
	@ResponseBody
	public ModelAndView save(@PathVariable("domain")String domain,HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution/patent/add");
		SimpleUser user = SessionUtils.getUser(request);
		String existPapers = request.getParameter("patentIds");
		if(null == existPapers || "".equals(existPapers)){
			return null;
		}
		String[] patentId = existPapers.split(",");
//		List<InstitutionPatent> list = patentService.getPatentByInit(offset, size, CommonUtils.trim(user.getZhName()));
		patentService.insertPatent(patentId, user.getId());
		mv.addObject("domain",domain);
		addBaseData(mv,user);
		return mv;
	}
}
