package com.submeter.android.entity;

import java.util.List;

/**
 * Created by yangzhao on 2019/12/7 0007.
 */

public class FacilityListBean {

    /**
     * msg : success
     * code : 0
     * enterpriseDtos : [{"equipmentid":"136000300","equipmentName":"生产表1（一车间焊接）","eleHistryEntities":[{"id":null,"createTime":"2019-12-06 12:15:00","power":"0.0149999997","number":"136000300"},{"id":null,"createTime":"2019-12-06 12:20:00","power":"0.0149999997","number":"136000300"},{"id":null,"createTime":"2019-12-06 12:25:00","power":"0.0148999998","number":"136000300"},{"id":null,"createTime":"2019-12-06 12:30:00","power":"0.0149999997","number":"136000300"}]},{"equipmentid":"136000289","equipmentName":"生产表6（二车间焊接）","eleHistryEntities":[{"id":null,"createTime":"2019-12-06 12:15:00","power":"0","number":"136000289"},{"id":null,"createTime":"2019-12-06 12:20:00","power":"0","number":"136000289"},{"id":null,"createTime":"2019-12-06 12:25:00","power":"0","number":"136000289"},{"id":null,"createTime":"2019-12-06 12:30:00","power":"0","number":"136000289"}]},{"equipmentid":"136000290","equipmentName":"生产表9（二车间焊接）","eleHistryEntities":[{"id":null,"createTime":"2019-12-06 12:15:00","power":"0","number":"136000290"},{"id":null,"createTime":"2019-12-06 12:20:00","power":"0","number":"136000290"},{"id":null,"createTime":"2019-12-06 12:25:00","power":"0","number":"136000290"},{"id":null,"createTime":"2019-12-06 12:30:00","power":"0","number":"136000290"}]},{"equipmentid":"136000293","equipmentName":"生产表8（二车间焊接）","eleHistryEntities":[{"id":null,"createTime":"2019-12-06 12:15:00","power":"0.0","number":"136000293"},{"id":null,"createTime":"2019-12-06 12:20:00","power":"0.0","number":"136000293"},{"id":null,"createTime":"2019-12-06 12:25:00","power":"0.0","number":"136000293"},{"id":null,"createTime":"2019-12-06 12:30:00","power":"0.0","number":"136000293"}]},{"equipmentid":"136000227","equipmentName":"生产表2（一车间焊接）","eleHistryEntities":[{"id":null,"createTime":"2019-12-06 12:15:00","power":"0.0","number":"136000227"},{"id":null,"createTime":"2019-12-06 12:20:00","power":"0.0","number":"136000227"},{"id":null,"createTime":"2019-12-06 12:25:00","power":"0.0","number":"136000227"},{"id":null,"createTime":"2019-12-06 12:30:00","power":"0.0","number":"136000227"}]},{"equipmentid":"136000297","equipmentName":"总表（喷漆）","eleHistryEntities":[{"id":null,"createTime":"2019-12-06 12:15:00","power":"8.0E-4","number":"136000297"},{"id":null,"createTime":"2019-12-06 12:20:00","power":"8.0E-4","number":"136000297"},{"id":null,"createTime":"2019-12-06 12:25:00","power":"8.0E-4","number":"136000297"},{"id":null,"createTime":"2019-12-06 12:30:00","power":"8.0E-4","number":"136000297"}]},{"equipmentid":"136000226","equipmentName":"治污表1（除尘）","eleHistryEntities":[{"id":null,"createTime":"2019-12-06 12:15:00","power":"0.0","number":"136000226"},{"id":null,"createTime":"2019-12-06 12:20:00","power":"0.0","number":"136000226"},{"id":null,"createTime":"2019-12-06 12:25:00","power":"0.0","number":"136000226"},{"id":null,"createTime":"2019-12-06 12:30:00","power":"0.0","number":"136000226"}]},{"equipmentid":"136000225","equipmentName":"生产表4（一车间焊接）","eleHistryEntities":[{"id":null,"createTime":"2019-12-06 12:15:00","power":"0.0534","number":"136000225"},{"id":null,"createTime":"2019-12-06 12:20:00","power":"0.0532","number":"136000225"},{"id":null,"createTime":"2019-12-06 12:25:00","power":"0.0534","number":"136000225"},{"id":null,"createTime":"2019-12-06 12:30:00","power":"0.0531","number":"136000225"}]},{"equipmentid":"136000294","equipmentName":"生产表5（二车间焊接）","eleHistryEntities":[{"id":null,"createTime":"2019-12-06 12:15:00","power":"0.002","number":"136000294"},{"id":null,"createTime":"2019-12-06 12:20:00","power":"0.0019","number":"136000294"},{"id":null,"createTime":"2019-12-06 12:25:00","power":"0.002","number":"136000294"},{"id":null,"createTime":"2019-12-06 12:30:00","power":"0.002","number":"136000294"}]},{"equipmentid":"136000228","equipmentName":"生产表3（一车间焊接）","eleHistryEntities":[{"id":null,"createTime":"2019-12-06 12:15:00","power":"0.0065","number":"136000228"},{"id":null,"createTime":"2019-12-06 12:20:00","power":"0.0065","number":"136000228"},{"id":null,"createTime":"2019-12-06 12:25:00","power":"0.0064","number":"136000228"},{"id":null,"createTime":"2019-12-06 12:30:00","power":"0.0064","number":"136000228"}]},{"equipmentid":"136000299","equipmentName":"生产表7（二车间焊接）","eleHistryEntities":[{"id":null,"createTime":"2019-12-06 12:15:00","power":"0.0013","number":"136000299"},{"id":null,"createTime":"2019-12-06 12:20:00","power":"0.0013","number":"136000299"},{"id":null,"createTime":"2019-12-06 12:25:00","power":"0.0013","number":"136000299"},{"id":null,"createTime":"2019-12-06 12:30:00","power":"0.0013","number":"136000299"}]},{"equipmentid":"136000298","equipmentName":"治污表2（voc）","eleHistryEntities":[{"id":null,"createTime":"2019-12-06 12:15:00","power":"9.0E-4","number":"136000298"},{"id":null,"createTime":"2019-12-06 12:20:00","power":"9.0E-4","number":"136000298"},{"id":null,"createTime":"2019-12-06 12:25:00","power":"8.0E-4","number":"136000298"},{"id":null,"createTime":"2019-12-06 12:30:00","power":"9.0E-4","number":"136000298"}]},{"equipmentid":"136000135","equipmentName":"治污表3（除尘）","eleHistryEntities":[]},{"equipmentid":"136000136","equipmentName":"治污表4（除尘）","eleHistryEntities":[{"id":null,"createTime":"2019-12-06 12:15:00","power":"0.0012","number":"136000136"},{"id":null,"createTime":"2019-12-06 12:20:00","power":"0.0014","number":"136000136"},{"id":null,"createTime":"2019-12-06 12:25:00","power":"0.0013","number":"136000136"},{"id":null,"createTime":"2019-12-06 12:30:00","power":"0.0013","number":"136000136"}]}]
     */

