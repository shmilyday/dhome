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
package net.duckling.dhome.dao;

import net.duckling.dhome.domain.people.UserSetting;

/**
 * @author lvly
 * @since 2013-9-5
 */
public interface IUserSettingDAO {
	/**
	 * 获得一个用户配置
	 * @param uid
	 * @param key
	 * @return
	 */
	UserSetting getSetting(int uid,String key);
	/**
	 * 是否存在
	 * @param uid
	 * @param key
	 * @return
	 */
	boolean isExists(int uid,String key);
	
	/**
	 * 插入一条设置
	 * @param uid
	 * @param key
	 * @param value
	 * @return
	 */
	int insertSetting(int uid,String key,String value);
	
	/**
	 * 删除设置
	 * @param uid
	 * @param key
	 * @return
	 */
	void deleteSetting(int uid,String key);
	/**
	 * 更新配置
	 * @param uid
	 * @param key
	 * @param value
	 * @return
	 */
	void updateSetting(int uid,String key,String value);
}
