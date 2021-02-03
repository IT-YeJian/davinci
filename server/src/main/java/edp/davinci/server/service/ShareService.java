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

import edp.davinci.server.dto.share.*;
import edp.davinci.server.dto.user.UserLogin;
import edp.davinci.server.dto.view.WidgetDistinctParam;
import edp.davinci.server.dto.view.WidgetQueryParam;
import edp.davinci.server.exception.ForbiddenException;
import edp.davinci.server.exception.NotFoundException;
import edp.davinci.server.exception.ServerException;
import edp.davinci.server.exception.UnAuthorizedException;
import edp.davinci.server.model.Paging;
import edp.davinci.core.dao.entity.User;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
public interface ShareService {
    ShareWidget getShareWidget(User user) throws NotFoundException, ServerException, ForbiddenException, UnAuthorizedException;

    String generateShareToken(Long shareEntityId, String username, Long userId) throws ServerException;

    User shareLogin(UserLogin userLogin) throws NotFoundException, ServerException, UnAuthorizedException;

    ShareDisplay getShareDisplay(User user) throws NotFoundException, ServerException, ForbiddenException, UnAuthorizedException;

    ShareDashboard getShareDashboard(User user) throws NotFoundException, ServerException, ForbiddenException, UnAuthorizedException;

    Paging<Map<String, Object>> getShareData(WidgetQueryParam queryParam, User user) throws NotFoundException, ServerException, ForbiddenException, UnAuthorizedException, SQLException;

    List<Map<String, Object>> getDistinctValue(WidgetDistinctParam param, User user);

    void formatShareParam(Long projectId, ShareEntity entity);

    Map<String, Object> checkShareToken() throws ServerException, ForbiddenException;

    Map<String, Object> getSharePermissions() throws ServerException, ForbiddenException;

    @Deprecated
    ShareInfo getShareInfo(String token, User user) throws ServerException, ForbiddenException;

    @Deprecated
    void verifyShareUser(User user, ShareInfo shareInfo) throws ServerException, ForbiddenException;
}