    private String msg;
    private int code;
    private List<EnterpriseDtosBean> enterpriseDtos;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<EnterpriseDtosBean> getEnterpriseDtos() {
        return enterpriseDtos;
    }

    public void setEnterpriseDtos(List<EnterpriseDtosBean> enterpriseDtos) {
        this.enterpriseDtos = enterpriseDtos;
    }

    public static class EnterpriseDtosBean {
        /**
         * equipmentid : 136000300
         * equipmentName : 生产表1（一车间焊接）
         * eleHistryEntities : [{"id":null,"createTime":"2019-12-06 12:15:00","power":"0.0149999997","number":"136000300"},{"id":null,"createTime":"2019-12-06 12:20:00","power":"0.0149999997","number":"136000300"},{"id":null,"createTime":"2019-12-06 12:25:00","power":"0.0148999998","number":"136000300"},{"id":null,"createTime":"2019-12-06 12:30:00","power":"0.0149999997","number":"136000300"}]
         */

        private String equipmentid;
        private String equipmentName;
        private List<EleHistryEntitiesBean> eleHistryEntities;

        public String getEquipmentid() {
            return equipmentid;
        }

        public void setEquipmentid(String equipmentid) {
            this.equipmentid = equipmentid;
        }

        public String getEquipmentName() {
            return equipmentName;
        }

        public void setEquipmentName(String equipmentName) {
            this.equipmentName = equipmentName;
        }

        public List<EleHistryEntitiesBean> getEleHistryEntities() {
            return eleHistryEntities;
        }

        public void setEleHistryEntities(List<EleHistryEntitiesBean> eleHistryEntities) {
            this.eleHistryEntities = eleHistryEntities;
        }

        public static class EleHistryEntitiesBean {
            /**
             * id : null
             * createTime : 2019-12-06 12:15:00
             * power : 0.0149999997
             * number : 136000300
             */

            private Object id;
            private String createTime;
            private String power;
            private String number;

            public Object getId() {
                return id;
            }

            public void setId(Object id) {
                this.id = id;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getPower() {
                return power;
            }

            public void setPower(String power) {
                this.power = power;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }
        }
    }
}
