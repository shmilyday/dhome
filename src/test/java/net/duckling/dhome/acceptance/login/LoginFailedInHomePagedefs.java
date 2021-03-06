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
package net.duckling.dhome.acceptance.login;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import net.duckling.dhome.common.auth.sso.IAuthenticationService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.impl.mock.MockAuthenticationService;
import net.duckling.dhome.service.impl.mock.MockHomeService;
import net.duckling.dhome.service.impl.mock.MockUserService;
import net.duckling.dhome.testutil.SetFieldUtils;
import net.duckling.dhome.web.LoginController;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

public class LoginFailedInHomePagedefs {
	
	private MockHttpServletRequest request = new MockHttpServletRequest();
	private LoginController lc = new LoginController();
	private IAuthenticationService aus = new MockAuthenticationService();
	private IUserService us = new MockUserService();
	private IHomeService hs = new MockHomeService();
	private ModelAndView mv = null;
	
	@Given("^红盖在dhome的注册邮箱是\"([^\"]*)\"$")
	public void email_in_dhome(String arg1) {
		request.setParameter("email", arg1);
	}

	@Given("^红盖在dhome的登录密码是\"([^\"]*)\"$")
	public void correct_psw_in_dhome(String arg1) {
	    
	}

	@Given("^红盖的输入的密码是\"([^\"]*)\"$")
	public void error_psw_in_dhome(String arg1){
	    request.setParameter("password", arg1);
	}

	@When("^红盖点击\"([^\"]*)\"按钮$")
	public void click(String arg1){
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("authenticationService", aus);
		param.put("userService", us);
		param.put("homeService", hs);
		SetFieldUtils.setValues(lc, param);
		mv = lc.login(request);
	}

	@Then("^红盖看到提示信息\"([^\"]*)\"$")
	public void info(String arg1){
		Assert.assertNotNull(mv);
		Assert.assertEquals("login", mv.getViewName());
		System.out.println(mv.getModel().get("message"));
	}
}
