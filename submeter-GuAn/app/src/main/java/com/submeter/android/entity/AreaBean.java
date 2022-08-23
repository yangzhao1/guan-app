package com.submeter.android.entity;

import java.util.List;

/**
 * Created by yangzhao on 2019/12/6 0006.
 */

public class AreaBean {

    private List<AreaBeanN> sysOrganizationEntities;

    public List<AreaBeanN> getSysOrganizationEntities() {
        return sysOrganizationEntities;
    }

    public class AreaBeanN{
        /**
         * orgId : 1
         * description : 广阳区
         * name : 广阳区
         * zonCode : 131003000000
         */

        private int orgId;
        private String description;
        private String name;
        private String zonCode;

        public int getOrgId() {
            return orgId;
        }

        public void setOrgId(int orgId) {
            this.orgId = orgId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getZonCode() {
            return zonCode;
        }

        public void setZonCode(String zonCode) {
            this.zonCode = zonCode;
        }
    }
}
