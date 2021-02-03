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

import edp.davinci.core.dao.entity.Source;
import edp.davinci.server.dto.source.*;
import edp.davinci.server.exception.NotFoundException;
import edp.davinci.server.exception.ServerException;
import edp.davinci.server.exception.UnAuthorizedException;
import edp.davinci.server.model.DBTables;
import edp.davinci.server.model.TableInfo;
import edp.davinci.core.dao.entity.User;
import edp.davinci.data.pojo.DatabaseType;
import edp.davinci.data.pojo.SourceConfig;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public interface SourceService extends CheckEntityService {

    List<Source> getSources(Long projectId, User user) throws NotFoundException, UnAuthorizedException, ServerException;

    Source createSource(SourceCreate sourceCreate, User user) throws NotFoundException, UnAuthorizedException, ServerException;

    Source updateSource(SourceInfo sourceInfo, User user) throws NotFoundException, UnAuthorizedException, ServerException;

    boolean deleteSource(Long id, User user) throws NotFoundException, UnAuthorizedException, ServerException;

    boolean testSource(SourceConfig sourceConfig, User user) throws ServerException;

    void validCsvmeta(Long sourceId, UploadMeta uploadMeta, User user) throws NotFoundException, UnAuthorizedException, ServerException;

    Boolean dataUpload(Long sourceId, SourceDataUpload sourceDataUpload, MultipartFile file, User user, String type) throws NotFoundException, UnAuthorizedException, ServerException;

    List<String> getSourceDatabases(Long id, User user) throws NotFoundException, ServerException;

    DBTables getSourceTables(Long id, String dbName, User user) throws NotFoundException;

    TableInfo getTableInfo(Long id, String dbName, String tableName, User user) throws NotFoundException;

    Source getSourceDetail(Long id, User user) throws NotFoundException, UnAuthorizedException, ServerException;

    List<DatabaseType> getSupportDatabases();

    boolean reconnect(Long id, DbBaseInfo dbBaseInfo, User user) throws NotFoundException, UnAuthorizedException, ServerException;
}
