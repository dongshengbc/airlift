/*
 * Copyright 2010 Proofpoint, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.airlift.http.server.testing;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import io.airlift.event.client.NullEventClient;
import io.airlift.http.server.HttpServer;
import io.airlift.http.server.HttpServerBinder.HttpResourceBinding;
import io.airlift.http.server.HttpServerConfig;
import io.airlift.http.server.HttpServerInfo;
import io.airlift.http.server.HttpsConfig;
import io.airlift.http.server.RequestStats;
import io.airlift.http.server.TheServlet;
import io.airlift.node.NodeInfo;
import io.airlift.tracetoken.TraceTokenManager;
import jakarta.servlet.Filter;
import jakarta.servlet.Servlet;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class TestingHttpServer
        extends HttpServer
{
    private final HttpServerInfo httpServerInfo;

    public TestingHttpServer(
            HttpServerInfo httpServerInfo,
            NodeInfo nodeInfo,
            HttpServerConfig config,
            @TheServlet Servlet servlet,
            @TheServlet Map<String, String> initParameters)
            throws IOException
    {
        this(httpServerInfo,
                nodeInfo,
                config,
                Optional.empty(),
                servlet,
                initParameters,
                ImmutableSet.of(),
                ImmutableSet.of(),
                ClientCertificate.NONE);
    }

    @Inject
    public TestingHttpServer(
            HttpServerInfo httpServerInfo,
            NodeInfo nodeInfo,
            HttpServerConfig config,
            Optional<HttpsConfig> httpsConfig,
            @TheServlet Servlet servlet,
            @TheServlet Map<String, String> initParameters,
            @TheServlet Set<Filter> filters,
            @TheServlet Set<HttpResourceBinding> resources,
            ClientCertificate clientCertificate)
            throws IOException
    {
        super(httpServerInfo,
                nodeInfo,
                config.setLogEnabled(false),
                httpsConfig,
                servlet,
                initParameters,
                ImmutableSet.copyOf(filters),
                ImmutableSet.copyOf(resources),
                null,
                null,
                ImmutableSet.of(),
                clientCertificate,
                null,
                null,
                new TraceTokenManager(),
                new RequestStats(),
                new NullEventClient(),
                Optional.empty());
        this.httpServerInfo = httpServerInfo;
    }

    public URI getBaseUrl()
    {
        return httpServerInfo.getHttpUri();
    }

    public int getPort()
    {
        return httpServerInfo.getHttpUri().getPort();
    }

    public HttpServerInfo getHttpServerInfo()
    {
        return httpServerInfo;
    }
}
