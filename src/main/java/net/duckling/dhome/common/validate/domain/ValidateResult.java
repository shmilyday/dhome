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
/**
 * 
 */
package net.duckling.dhome.common.validate.domain;

import java.util.ArrayList;
import java.util.List;

import net.duckling.dhome.common.util.CommonUtils;

/**
 * 验证结果
 * 
 * @author lvly
 * @since 2012-9-28
 */
public class ValidateResult {
	private List<String> keys;
	
	/**
	 * 放入国际化中的key
	 * */
	public void setKey(String key) {
		if (keys == null) {
			keys = new ArrayList<String>();
		}
		keys.add(key);
	}
	/**判断是有验证通过
	 * @return 通过了吗？*/
	public boolean isPass() {
		return CommonUtils.isNull(keys);
	}
	public List<String> getKeys() {
		return keys;
	}

}
