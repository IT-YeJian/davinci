/*
 * <<
 *  Davinci
 *  ==
 *  Copyright (C) 2016 - 2020 EDP
 *  ==
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  >>
 *
 */

package edp.davinci.server.service;

import edp.davinci.core.dao.entity.User;
import edp.davinci.server.dto.view.*;
import edp.davinci.server.exception.NotFoundException;
import edp.davinci.server.exception.ServerException;
import edp.davinci.server.exception.UnAuthorizedException;
import edp.davinci.server.model.Paging;
import edp.davinci.server.model.PagingWithQueryColumns;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface ViewService extends CheckEntityService {
    
    ViewWithSourceBaseInfo getView(Long id, User user) throws NotFoundException, UnAuthorizedException, ServerException;
    
    List<ViewBaseInfo> getViews(Long projectId, User user) throws NotFoundException, UnAuthorizedException, ServerException;

    ViewWithSourceBaseInfo createView(ViewCreate viewCreate, User user) throws NotFoundException, UnAuthorizedException, ServerException;

    boolean updateView(ViewUpdate viewUpdate, User user) throws NotFoundException, UnAuthorizedException, ServerException;

    boolean deleteView(Long id, User user) throws NotFoundException, UnAuthorizedException, ServerException;

    PagingWithQueryColumns execute(ViewExecuteParam executeParam, User user) throws NotFoundException, UnAuthorizedException, ServerException;

    Paging<Map<String, Object>> getData(Long id, WidgetQueryParam queryParam, User user) throws NotFoundException, UnAuthorizedException, ServerException;

    PagingWithQueryColumns getDataWithQueryColumns(Long id, WidgetQueryParam queryParam, User user) throws NotFoundException, UnAuthorizedException, ServerException;

    PagingWithQueryColumns getDataWithQueryColumns(boolean isMaintainer, ViewWithSource viewWithSource, WidgetQueryParam executeParam, User user) throws NotFoundException, UnAuthorizedException, ServerException;

    List<Map<String, Object>> getDistinctValue(Long id, WidgetDistinctParam param, User user) throws NotFoundException, UnAuthorizedException, ServerException;

    List<Map<String, Object>> getDistinctValue(boolean isMaintainer, ViewWithSource viewWithSource, WidgetDistinctParam param, User user) throws ServerException;

    String showSql(Long id, WidgetQueryParam queryParam, User user) throws NotFoundException, UnAuthorizedException, ServerException;
}
