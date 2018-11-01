package apps.assessment.components.content.breadcrumb;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.ValueMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to use as a Breadcrumb by extending WCMUsePojo.
 *
 * @version 1.0
 */

public class Breadcrumb extends WCMUsePojo {

    private List<Page> pageLinks;
    /**
     * This is the default method of WCMUsePojo class
     *
     * @throws Exception
     */

    @Override
    public void activate() throws Exception {
        pageLinks = new ArrayList<>();
        InheritanceValueMap breadcrumbValueMap = new HierarchyNodeInheritanceValueMap(getResource());
        String rootPath = breadcrumbValueMap.getInherited("rootPath", String.class);
        Page currentPage = getCurrentPage();
        PageManager pageManager = getResourceResolver().adaptTo(PageManager.class);
        if (rootPath != null && !rootPath.equals(currentPage.getPath()) && pageManager != null) {
            int rootPageLevel = pageManager.getPage(rootPath).getDepth();
            int currentPageLevel = currentPage.getDepth();
            while (currentPageLevel >= rootPageLevel) {
                Page page = currentPage.getAbsoluteParent(rootPageLevel - 1);
                if (page != null && !page.isHideInNav()) {
                    ValueMap valueMap = page.getContentResource().getValueMap();
                    Page parentPage = valueMap.containsKey("redirectTarget") ? pageManager.getPage(valueMap.get("redirectTarget", String.class)) : page;
                    pageLinks.add(parentPage);
                }
                rootPageLevel++;
            }
        }
    }

    /**
     * Method to get the breadcrumb links.
     *
     * @return List of pages
     */
    public List<Page> getPageLinks() {
        return pageLinks;
    }
}
