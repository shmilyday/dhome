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
package net.duckling.dhome.dao;

import java.util.List;

import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionTreatiseAuthor;

public interface IInstitutionBackendPatentAuthorDAO {

	List<InstitutionAuthor> getAuthorListByPatentId(int patentId);
	List<InstitutionAuthor> getAuthorListByPatentIds(List<Integer> patentIds);
//	void deleteAuthorsByTopicIds(int[] topicIds);
	void createAuthor(InstitutionAuthor author);
//	void updateRef(int topicId,int[] uid,String[] authorType);
	void createRef(int patentId,int[] uid,int[] order);
	void deleteRef(int patentId);
	
   void updateRef(int authorId,int patentId,int order);
	
	/**
	 * 判断作者是否存在
	 * @param author
	 * @return
	 */
	boolean isExist(InstitutionAuthor author);
}
