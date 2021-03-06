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
package net.duckling.dhome.web.institution.backend;

/**
 * 总览
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.duckling.common.util.CommonUtils;
import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.util.PinyinUtil;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionMemberFromVmt;
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.institution.InstitutionPublicationStatistic;
import net.duckling.dhome.domain.institution.InstitutionVmtInfo;
import net.duckling.dhome.domain.object.JsonResult;
import net.duckling.dhome.domain.people.Home;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionBackendPaperService;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionPubStatisticService;
import net.duckling.dhome.service.IInstitutionVmtService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.impl.VMTMessageHandlerImpl;
import net.duckling.vmt.api.domain.VmtUser;
import net.duckling.vmt.api.impl.GroupService;
import net.duckling.vmt.api.impl.OrgService;
import net.duckling.vmt.api.impl.UserService;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import cn.vlabs.rest.ServiceException;

@Controller
@NPermission(role = "admin", authenticated=true)
@RequestMapping("/institution/{domain}/backend/pandect")
public class InstitutionBackEndPandectController {
	private static final String SPLIT = ",";
	private static final String EMPTY = "";
	private static final String END_EMPTY_IPS = ",0],";
	/**前台显示走势图时需要至少9个点     **/
	private static final int IPS_NUMBER = 9;
	
	private static final Logger LOG=Logger.getLogger(InstitutionBackEndPandectController.class);
	@Autowired
	private IHomeService userHomeService;
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private UserService vmtUserService;
	
	@Autowired
	private IInstitutionHomeService homeService;
	@Autowired
	private IInstitutionVmtService vmtService;
	
	@Autowired
	private IUserService userService;
	@Autowired
	private IInstitutionBackendService backEndService;
	
	@Autowired
	private IInstitutionBackendPaperService paperService;
	
	@Autowired
	private IInstitutionPubStatisticService ipss;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private VMTMessageHandlerImpl vmtMessageHandleImpl;
