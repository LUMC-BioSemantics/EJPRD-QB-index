/**
 * The MIT License
 * Copyright © 2020 Rajaram Kaliyaperumal
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import nl.rajaram.ejp.resource.api.configuration.FDPIndexProperties;
import nl.rajaram.ejp.resource.model.Resource;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service layer to handle catalogues related operations
 *
 * @author Rajaram Kaliyaperumal <rr.kaliyaperumal@gmail.com>
 * @since 2020-09-11
 * @version 0.1
 */
@Service
public class ResourcesService {

    private static final Logger LOGGER = LoggerFactory.
            getLogger(ResourcesService.class);
    private final String GET_QUERYABLE_RESOURCES_QUERY
            = "list-queryable-resources.rq";
    private final String GET_DISCOVERABLE_RESOURCES_QUERY
            = "list-discoverable-resources.rq";

    @Autowired
    private FDPIndexProperties fdpIndexProperties;

    public List<Resource> getCatalogues() throws IOException {
        LOGGER.info("Get all recources list");
        List<Resource> catalogues = getQueryableResources();
        catalogues.addAll(getDiscoverableResources());

        return catalogues;
    }

    private List<Resource> getQueryableResources() throws IOException {
        LOGGER.info("Get queryable recources list");
        List<Resource> catalogues = new ArrayList();
        URL fileURL = ResourcesService.class.
                getResource(GET_QUERYABLE_RESOURCES_QUERY);
        String queryString = Resources.toString(fileURL, Charsets.UTF_8);
        Repository repository = new SPARQLRepository(fdpIndexProperties.
                triplestoreUrl);

        try ( RepositoryConnection conn = repository.getConnection()) {

            TupleQuery query = conn.prepareTupleQuery(queryString);

            String previousID = "";
            Resource resourceCopy = null;

            try ( TupleQueryResult result = query.evaluate()) {
                while (result.hasNext()) {
                    BindingSet solution = result.next();
                    String id = solution.getValue("resource").stringValue();
                    String theme = solution.getValue("theme").stringValue();
                    
                    if (!previousID.contentEquals(id)) {
                        
                        Resource resource = new Resource();
                        addCommonResourceProperties(resource, solution);
                        
                        List<String> type = new ArrayList();
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

                    if (theme.contentEquals(
                            "http://purl.obolibrary.org/obo/NCIT_C47846")) {
                        resourceCopy.getTheme().
                                add("http://purl.org/ejp-rd/vocabulary/KnowledgeBase");

                    }                    
                    if(!resourceCopy.getTheme().contains(theme)){
                        resourceCopy.getTheme().add(theme);
                    }
                }
            }
        }
        return catalogues;
    }
    
    private List<Resource> getDiscoverableResources() throws IOException {
        LOGGER.info("Get discoverable recources list");
        List<Resource> catalogues = new ArrayList();
        URL fileURL = ResourcesService.class.
                getResource(GET_DISCOVERABLE_RESOURCES_QUERY);
        String queryString = Resources.toString(fileURL, Charsets.UTF_8);
        Repository repository = new SPARQLRepository(fdpIndexProperties.
                triplestoreUrl);

        try ( RepositoryConnection conn = repository.getConnection()) {

            TupleQuery query = conn.prepareTupleQuery(queryString);
            String previousID = "";
            Resource resourceCopy = null;

            try ( TupleQueryResult result = query.evaluate()) {
                while (result.hasNext()) {
                    BindingSet solution = result.next();
                    String id = solution.getValue("resource").stringValue();
                    String theme = solution.getValue("theme").stringValue();
                    String rtype = solution.getValue("resource_type")
                            .stringValue();

                    if (!previousID.contentEquals(id)) {                        

                        Resource resource = new Resource();
                        addCommonResourceProperties(resource, solution);
                        
                        List<String> type = new ArrayList();
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
            }
        }
        return catalogues;
    }
    
    private void addCommonResourceProperties(Resource resource,
            BindingSet solution) {
        
        String name = solution.getValue("resource_name").stringValue();
        resource.setResourceName(name);
        if (solution.getValue("resource_description") != null) {
            resource.setResourceDescription(
                    solution.getValue("resource_description").stringValue());
        }
        if (solution.getValue("resource_logo") != null) {
            resource.setLogo(solution.getValue("resource_logo").stringValue());
        } else {
            resource.setLogo("");
        }
        
        if (solution.getValue("home_page") != null) {
            resource.setHomepage(solution.getValue("home_page").stringValue());
        } else {
            resource.setHomepage(resource.getId());
        }
        if (solution.getValue("created_time") != null) {
            resource.setCreateDateTime(
                    solution.getValue("created_time").stringValue());
        }        
        if (solution.getValue("updated_time") != null) {
            resource.setUpdateDateTime(
                    solution.getValue("updated_time").stringValue());
        }

    }
}
