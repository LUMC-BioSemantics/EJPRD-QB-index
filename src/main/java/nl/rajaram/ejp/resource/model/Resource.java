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
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
     * @return the createDateTime
     */
    public String getCreateDateTime() {
        return createDateTime;
    }

    /**
     * @param createDateTime the createDateTime to set
     */
    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }

    /**
     * @return the updateDateTime
     */
    public String getUpdateDateTime() {
        return updateDateTime;
    }

    /**
     * @param updateDateTime the updateDateTime to set
     */
    public void setUpdateDateTime(String updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    /**
     * @return the homepage
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     * @param homepage the homepage to set
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
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
    public List<String> getResourceContentType() {
        return resourceContentType;
    }

    /**
     * @param resourceContentType the resourceType to set
     */
    public void setResourceContentType(List<String> resourceContentType) {
        this.resourceContentType = resourceContentType;
    }

    private String resourceName;

    private String resourceAddress;

    private String resourceDescription;

    private String specsURL;

    private String logo;

    private String homepage;

    private String createDateTime;

    private String updateDateTime;

    @JsonProperty("_id")
    private String id;

    private List<String> resourceContentType = new ArrayList();

    private List<String> theme = new ArrayList();
}
