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
package net.duckling.dhome.service.impl.mock;

import java.util.List;

import net.duckling.dhome.domain.object.AccessLog;
import net.duckling.dhome.service.IAccessLogService;

/**
 * @author lvly
 * @since 2012-12-14
 */
public class StubAccessLogService implements IAccessLogService {

	@Override
	public void addAccessLog(int visitorUid, int visitedUid, String visitorDomain, String ip) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getAccessLogCount(int visitedUid) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void updateAccessLog(int uid, String domain) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<AccessLog> getAccessLogs(int visitedUid) {
		// TODO Auto-generated method stub
		return null;
	}

}
