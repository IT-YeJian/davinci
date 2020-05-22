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

package edp.davinci.data.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ColumnModel {
	private String name;
	private String sqlType;
	private String visualType;
	private String modelType;

	@JsonCreator
	public ColumnModel(@JsonProperty("name") String name, @JsonProperty("sqlType") String sqlType,
			@JsonProperty("visualType") String visualType, @JsonProperty("modelType") String modelType) {
		this.name = name;
		this.sqlType = sqlType;
		this.visualType = visualType;
		this.modelType = modelType;
	}
}
