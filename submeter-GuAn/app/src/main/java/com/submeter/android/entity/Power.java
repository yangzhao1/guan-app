package com.submeter.android.entity;

import java.util.List;

/**
 * Created by yangzhao on 2019/4/17.
 */

public class Power {

    /**
     * msg : success
     * code : 0
     * data : [{"numGroup":"0","enId":"10","coulometryDtos":[{"id":7,"equipmentName":"第一喷漆房治污电表","equipmentNumber":"0201909209","type":"2","groupNum":null,"eleDataDtos":[{"dataTime":"2019-05-03 00:00:00","power":22.4},{"dataTime":"2019-05-04 00:00:00","power":10.4},{"dataTime":"2019-05-05 00:00:00","power":52.4},{"dataTime":"2019-05-06 00:00:00","power":12.4},{"dataTime":"2019-05-07 00:00:00","power":1.4},{"dataTime":"2019-05-08 00:00:00","power":4.4},{"dataTime":"2019-05-09 00:00:00","power":6.4}]},{"id":6,"equipmentName":"第一喷漆房生产电表","equipmentNumber":"0201909208","type":"1","groupNum":null,"eleDataDtos":[{"dataTime":"2019-05-03 00:00:00","power":27.4},{"dataTime":"2019-05-04 00:00:00","power":32.4},{"dataTime":"2019-05-05 00:00:00","power":37.4},{"dataTime":"2019-05-06 00:00:00","power":42.4},{"dataTime":"2019-05-07 00:00:00","power":47.4},{"dataTime":"2019-05-08 00:00:00","power":52.4},{"dataTime":"2019-05-09 00:00:00","power":57.4}]}]},{"numGroup":"55","enId":"10","coulometryDtos":[{"id":13,"equipmentName":"设备01","equipmentNumber":"设备名称","type":"1","groupNum":null,"eleDataDtos":[]}]}]
     */

    private String msg;
    private int code;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * numGroup : 0
         * enId : 10
         * coulometryDtos : [{"id":7,"equipmentName":"第一喷漆房治污电表","equipmentNumber":"0201909209","type":"2","groupNum":null,"eleDataDtos":[{"dataTime":"2019-05-03 00:00:00","power":22.4},{"dataTime":"2019-05-04 00:00:00","power":10.4},{"dataTime":"2019-05-05 00:00:00","power":52.4},{"dataTime":"2019-05-06 00:00:00","power":12.4},{"dataTime":"2019-05-07 00:00:00","power":1.4},{"dataTime":"2019-05-08 00:00:00","power":4.4},{"dataTime":"2019-05-09 00:00:00","power":6.4}]},{"id":6,"equipmentName":"第一喷漆房生产电表","equipmentNumber":"0201909208","type":"1","groupNum":null,"eleDataDtos":[{"dataTime":"2019-05-03 00:00:00","power":27.4},{"dataTime":"2019-05-04 00:00:00","power":32.4},{"dataTime":"2019-05-05 00:00:00","power":37.4},{"dataTime":"2019-05-06 00:00:00","power":42.4},{"dataTime":"2019-05-07 00:00:00","power":47.4},{"dataTime":"2019-05-08 00:00:00","power":52.4},{"dataTime":"2019-05-09 00:00:00","power":57.4}]}]
         */

        private String numGroup;
        private String enId;
        private List<CoulometryDtosBean> coulometryDtos;

        public String getNumGroup() {
            return numGroup;
        }

        public void setNumGroup(String numGroup) {
            this.numGroup = numGroup;
        }

        public String getEnId() {
            return enId;
        }

        public void setEnId(String enId) {
            this.enId = enId;
        }

        public List<CoulometryDtosBean> getCoulometryDtos() {
            return coulometryDtos;
        }

        public void setCoulometryDtos(List<CoulometryDtosBean> coulometryDtos) {
            this.coulometryDtos = coulometryDtos;
        }

        public static class CoulometryDtosBean {
            /**
             * id : 7
             * equipmentName : 第一喷漆房治污电表
             * equipmentNumber : 0201909209
             * type : 2
             * groupNum : null
             * eleDataDtos : [{"dataTime":"2019-05-03 00:00:00","power":22.4},{"dataTime":"2019-05-04 00:00:00","power":10.4},{"dataTime":"2019-05-05 00:00:00","power":52.4},{"dataTime":"2019-05-06 00:00:00","power":12.4},{"dataTime":"2019-05-07 00:00:00","power":1.4},{"dataTime":"2019-05-08 00:00:00","power":4.4},{"dataTime":"2019-05-09 00:00:00","power":6.4}]
             */

            private int id;
            private String equipmentName;
            private String equipmentNumber;
            private String type;
            private Object groupNum;
            private List<EleDataDtosBean> eleDataDtos;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getEquipmentName() {
                return equipmentName;
            }

            public void setEquipmentName(String equipmentName) {
                this.equipmentName = equipmentName;
            }

            public String getEquipmentNumber() {
                return equipmentNumber;
            }

            public void setEquipmentNumber(String equipmentNumber) {
                this.equipmentNumber = equipmentNumber;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public Object getGroupNum() {
                return groupNum;
            }

            public void setGroupNum(Object groupNum) {
                this.groupNum = groupNum;
            }

            public List<EleDataDtosBean> getEleDataDtos() {
                return eleDataDtos;
            }

            public void setEleDataDtos(List<EleDataDtosBean> eleDataDtos) {
                this.eleDataDtos = eleDataDtos;
            }

            public static class EleDataDtosBean {
                /**
                 * dataTime : 2019-05-03 00:00:00
                 * power : 22.4
                 */

                private String dataTime;
                private double power;

                public String getDataTime() {
                    return dataTime;
                }

                public void setDataTime(String dataTime) {
                    this.dataTime = dataTime;
                }

                public double getPower() {
                    return power;
                }

                public void setPower(double power) {
                    this.power = power;
                }
            }
        }
    }
}
