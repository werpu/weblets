package net.java.dev.weblets.sandbox;

import java.util.List;
import java.util.LinkedList;

/**
 * @author werpu
 * @date: 03.11.2008
 * <p/>
 * Weblets Subbundle for further processing in the processing stage
 */
public class Subbundle {
    /*the subbundle id for our resource subbundle*/
    String subbundleId = "";
    /*the list of resources to be served by the subbundle
    * as unified resource*/
    List resources = new LinkedList();

    public Subbundle() {
    }

    public Subbundle(String subbundleId, List resources) {
        this.subbundleId = subbundleId;
        this.resources = resources;
    }

    public String getSubbundleId() {
        return subbundleId;
    }

    public void setSubbundleId(String subbundleId) {
        this.subbundleId = subbundleId;
    }

    public List getResources() {
        return resources;
    }

    public void setResources(List resources) {
        this.resources = resources;
    }
}
