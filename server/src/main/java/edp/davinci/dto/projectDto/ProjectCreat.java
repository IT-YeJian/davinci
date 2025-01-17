/*
 * <<
 *  Davinci
 *  ==
 *  Copyright (C) 2016 - 2019 EDP
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

package edp.davinci.dto.projectDto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NotNull(message = "Project info cannot be null")
public class ProjectCreat {

    @NotBlank(message = "Project name cannot be empty")
    private String name;

    private String description;

    @Min(value = 1L, message = "OrgId cannot be empty")
    private Long orgId;

    private String pic;

    private boolean visibility;

    private JSONObject config = JSON.parseObject("{\"watermark\": {\"isProject\": true,\"isUsername\": true, \"color\": \"#e1e1e1\", \"enable\": true, \"dateFormat\": \"yyyy-MM-dd hh:mm:ss\"}}");

}
