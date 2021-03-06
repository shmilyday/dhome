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

import net.duckling.dhome.domain.object.InterestCount;
import net.duckling.dhome.domain.people.Interest;
import net.duckling.dhome.domain.people.Dictionary;
import net.duckling.dhome.service.IInterestService;

/**
 * @author lvly
 * @since 2012-9-27
 */
public class MockInterestService implements IInterestService {
	

	@Override
	public int[] batchCreate(int uid, List<String> interests) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteByUid(int uid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Interest> getInterest(int uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Dictionary> searchInterest(String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InterestCount> getInterestAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
