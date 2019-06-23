package io.bearcave.yakba.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Controller
public class FrontendController {

    @Bean
    public RouterFunction<ServerResponse> getPageRedirectingToApp(@Value("classpath:/static/index.html") Resource html) {
        return route(
                RequestPredicates.GET("/"),
                request -> ok()
                        .contentType(MediaType.TEXT_HTML)
                        .syncBody(html)
        );
    }

    @Bean
    public RouterFunction<ServerResponse> htmlRouter(@Value("classpath:/dist/index.html") Resource html) {
        return route(
                RequestPredicates.GET("/app/**"),
                request -> ok()
                        .contentType(MediaType.TEXT_HTML)
                        .syncBody(html)
        );
    }
}
