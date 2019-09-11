/*
 * Copyright 2019 Maarten Mulders and other contributors as indicated by the @author tags.
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
 */
package it.mulders.quarkusmvc.exampleapp;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.mvc.Controller;
import javax.mvc.Models;
import javax.mvc.View;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Controller
@Path("/greeting/")
@RequestScoped
public class GreetingResource {
    @Inject
    UserInfoService userInfoService;

    @Inject
    Models models;

    @GET
    @View("hello.jsp")
    public void hello(@QueryParam("name") final String name) {
        models.put("name", name);
        models.put("remoteIp", userInfoService.getRemoteIp());
    }

    @GET
    @View("hello.jsp")
    public void hello() {
        hello("World");
    }
}
