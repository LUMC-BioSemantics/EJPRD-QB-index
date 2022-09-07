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
package nl.rajaram.ejp.catalogue.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rajaram
 */
public class Catalogue {

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the theme
     */
    public List<String> getTheme() {
        return theme;
    }

    /**
     * @param theme the theme to set
     */
    public void setTheme(List<String> theme) {
        this.theme = theme;
    }

    /**
     * @return the logo
     */
    public String getLogo() {
        return logo;
    }

    /**
     * @param logo the logo to set
     */
    public void setLogo(String logo) {
        this.logo = logo;
    }

    /**
     * @return the specsURL
     */
    public String getSpecsURL() {
        return specsURL;
    }

    /**
     * @param specsURL the specsURL to set
     */
    public void setSpecsURL(String specsURL) {
        this.specsURL = specsURL;
    }

    /**
     * @return the catalogueDescription
     */
    public String getCatalogueDescription() {
        return catalogueDescription;
    }

    /**
     * @param catalogueDescription the catalogueDescription to set
     */
    public void setCatalogueDescription(String catalogueDescription) {
        this.catalogueDescription = catalogueDescription;
    }

    /**
     * @return the catalogueName
     */
    public String getCatalogueName() {
        return catalogueName;
    }

    /**
     * @param catalogueName the catalogueName to set
     */
    public void setCatalogueName(String catalogueName) {
        this.catalogueName = catalogueName;
    }

    /**
     * @return the catalogueAddress
     */
    public String getCatalogueAddress() {
        return catalogueAddress;
    }

    /**
     * @param catalogueAddress the catalogueAddress to set
     */
    public void setCatalogueAddress(String catalogueAddress) {
        this.catalogueAddress = catalogueAddress;
    }

    /**
     * @return the created
     */
    public String getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(String created) {
        this.created = created;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the catalogueType
     */
    public List<String> getCatalogueType() {
        return catalogueType;
    }

    /**
     * @param catalogueType the catalogueType to set
     */
    public void setCatalogueType(List<String> catalogueType) {
        this.catalogueType = catalogueType;
    }
    
    private String catalogueName;
    
    private String catalogueAddress;
    
    private String catalogueDescription;
            
    private String created;
    
    private String specsURL;
    
    private String logo;
    
    @JsonProperty("_id")
    private String id;
            
    private List<String> catalogueType = new ArrayList();
    
    private String type;
    
    private List<String> theme = new ArrayList();
}
