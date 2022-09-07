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
package nl.rajaram.ejp.catalogue.api.service;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import nl.rajaram.ejp.catalogue.api.configuration.FDPIndexProperties;
import nl.rajaram.ejp.catalogue.model.Catalogue;
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
public class CataloguesService {

    private static final Logger LOGGER = LoggerFactory.
            getLogger(CataloguesService.class);
    private final String GET_QUERYABLE_RESOURCES_QUERY
            = "list-queryable-resources.rq";
    private final String GET_DISCOVERABLE_RESOURCES_QUERY
            = "list-discoverable-resources.rq";

    @Autowired
    private FDPIndexProperties fdpIndexProperties;

    public List<Catalogue> getCatalogues() throws IOException {
        LOGGER.info("Get all recources list");
        List<Catalogue> catalogues = getQueryableResources();
        catalogues.addAll(getDiscoverableResources());

        return catalogues;
    }

    private List<Catalogue> getQueryableResources() throws IOException {
        LOGGER.info("Get queryable recources list");
        List<Catalogue> catalogues = new ArrayList();
        URL fileURL = CataloguesService.class.
                getResource(GET_QUERYABLE_RESOURCES_QUERY);
        String queryString = Resources.toString(fileURL, Charsets.UTF_8);
        Repository repository = new SPARQLRepository(fdpIndexProperties.
                triplestoreUrl);

        try ( RepositoryConnection conn = repository.getConnection()) {

            TupleQuery query = conn.prepareTupleQuery(queryString);

            String previousID = "";
            Catalogue resourceCopy = null;

            try ( TupleQueryResult result = query.evaluate()) {
                while (result.hasNext()) {
                    BindingSet solution = result.next();
                    String id = solution.getValue("resource").stringValue();
                    String theme = solution.getValue("theme").stringValue();
                    
                    if (!previousID.contentEquals(id)) {
                        
                        Catalogue resource = new Catalogue();
                        addCommonResourceProperties(resource, solution);
                        
                        resource.setType("http://www.w3.org/ns/dcat#Dataset");

                        if (solution.getValue("resource_endpoint") != null) {
                            resource.setCatalogueAddress(
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
                        List<String> catalogueType = new ArrayList();
                        catalogueType.add("knowledge");
                        resourceCopy.setCatalogueType(catalogueType);

                    }                    
                    resourceCopy.getTheme().add(theme);
                }
            }
        }
        return catalogues;
    }
    
    private List<Catalogue> getDiscoverableResources() throws IOException {
        LOGGER.info("Get discoverable recources list");
        List<Catalogue> catalogues = new ArrayList();
        URL fileURL = CataloguesService.class.
                getResource(GET_DISCOVERABLE_RESOURCES_QUERY);
        String queryString = Resources.toString(fileURL, Charsets.UTF_8);
        Repository repository = new SPARQLRepository(fdpIndexProperties.
                triplestoreUrl);

        try ( RepositoryConnection conn = repository.getConnection()) {

            TupleQuery query = conn.prepareTupleQuery(queryString);
            String previousID = "";
            Catalogue resourceCopy = null;

            try ( TupleQueryResult result = query.evaluate()) {
                while (result.hasNext()) {
                    BindingSet solution = result.next();
                    String id = solution.getValue("resource").stringValue();
                    String theme = solution.getValue("theme").stringValue();
                    String type = solution.getValue("resource_type")
                            .stringValue();

                    if (!previousID.contentEquals(id)) {                        

                        Catalogue resource = new Catalogue();
                        addCommonResourceProperties(resource, solution);
                        resource.setType(type);
                        resource.setId(id);
                        catalogues.add(resource);
                        previousID = id;
                        resourceCopy = resource;
                    }                   
                    resourceCopy.getTheme().add(theme);
                }
            }
        }
        return catalogues;
    }
    
    private void addCommonResourceProperties(Catalogue resource,
            BindingSet solution) {
        
        String name = solution.getValue("resource_name").stringValue();
        resource.setCatalogueName(name);
        if (solution.getValue("resource_description") != null) {
            resource.setCatalogueDescription(
                    solution.getValue("resource_description").stringValue());
        }
        if (solution.getValue("resource_logo") != null) {
            resource.setLogo(solution.getValue("resource_logo").stringValue());
        } else {
            resource.setLogo("");
        }
        String uniqueID = UUID.randomUUID().toString();        
        resource.setCreated(uniqueID);

    }
}
