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
package nl.rajaram.ejp.resource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rajaram
 */
public class Resource {

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
     * @return the resourceDescription
     */
    public String getResourceDescription() {
        return resourceDescription;
    }

    /**
     * @param resourceDescription the resourceDescription to set
     */
    public void setResourceDescription(String resourceDescription) {
        this.resourceDescription = resourceDescription;
    }

    /**
     * @return the resourceName
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * @param resourceName the resourceName to set
     */
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    /**
     * @return the resourceAddress
     */
    public String getResourceAddress() {
        return resourceAddress;
    }

    /**
     * @param resourceAddress the resourceAddress to set
     */
    public void setResourceAddress(String resourceAddress) {
        this.resourceAddress = resourceAddress;
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
     * @return the resourceType
     */
    public List<String> getResourceType() {
        return resourceType;
    }

    /**
     * @param resourceType the resourceType to set
     */
    public void setResourceType(List<String> resourceType) {
        this.resourceType = resourceType;
    }
    
    private String resourceName;
    
    private String resourceAddress;
    
    private String resourceDescription;
            
    private String created;
    
    private String specsURL;
    
    private String logo;
    
    @JsonProperty("_id")
    private String id;
            
    private List<String> resourceType = new ArrayList();
    
    private List<String> theme = new ArrayList();
}
