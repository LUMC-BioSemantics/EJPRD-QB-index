/*
 * MIT License
 *
 * Copyright (c) 2022 Biosemantics group
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template uploadFileToAgraphWorkFlow, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rajaram.ejp.resource.api.service;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.rajaram.ejp.resource.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.repository.util.Repositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * Service layer to handle catalogues related operations
 *
 * @author Rajaram Kaliyaperumal <rr.kaliyaperumal@gmail.com>
 * @since 2020-09-11
 * @version 0.1
 */
@Service
public class ResourcesService implements InitializingBean, DisposableBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourcesService.class);
    private static final String QUERYABLE_RESOURCES_QUERY_STRING;
    private static final String DISCOVERABLE_RESOURCES_QUERY_STRING;

    static {
        try {
            var fileURL = ResourcesService.class.getResource("list-queryable-resources.rq");
            QUERYABLE_RESOURCES_QUERY_STRING = Resources.toString(fileURL, Charsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        try {
            var fileURL = ResourcesService.class.getResource("list-discoverable-resources.rq");
            DISCOVERABLE_RESOURCES_QUERY_STRING = Resources.toString(fileURL, Charsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @org.springframework.beans.factory.annotation.Value("${fdp-index.triplestoreUrl}")
    private String triplestoreUrl;

    private Repository repository;

    @Override
    public void afterPropertiesSet() throws Exception {
        repository = new SPARQLRepository(triplestoreUrl);
        repository.init();
    }

    @Override
    public void destroy() throws Exception {
        repository.shutDown();
    }

    public List<Resource> getCatalogues() {
        LOGGER.info("Get all resources list");

        var resources = getQueryableResources();
        resources.addAll(getDiscoverableResources());

        return resources;
    }

    private List<Resource> getQueryableResources() {
        LOGGER.info("Get queryable resources list");
        var results = Repositories.tupleQueryNoTransaction(repository, QUERYABLE_RESOURCES_QUERY_STRING, QueryResults::asList);

        var catalogues = new ArrayList<Resource>();
        var previousID = "";
        Resource resourceCopy = null;

        for (var solution : results) {
            var id = solution.getValue("resource").stringValue();
            var theme = solution.getValue("theme").stringValue();

            if (!previousID.contentEquals(id)) {

                var resource = new Resource();
                addCommonResourceProperties(resource, solution);

                var type = new ArrayList<String>();
                type.add("http://www.w3.org/ns/dcat#Dataset");
                resource.setResourceType(type);

                if (solution.getValue("resource_endpoint") != null) {
                    resource.setResourceAddress(
                            solution.getValue("resource_endpoint")
                                    .stringValue());
                }

                if (solution.getValue("specs_iri") != null) {
                    resource.setSpecsURL(
                            solution.getValue("specs_iri")
                                    .stringValue());
                }
                resource.setId(id);
                catalogues.add(resource);
                previousID = id;
                resourceCopy = resource;
            }

            if ("http://purl.obolibrary.org/obo/NCIT_C47846".contentEquals(theme)) {
                resourceCopy.getTheme().add("http://purl.org/ejp-rd/vocabulary/KnowledgeBase");

            }
            if(!resourceCopy.getTheme().contains(theme)){
                resourceCopy.getTheme().add(theme);
            }
        }

        return catalogues;
    }
    
    private List<Resource> getDiscoverableResources() {
        LOGGER.info("Get discoverable resources list");
        var results = Repositories.tupleQueryNoTransaction(repository, DISCOVERABLE_RESOURCES_QUERY_STRING, QueryResults::asList);

        var catalogues = new ArrayList<Resource>();
        var previousID = "";
        Resource resourceCopy = null;

        for (var solution : results) {
            var id = solution.getValue("resource").stringValue();
            var theme = solution.getValue("theme").stringValue();
            var rtype = solution.getValue("resource_type").stringValue();

            if (!previousID.contentEquals(id)) {

                var resource = new Resource();
                addCommonResourceProperties(resource, solution);

                var type = new ArrayList<String>();
                type.add(rtype);
                resource.setResourceType(type);
                resource.setId(id);
                catalogues.add(resource);
                previousID = id;
                resourceCopy = resource;
            }
            if(!resourceCopy.getTheme().contains(theme)){
                resourceCopy.getTheme().add(theme);
            }
        }

        return catalogues;
    }
    
    private void addCommonResourceProperties(Resource resource, BindingSet solution) {
        Optional.of(solution.getValue("resource_name"))
                .map(Value::stringValue)
                .ifPresent(resource::setResourceName);

        Optional.ofNullable(solution.getValue("resource_description"))
                .map(Value::stringValue)
                .ifPresent(resource::setResourceDescription);

        Optional.ofNullable(solution.getValue("resource_logo"))
                .map(Value::stringValue)
                .ifPresentOrElse(resource::setLogo, () -> resource.setLogo(""));

        Optional.ofNullable(solution.getValue("home_page"))
                .map(Value::stringValue)
                .ifPresentOrElse(resource::setHomepage, () -> resource.setHomepage(resource.getId()));

        Optional.ofNullable(solution.getValue("created_time"))
                .map(Value::stringValue)
                .ifPresent(resource::setCreateDateTime);

        Optional.ofNullable(solution.getValue("updated_time"))
                .map(Value::stringValue)
                .ifPresent(resource::setUpdateDateTime);
    }
}