//	private Map<Integer,InstitutionDepartment> extractDept(List<InstitutionDepartment> depts){
//		if(CommonUtils.isNull(depts)){
//			return Collections.emptyMap();
//		}
//		Map<Integer,InstitutionDepartment> map=new HashMap<Integer,InstitutionDepartment>();
//		for(InstitutionDepartment dept:depts){
//			map.put(dept.getId(),dept);
//		}
//		return map;
//	}
	
	private List<Integer> getDeptId(List<InstitutionMemberFromVmt> member){
		List<Integer> deptIds=new ArrayList<Integer>();
		if(CommonUtils.isNull(member)){
			return deptIds;
		}
		for(InstitutionMemberFromVmt vmt:member){
			deptIds.add(vmt.getDepartId());
		}
		return deptIds;
	}
	
	private Map<Integer,Home> extractHome(List<Home> homes){
		if(CommonUtils.isNull(homes)){
			return Collections.emptyMap();
		}
		Map<Integer,Home> map=new HashMap<Integer,Home>();
		for(Home home:homes){
			map.put(home.getUid(),home);
		}
		return map;
	}
	
	private Map<String,SimpleUser> extract(List<SimpleUser> users){
		if(CommonUtils.isNull(users)){
			return Collections.emptyMap();
		}
		Map<String,SimpleUser> map=new HashMap<String,SimpleUser>();
		for(SimpleUser user:users){
			map.put(user.getEmail(),user);
		}
		return map;
	}
	private List<String> getEmail(List<InstitutionMemberFromVmt> vmts){
		List<String> email=new ArrayList<String>();
		if(CommonUtils.isNull(vmts)){
			return email;
		}
		for(InstitutionMemberFromVmt vmt:vmts){
			email.add(vmt.getCstnetId());
		}
		return email;
	}
	private List<Integer> getUids(List<SimpleUser> users){
		if(CommonUtils.isNull(users)){
			return Collections.emptyList();
		}
		List<Integer> uids=new ArrayList<Integer>();
		for(SimpleUser user:users){
			uids.add(user.getId());
		}
		return uids;
	}
	
	@RequestMapping("index")
	public ModelAndView list(@PathVariable("domain")String domain,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		
		if(!backEndService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("list:user["+SessionUtils.getUserId(request)+"]is not admin@["+domain+"]");
			return new ModelAndView("institution_backend/permission");
		}
		
		ModelAndView mv=new ModelAndView("institution_backend/pandect/index");
		
		//学历统计
		Map<String, Integer> degreesMap = backEndService.getDegreesMap(home.getInstitutionId(),-1);
		
		//部门
		List<InstitutionDepartment> depts=backEndService.getVmtDepartment(home.getInstitutionId());
		InstitutionDepartment department = new InstitutionDepartment();
		department.setShortName("全所");
		department.setId(-1);
		depts.add(0,department);
		
		mv.addObject("dept",depts);
		mv.addObject("educations",formatPieData(formatDegree(degreesMap),true));
		
		Map<String, Map<String, Integer>> paperDeptMap = paperService.getPaperStatisticsForDept();
//		mv.addObject("stats", getJSONStatisticForDept(paperDeptMap));
		int paperCount=backEndService.getPaperCount(home.getInstitutionId(), -1, -1);
		
		List<ArrayList<Object>> cites = new ArrayList<ArrayList<Object>>();
		List<ArrayList<Object>> pubCounts = new ArrayList<ArrayList<Object>>();
		addPaperStatistic(paperCount,cites,pubCounts,paperDeptMap);
		mv.addObject("deptCites", cites);
		mv.addObject("deptPubCounts", pubCounts);
		
		List<ArrayList<Object>> yearCites = new ArrayList<ArrayList<Object>>();
		List<ArrayList<Object>> yearPubCounts = new ArrayList<ArrayList<Object>>();
		addPaperStatistic(paperCount,yearCites,yearPubCounts,paperService.getPaperStatisticsForYear());
		mv.addObject("yearCites", yearCites);
		mv.addObject("yearPubCounts", yearPubCounts);
		
		
		mv.addObject("dPaperCount", paperCount);
		
		mv.addObject("citedNum",paperService.getPapersCite(home.getInstitutionId()));
//		PageResult<InstitutionMemberFromVmt> result=backEndService.getVmtMember(home.getInstitutionId(), page);
//		mv.addObject("page",result);
//		List<SimpleUser> users=userService.getUserByEmails(getEmail(result.getDatas()));
//		mv.addObject("userMap",extract(users));
//		
//		List<Home> homes=userHomeService.getDomainsByUids(getUids(users));
//		mv.addObject("homeMap",extractHome(homes));
//		
//		List<InstitutionDepartment> depts=backEndService.getVmtDepartment(getDeptId(result.getDatas()));
//		mv.addObject("deptMap",extractDept(depts));
//		mv.addObject("domain",domain);
		return mv;
	}
	
	public List<ArrayList<Object>> formatPieData(Map<String, Integer> map,boolean isSplit){
		List<ArrayList<Object>> list = new ArrayList<ArrayList<Object>>();
		for(Map.Entry<String, Integer> entry : map.entrySet()){
			ArrayList<Object> temp = new ArrayList<Object>();
			if(isSplit){
				temp.add("'"+entry.getKey()+"'");
			}else{
				temp.add(entry.getKey());
			}
			temp.add(entry.getValue());
			list.add(temp);
		}
		
		return list;
	}
	
	
	/**
	 * 已部门为标准统计论文
	 * @param stats
	 * @return
	 */
	private void addPaperStatistic(int total,List<ArrayList<Object>> cites,List<ArrayList<Object>> pubCounts,Map<String, Map<String, Integer>> data){
		
		int sum=0;
		for(Map.Entry<String, Map<String, Integer>> entry : data.entrySet()){
			
			String key = entry.getKey();
//			if(deptMap != null && !"未知".equals(key)){
//				key = deptMap.get(Integer.valueOf(key)).getShortName();
//			}
			
			ArrayList<Object> cite = new ArrayList<Object>();
			cite.add("'"+key+"'");
			cite.add(entry.getValue().get("cite"));
			cites.add(cite);
			
			ArrayList<Object> count = new ArrayList<Object>();
			count.add("'"+key+"'");
			count.add(entry.getValue().get("count"));
			pubCounts.add(count);
			sum+=entry.getValue().get("count");
		}
		
		if(total-sum>0){
			ArrayList<Object> count = new ArrayList<Object>();
			count.add("'其他'");
			count.add(total-sum);
			pubCounts.add(count);
		}
	}
	/**
	 * 总览
	 * @return 学术成果数量
	 */
	@RequestMapping("count/{departmentId}/{year}")
	@ResponseBody
	private JsonResult getCount(@PathVariable("domain") String domain,@PathVariable("departmentId") int deptId,@PathVariable("year") int year){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		
		JsonResult result = new JsonResult();
		Map<String , Integer> map = new HashMap<String, Integer>();
		int memberCount=backEndService.getMemberCount(home.getInstitutionId(), deptId, year);
		map.put("member", memberCount);
		int paperCount=backEndService.getPaperCount(home.getInstitutionId(), deptId, year);
		map.put("paper", paperCount);
		
		int treatiseCount=backEndService.getTreatiseCount(home.getInstitutionId(), deptId, year);
		map.put("treatise", treatiseCount);
		int awardCount=backEndService.getAwardCount(home.getInstitutionId(), deptId, year);
		map.put("award", awardCount);
		
		int copyrightCount=backEndService.getCopyrightCount(home.getInstitutionId(), deptId, year);
		map.put("copyright", copyrightCount);
		int patentCount=backEndService.getPatentCount(home.getInstitutionId(), deptId, year);
		map.put("patent", patentCount);
		
		int topicCount=backEndService.getTopicCount(home.getInstitutionId(), deptId, year);
		map.put("topic", topicCount);
		int academicCount=backEndService.getAcademicCount(home.getInstitutionId(), deptId, year);
		map.put("academic", academicCount);
		
		int periodicalCount=backEndService.getPeriodicalCount(home.getInstitutionId(), deptId, year);
		map.put("periodical", periodicalCount);
		int trainingCount=backEndService.getTrainingCount(home.getInstitutionId(), deptId, year);
		map.put("training", trainingCount);
		
		result.setData(map);
		return result;
	}
	/**
	 * 成员分布
	 * @return
	 */
	@RequestMapping("member/{op}/{deptId}")
	@ResponseBody
	private JsonResult memberDistribute(@PathVariable("domain") String domain,@PathVariable("deptId") int deptId,@PathVariable("op") String op){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		
		int insId = home.getInstitutionId();
		JsonResult result = new JsonResult();
		Map<String , Integer> map = new LinkedHashMap<String, Integer>();
		if(op.equals("degree")){
			map = formatDegree(backEndService.getDegreesMap(insId, deptId));
		}
		
		if(op.equals("age")){
			map = formatAge(backEndService.getAgesMap(insId, deptId));
		}
		
		if(op.equals("title")){
			Map<String , Integer> temp = backEndService.getTitlesMap(insId, deptId);
			for(Map.Entry<String, Integer> entry : temp.entrySet()){
				if(!entry.getKey().equals("其他")){
					map.put(entry.getKey()+"("+entry.getValue()+"人)", entry.getValue());
				}
			}
			map.put("其他"+"("+temp.get("其他")+"人)", temp.get("其他"));
		}
		
		result.setData(formatPieData(map,false));
		return result;
	}
	
	public Map<String,Integer> formatDegree(Map<String, Integer> data){
		String doctor = "博士";
		String master = "硕士";
		String college = "大学本科";
		String other = "其他";
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		map.put(doctor, 0);
		map.put(master, 0);
		map.put(college, 0);
		map.put(other, 0);
		for(Map.Entry<String, Integer> entry : data.entrySet()){
			String key = entry.getKey();
			if(key.contains(doctor)){
				key = doctor;
			}else if(key.contains(master)){
				key = master;
			}else if(key.contains("学士")){
				key = college;
			}else{
				key = other;
			}
			map.put(key, map.get(key) + entry.getValue());
		}
		
		Map<String, Integer> temp = new LinkedHashMap<String, Integer>();
		for(Map.Entry<String, Integer> entry : map.entrySet()){
			temp.put(entry.getKey()+"("+entry.getValue()+"人)", entry.getValue());
		}
		return temp;
	}
	
	public Map<String, Integer> formatAge(Map<String, Integer> data){
		Map<String, Integer> temp = new LinkedHashMap<String, Integer>();
		for(Map.Entry<String, Integer> entry : data.entrySet()){
			String key = entry.getKey();
			if(key.equals("其他")){
				temp.put("其他", entry.getValue());
			}else{
				int age = Integer.valueOf(key);
				if(age < 20){
					key = "20岁以下";
				}else if(age >= 20 && age < 25){
					key ="20 ~ 24岁";
				}else if(age >= 25 && age < 30){
					key ="25 ~ 29岁";
				}else if(age >= 30 && age < 35){
				    key ="30 ~ 34岁";
			    }else if(age >= 35 && age < 40){
				    key ="35 ~ 39岁";
			    }else if(age >= 40 && age < 45){
			    	key ="40 ~ 44岁";
			    }else if(age >= 45 && age < 50){
			    	key ="45 ~ 49岁";
			    }else if(age >= 50 && age < 55){
			    	key ="50 ~ 54岁";
			    }else if(age >= 55 && age < 60){
			    	key ="55 ~ 59岁";
			    }else{
			    	key ="60岁以上";
				}
			
				if(temp.containsKey(key)){
					temp.put(key, temp.get(key) + entry.getValue());
				}else{
					temp.put(key, entry.getValue());
				}
			}
		}
		
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		for(Map.Entry<String, Integer> entry : temp.entrySet()){
			if(!entry.getKey().equals("其他")){
				map.put(entry.getKey()+"("+entry.getValue()+"人)", entry.getValue());
			}
		}
		map.put("其他"+"("+temp.get("其他")+"人)", temp.get("其他"));
		return map;
	}
	
	
	/**
	 * 此处默认stats中的数据都是按年份排好序的
	 * @param stats
	 * @return
	 */
	private JSONObject getJSONStatistic(List<InstitutionPublicationStatistic> stats){
		JSONObject result = new JSONObject();
		StringBuilder cite = new StringBuilder();//每年的引用次数
		StringBuilder num = new StringBuilder();//每年的论文数
		StringBuilder year = new StringBuilder();//年份
		StringBuilder totalCite = new StringBuilder();//截止到该年的累积引用次数
		StringBuilder totalNum = new StringBuilder();//截止到该年的累积论文数
		int curYear = -1;
		if(null != stats && !stats.isEmpty()){
			for(InstitutionPublicationStatistic statsRecord : stats){
				getData(statsRecord, cite, num, year, totalCite, totalNum, curYear);
				curYear = statsRecord.getYear();
			}
			int systemYear = Calendar.getInstance().get(Calendar.YEAR);
			if(curYear != systemYear){
				insertEmptyYear(cite, num, year, totalCite, totalNum, curYear, systemYear+1);
			}
			paddingRecords(cite, num, year, totalCite, totalNum);
			cite.replace(cite.lastIndexOf(SPLIT), cite.length(), EMPTY);
			num.replace(num.lastIndexOf(SPLIT), num.length(), EMPTY);
			year.replace(year.lastIndexOf(SPLIT), year.length(), EMPTY);
			totalCite.replace(totalCite.lastIndexOf(SPLIT), totalCite.length(), EMPTY);
			totalNum.replace(totalNum.lastIndexOf(SPLIT), totalNum.length(), EMPTY);
		}
		result.put("cite", cite.toString());
		result.put("num", num.toString());
		result.put("year", year.toString());
		result.put("totalCite", totalCite.toString());
		result.put("totalNum", totalNum.toString());
		result.put("isEmpty", isNoPaper(stats));
		return result;
	}
	
	
	
	private boolean isNoPaper(List<InstitutionPublicationStatistic> stats){
		if(null == stats || stats.isEmpty()){
			return true;
		}
		InstitutionPublicationStatistic ipsEntity = stats.get(stats.size()-1);
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		if(currentYear == ipsEntity.getYear()){
			return ipsEntity.getTotalPaperCount() == 0;
		}else{
			LOG.info("");
			return false;
		}
	}
	
	
	/**
	 * 将论文统计信息解析出来，并存入对应的StringBuilder对象中
	 * @param ips 论文统计信息
	 * @param cite 引用次数
	 * @param num 论文数量
	 * @param year 年份
	 * @param totalCite 累积引用次数
	 * @param totalNum 累积论文数量
	 * @param curYear 当前年
	 */
	private void getData(InstitutionPublicationStatistic ips, StringBuilder cite,
			StringBuilder num, StringBuilder year, StringBuilder totalCite,
			StringBuilder totalNum, int curYear){
		int ipsYear = ips.getYear();
		if(curYear != -1 && (ipsYear - curYear > 1)){
			insertEmptyYear(cite, num, year, totalCite, totalNum, curYear, ipsYear);
		}
		year.append(ipsYear+SPLIT);
		cite.append("["+ipsYear+SPLIT+ips.getAnnualCitationCount()+"]"+SPLIT);
		num.append("["+ipsYear+SPLIT+ips.getAnnualPaperCount()+"]"+SPLIT);
		totalCite.append("["+ipsYear+SPLIT+ips.getTotalCitationCount()+"]"+SPLIT);
		totalNum.append("["+ipsYear+SPLIT+ips.getTotalPaperCount()+"]"+SPLIT);
	}
	
	/**
	 * 对于论文时间跳跃度比较大的年份之间插入冗余年份信息。 如某机构有两篇论文分别发表于2009、2012年，<br/>
	 * 则插入2010和2011年的冗余信息。
	 * @param cite 引用次数
	 * @param num 论文数量
	 * @param year 年份
	 * @param totalCite 累积引用次数
	 * @param totalNum 累积论文数量
	 * @param curYear 起始年
	 * @param ipsYear 结束年
	 */
	private void insertEmptyYear(StringBuilder cite, StringBuilder num, StringBuilder year, StringBuilder totalCite,
			StringBuilder totalNum, int curYear, int ipsYear){
		String temp = totalCite.substring(0, totalCite.lastIndexOf(SPLIT));
		int start = temp.lastIndexOf(SPLIT)>=0?temp.lastIndexOf(SPLIT)+1:0;
		int end = temp.lastIndexOf(']')>=0?temp.lastIndexOf(']'):temp.length();
		String curTotalCite = temp.substring(start, end);
		temp = totalNum.substring(0, totalNum.lastIndexOf(SPLIT));
		start = temp.lastIndexOf(SPLIT)>=0?temp.lastIndexOf(SPLIT)+1:0;
		end = temp.lastIndexOf(']')>=0?temp.lastIndexOf(']'):temp.length();
		String curTotalNum = temp.substring(start, end);
		for(int i=curYear+1; i<ipsYear; i++){
			year.append(i+SPLIT);
			cite.append("["+i+",0]"+SPLIT);
			num.append("["+i+",0]"+SPLIT);
			totalCite.append("["+i+SPLIT+curTotalCite+"]"+SPLIT);
			totalNum.append("["+i+SPLIT+curTotalNum+"]"+SPLIT);
		}
	}
	
	
	/**
	 * 填充空的统计数据，已满足至少返回9条论文统计信息供前台显示
	 * @param cite
	 * @param num
	 * @param year
	 * @param totalCite
	 * @param totalNum
	 */
	private void paddingRecords(StringBuilder cite, StringBuilder num, StringBuilder year, StringBuilder totalCite,
			StringBuilder totalNum){
		String[] years = year.toString().split(SPLIT);
		int yearNum = years.length;
		if(yearNum < IPS_NUMBER){
			int firstYear = Integer.valueOf(years[0]);
			int paddingLen = IPS_NUMBER - yearNum;
			for(int i =1; i<=paddingLen; i++){
				int tempYear = (firstYear-i);
				year.insert(0, tempYear+",");
				cite.insert(0, "["+tempYear+END_EMPTY_IPS);
				num.insert(0, "["+tempYear+END_EMPTY_IPS);
				totalCite.insert(0, "["+tempYear+END_EMPTY_IPS);
				totalNum.insert(0, "["+tempYear+END_EMPTY_IPS);
			}
		}
	}
	@RequestMapping("delete")
	public ModelAndView delete(@PathVariable("domain")String domain,@RequestParam("umtId[]")String[] umtId,@RequestParam("page")String page,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		if(!backEndService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			return null;
		}
		InstitutionVmtInfo vmtInfo=vmtService.getVmtInfoByInstitutionId(home.getInstitutionId());
		if(vmtInfo==null){
			return null;
		}
		try {
			List<VmtUser> users=vmtUserService.searchUserByUmtId(vmtInfo.getDn(),umtId);
			if(CommonUtils.isNull(users)){
				LOG.error("why not found user["+Arrays.toString(umtId)+"],in "+vmtInfo.getDn());
			}else{
				vmtUserService.removeUser(extractDN(users));
				backEndService.deleteMember(home.getInstitutionId(), umtId);
			}
			LOG.info("delete user["+Arrays.toString(umtId)+"] in ["+vmtInfo.getDn()+"]");
		} catch (ServiceException e) {
			LOG.error(e.getMessage(),e);
		}
		return new ModelAndView(new RedirectView(request.getContextPath()+"/institution/"+domain+"/backend/member/list/"+page));
	}
	
	private String[] extractDN(List<VmtUser> users){
		if(CommonUtils.isNull(users)){
			return new String[]{};
		}
		String[] dns=new String[users.size()];
		int index=0;
		for(VmtUser u:users){
			dns[index++]=u.getDn();
		}
		return dns;
	}
	@RequestMapping("update/{umtId}")
	@ResponseBody
	public boolean update(InstitutionMemberFromVmt vmtMember,@PathVariable("domain")String domain,@PathVariable("umtId")String umtId,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(CommonUtils.isNull(vmtMember.getUmtId())||!vmtMember.getUmtId().equals(umtId)){
			LOG.error("update.error:umtId not found["+umtId+"]");
			return false;
		}
		if(CommonUtils.isNull(vmtMember.getTrueName())){
			LOG.error("update.error:trueName can't be null");
			return false;
		}
		if(home==null){
			LOG.error("update.error:not found institutiton["+domain+"]");
			return false;
		}
		InstitutionVmtInfo vmtInfo=vmtService.getVmtInfoByInstitutionId(home.getInstitutionId());
		if(vmtInfo==null){
			LOG.error("update.error:can't found vmtInfo[insId="+home.getInstitutionId()+"]");
			return false;
		}
		if(!backEndService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			LOG.error("update.error:not admin!");
			return false;
		}
		if(CommonUtils.isNull(vmtMember.getUmtId())){
			LOG.error("update.error：umtId is Null!");
			return false;
		}
		try {
			VmtUser user=CommonUtils.first(vmtUserService.searchUserByUmtId(vmtInfo.getDn(), new String[]{umtId}));
			if(user==null){
				LOG.error("update.error:can't find vmt user!");
				return false;
			}
			user.setTitle(vmtMember.getTitle());
			user.setName(vmtMember.getTrueName());
			user.setOffice(vmtMember.getOfficeAddress());
			user.setOfficePhone(vmtMember.getOfficeTelephone());
			user.setTelephone(vmtMember.getMobilePhone());
			user.setTitle(vmtMember.getTitle());
			user.setSex(vmtMember.getSex());
			user.setUmtId(vmtMember.getUmtId());
			vmtUserService.updateByUmtId(vmtInfo.getDn(), user);
			
		} catch (ServiceException e) {
			LOG.error(e.getMessage(),e);
			return false;
		}
		return true;
	}
	
	@RequestMapping("detail/{umtId}.json")
	@ResponseBody
	public InstitutionMemberFromVmt detailJson(@PathVariable("domain")String domain,@PathVariable("umtId")String umtId,HttpServletRequest request){
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		if(home==null){
			return null;
		}
		if(!backEndService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(request).getEmail())){
			return null;
		}
		return backEndService.getVmtMemberByUmtId(home.getInstitutionId(),umtId);
	}
	
	@RequestMapping("detail/{umtId}")
	public ModelAndView detail(@PathVariable("domain")String domain,@PathVariable("umtId")String umtId,HttpServletRequest request){
		ModelAndView mv=new ModelAndView("institution_backend/member/detail");
		InstitutionMemberFromVmt vmtMember=detailJson(domain, umtId, request);
		if(vmtMember==null){
			return null;
		}
		if(vmtMember.getDepartId()>0){
			mv.addObject("dept",backEndService.getVmtDepartmentById(vmtMember.getDepartId()));
		}
		mv.addObject("member",vmtMember);
		mv.addObject("domain",domain);
		return mv;
	}
}
