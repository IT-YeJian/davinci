/*
 * <<
 *  Davinci
 *  ==
 *  Copyright (C) 2016 - 2021 EDP
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

package edp.davinci.task;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(
        exclude = {
                org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
        })
@EnableScheduling
@ComponentScan(value="edp.davinci.*")
@MapperScan({"edp.davinci.core.dao","edp.davinci.task.mapper"})
public class DavinciTaskApplication {

	public static void main(String[] args) {
		System.out.println("DAVINCI_TASK_HOME:" + System.getenv("DAVINCI_TASK_HOME"));
		SpringApplication.run(DavinciTaskApplication.class, args);
	}
}

